//using System;
//using System.Collections.Generic;
//using System.Linq;
//using System.Text;
//using System.Threading.Tasks;

//namespace TrailMe.DAL
//{
//    public class UserRepository
//    {
//        public static bool AddUser(string mailaddress, string last_name, string first_name, string city, DateTime birthdate)
//        {
//            // Create the database context
//            using (var dbContext = new TrailmeProject.TrailMeDBEntities())
//            {
//                dbContext.insert_User(mailaddress, last_name, first_name, city, birthdate);


//                // Save the changes to the database, and record the number of changes
//                var changesSaved = dbContext.SaveChanges();

//                // Return a bool based on whether any changes have been stored
//                return changesSaved >= 1;
//            }
//        }

//        public static bool DeleteUser(Guid user_id)
//        {

//            using (var dbContext = new TrailmeProject.TrailMeDBEntities())
//            {
//                dbContext.delete_User(user_id);

//                // Save the changes to the database, and record the number of changes
//                var changesSaved = dbContext.SaveChanges();

//                // Return a bool based on whether any changes have been stored
//                return changesSaved >= 1;
//            }
//        }

//        public static bool AddUserToGroup(Guid group_id, Guid user_id)
//        {

//            using (var dbContext = new TrailmeProject.TrailMeDBEntities())
//            {
//                dbContext.add_user_to_group(group_id, user_id);

//                // Save the changes to the database, and record the number of changes
//                var changesSaved = dbContext.SaveChanges();

//                // Return a bool based on whether any changes have been stored
//                return changesSaved >= 1;
//            }
//        }
//    }
//}
