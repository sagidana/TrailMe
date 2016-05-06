using Microsoft.Owin.Hosting;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using TrailMe.GoogleCloudMessaging;

namespace TrailMe.WebServer
{
    public class TrailMeServer
    {
        #region Consts

        const string GCM_KEY = "AIzaSyCvU8Qs3VbnuwsQZh4LAYaOfogF9pALae0";

        const string POST_METHOD = "POST";
        const string GET_METHOD = "GET";
        const string PUTS_METHOD = "PUTS";

        const string USERS_URL = "/users";
        const string GROUPS_URL = "/groups";
        const string TRACKS_URL = "/tracks";
        const string RECOMMENDATIONS_URL = "/recommendations";
        const string REGISTER_URL = "/register";

        #endregion

        #region Data Members

        IDisposable m_Server;
        GcmManager m_GcmManager;

        #endregion

        #region C-tors

        public TrailMeServer()
        {
            m_GcmManager = GcmManager.GetInstance();
            m_GcmManager.Key = GCM_KEY;

            intializeResources();
        }

        #endregion

        #region Public Methods

        public void Start(string url = "http://+:80/")
        {
            m_Server = WebApp.Start<Startup>(url);
        }

        public void Stop()
        {
            if (m_Server != null)
                m_Server.Dispose();
        }

        #endregion

        #region Private Methods

        private void intializeResources()
        {
            Startup.Resources = new List<WebResource>();

            // gets.
            Startup.Resources.Add(new WebResource { Path = REGISTER_URL, Method = POST_METHOD, Handler = registerClient });
            Startup.Resources.Add(new WebResource { Path = USERS_URL, Method = GET_METHOD, Handler = getAllUsers });
            Startup.Resources.Add(new WebResource { Path = GROUPS_URL, Method = GET_METHOD, Handler = getAllGroups });
            Startup.Resources.Add(new WebResource { Path = TRACKS_URL, Method = GET_METHOD, Handler = getAllTracks });

            // posts.
            Startup.Resources.Add(new WebResource { Path = USERS_URL, Method = POST_METHOD, Handler = getUser });
            Startup.Resources.Add(new WebResource { Path = GROUPS_URL, Method = POST_METHOD, Handler = getGroup });
            Startup.Resources.Add(new WebResource { Path = TRACKS_URL, Method = POST_METHOD, Handler = getTrack });
            Startup.Resources.Add(new WebResource { Path = RECOMMENDATIONS_URL, Method = POST_METHOD, Handler = getRecommendations });

            // puts.
            Startup.Resources.Add(new WebResource { Path = USERS_URL, Method = PUTS_METHOD, Handler = addUsers });
            Startup.Resources.Add(new WebResource { Path = GROUPS_URL, Method = PUTS_METHOD, Handler = addGroups });
            Startup.Resources.Add(new WebResource { Path = TRACKS_URL, Method = PUTS_METHOD, Handler = addTracks });
        }

        #region Get

        private void getRecommendations(Microsoft.Owin.IOwinContext context)
        {
            //
        }

        #region GetAll

        private void getAllUsers(Microsoft.Owin.IOwinContext context)
        {
            //
        }

        private void getAllTracks(Microsoft.Owin.IOwinContext context)
        {
        }

        private void getAllGroups(Microsoft.Owin.IOwinContext context)
        {
        }

        #endregion

        #region GetOne

        private void getUser(Microsoft.Owin.IOwinContext context)
        {
            //
        }

        private void getTrack(Microsoft.Owin.IOwinContext context)
        {
        }

        private void getGroup(Microsoft.Owin.IOwinContext context)
        {
        }

        #endregion 

        #endregion

        #region Add

        private void addUsers(Microsoft.Owin.IOwinContext context)
        {
            //
        }

        private void addTracks(Microsoft.Owin.IOwinContext context)
        {
        }

        private void addGroups(Microsoft.Owin.IOwinContext context)
        {
        }

        #endregion

        private void registerClient(Microsoft.Owin.IOwinContext context)
        {
            JObject jsonRequest = getJsonFromRequest(context);
            string token = jsonRequest.GetValue("registerationID").Value<string>();

            m_GcmManager.RegisterClient(token);
        }

        private JObject getJsonFromRequest(Microsoft.Owin.IOwinContext context)
        {
            string body = new StreamReader(context.Request.Body).ReadToEnd();
            JObject jsonRequest = JObject.Parse(body);
            return jsonRequest;
        }

        #endregion
    }
}
