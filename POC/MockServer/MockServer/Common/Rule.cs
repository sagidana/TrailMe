using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TrailMe.Common
{
    public class Rule
    {
        public List<Track> From { get; set; }

        public List<Track> To { get; set; }

        public double Confidence { get; set; }
    }
}
