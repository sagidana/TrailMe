using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TrailMe.DAL.Model;

namespace TrailMe.DAL
{
    public class LanguageRepository
    {
        #region Static Methods

        public static void AddLanguage(string language_name)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                dbContext.Languages.Add(new Language { Id = Guid.NewGuid(), Name = language_name});
                dbContext.SaveChanges();
            }
        }

        public static void DeleteLanguage(Guid language_id)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                var language = dbContext.Languages.Find(language_id);

                foreach (var user in language.Users)
                    user.Languages.Remove(language);

                dbContext.Languages.Remove(language);
                dbContext.SaveChanges();
            }
        }

        public static IEnumerable<Language> GetLanguages()
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                return dbContext.Languages.ToList();
            }
        }

        #endregion
    }
}
