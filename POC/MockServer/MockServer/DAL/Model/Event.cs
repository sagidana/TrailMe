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
    
    public partial class Event
    {
        public System.Guid Id { get; set; }
        public string Name { get; set; }
    
        public virtual Track Track { get; set; }
        public virtual Group Group { get; set; }
    }
}
