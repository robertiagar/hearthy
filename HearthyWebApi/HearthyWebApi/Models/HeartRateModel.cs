using HearthyWebApi.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using HearthyWebApi.Models;

namespace HearthyWebApi.Models
{
    public class HeartRateModel
    {
        public virtual UserModel User{ get; set; }
        public DateTime Timestamp { get; set; }
        public int Heartrate { get; set; }
        public ActivityType ActivityType { get; set; }
        public LocationModel Location { get; set; }
    }

    public class LocationModel
    {
        public float Latitude { get; set; }
        public float Longitude { get; set; }
    }
}
