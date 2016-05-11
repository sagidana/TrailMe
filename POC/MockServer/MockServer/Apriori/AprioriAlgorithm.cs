using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using System.Collections.ObjectModel;
using TrailMe.Common;

namespace TrailMe.Apriori
{
    public static class extensionMethods
    {
        private static IEnumerable<T[]> combinationsImpl<T>(IList<T> argList, int argStart, int argIteration, List<int> argIndicies = null)
        {
            argIndicies = argIndicies ?? new List<int>();
            for (int i = argStart; i < argList.Count; i++)
            {
                argIndicies.Add(i);
                if (argIteration > 0)
                {
                    foreach (var array in combinationsImpl(argList, i + 1, argIteration - 1, argIndicies))
                    {
                        yield return array;
                    }
                }
                else
                {
                    var array = new T[argIndicies.Count];
                    for (int j = 0; j < argIndicies.Count; j++)
                    {
                        array[j] = argList[argIndicies[j]];
                    }

                    yield return array;
                }
                argIndicies.RemoveAt(argIndicies.Count - 1);
            }
        }

        public static IEnumerable<T[]> Combinations<T>(this IList<T> argList, int argSetSize)
        {
            if (argList == null) throw new ArgumentNullException("argList");
            if (argSetSize <= 0) throw new ArgumentException("argSetSize Must be greater than 0", "argSetSize");
            return combinationsImpl(argList, 0, argSetSize - 1);
        }

    }
    public class AprioriAlgorithm
    {
        #region C-tors

        public AprioriAlgorithm() { }

        #endregion

        #region Public Methods

        public Result ProcessTransaction(double minSupport, double minConfidence, List<Track> items, List<Transaction> transactions)
        {
            List<Item> allFrequentItems = getAllFrequentItems(minSupport, items, transactions);

            //printItems(allFrequentItems);
            //Console.WriteLine("RULES______________________________________");
            
            List<Rule> rules = generateRules(allFrequentItems);
            
            //printRules(rules);
            //Console.WriteLine("STRONG RULES_____________________________________________");
            
            List<Rule> strongRules = getStrongRules(rules, minConfidence, allFrequentItems);
            
            //printRules(strongRules);

            Result result = new Result()
            {
                FrequentItems = allFrequentItems,
                StrongRules = strongRules
            };
            
            return result;
        }

        private void printItems(List<Item> allFrequentItems)
        {
            foreach(var item in allFrequentItems)
            {
                Console.WriteLine("Tracks in item:");
                foreach (var track in item.Tracks)
                    Console.WriteLine(track.TrackId);
            }
        }

        #endregion

        #region Private Methods

        #region Debug

        private void printRules(List<Rule> rules)
        {
            foreach (var rule in rules)
            {
                printRule(rule);
                Console.WriteLine("-------------------------------------------------------------------");
            }
        }

        private void printRule(Rule rule)
        {
            foreach(var item in rule.From)
                Console.WriteLine("{0}, ", item.TrackId);

            Console.WriteLine("=>");

            foreach (var item in rule.To)
                Console.WriteLine("{0}, ", item.TrackId);
        }

        #endregion

        #region GetFrequentItems

        private List<Item> getAllFrequentItems(double minSupport, List<Track> items, List<Transaction> transactions)
        {
            var frequentItems = getL1FrequentItems(minSupport, items, transactions);

            List<Item> allFrequentItems = new List<Item>();
            allFrequentItems.AddRange(frequentItems);
            List<Item> candidates = new List<Item>();

            do
            {
                candidates = generateCandidates(frequentItems, transactions);
                frequentItems = getFrequentItems(candidates, minSupport, transactions.Count);
                allFrequentItems.AddRange(frequentItems);
            }
            while (candidates.Count != 0);

            return allFrequentItems;
        }

        private List<Item> getFrequentItems(List<Item> candidates, double minSupport, int transactionsCount)
        {
            var frequentItems = new List<Item>();

            foreach (var candidate in candidates)
                if (candidate.Support / transactionsCount >= minSupport)
                    frequentItems.Add(new Item { Tracks = candidate.Tracks, Support = candidate.Support });

            return frequentItems;
        }

        private List<Item> generateCandidates(List<Item> frequentItems, List<Transaction> transactions)
        {
            var generatedCandidates = new List<Item>();

            for (int i = 0; i < frequentItems.Count - 1; i++)
            {
                Item first = frequentItems[i];
                for (int j = i + 1; j < frequentItems.Count; j++)
                {
                    Item second = frequentItems[j];
                    Item generatedCandidate = generateCandidates(first, second);

                    if (generatedCandidate.Tracks.Count > 0)
                    {
                        generatedCandidate.Support = getSupport(generatedCandidate.Tracks, transactions);
                        generatedCandidates.Add(generatedCandidate);
                    }
                }
            }

            return generatedCandidates;
        }

