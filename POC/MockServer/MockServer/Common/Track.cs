using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TrailMe.Common
{
    public class Track
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
    }
}
