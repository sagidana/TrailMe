using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TrailMe.Common
{
    public class Item : IEqualityComparer<Item>
    {
        public List<Track> Tracks { get; set; }

        public double Support { get; set; }

        public override bool Equals(object obj)
        {
            var item = obj as Item;

            if (item == null)
            {
                return false;
            }

            foreach (var track in Tracks)
                if (!item.Tracks.Any(curr => curr.TrackId == track.TrackId))
                    return false;

            foreach (var track in item.Tracks)
                if (!Tracks.Any(curr => curr.TrackId == track.TrackId))
                    return false;

            return true;
        }

        public override int GetHashCode()
        {
            Tracks.Sort();
            string tracksAsString = String.Empty;

            foreach (var track in Tracks)
                tracksAsString += track.TrackId.ToString();
            return tracksAsString.GetHashCode();
        }

        public bool Equals(Item x, Item y)
        {
            return x == y;
        }

        public int GetHashCode(Item obj)
        {
            string tracksAsString = String.Empty;
            
            foreach (var track in obj.Tracks)
                tracksAsString += track.TrackId.ToString();
            return tracksAsString.GetHashCode();
        }
    }
}
