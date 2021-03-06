﻿using Microsoft.Owin.Hosting;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using TrailMe.Apriori;
using TrailMe.DAL;
using TrailMe.GoogleCloudMessaging;
using TrailMe.DAL.Model;
using Microsoft.Owin;
using System.Linq;
using System.Globalization;

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
        const string LOGIN_URL = "/login";
        const string GROUPS_URL = "/groups";
        const string TRACKS_URL = "/tracks";
        const string EVENTS_URL = "/events";
        const string SKILLS_URL = "/skills";
        const string CATEGORIES_URL = "/categories";
        const string LANGUAGES_URL = "/languages";
        const string METHODS_URL = "/methods";
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
            Startup.Resources.Add(new WebResource { Path = SKILLS_URL, Method = GET_METHOD, Handler = getAllSkills });
            Startup.Resources.Add(new WebResource { Path = LANGUAGES_URL, Method = GET_METHOD, Handler = getAllLanguages });
            Startup.Resources.Add(new WebResource { Path = CATEGORIES_URL, Method = GET_METHOD, Handler = getAllCategories });
            Startup.Resources.Add(new WebResource { Path = EVENTS_URL, Method = GET_METHOD, Handler = getAllEvents });


            // posts.
            Startup.Resources.Add(new WebResource { Path = REGISTER_URL, Method = POST_METHOD, Handler = registerClient });
            Startup.Resources.Add(new WebResource { Path = USERS_URL, Method = POST_METHOD, Handler = getUser });
            Startup.Resources.Add(new WebResource { Path = LOGIN_URL, Method = POST_METHOD, Handler = isAuthorizedUser });
            Startup.Resources.Add(new WebResource { Path = GROUPS_URL, Method = POST_METHOD, Handler = getGroup });
            Startup.Resources.Add(new WebResource { Path = TRACKS_URL, Method = POST_METHOD, Handler = getTrack });
            Startup.Resources.Add(new WebResource { Path = RECOMMENDATIONS_URL, Method = POST_METHOD, Handler = getRecommendations });
            Startup.Resources.Add(new WebResource { Path = METHODS_URL, Method = POST_METHOD, Handler = handleMethods });

            // puts.
            Startup.Resources.Add(new WebResource { Path = USERS_URL, Method = PUT_METHOD, Handler = addUser });
            Startup.Resources.Add(new WebResource { Path = GROUPS_URL, Method = PUT_METHOD, Handler = addGroup });
            Startup.Resources.Add(new WebResource { Path = TRACKS_URL, Method = PUT_METHOD, Handler = addTrack });
            Startup.Resources.Add(new WebResource { Path = SKILLS_URL, Method = PUT_METHOD, Handler = addSkill });
            Startup.Resources.Add(new WebResource { Path = LANGUAGES_URL, Method = PUT_METHOD, Handler = addLanguage });
            Startup.Resources.Add(new WebResource { Path = CATEGORIES_URL, Method = PUT_METHOD, Handler = addCategory });
            Startup.Resources.Add(new WebResource { Path = EVENTS_URL, Method = PUT_METHOD, Handler = addEvent });

            // deletes.
            Startup.Resources.Add(new WebResource { Path = USERS_URL, Method = DELETE_METHOD, Handler = deleteUser });
            Startup.Resources.Add(new WebResource { Path = GROUPS_URL, Method = DELETE_METHOD, Handler = deleteGroup });
            Startup.Resources.Add(new WebResource { Path = TRACKS_URL, Method = DELETE_METHOD, Handler = deleteTrack });
            Startup.Resources.Add(new WebResource { Path = SKILLS_URL, Method = DELETE_METHOD, Handler = deleteSkill });
            Startup.Resources.Add(new WebResource { Path = LANGUAGES_URL, Method = DELETE_METHOD, Handler = deleteLanguage });
            Startup.Resources.Add(new WebResource { Path = CATEGORIES_URL, Method = DELETE_METHOD, Handler = deleteCategory });
            Startup.Resources.Add(new WebResource { Path = EVENTS_URL, Method = DELETE_METHOD, Handler = deleteEvent });
        }

        private void createWebResponse(Microsoft.Owin.IOwinContext context, string dataType, string data)
        {
            context.Response.ContentType = dataType;
            context.Response.Write(data);
        }

        private JObject getJsonErrro()
        {
            JObject errorJson = new JObject();
            errorJson.Add("error",true);
            return errorJson;
        }

        private JObject getJsonFromRequest(Microsoft.Owin.IOwinContext context)
        {
            string body = new StreamReader(context.Request.Body).ReadToEnd();
            
            if(body != string.Empty)
            {
                JObject jsonRequest = JObject.Parse(body);
                return jsonRequest;
            }
            //body = body.Replace("\\\"", "\"");
            //body = body.Remove(body.IndexOf("\""), 1);
            //body = body.Remove(body.LastIndexOf("\""), 1);

            return getJsonErrro();
        }

        #region Get

        private void getAllEvents(Microsoft.Owin.IOwinContext context)
        {
            try
            {
                var dbEvents = EventRepository.GetEvents();
                JArray events = convertDbEventsToJson(dbEvents);
            
                createWebResponse(context, JSON_TYPE, events.ToString());
            }
            catch (Exception) { }
        }

        private void getAllSkills(Microsoft.Owin.IOwinContext context)
        {
            try
            {
                var dbSkills = SkillRepository.GetSkills();
                JArray skills = convertDbSkillsToJson(dbSkills);

                createWebResponse(context, JSON_TYPE, skills.ToString());
            }
            catch (Exception) { }
        }

        private void getAllCategories(Microsoft.Owin.IOwinContext context)
        {
            try
            {
                var dbCategories = CategoryRepository.GetCategories();
                JArray categories = convertDbCategoriesToJson(dbCategories);

                createWebResponse(context, JSON_TYPE, categories.ToString());
            }
            catch (Exception) { }
        }

        private void getAllLanguages(Microsoft.Owin.IOwinContext context)
        {
            try
            {
                var dbLanguages = LanguageRepository.GetLanguages();
                JArray languages = convertDbLanguagesToJson(dbLanguages);

                createWebResponse(context, JSON_TYPE, languages.ToString());
            }
            catch (Exception) { }
        }

        private void getAllUsers(Microsoft.Owin.IOwinContext context)
        {
            try
            {
                var dbUsers = UserRepository.GetUsers();
                JArray users = convertDbUsersToJson(dbUsers);

                createWebResponse(context, JSON_TYPE, users.ToString());
            }
            catch (Exception) { }
        }

        private void getAllTracks(Microsoft.Owin.IOwinContext context)
        {
            try
            {
                var dbTracks = TrackRepository.GetTracks();
                JArray tracks = convertDbTracksToJson(dbTracks);

                createWebResponse(context, JSON_TYPE, tracks.ToString());
            }
            catch (Exception) { }
        }

        private void getAllGroups(Microsoft.Owin.IOwinContext context)
        {
            try
            {
                var dbGroups = GroupRepository.GetGroups();
                JArray groups = convertDbGroupsToJson(dbGroups);

                createWebResponse(context, JSON_TYPE, groups.ToString());
            }
            catch (Exception) { }
        }

        #endregion

        #region Post

        private void handleMethods(IOwinContext context)
        {
            try
            {
                JObject jsonRequest = getJsonFromRequest(context);

                string method = jsonRequest.GetValue("Method").Value<string>();

                switch (method)
                {
                    case ("getTracksByUserId"):
                        {
                            Guid id = Guid.Parse(jsonRequest.GetValue("Id").Value<string>());
                            var dbTracks = TrackRepository.GetTracksByUserId(id);
                            var jTracks = convertDbTracksToJson(dbTracks);
                            createWebResponse(context, JSON_TYPE, jTracks.ToString());
                            break;
                        }
                    case ("getTracksByEventId"):
                        {
                            Guid id = Guid.Parse(jsonRequest.GetValue("Id").Value<string>());
                            var dbTracks = TrackRepository.GetTracksByEventId(id);
                            var jTracks = convertDbTracksToJson(dbTracks);
                            createWebResponse(context, JSON_TYPE, jTracks.ToString());
                            break;
                        }
                    case ("getUsersByGroupId"):
                        {
                            Guid id = Guid.Parse(jsonRequest.GetValue("Id").Value<string>());
                            var dbUsers = UserRepository.getUsersByGroupId(id);
                            var jUsers = convertDbUsersToJson(dbUsers);
                            createWebResponse(context, JSON_TYPE, jUsers.ToString());
                            break;
                        }
                    case ("getUsersByTrackId"):
                        {
                            Guid id = Guid.Parse(jsonRequest.GetValue("Id").Value<string>());
                            var dbUsers = UserRepository.getUsersByTrackId(id);
                            var jUsers = convertDbUsersToJson(dbUsers);
                            createWebResponse(context, JSON_TYPE, jUsers.ToString());
                            break;
                        }
                    case ("getGroupsByUserId"):
                        {
                            Guid id = Guid.Parse(jsonRequest.GetValue("Id").Value<string>());
                            var dbGroups = GroupRepository.getGroupsByUserId(id);
                            var jGroups = convertDbGroupsToJson(dbGroups);
                            createWebResponse(context, JSON_TYPE, jGroups.ToString());
                            break;
                        }
                    case ("getGroupsByEventId"):
                        {
                            Guid id = Guid.Parse(jsonRequest.GetValue("Id").Value<string>());
                            var dbGroups = GroupRepository.getGroupsByEventId(id);
                            var jGroups = convertDbGroupsToJson(dbGroups);
                            createWebResponse(context, JSON_TYPE, jGroups.ToString());
                            break;
                        }
                    case ("getEventsByGroupId"):
                        {
                            Guid id = Guid.Parse(jsonRequest.GetValue("Id").Value<string>());
                            var dbEvents = EventRepository.getEventsByGroupId(id);
                            var jEvents = convertDbEventsToJson(dbEvents);
                            createWebResponse(context, JSON_TYPE, jEvents.ToString());
                            break;
                        }
                    case ("getEventsByTrackId"):
                        {
                            Guid id = Guid.Parse(jsonRequest.GetValue("Id").Value<string>());
                            var dbEvents = EventRepository.getEventsByTrackId(id);
                            var jEvents = convertDbEventsToJson(dbEvents);
                            createWebResponse(context, JSON_TYPE, jEvents.ToString());
                            break;
                        }
                    case ("getLanguagesByUserId"):
                        {
                            Guid id = Guid.Parse(jsonRequest.GetValue("Id").Value<string>());
                            var dbLanguages = LanguageRepository.GetLanguagesByUserId(id);
                            var jLanguages = convertDbLanguagesToJson(dbLanguages);
                            createWebResponse(context, JSON_TYPE, jLanguages.ToString());
                            break;
                        }
                    case ("addUserToGroup"):
                        {
                            Guid source = Guid.Parse(jsonRequest.GetValue("SourceId").Value<string>());
                            Guid destination = Guid.Parse(jsonRequest.GetValue("DestinationId").Value<string>());
                            GroupRepository.AddUserToGroup(source, destination);
                            break;
                        }
                    case ("addUserToTrack"):
                        {
                            Guid source = Guid.Parse(jsonRequest.GetValue("SourceId").Value<string>());
                            Guid destination = Guid.Parse(jsonRequest.GetValue("DestinationId").Value<string>());
                            TrackRepository.AddUserToTrack(source, destination);
                            break;
                        }
                    case ("addSkillToUser"):
                        {
                            Guid source = Guid.Parse(jsonRequest.GetValue("SourceId").Value<string>());
                            Guid destination = Guid.Parse(jsonRequest.GetValue("DestinationId").Value<string>());
                            UserRepository.AddSkillToUser(source, destination);
                            break;
                        }
                    case ("addLanguageToUser"):
                        {
                            Guid source = Guid.Parse(jsonRequest.GetValue("SourceId").Value<string>());
                            Guid destination = Guid.Parse(jsonRequest.GetValue("DestinationId").Value<string>());
                            UserRepository.AddLanguageToUser(source, destination);
                            break;
                        }
                    case ("addCategoryToTrack"):
                        {
                            Guid source = Guid.Parse(jsonRequest.GetValue("SourceId").Value<string>());
                            Guid destination = Guid.Parse(jsonRequest.GetValue("DestinationId").Value<string>());
                            TrackRepository.AddCategoryToTrack(source, destination);
                            break;
                        }
                }
            }
            catch (Exception) { }
        }

        private void registerClient(Microsoft.Owin.IOwinContext context)
        {
            try
            {
                JObject jsonRequest = getJsonFromRequest(context);
                string token = jsonRequest.GetValue("registerationID").Value<string>();

                m_GcmManager.RegisterClient(token);
            }
            catch (Exception) { }
        }

        //TODO: Complete it after 15.5

        ///// <summary>
        ///// Get all the tracks which are closer than spesific distance from the user
        ///// </summary>
        ///// <param name="context"></param>
        //private void GetNearByTracks(Microsoft.Owin.IOwinContext context)
        //{
        //    JObject request = getJsonFromRequest(context);

        //    Guid userId = Guid.Parse(request.GetValue("UserId").Value<string>());

        //    var nearByTracks = NearByTracks(0, 0, 20);
        //    JArray jNearByTracks = convertTracksToJson(nearByTracks);

        //    createWebResponse(context, JSON_TYPE, jNearByTracks.ToString());
        //}

        //private List<Common.Track> NearByTracks(double latitude, double longitude, int distance)
        //{
        //    IEnumerable<Track> allTracks = TrackRepository.GetTracks();

        //    return null;
        //}

        private void getRecommendations(Microsoft.Owin.IOwinContext context)
        {
            try
            {
                JObject request = getJsonFromRequest(context);

                Guid userId = Guid.Parse(request.GetValue("UserId").Value<string>());

                var recommendedTracks = m_AprioriManager.GetRecommendations(userId);
                JArray jRecommendedTracks = convertTracksToJson(recommendedTracks);

                createWebResponse(context, JSON_TYPE, jRecommendedTracks.ToString());
            }
            catch (Exception) { }
        }

        private void getUser(Microsoft.Owin.IOwinContext context)
        {
            try
            {
                JObject request = getJsonFromRequest(context);
                Guid userId = Guid.Parse(request["id"].ToString());

                var dbUser = UserRepository.GetUserById(userId);

                JObject user = convertDbUserToJson(dbUser);

                createWebResponse(context, JSON_TYPE, user.ToString());
            }
            catch (Exception) { }
        }

        private void isAuthorizedUser(Microsoft.Owin.IOwinContext context)
        {
            try
            {
                JObject request = getJsonFromRequest(context);
                string userName = request["username"].ToString();
                string PassUser = request["password"].ToString();

                var dbUser = UserRepository.GetUserByName(userName);
                bool isAuthorized = false;

                JObject authorizedUser = new JObject();

                if (dbUser != null)
                {
                    isAuthorized = dbUser.PasswordUser.Equals(PassUser);
                    

                    //authorizedUser.Add("Id", dbUser.Id);
                    authorizedUser.Add("user", convertDbUserToJson(dbUser));

                    
                }

                authorizedUser.Add("isAutorizeUser", isAuthorized);

                createWebResponse(context, JSON_TYPE, authorizedUser.ToString());

                
            }
            catch (Exception ex) 
            {
                Console.WriteLine(ex.ToString());
            }
        }

        private void getTrack(Microsoft.Owin.IOwinContext context)
        {
            try
            {
                JObject request = getJsonFromRequest(context);
                Guid trackId = Guid.Parse(request["id"].ToString());

                var dbTrack = TrackRepository.GetTrackById(trackId);

                JObject track = convertDbTrackToJson(dbTrack);

                createWebResponse(context, JSON_TYPE, track.ToString());
            }
            catch (Exception) { }
        }

        private void getGroup(Microsoft.Owin.IOwinContext context)
        {
            try
            {
                JObject request = getJsonFromRequest(context);
                Guid groupId = Guid.Parse(request["id"].ToString());

                var dbGroup = GroupRepository.GetGroupById(groupId);

                JObject group = convertDbGroupToJson(dbGroup);

                createWebResponse(context, JSON_TYPE, group.ToString());
            }
            catch (Exception) { }
        }

        #endregion

        #region Put

        private void addEvent(IOwinContext context)
        {
            try
            {
                JObject request = getJsonFromRequest(context);
                var dbEvent = convertJsonToDbEvent(request);

                EventRepository.AddEvent(dbEvent.Name,
                                            DateTime.Now,
                                            dbEvent.Track.Id,
                                            dbEvent.Group.Id);
            }
            catch (Exception) { }
        }

        private void addCategory(IOwinContext context)
        {
            try
            {
                JObject request = getJsonFromRequest(context);
                var dbCategory = convertJsonToDbCategory(request);

                CategoryRepository.AddCategory(dbCategory.Name);
            }
            catch (Exception) { }
        }

        private void addLanguage(IOwinContext context)
        {
            try
            {
                JObject request = getJsonFromRequest(context);
                var dbLanguage = convertJsonToDbLanguage(request);

                LanguageRepository.AddLanguage(dbLanguage.Name);
            }
            catch (Exception) { }
        }

        private void addSkill(IOwinContext context)
        {
            try
            {
                JObject request = getJsonFromRequest(context);
                var dbSkill = convertJsonToDbSkill(request);

                SkillRepository.AddSkill(dbSkill.Name);
            }
            catch (Exception) { }
        }

        private void addUser(Microsoft.Owin.IOwinContext context)
        {
            try
            {
                JObject request = getJsonFromRequest(context);
                var dbUser = convertJsonToDbUser(request);

                UserRepository.AddUser(dbUser.MailAddress,
                                        dbUser.LastName,
                                        dbUser.FirstName,
                                        dbUser.City,
                                        dbUser.Birthdate,
                                        dbUser.PasswordUser,
                                        dbUser.Gender);
            }
            catch (Exception ex) {
                Console.WriteLine(ex);
            }
        }

        private void addTrack(Microsoft.Owin.IOwinContext context)
        {
            try
            {
                JObject request = getJsonFromRequest(context);
                var dbTrack = convertJsonToDbTrack(request);

                TrackRepository.AddTrack(dbTrack.Name,
                                            dbTrack.Zone,
                                            dbTrack.Kilometers,
                                            dbTrack.Difficulty,
                                            dbTrack.Latitude.Value,
                                            dbTrack.Longitude.Value,
                                            dbTrack.Rating);
            }
            catch (Exception) { }
        }

        private void addGroup(Microsoft.Owin.IOwinContext context)
        {
            try
            {
                JObject request = getJsonFromRequest(context);
                var dbGroup = convertJsonToDbGroup(request);

                GroupRepository.AddGroup(dbGroup.Name);
            }
            catch (Exception) { }
        }

        #endregion

        #region Delete

        private void deleteEvent(IOwinContext context)
        {
            try
            {
                JObject request = getJsonFromRequest(context);
                Guid eventId = Guid.Parse(request["id"].ToString());

                EventRepository.DeleteEvent(eventId);
            }
            catch (Exception) { }
        }

        private void deleteCategory(IOwinContext context)
        {
            try
            {
                JObject request = getJsonFromRequest(context);
                Guid categoryId = Guid.Parse(request["id"].ToString());

                CategoryRepository.DeleteCategory(categoryId);
            }
            catch (Exception) { }
        }

        private void deleteLanguage(IOwinContext context)
        {
            try
            {
                JObject request = getJsonFromRequest(context);
                Guid LanguageId = Guid.Parse(request["id"].ToString());

                LanguageRepository.DeleteLanguage(LanguageId);
            }
            catch (Exception) { }
        }

        private void deleteSkill(IOwinContext context)
        {
            try
            {
                JObject request = getJsonFromRequest(context);
                Guid skillId = Guid.Parse(request["id"].ToString());

                SkillRepository.DeleteSkill(skillId);
            }
            catch (Exception) { }
        }

        private void deleteUser(Microsoft.Owin.IOwinContext context)
        {
            try
            {
                JObject request = getJsonFromRequest(context);
                Guid userId = Guid.Parse(request["id"].ToString());

                UserRepository.DeleteUser(userId);
            }
            catch (Exception) { }
        }

        private void deleteTrack(Microsoft.Owin.IOwinContext context)
        {
            try
            {
                JObject request = getJsonFromRequest(context);
                Guid trackId = Guid.Parse(request["id"].ToString());

                TrackRepository.DeleteTrack(trackId);
            }
            catch (Exception) { }
        }

        private void deleteGroup(Microsoft.Owin.IOwinContext context)
        {
            try
            {
                JObject request = getJsonFromRequest(context);
                Guid groupId = Guid.Parse(request["id"].ToString());

                GroupRepository.DeleteGroup(groupId);
            }
            catch (Exception) { }
        }

        #endregion

        #region Database Converters

        #region To Json

        private JArray convertTracksToJson(List<Common.Track> tracks)
        {
            JArray arrayTracks = new JArray();

            foreach (var track in tracks)
                arrayTracks.Add(convertDbTrackToJson(TrackRepository.GetTrackById(track.TrackId)));

            return arrayTracks;
        }

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
            user.Add("Password", dbUser.PasswordUser);
            user.Add("City", dbUser.City);
            int userAge = UserRepository.getAgeByUserId(dbUser.Id);
            user.Add("Age", userAge);
            user.Add("BirthDate", dbUser.Birthdate);
            user.Add("Gender", dbUser.Gender);
            var jLanguages = convertDbLanguagesToJson(LanguageRepository.GetLanguagesByUserId(dbUser.Id));
            user.Add("Languages", jLanguages);

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
            track.Add("Rating",dbTrack.Rating);

            return track;
        }

        private JObject convertDbGroupToJson(DAL.Model.Group dbGroup)
        {
            JObject jGroup = new JObject();

            jGroup.Add("Id", dbGroup.Id);
            jGroup.Add("Name", dbGroup.Name);

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

        private JArray convertDbSkillsToJson(IEnumerable<Skill> dbSkills)
        {
            JArray arraySkills = new JArray();

            foreach (var dbSkill in dbSkills)
            {
                JObject skill = new JObject();

                skill.Add("Id", dbSkill.Id);
                skill.Add("Name", dbSkill.Name);

                arraySkills.Add(skill);
            }

            return arraySkills;
        }

        private JArray convertDbCategoriesToJson(IEnumerable<Category> dbCategories)
        {
            JArray arrayCategories = new JArray();

            foreach (var dbCategory in dbCategories)
            {
                JObject category = new JObject();

                category.Add("Id", dbCategory.Id);
                category.Add("Name", dbCategory.Name);

                arrayCategories.Add(category);
            }

            return arrayCategories;
        }

        private JArray convertDbLanguagesToJson(IEnumerable<Language> dbLanguages)
        {
            JArray arrayLanguages = new JArray();

            foreach (var dbLanguage in dbLanguages)
            {
                JObject language = new JObject();

                //language.Add("Id", dbLanguage.Id);
                language.Add("Name", dbLanguage.Name);

                arrayLanguages.Add(language);
            }

            return arrayLanguages;
        }

        private JArray convertDbEventsToJson(IEnumerable<Event> dbEvents)
        {
            JArray arrayEvents= new JArray();

            foreach (var dbEvent in dbEvents)
            {
                JObject jEvent = new JObject();

                jEvent.Add("Id", dbEvent.Id);
                jEvent.Add("Name", dbEvent.Name);
                jEvent.Add("GroupId", dbEvent.Group.Id);
                jEvent.Add("TrackId", dbEvent.Track.Id);

                arrayEvents.Add(jEvent);
            }

            return arrayEvents;
        }

        #endregion

        #region To DB

        private DAL.Model.Language convertJsonToDbLanguage(JObject jLanguage)
        {
            string name = jLanguage.GetValue("Name").Value<string>();

            var dbLanguage = new DAL.Model.Language
            {
                Name = name,
            };

            return dbLanguage;
        }

        private DAL.Model.Skill convertJsonToDbSkill(JObject jSkill)
        {
            string name = jSkill.GetValue("Name").Value<string>();

            var dbSkill= new DAL.Model.Skill
            {
                Name = name,
            };

            return dbSkill;
        }

        private DAL.Model.Category convertJsonToDbCategory(JObject jCategory)
        {
            string name = jCategory.GetValue("Name").Value<string>();

            var dbCategory = new DAL.Model.Category
            {
                Name = name,
            };

            return dbCategory;
        }

        private DAL.Model.Event convertJsonToDbEvent(JObject jEvent)
        {
            string eventName = jEvent.GetValue("Name").Value<string>();
            Guid trackId= Guid.Parse(jEvent.GetValue("TrackId").Value<string>());
            Guid groupId = Guid.Parse(jEvent.GetValue("GroupId").Value<string>());

            var dbEvent = new DAL.Model.Event
            {
                Name = eventName,
                Track = TrackRepository.GetTrackById(trackId),
                Group = GroupRepository.GetGroupById(groupId)
            };

            return dbEvent;
        }

        private DAL.Model.User convertJsonToDbUser(JObject user)
        {
            DAL.Model.User dbUser = new DAL.Model.User
            {
                FirstName = user["FirstName"].Value<string>(),
                LastName = user["LastName"].Value<string>(),
                MailAddress = user["MailAddress"].Value<string>(),
                PasswordUser = user["Password"].Value<string>(),
                City = user["City"].Value<string>(),
               Birthdate = DateTime.ParseExact(user["Birthdate"].Value<string>(),"ddd MMMM dd HH:mm:ss GMT+00:00 yyyy",CultureInfo.CurrentCulture),
             //   Birthdate = DateTime.ParseExact(user["Birthdate"].Value<string>(), "ddd, MMMM dd HH:mm:ss GMT+00:00 yyyy", CultureInfo.CurrentCulture),
                Gender = user["Gender"].Value<string>()
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
                Longitude = track["Longitude"].Value<double>(),
                Rating = track["Rating"].Value<int>()
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
