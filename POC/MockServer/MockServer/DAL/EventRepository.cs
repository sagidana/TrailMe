using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TrailMe.DAL.Model;

namespace TrailMe.DAL
{
    public class EventRepository
    {
        #region Static Methods

        public static bool AddEvent(string event_name, DateTime timestamp, Guid track_id, Guid group_id)
        {
            // Create the database context
            using (var dbContext = new TrailMeModelContainer())
            {
                DAL.Model.Track matchingTrack = dbContext.Tracks.Where(track => track.Id == track_id).First();
                DAL.Model.Group matchingGroup = dbContext.Groups.Where(group => group.Id == group_id).First();

                DAL.Model.Event newEvent = new Event()
                {
                    Id = Guid.NewGuid(),
                    Name = event_name,
                    Track = matchingTrack,
                    Group = matchingGroup
                };

                dbContext.Events.Add(newEvent);

                // Save the changes to the database, and record the number of changes
                var changesSaved = dbContext.SaveChanges();

                // Return a bool based on whether any changes have been stored
                return changesSaved >= 1;
            }
        }

        public static bool DeleteEvent(Guid event_id)
        {

            using (var dbContext = new TrailMeModelContainer())
            {
                var matchingEvent = dbContext.Events.Where(Event => Event.Id == event_id).First();
                dbContext.Events.Remove(matchingEvent);

                // Save the changes to the database, and record the number of changes
                var changesSaved = dbContext.SaveChanges();

                // Return a bool based on whether any changes have been stored
                return changesSaved >= 1;
            }
        }

        public static Event GetEventById(Guid event_id)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                return dbContext.Events.Find(event_id);
            }
        }

        public static IEnumerable<Event> GetEvents()
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                return dbContext.Events.ToList();
            }

        }

        #endregion
    }
}
