using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TrailMe.Common
{
    public class Result
    {
        #region Properties

        public List<Rule> StrongRules { get; set; }

        public List<Item> FrequentItems { get; set; }

        #endregion

    }
}
