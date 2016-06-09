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

        public static void AddSkillToUser(Guid user_id, Guid skill_id)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                User user = dbContext.Users.Find(user_id);
                Skill skill = dbContext.Skills.Find(skill_id);

                skill.Users.Add(user);
                user.Skills.Add(skill);

                var changesSaved = dbContext.SaveChanges();
            }
        }

        public static void AddLanguageToUser(Guid user_id, Guid language_id)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                User user = dbContext.Users.Find(user_id);
                Language language = dbContext.Languages.Find(language_id);

                user.Languages.Add(language);
                language.Users.Add(user);

                var changesSaved = dbContext.SaveChanges();
            }
        }

        public static bool AddUser(string mailaddress, string last_name, string first_name, string city, DateTime? birthdate, string password_user, string gender)
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
                    Birthdate = birthdate,
                    PasswordUser = password_user,
                    Gender = gender

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
                var user = dbContext.Users.Find(user_id);

                foreach (var language in user.Languages)
                    language.Users.Remove(user);
                foreach (var track in user.Tracks)
                    track.Users.Remove(user);
                foreach (var skill in user.Skills)
                    skill.Users.Remove(user);
                foreach (var group in user.Groups)
                    group.Users.Remove(user);

                dbContext.Users.Remove(user);

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
                var group = dbContext.Groups.Find(group_id);
                var user = dbContext.Users.Find(user_id);

                user.Groups.Add(group);
                group.Users.Add(user);

                var changesSaved = dbContext.SaveChanges();
                return changesSaved >= 1;
            }
        }

        public static User GetUserById(Guid user_id)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                User user = dbContext.Users.Find(user_id);
                user.Languages = LanguageRepository.GetLanguagesByUserId(user_id).ToList<Language>();

                return user;
            }
        }

        public static User GetUserByName(string userName)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                User user = dbContext.Users.SingleOrDefault((item) => item.MailAddress == userName);
                user.Languages = LanguageRepository.GetLanguagesByUserId(user.Id).ToList<Language>();
                return user;
            }
        }

        public static IEnumerable<User> GetUsers()
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                return dbContext.Users.Include("Groups").ToList();
            }
        }

        public static IEnumerable<User> getUsersByGroupId(Guid id)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                return dbContext.Users.Where(user => user.Groups.Where(group => group.Id == id).Any()).ToList();
            }
        }

        public static IEnumerable<User> getUsersByTrackId(Guid id)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                return dbContext.Users.Where(user => user.Tracks.Where(track => track.Id == id).Any()).ToList();
            }
        }

        public static int getAgeByUserId(Guid id)
        {
            int userAge = 0;
            using (var dbContext = new TrailMeModelContainer())
            {
                User currentUser = dbContext.Users.SingleOrDefault(user => user.Id == id);
                DateTime? bday = currentUser.Birthdate;
                
                if (bday != null)
                {
                    DateTime today = DateTime.Today;
                    userAge = today.Year - bday.Value.Year;

                    if (bday > today.AddYears(-userAge))
                        userAge--;
                }
            }

            return userAge;
        }

        #endregion
    }
}
