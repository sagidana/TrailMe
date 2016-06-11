using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TrailMe.Common
{
    public class Track : IComparable<Track>
    {
        public Guid TrackId { get; set; }

        public override bool Equals(object obj)
        {
            var item = obj as Track;

            if (item == null)
            {
                return false;
            }

            return TrackId.Equals(item.TrackId);
        }

        public int CompareTo(Track other)
        {
            return TrackId.ToString().CompareTo(other.TrackId.ToString());
        }

        public override int GetHashCode()
        {
            return TrackId.ToString().GetHashCode();
        }
    }
}
