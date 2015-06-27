using AutoMapper;
using HearthyWebApi.Entities;
using HearthyWebApi.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace HearthyWebApi.App_Start
{
    public class AutomapperConfig
    {
        public static void RegisterAutomapper()
        {
            Mapper.CreateMap<User, UserModel>()
                .ForMember(x => x.HeartRates, opts => opts.Ignore());

            Mapper.CreateMap<Location, LocationModel>();

            Mapper.CreateMap<HeartRate, HeartRateModel>()
                .ForMember(x => x.User, opts => opts.Ignore());
        }
    }
}