using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using System.Collections.ObjectModel;
using TrailMe.Common;

namespace TrailMe.Apriori
{ 
    public class AprioriAlgorithm
    {
        #region C-tors

        public AprioriAlgorithm() { }

        #endregion

        #region Public Methods

        public Result ProcessTransaction(double minSupport, double minConfidence, List<Track> items, List<Transaction> transactions)
        {
            List<Item> allFrequentItems = getAllFrequentItems(minSupport, items, transactions);
            List<Rule> rules = generateRules(allFrequentItems);
            List<Rule> strongRules = getStrongRules(rules, minConfidence, allFrequentItems);

            Result result = new Result()
            {
                FrequentItems = allFrequentItems,
                StrongRules = strongRules
            };
            
            return result;
        }

        #endregion

        #region Private Methods

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
            Item generatedCandidate = new Item() { Tracks = new List<Track>(first.Tracks) };

            foreach (var track in second.Tracks)
                if (!generatedCandidate.Tracks.Contains(track))
                    generatedCandidate.Tracks.Add(track);

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

                        if (!rules.Contains(rule))
                            rules.Add(rule);
                    }
                }
            }

            return rules;
        }

        private List<Item> generateSubItems(Item item)
        {
            var subItems = new List<Item>();

            for (int i = 1; i <= item.Tracks.Count / 2; i++)
            {
                var subSets = getPermutationsWithRept(item.Tracks, i);

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
