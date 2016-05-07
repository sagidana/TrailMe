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
            this.Longitude = 0D;
            this.Users = new HashSet<User>();
            this.Events = new HashSet<Event>();
            this.Categories = new HashSet<Category>();
        }
    
        public System.Guid Id { get; set; }
        public string Name { get; set; }
        public string Zone { get; set; }
        public int Kilometers { get; set; }
        public string Difficulty { get; set; }
        public Nullable<double> Latitude { get; set; }
        public double Longitude { get; set; }
    
        public virtual ICollection<User> Users { get; set; }
        public virtual ICollection<Event> Events { get; set; }
        public virtual ICollection<Category> Categories { get; set; }
    }
}
