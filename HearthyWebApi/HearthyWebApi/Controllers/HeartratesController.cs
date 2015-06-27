using HearthyWebApi.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace HearthyWebApi.Controllers
{
    public class HeartratesController : ApiController
    {
        public Guid Post([FromBody]HeartRateModel value)
        {
            if (value.User.UserId == null)
            {
                value.User.UserId = new Guid();
                using (var context = ApplicationDbContext.Create())
                {

                }
            }

            return value.User.UserId;
        }
    }
}
