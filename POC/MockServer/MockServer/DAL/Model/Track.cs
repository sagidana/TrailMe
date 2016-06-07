//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace TrailMe.DAL.Model
{
    using System;
    using System.Collections.Generic;
    
    public partial class Track
    {
        public Track()
        {
            this.Users = new HashSet<User>();
            this.Categories = new HashSet<Category>();
            this.Events = new HashSet<Event>();
        }
    
        public System.Guid Id { get; set; }
        public string Name { get; set; }
        public string Zone { get; set; }
        public int Kilometers { get; set; }
        public string Difficulty { get; set; }
        public Nullable<double> Latitude { get; set; }
        public Nullable<double> Longitude { get; set; }
        public Nullable<int> Rating { get; set; }
    
        public virtual ICollection<User> Users { get; set; }
        public virtual ICollection<Category> Categories { get; set; }
        public virtual ICollection<Event> Events { get; set; }
    }
}
