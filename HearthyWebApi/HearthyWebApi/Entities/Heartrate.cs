using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace HearthyWebApi.Entities
{
    public class HeartRate
    {
        public virtual User User{ get; set; }
        public DateTime Timestamp { get; set; }
        public int Heartrate { get; set; }
        public ActivityType ActivityType { get; set; }
        public Location Location { get; set; }
    }

    public enum ActivityType
    {
        IN_VEHICLE,
        ON_BICYCLE,
        ON_FOOT,
        RUNNING,
        STILL,
        TILTING,
        UNKNOWN,
        WALKING
    }

    public class Location
    {
        public float Latitude { get; set; }
        public float Longitude { get; set; }

    }
}