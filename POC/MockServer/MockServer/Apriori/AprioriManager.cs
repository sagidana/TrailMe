using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TrailMe.Common;
using TrailMe.DAL;

namespace TrailMe.Apriori
{
    public class AprioriManager
    {
        #region Data Members

        private AprioriAlgorithm m_AprioriAlgorithm;
        private double m_MinSupport;
        private double m_MinConfidence;

        #endregion

        #region Properties

        public Result Results { get; set; }

        #endregion

        #region C-tors

        public AprioriManager(double minSupport = 0.2, double minConfidence = 0.6)
        {
            m_MinSupport = minSupport;
            m_MinConfidence = minConfidence;
            m_AprioriAlgorithm = new AprioriAlgorithm();
        }

        #endregion

        #region Public Methods

        public void RunApriori()
        {
            var tracks = getTracksFromDb();
            var transactions = getTransactionsFromDb();

            Results = m_AprioriAlgorithm.ProcessTransaction(m_MinSupport, m_MinConfidence, tracks, transactions);
        }

        public List<Track> GetRecommendations(Guid userId)
        {
            if (Results != null || Results.StrongRules == null || Results.StrongRules.Count == 0)
                RunApriori(); //TODO : ok for development, but not for production.

            IEnumerable<DAL.Model.Track> dbUserTracks = TrackRepository.GetTracksByUserId(userId);
            List<Track> userTracks = convertTrackFromDb(dbUserTracks);

            return getRecommendations(userTracks);
        }

        #endregion

        #region Private Methods

        private int findMinimumDifferenceInRules(List<Track> tracks)
        {
            int minimumDifference = tracks.Count;

            foreach (Rule rule in Results.StrongRules)
            {
                var difference = rule.From.Except(tracks);

                if (difference.Count() < minimumDifference)
                    minimumDifference = difference.Count();
            }

            return minimumDifference;
        }

        private List<Track> getRecommendations(List<Track> tracks)
        {
            int minimumDifference = findMinimumDifferenceInRules(tracks);

            List<Track> recommendedTracks = new List<Track>();

            foreach (Rule rule in Results.StrongRules)
                if (rule.From.Except(tracks).Count() == minimumDifference)
                    recommendedTracks.AddRange(rule.To);

            return recommendedTracks;
        }

        private List<Track> convertTrackFromDb(IEnumerable<DAL.Model.Track> userTracks)
        {
            var tracks = new List<Track>();

            foreach (var track in userTracks)
                tracks.Add(new Track { TrackId = track.TrackID });

            return tracks;
        }

        private List<Track> getTracksFromDb()
        {
            var tracks = new List<Track>();

            foreach (var track in TrackRepository.GetTracks())
                tracks.Add(new Track() { TrackId = track.TrackID });

            return tracks;
        }

        private List<Transaction> getTransactionsFromDb()
        {
            var transactions = new List<Transaction>();

            foreach (var user in UserRepository.GetUsers())
            {
                List<Track> tracks = new List<Track>();

                foreach (var track in TrackRepository.GetTracksByUserId(user.UserID))
                    tracks.Add(new Track { TrackId = track.TrackID });

                transactions.Add(new Transaction { Items = tracks });
            }

            return transactions;
        }

        #endregion
    }
}
