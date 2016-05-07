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
                dbContext.Groups.Remove(dbContext.Groups.Where(group => group.Id == group_id).First());

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

        #endregion
    }
}
