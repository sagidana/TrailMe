using Microsoft.Owin.Hosting;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using TrailMe.Apriori;
using TrailMe.DAL;
using TrailMe.GoogleCloudMessaging;
using TrailMe.DAL.Model;

namespace TrailMe.WebServer
{
    public class TrailMeServer
    {
        #region Consts

        const string GCM_KEY = "AIzaSyCvU8Qs3VbnuwsQZh4LAYaOfogF9pALae0";

        const string POST_METHOD = "POST";
        const string GET_METHOD = "GET";
        const string PUT_METHOD = "PUT";
        const string DELETE_METHOD = "DELETE";

        const string USERS_URL = "/users";
        const string GROUPS_URL = "/groups";
        const string TRACKS_URL = "/tracks";
        const string RECOMMENDATIONS_URL = "/recommendations";
        const string REGISTER_URL = "/register";

        const string JSON_TYPE = "application/json";

        #endregion

        #region Data Members

        IDisposable m_Server;
        GcmManager m_GcmManager;
        AprioriManager m_AprioriManager;

        #endregion

        #region C-tors

        public TrailMeServer()
        {
            m_GcmManager = GcmManager.GetInstance();
            m_AprioriManager = new AprioriManager();
            m_GcmManager.Key = GCM_KEY;

            intializeResources();
        }

        #endregion

        #region Public Methods

        public void Start(string url = "http://trailmedev.cloudapp.net:9900/")
        {
            m_Server = WebApp.Start<Startup>(url);
            m_AprioriManager.Start();
        }

        public void Stop()
        {
            m_AprioriManager.Stop();

            if (m_Server != null)
                m_Server.Dispose();
        }

        #endregion

        #region Private Methods

        private void intializeResources()
        {
            Startup.Resources = new List<WebResource>();

            // gets.
            Startup.Resources.Add(new WebResource { Path = USERS_URL, Method = GET_METHOD, Handler = getAllUsers });
            Startup.Resources.Add(new WebResource { Path = GROUPS_URL, Method = GET_METHOD, Handler = getAllGroups });
            Startup.Resources.Add(new WebResource { Path = TRACKS_URL, Method = GET_METHOD, Handler = getAllTracks });

            // posts.
            Startup.Resources.Add(new WebResource { Path = REGISTER_URL, Method = POST_METHOD, Handler = registerClient });
            Startup.Resources.Add(new WebResource { Path = USERS_URL, Method = POST_METHOD, Handler = getUser });
            Startup.Resources.Add(new WebResource { Path = GROUPS_URL, Method = POST_METHOD, Handler = getGroup });
            Startup.Resources.Add(new WebResource { Path = TRACKS_URL, Method = POST_METHOD, Handler = getTrack });
            Startup.Resources.Add(new WebResource { Path = RECOMMENDATIONS_URL, Method = POST_METHOD, Handler = getRecommendations });

            // puts.
            Startup.Resources.Add(new WebResource { Path = USERS_URL, Method = PUT_METHOD, Handler = addUser });
            Startup.Resources.Add(new WebResource { Path = GROUPS_URL, Method = PUT_METHOD, Handler = addGroup });
            Startup.Resources.Add(new WebResource { Path = TRACKS_URL, Method = PUT_METHOD, Handler = addTrack });

            // deletes.
            Startup.Resources.Add(new WebResource { Path = USERS_URL, Method = DELETE_METHOD, Handler = deleteUser });
            Startup.Resources.Add(new WebResource { Path = GROUPS_URL, Method = DELETE_METHOD, Handler = deleteGroup });
            Startup.Resources.Add(new WebResource { Path = TRACKS_URL, Method = DELETE_METHOD, Handler = deleteTrack });
        }

        private void createWebResponse(Microsoft.Owin.IOwinContext context, string dataType, string data)
        {
            context.Response.ContentType = dataType;
            context.Response.Write(data);
        }

        private JObject getJsonFromRequest(Microsoft.Owin.IOwinContext context)
        {
            string body = new StreamReader(context.Request.Body).ReadToEnd();
            JObject jsonRequest = JObject.Parse(body);
            return jsonRequest;
        }

        #region Get

        private void getAllUsers(Microsoft.Owin.IOwinContext context)
        {
            var dbUsers = UserRepository.GetUsers();
            JObject users = convertDbUsersToJson(dbUsers);

            createWebResponse(context, JSON_TYPE, users.ToString());
        }

