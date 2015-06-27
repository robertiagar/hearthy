using HearthyWebApi.Controllers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HearthyWebApi.Models
{
    public class UserModel
    {
        public Guid UserId { get; set; }
        public string Username { get; set; }

        public IList<HeartRateModel> HeartRates { get; set; }
    }
}
