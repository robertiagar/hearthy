using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HearthyWebApi.Entities
{
    public class User
    {
        public Guid UserId { get; set; }
        public string Username { get; set; }

        public virtual IList<HeartRate> HeartRates { get; set; }
    }
}