        private void getAllTracks(Microsoft.Owin.IOwinContext context)
        {
            var dbTracks= TrackRepository.GetTracks();
            JObject tracks = convertDbTracksToJson(dbTracks);

            createWebResponse(context, JSON_TYPE, tracks.ToString());
        }

        private void getAllGroups(Microsoft.Owin.IOwinContext context)
        {
            var dbGroups = GroupRepository.GetGroups();
            JObject groups = convertDbGroupsToJson(dbGroups);

            createWebResponse(context, JSON_TYPE, groups.ToString());
        }

        #endregion

        #region Post

        private void registerClient(Microsoft.Owin.IOwinContext context)
        {
            JObject jsonRequest = getJsonFromRequest(context);
            string token = jsonRequest.GetValue("registerationID").Value<string>();

            m_GcmManager.RegisterClient(token);
        }

        private void getRecommendations(Microsoft.Owin.IOwinContext context)
        {
            
        }

        private void getUser(Microsoft.Owin.IOwinContext context)
        {
            JObject request = getJsonFromRequest(context);
            Guid userId = Guid.Parse(request["id"].ToString());

            var dbUser = UserRepository.GetUserById(userId);

            JObject user = convertDbUserToJson(dbUser);

            createWebResponse(context, JSON_TYPE, user.ToString());
        }

        private void getTrack(Microsoft.Owin.IOwinContext context)
        {
            JObject request = getJsonFromRequest(context);
            Guid trackId = Guid.Parse(request["id"].ToString());

            var dbTrack = TrackRepository.GetTrackById(trackId);

            JObject track = convertDbTrackToJson(dbTrack);

            createWebResponse(context, JSON_TYPE, track.ToString());
        }

        private void getGroup(Microsoft.Owin.IOwinContext context)
        {
            JObject request = getJsonFromRequest(context);
            Guid groupId = Guid.Parse(request["id"].ToString());

            var dbGroup = GroupRepository.GetGroupById(groupId);

            JObject group = convertDbGroupToJson(dbGroup);

            createWebResponse(context, JSON_TYPE, group.ToString());
        }

        #endregion 

        #region Put

        private void addUser(Microsoft.Owin.IOwinContext context)
        {
            JObject request = getJsonFromRequest(context);
            var dbUser = convertJsonToDbUser(request);

            UserRepository.AddUser( dbUser.MailAddress, 
                                    dbUser.LastName, 
                                    dbUser.FirstName, 
                                    dbUser.City, 
                                    dbUser.BirthDate.Value);
        }

        private void addTrack(Microsoft.Owin.IOwinContext context)
        {
            JObject request = getJsonFromRequest(context);
            var dbTrack = convertJsonToDbTrack(request);

            TrackRepository.AddTrack(   dbTrack.TrackName, 
                                        dbTrack.LocationZone, 
                                        dbTrack.DistanceKM.Value, 
                                        dbTrack.LevelOfDifficulty, 
                                        dbTrack.Latitude, 
                                        dbTrack.Longitude);
        }

        private void addGroup(Microsoft.Owin.IOwinContext context)
        {
            JObject request = getJsonFromRequest(context);
            var dbGroup = convertJsonToDbGroup(request);

            GroupRepository.AddGroup(dbGroup.GroupName);
        }

        #endregion

        #region Delete

        private void deleteUser(Microsoft.Owin.IOwinContext context)
        {
            JObject request = getJsonFromRequest(context);
            Guid userId = Guid.Parse(request["id"].ToString());

            UserRepository.DeleteUser(userId);
        }

        private void deleteTrack(Microsoft.Owin.IOwinContext context)
        {
            JObject request = getJsonFromRequest(context);
            Guid trackId = Guid.Parse(request["id"].ToString());

            TrackRepository.DeleteTrack(trackId);
        }

        private void deleteGroup(Microsoft.Owin.IOwinContext context)
        {
            JObject request = getJsonFromRequest(context);
            Guid groupId = Guid.Parse(request["id"].ToString());

            GroupRepository.DeleteGroup(groupId);
        }

        #endregion

        #region Database Converters

        #region From DB

