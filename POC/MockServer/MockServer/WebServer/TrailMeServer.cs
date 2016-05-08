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
            JArray users = convertDbUsersToJson(dbUsers);

            createWebResponse(context, JSON_TYPE, users.ToString());
        }

        private void getAllTracks(Microsoft.Owin.IOwinContext context)
        {
            var dbTracks= TrackRepository.GetTracks();
            JArray tracks = convertDbTracksToJson(dbTracks);

            createWebResponse(context, JSON_TYPE, tracks.ToString());
        }

        private void getAllGroups(Microsoft.Owin.IOwinContext context)
        {
            var dbGroups = GroupRepository.GetGroups();
            JArray groups = convertDbGroupsToJson(dbGroups);

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
                                    dbUser.Birthdate);
        }

        private void addTrack(Microsoft.Owin.IOwinContext context)
        {
            JObject request = getJsonFromRequest(context);
            var dbTrack = convertJsonToDbTrack(request);

            TrackRepository.AddTrack(   dbTrack.Name, 
                                        dbTrack.Zone, 
                                        dbTrack.Kilometers, 
                                        dbTrack.Difficulty, 
                                        dbTrack.Latitude, 
                                        dbTrack.Longitude);
        }

        private void addGroup(Microsoft.Owin.IOwinContext context)
        {
            JObject request = getJsonFromRequest(context);
            var dbGroup = convertJsonToDbGroup(request);

            GroupRepository.AddGroup(dbGroup.Name);
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

                jUser.Add("Id", user.Id);
                jUser.Add("FirstName", user.FirstName);
                jUser.Add("LastName", user.LastName);
                jUser.Add("MailAddress", user.MailAddress);
                jUser.Add("City", user.City);
                jUser.Add("Birthdate", user.Birthdate);

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

                jGroup.Add("Id", group.Id);
                jGroup.Add("Name", group.Name);

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

                jEvent.Add("Id", curEvent.Id);
                jEvent.Add("Name", curEvent.Name);
                jEvent.Add("TrackId", curEvent.Track.Id);
                jEvent.Add("GroupId", curEvent.Group.Id);

                jEvents.Add(jEvent);
            }

            json.Add("Events", jEvents);
        }

        private JObject convertDbUserToJson(DAL.Model.User dbUser)
        {
            JObject user = new JObject();

            user.Add("Id", dbUser.Id);
            user.Add("FirstName", dbUser.FirstName);
            user.Add("LastName", dbUser.LastName);
            user.Add("MailAddress", dbUser.MailAddress);
            user.Add("City", dbUser.City);
            user.Add("BirthDate", dbUser.Birthdate);

            addDbGroupsToJson(user, dbUser.Groups);

            return user;
        }

        private JObject convertDbTrackToJson(DAL.Model.Track dbTrack)
        {
            JObject track = new JObject();

            track.Add("Id", dbTrack.Id);
            track.Add("Name", dbTrack.Name);
            track.Add("Latitude", dbTrack.Latitude);
            track.Add("Longitude", dbTrack.Longitude);
            track.Add("Zone", dbTrack.Zone);
            track.Add("Difficulty", dbTrack.Difficulty);
            track.Add("Kilometers", dbTrack.Kilometers);

            addEventsToJson(track, dbTrack.Events);

            return track;
        }

        private JObject convertDbGroupToJson(DAL.Model.Group dbGroup)
        {
            JObject jGroup = new JObject();

            jGroup.Add("Id", dbGroup.Id);
            jGroup.Add("Name", dbGroup.Name);

            addDbUserToJson(jGroup, dbGroup.Users);

            return jGroup;
        }

        private JArray convertDbUsersToJson(IEnumerable<DAL.Model.User> dbUsers)
        {
            JArray arrayUsers = new JArray();

            foreach(var dbUser in dbUsers)
                arrayUsers.Add(convertDbUserToJson(dbUser));

            return arrayUsers;
        }

        private JArray convertDbTracksToJson(IEnumerable<Track> dbTracks)
        {
            JArray arrayTracks = new JArray();

            foreach (var dbTrack in dbTracks)
                arrayTracks.Add(convertDbTrackToJson(dbTrack));

            return arrayTracks;
        }

        private JArray convertDbGroupsToJson(IEnumerable<DAL.Model.Group> dbGroups)
        {
            JArray arrayGroups= new JArray();

            foreach (var dbGroup in dbGroups)
                arrayGroups.Add(convertDbGroupToJson(dbGroup));

            return arrayGroups;
        }

        #endregion

        #region To DB

        private DAL.Model.User convertJsonToDbUser(JObject user)
        {
            DAL.Model.User dbUser = new DAL.Model.User
            {
                FirstName = user["FirstName"].Value<string>(),
                LastName = user["LastName"].Value<string>(),
                MailAddress = user["MailAddress"].Value<string>(),
                City = user["City"].Value<string>(),
                Birthdate = user["Birthdate"].Value<DateTime>()
            };

            return dbUser;
        }

        private DAL.Model.Track convertJsonToDbTrack(JObject track)
        {
            DAL.Model.Track dbTrack = new DAL.Model.Track()
            {
                Name = track["Name"].Value<string>(),
                Zone = track["Zone"].Value<string>(),
                Kilometers = track["Kilometers"].Value<int>(),
                Difficulty = track["Difficulty"].Value<string>(),
                Latitude = track["Latitude"].Value<double>(),
                Longitude = track["Longitude"].Value<double>()
            };

            return dbTrack;
        }

        private DAL.Model.Group convertJsonToDbGroup(JObject group)
        {
            DAL.Model.Group dbGroup = new DAL.Model.Group() {
                Name = group["Name"].Value<string>(),
            };

            return dbGroup;
        }

        #endregion

        #endregion

        #endregion
    }
}
