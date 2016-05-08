using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TrailMe.DAL.Model;

namespace TrailMe.DAL
{
    public class SkillRepository
    {
        #region Static Methods

        public static void AddSkill(string skill_name)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                dbContext.Skills.Add(new Skill { Id = Guid.NewGuid(), Name = skill_name });
                dbContext.SaveChanges();
            }
        }

        public static void Deleteskill(Guid skill_id)
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                dbContext.Skills.Remove(dbContext.Skills.Find(skill_id));
                dbContext.SaveChanges();
            }
        }

        public static IEnumerable<Skill> GetSkills()
        {
            using (var dbContext = new TrailMeModelContainer())
            {
                return dbContext.Skills.ToList();
            }
        }

        #endregion
    }
}
