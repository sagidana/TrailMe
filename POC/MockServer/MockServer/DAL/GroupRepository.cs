using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TrailMe.DAL.Model;

namespace TrailMe.DAL
{
    public class GroupRepository
    {
        #region Static Methods

        public static bool AddUserToGroup(Guid group_id, Guid user_id)
        {
            // Create the database context
            using (var dbContext = new TrailMeModelContainer())
            {
                DAL.Model.Group group = dbContext.Groups.Find(group_id);
                DAL.Model.User user = dbContext.Users.Find(user_id);

                user.Groups.Add(group);
                group.Users.Add(user);

                var changesSaved = dbContext.SaveChanges();
                return changesSaved >= 1;
            }
        }

        public static bool AddGroup(string group_name)
        {
            // Create the database context
            using (var dbContext = new TrailMeModelContainer())
            {
                DAL.Model.Group newGroup = new Group()
                {
                    Id = Guid.NewGuid(),
                    Name = group_name
                };

                dbContext.Groups.Add(newGroup);

                // Save the changes to the database, and record the number of changes
                var changesSaved = dbContext.SaveChanges();

                // Return a bool based on whether any changes have been stored
                return changesSaved >= 1;
            }
        }

        public static bool DeleteGroup(Guid group_id)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                var group = dbContext.Groups.Find(group_id);

                foreach (var user in group.Users)
                    user.Groups.Remove(group);
                foreach (var dbEvent in group.Events)
                    EventRepository.DeleteEvent(dbEvent.Id);

                dbContext.Groups.Remove(group);

                // Save the changes to the database, and record the number of changes
                var changesSaved = dbContext.SaveChanges();

                // Return a bool based on whether any changes have been stored
                return changesSaved >= 1;
            }
        }

        public static Group GetGroupById(Guid group_id)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                return dbContext.Groups.Find(group_id);
            }
        }

        public static IEnumerable<Group> GetGroups()
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                return dbContext.Groups.Include("Users").ToList();
            }
        }

        public static IEnumerable<Group> getGroupsByUserId(Guid id)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                return dbContext.Groups.Where(group => group.Users.Where(user => user.Id == id).Any()).ToList();
            }
        }

        public static IEnumerable<Group> getGroupsByEventId(Guid id)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                return dbContext.Groups.Where(group => group.Events.Where(dbEvent => dbEvent.Id == id).Any()).ToList();
            }
        }

        #endregion
    }
}
