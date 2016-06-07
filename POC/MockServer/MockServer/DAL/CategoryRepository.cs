using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TrailMe.DAL.Model;

namespace TrailMe.DAL
{
    public class CategoryRepository
    {
        #region Static Methods

        public static void AddCategory(string category_name)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                Category category = new Category
                {
                    Id = Guid.NewGuid(),
                    Name = category_name
                };

                dbContext.Categories.Add(category);
                dbContext.SaveChanges();
            }
        }

        public static void DeleteCategory(Guid category_id)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                var category = dbContext.Categories.Find(category_id);

                foreach (var track in category.Tracks)
                    track.Categories.Remove(category);
                
                dbContext.Categories.Remove(category);
                dbContext.SaveChanges();
            }
        }

        public static IEnumerable<Category> GetCategories()
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                return dbContext.Categories.ToList();
            }
        }

        #endregion
    }
}
