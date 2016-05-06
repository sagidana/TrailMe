using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TrailMe.DAL.Model;

namespace TrailMe.DAL
{
    public class TrackRepository
    {
        #region Static Methods

        public static bool AddTrack(string track_name, string location_zone, int distance_km, string level_of_diffuclty, double latitude_, double longitude_)
        {
            // Create the database context
            using (var dbContext = new TrailMeDBEntities())
            {

                dbContext.insert_Track(track_name, location_zone, distance_km, level_of_diffuclty, latitude_, longitude_);

                // Save the changes to the database, and record the number of changes
                var changesSaved = dbContext.SaveChanges();

                // Return a bool based on whether any changes have been stored
                return changesSaved >= 1;
            }
        }

        public static bool DeleteTrack(Guid track_id)
        {

            using (var dbContext = new TrailMeDBEntities())
            {
                dbContext.delete_Track(track_id);

                // Save the changes to the database, and record the number of changes
                var changesSaved = dbContext.SaveChanges();

                // Return a bool based on whether any changes have been stored
                return changesSaved >= 1;
            }
        }

        public static Track GetTrackById(Guid track_id)
        {
            using (var dbContext = new TrailMeDBEntities())
            {
                return dbContext.Tracks.Find(track_id);
            }
        }

        public static IEnumerable<Track> GetTracks()
        {
            using (var dbContext = new TrailMeDBEntities())
            {
                return dbContext.Tracks.ToList();
            }
        }

        public static IEnumerable<Track> GetTracksByUserId(Guid userId)
        {
            return null;
        }

        #endregion
    }
}
