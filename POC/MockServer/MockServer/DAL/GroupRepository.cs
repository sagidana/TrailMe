//using System;
//using System.Collections.Generic;
//using System.Linq;
//using System.Text;
//using System.Threading.Tasks;

//namespace TrailMe.DAL
//{
//    public class GroupRepository
//    {
//        #region Static Methods

//        public static bool AddGroup(string group_name)
//        {
//            // Create the database context
//            using (var dbContext = new TrailmeProject.TrailMeDBEntities())
//            {

//                dbContext.insert_Group(group_name);

//                // Save the changes to the database, and record the number of changes
//                var changesSaved = dbContext.SaveChanges();

//                // Return a bool based on whether any changes have been stored
//                return changesSaved >= 1;
//            }
//        }

//        public static bool DeleteGroup(Guid group_id)
//        {

//            using (var dbContext = new TrailmeProject.TrailMeDBEntities())
//            {
//                dbContext.delete_Group(group_id);

//                // Save the changes to the database, and record the number of changes
//                var changesSaved = dbContext.SaveChanges();

//                // Return a bool based on whether any changes have been stored
//                return changesSaved >= 1;
//            }
//        } 
        
//        #endregion
//    }
//}
