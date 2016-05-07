using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TrailMe.DAL.Model;

namespace TrailMe.DAL
{
    public class UserRepository
    {
        #region Static Methods

        public static bool AddUser(string mailaddress, string last_name, string first_name, string city, DateTime birthdate)
        {
            // Create the database context
            using (var dbContext = new TrailMeModelContainer())
            {
                DAL.Model.User newUser = new User()
                {
                    Id = Guid.NewGuid(),
                    MailAddress = mailaddress,
                    LastName = last_name,
                    FirstName = first_name,
                    City = city,
                    Birthdate = birthdate
                };

                dbContext.Users.Add(newUser);

                // Save the changes to the database, and record the number of changes
                var changesSaved = dbContext.SaveChanges();

                // Return a bool based on whether any changes have been stored
                return changesSaved >= 1;
            }
        }

        public static bool DeleteUser(Guid user_id)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                dbContext.Users.Remove(dbContext.Users.Where(user => user.Id == user_id).First());

                // Save the changes to the database, and record the number of changes
                var changesSaved = dbContext.SaveChanges();

                // Return a bool based on whether any changes have been stored
                return changesSaved >= 1;
            }
        }

        public static bool AddUserToGroup(Guid group_id, Guid user_id)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                var matchingGroup = dbContext.Groups.Where(group => group.Id == group_id).First();
                var matchingUser = dbContext.Users.Where(user => user.Id == user_id).First();

                matchingGroup.Users.Add(matchingUser);

                // Save the changes to the database, and record the number of changes
                var changesSaved = dbContext.SaveChanges();

                // Return a bool based on whether any changes have been stored
                return changesSaved >= 1;
            }
        }

        public static User GetUserById(Guid user_id)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                return dbContext.Users.Find(user_id);
            }
        }

        public static IEnumerable<User> GetUsers()
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                return dbContext.Users.Include("Groups").ToList();
            }
        } 

        #endregion
    }
}
