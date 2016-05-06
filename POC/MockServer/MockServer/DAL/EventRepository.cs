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

        public static bool AddEvent(string event_name, DateTime start_date, DateTime end_date, Guid track_id)
        {
            // Create the database context
            using (var dbContext = new TrailMeDBEntities())
            {

                dbContext.insert_Event(event_name, start_date, end_date, track_id);

                // Save the changes to the database, and record the number of changes
                var changesSaved = dbContext.SaveChanges();

                // Return a bool based on whether any changes have been stored
                return changesSaved >= 1;
            }
        }

        public static bool DeleteEvent(Guid event_id)
        {

            using (var dbContext = new TrailMeDBEntities())
            {
                dbContext.delete_Event(event_id);

                // Save the changes to the database, and record the number of changes
                var changesSaved = dbContext.SaveChanges();

                // Return a bool based on whether any changes have been stored
                return changesSaved >= 1;
            }
        }

        public static Event GetEventById(Guid event_id)
        {
            using (var dbContext = new TrailMeDBEntities())
            {
                return dbContext.Events.Find(event_id);
            }
        }

        public static IEnumerable<Event> GetEvents()
        {
            using (var dbContext = new TrailMeDBEntities())
            {
                return dbContext.Events.ToList();
            }

        }

        #endregion
    }
}
