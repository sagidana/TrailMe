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

        public static void AddCategoryToTrack(Guid track_id, Guid category_id)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                Track track = dbContext.Tracks.Find(track_id);
                Category category = dbContext.Categories.Find(category_id);

                track.Categories.Add(category);
                category.Tracks.Add(track);

                var changesSaved = dbContext.SaveChanges();
            }
        }

        public static void AddUserToTrack(Guid track_id, Guid user_id)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                Track track = dbContext.Tracks.Find(track_id);
                User user = dbContext.Users.Find(user_id);

                track.Users.Add(user);
                user.Tracks.Add(track);

                var changesSaved = dbContext.SaveChanges();
            }
        }

        public static bool AddTrack(string track_name, string location_zone, int distance_km, string level_of_diffuclty, double latitude_, double longitude_)
        {
            // Create the database context
            using (var dbContext = new TrailMeModelContainer())
            {
                DAL.Model.Track newTrack = new Track()
                {
                    Id = Guid.NewGuid(),
                    Name = track_name,
                    Zone = location_zone,
                    Kilometers = distance_km,
                    Difficulty = level_of_diffuclty,
                    Latitude = latitude_,
                    Longitude = longitude_
                };

                dbContext.Tracks.Add(newTrack);

                // Save the changes to the database, and record the number of changes
                var changesSaved = dbContext.SaveChanges();

                // Return a bool based on whether any changes have been stored
                return changesSaved >= 1;
            }
        }

        public static bool DeleteTrack(Guid track_id)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                var track = dbContext.Tracks.Find(track_id);

                foreach (var user in track.Users)
                    user.Tracks.Remove(track);
                foreach (var category in track.Categories)
                    category.Tracks.Remove(track);

                dbContext.Tracks.Remove(track);

                // Save the changes to the database, and record the number of changes
                var changesSaved = dbContext.SaveChanges();

                // Return a bool based on whether any changes have been stored
                return changesSaved >= 1;
            }
        }

        public static Track GetTrackById(Guid track_id)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                return dbContext.Tracks.Find(track_id);
            }
        }

        public static IEnumerable<Track> GetTracks()
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                return dbContext.Tracks.Include("Events").Include("Events.Group").Include("Events.Track").ToList();
            }
        }

        public static IEnumerable<Track> GetTracksByUserId(Guid userId)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                return dbContext.Users.Where(user => user.Id == userId).First().Tracks;
            }
        }

        #endregion
    }
}