        private void addDbUserToJson(JObject json, ICollection<User> users)
        {
            JArray jUsers = new JArray();

            foreach (var user in users)
            {
                JObject jUser = new JObject();

                jUser.Add("Id", user.UserID);
                jUser.Add("Id", user.FirstName);
                jUser.Add("Id", user.LastName);
                jUser.Add("Id", user.MailAddress);
                jUser.Add("Id", user.City);
                jUser.Add("Id", user.BirthDate);

                jUsers.Add(jUsers);
            }

            json.Add("Users", jUsers);
        }

        private void addDbGroupsToJson(JObject json, ICollection<Group> groups)
        {
            JArray jGroups = new JArray();
            
            foreach (var group in groups)
            {
                JObject jGroup = new JObject();

                jGroup.Add("Id", group.Groupid);
                jGroup.Add("Name", group.GroupName);

                jGroups.Add(jGroup);
            }

            json.Add("Groups", jGroups);
        }

        private void addEventsToJson(JObject json, ICollection<Event> events)
        {
            JArray jEvents = new JArray();

            foreach (var curEvent in events)
            {
                JObject jEvent = new JObject();

                jEvent.Add("Id", curEvent.EventID);
                jEvent.Add("Name", curEvent.EventName);
                jEvent.Add("StartDate", curEvent.StartDate);
                jEvent.Add("EndDate", curEvent.EndDate);

                jEvents.Add(jEvent);
            }

            json.Add("Events", jEvents);
        }

        private JObject convertDbUserToJson(DAL.Model.User dbUser)
        {
            JObject user = new JObject();
            
            user.Add("FirstName", dbUser.FirstName);
            user.Add("LastName", dbUser.LastName);
            user.Add("MailAddress", dbUser.MailAddress);
            user.Add("City", dbUser.City);
            user.Add("BirthDate", dbUser.BirthDate);

            addDbGroupsToJson(user, dbUser.Groups);

            return user;
        }

        private JObject convertDbTrackToJson(DAL.Model.Track dbTrack)
        {
            JObject track = new JObject();

            track.Add("Id", dbTrack.TrackID);
            track.Add("Name", dbTrack.TrackName);
            track.Add("Latitude", dbTrack.Latitude);
            track.Add("Longitude", dbTrack.Longitude);
            track.Add("Zone", dbTrack.LocationZone);
            track.Add("Difficulty", dbTrack.LevelOfDifficulty);
            track.Add("DistanceKM", dbTrack.DistanceKM);

            addEventsToJson(track, dbTrack.Events);

            return track;
        }

        private JObject convertDbGroupToJson(DAL.Model.Group dbGroup)
        {
            JObject jGroup = new JObject();

            jGroup.Add("Id", dbGroup.Groupid);
            jGroup.Add("Name", dbGroup.GroupName);

            addDbUserToJson(jGroup, dbGroup.Users);

            return jGroup;
        }

        private JObject convertDbUsersToJson(IEnumerable<DAL.Model.User> dbUsers)
        {
            JObject users = new JObject();
            JArray arrayUsers = new JArray();

            foreach(var dbUser in dbUsers)
                arrayUsers.Add(convertDbUserToJson(dbUser));

            users.Add("Users", arrayUsers);
            return users;
        }

        private JObject convertDbTracksToJson(IEnumerable<Track> dbTracks)
        {
            JObject tracks = new JObject();
            JArray arrayTracks = new JArray();

            foreach (var dbTrack in dbTracks)
                arrayTracks.Add(convertDbTrackToJson(dbTrack));

            tracks.Add("Tracks", arrayTracks);
            return tracks;
        }

        private JObject convertDbGroupsToJson(IEnumerable<DAL.Model.Group> dbGroups)
        {
            JObject groups = new JObject();
            JArray arrayGroups= new JArray();

            foreach (var dbGroup in dbGroups)
                arrayGroups.Add(convertDbGroupToJson(dbGroup));

            groups.Add("Groups", arrayGroups);

            return groups;
        }

        #endregion

        #region To DB

        private DAL.Model.User convertJsonToDbUser(JObject user)
        {
            return null;
        }

        private DAL.Model.Track convertJsonToDbTrack(JObject track)
        {
            return null;
        }

        private DAL.Model.Group convertJsonToDbGroup(JObject group)
        {
            return null;
        }

        #endregion

        #endregion

        #endregion
    }
}