        private Item generateCandidates(Item first, Item second)
        {
            Item generatedCandidate = new Item() { Tracks = new List<Track>() };

            foreach (var track in second.Tracks)
            {
                if (!first.Tracks.Contains(track))
                {
                    generatedCandidate.Tracks.AddRange(first.Tracks);
                    generatedCandidate.Tracks.Add(track);
                    break;
                }
            }

            return generatedCandidate;
        }

        private bool isTracksInTransaction(List<Track> tracks, Transaction transaction)
        {
            foreach (var track in tracks)
                if (!transaction.Items.Contains(track))
                    return false;

            return true;
        }

        private double getSupport(List<Track> tracks, List<Transaction> transactions)
        {
            double support = 0;

            foreach (var transaction in transactions)
                if (isTracksInTransaction(tracks, transaction))
                    support++;

            return support;
        }

        private List<Item> getL1FrequentItems(double minSupport, List<Track> items, List<Transaction> transactions)
        {
            var frequentItemsL1 = new List<Item>();

            foreach (var item in items)
            {
                double support = getSupport(new List<Track> { item }, transactions);

                if (support / transactions.Count() >= minSupport)
                    frequentItemsL1.Add(new Item { Tracks = new List<Track> { item }, Support = support });
            }

            //frequentItemsL1.Sort();
            return frequentItemsL1;
        }

        #endregion

        #region GenerateStrongRules

        private double getConfidence(List<Track> X, List<Track> XY, List<Item> allFrequentItems)
        {
            var xItem = allFrequentItems.Where(item => item.Tracks.SequenceEqual(X));
            var xyItem = allFrequentItems.Where(item => item.Tracks.SequenceEqual(XY));

            if (!xItem.Any() || !xyItem.Any())
                return 0;

            double xSupport = xItem.First().Support;
            double xySupport = xyItem.First().Support;

            return xySupport / xSupport;
        }

        private void addStrongRule(Rule rule, List<Track> xyTracks, List<Rule> strongRules, double minConfidence, List<Item> allFrequentItems)
        {
            double confidence = getConfidence(rule.From, xyTracks, allFrequentItems);
            
            if (confidence >= minConfidence)
            {
                Rule newRule = new Rule() { From = rule.From, To = rule.To, Confidence = confidence };
                strongRules.Add(newRule);
            }

            confidence = getConfidence(rule.To, xyTracks, allFrequentItems);

            if (confidence >= minConfidence)
            {
                Rule newRule = new Rule() { From = rule.To, To = rule.From, Confidence = confidence };
                strongRules.Add(newRule);
            }
        }

        private List<Rule> getStrongRules(List<Rule> rules, double minConfidence, List<Item> allFrequentItems)
        {
            var strongRules = new List<Rule>();

            foreach (Rule rule in rules)
            {
                List<Track> xyTracks = new List<Track>(rule.From);
                xyTracks.AddRange(rule.To);
                addStrongRule(rule, xyTracks, strongRules, minConfidence, allFrequentItems);
            }

            return strongRules;
        }

        private List<Track> getRemaining(List<Track> tracks, Item item)
        {
            var remaining = new List<Track>();

            foreach (var track in item.Tracks)
                if (!tracks.Contains(track))
                    remaining.Add(track);

            return remaining;
        }

        private bool areTracksEqual(List<Track> first, List<Track> second)
        {
            foreach (var curr in first)
                if (!second.Contains(curr))
                    return false;
            foreach (var curr in second)
                if (!first.Contains(curr))
                    return false;
            return true;
        }
        private bool isRulesContainRule(List<Rule> rules, Rule rule)
        {
            foreach(var curr in rules)
                if ((areTracksEqual(curr.From, rule.From)) && areTracksEqual(curr.To, rule.To))
                    return true;
            return false;
        }

        private List<Rule> generateRules(List<Item> allFrequentItems)
        {
            var rules = new List<Rule>();

            foreach (var item in allFrequentItems)
            {
                if (item.Tracks.Count > 1)
                {
                    List<Item> subsets = generateSubItems(item);

                    foreach (var subset in subsets)
                    {
                        var remaining = getRemaining(subset.Tracks, item);
                        Rule rule = new Rule() { From = subset.Tracks, To = remaining };

                        if (!isRulesContainRule(rules, rule))
                            rules.Add(rule);
                    }
                }
            }

            return rules;
        }

        private List<Item> generateSubItems(Item item)
        {
            var subItems = new List<Item>();

            for (int i = 1; i <= item.Tracks.Count - 1; i++)
            {
                var subSets = item.Tracks.Combinations(i);

                foreach (var innerItem in subSets)
                    subItems.Add(new Item() { Tracks = innerItem.ToList() });
            }

            return subItems;
        }

        private IEnumerable<IEnumerable<T>> getPermutationsWithRept<T>(IEnumerable<T> list, int length)
        {
            if (length == 1) return list.Select(t => new T[] { t });
            return getPermutationsWithRept(list, length - 1)
                .SelectMany(t => list,
                    (t1, t2) => t1.Concat(new T[] { t2 }));
        }

        #endregion

        #endregion
    }
}
