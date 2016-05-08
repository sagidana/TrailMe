using Microsoft.Owin.Hosting;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using TrailMe.Apriori;
using TrailMe.DAL;
using TrailMe.GoogleCloudMessaging;
using TrailMe.DAL.Model;
using Microsoft.Owin;

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
        const string EVENTS_URL = "/events";
        const string SKILLS_URL = "/skills";
        const string CATEGORIES_URL = "/categories";
        const string LANGUAGES_URL = "/languages";
        const string LINK_URL = "/link";
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
            Startup.Resources.Add(new WebResource { Path = GROUPS_URL, Method = POST_METHOD, Handler = getGroup });
            Startup.Resources.Add(new WebResource { Path = TRACKS_URL, Method = POST_METHOD, Handler = getTrack });
            Startup.Resources.Add(new WebResource { Path = RECOMMENDATIONS_URL, Method = POST_METHOD, Handler = getRecommendations });
            Startup.Resources.Add(new WebResource { Path = LINK_URL, Method = POST_METHOD, Handler = addLinks });

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

        private JObject getJsonFromRequest(Microsoft.Owin.IOwinContext context)
        {
            string body = new StreamReader(context.Request.Body).ReadToEnd();
            JObject jsonRequest = JObject.Parse(body);
            return jsonRequest;
        }

        #region Get

        private void getAllEvents(Microsoft.Owin.IOwinContext context)
        {
            var dbEvents= EventRepository.GetEvents();
            JArray events = convertDbEventsToJson(dbEvents);

            createWebResponse(context, JSON_TYPE, events.ToString());
        }

        private void getAllSkills(Microsoft.Owin.IOwinContext context)
        {
            var dbSkills = SkillRepository.GetSkills();
            JArray skills = convertDbSkillsToJson(dbSkills);

            createWebResponse(context, JSON_TYPE, skills.ToString());
        }

        private void getAllCategories(Microsoft.Owin.IOwinContext context)
        {
            var dbCategories = CategoryRepository.GetCategories();
            JArray categories = convertDbCategoriesToJson(dbCategories);

            createWebResponse(context, JSON_TYPE, categories.ToString());
        }

        private void getAllLanguages(Microsoft.Owin.IOwinContext context)
        {
            var dbLanguages = LanguageRepository.GetLanguages();
            JArray languages = convertDbLanguagesToJson(dbLanguages);

            createWebResponse(context, JSON_TYPE, languages.ToString());
        }

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

        private void addLinks(IOwinContext context)
        {
            JObject jsonRequest = getJsonFromRequest(context);

            string method = jsonRequest.GetValue("Method").Value<string>();
            Guid source = jsonRequest.GetValue("SourceId").Value<Guid>();
            Guid destination = jsonRequest.GetValue("DestinationId").Value<Guid>();

            switch (method)
            {
                case ("addUserToGroup"):
                    {
                        GroupRepository.AddUserToGroup(source, destination);
                        break;
                    }
                case ("addUserToTrack"):
                    {
                        TrackRepository.AddUserToTrack(source, destination);
                        break;
                    }
                case ("addSkillToUser"):
                    {
                        UserRepository.AddSkillToUser(source, destination);
                        break;
                    }
                case ("addLanguageToUser"):
                    {
                        UserRepository.AddLanguageToUser(source, destination);
                        break;
                    }
                case ("addCategoryToTrack"):
                    {
                        TrackRepository.AddCategoryToTrack(source, destination);
                        break;
                    }
            }
        }

        private void registerClient(Microsoft.Owin.IOwinContext context)
        {
            JObject jsonRequest = getJsonFromRequest(context);
            string token = jsonRequest.GetValue("registerationID").Value<string>();

            m_GcmManager.RegisterClient(token);
        }

        private void getRecommendations(Microsoft.Owin.IOwinContext context)
        {
            JObject request = getJsonFromRequest(context);

            Guid userId = request.GetValue("UserId").Value<Guid>();

            var recommendedTracks = m_AprioriManager.GetRecommendations(userId);
            JArray jRecommendedTracks = convertTracksToJson(recommendedTracks);

            createWebResponse(context, JSON_TYPE, jRecommendedTracks.ToString());
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

        private void addEvent(IOwinContext context)
        {
            JObject request = getJsonFromRequest(context);
            var dbEvent = convertJsonToDbEvent(request);

            EventRepository.AddEvent(   dbEvent.Name,
                                        DateTime.Now,
                                        dbEvent.Track.Id,
                                        dbEvent.Group.Id);
        }

        private void addCategory(IOwinContext context)
        {
            JObject request = getJsonFromRequest(context);
            var dbCategory = convertJsonToDbCategory(request);

            CategoryRepository.AddCategory(dbCategory.Name);
        }

        private void addLanguage(IOwinContext context)
        {
            JObject request = getJsonFromRequest(context);
            var dbLanguage = convertJsonToDbLanguage(request);

            LanguageRepository.AddLanguage(dbLanguage.Name);
        }

        private void addSkill(IOwinContext context)
        {
            JObject request = getJsonFromRequest(context);
            var dbSkill = convertJsonToDbSkill(request);

            SkillRepository.AddSkill(dbSkill.Name);
        }

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

        private void deleteEvent(IOwinContext context)
        {
            JObject request = getJsonFromRequest(context);
            Guid eventId = Guid.Parse(request["id"].ToString());

            EventRepository.DeleteEvent(eventId);
        }

        private void deleteCategory(IOwinContext context)
        {
            JObject request = getJsonFromRequest(context);
            Guid categoryId = Guid.Parse(request["id"].ToString());

            CategoryRepository.DeleteCategory(categoryId);
        }

        private void deleteLanguage(IOwinContext context)
        {
            JObject request = getJsonFromRequest(context);
            Guid LanguageId = Guid.Parse(request["id"].ToString());

            LanguageRepository.DeleteLanguage(LanguageId);
        }

        private void deleteSkill(IOwinContext context)
        {
            JObject request = getJsonFromRequest(context);
            Guid skillId = Guid.Parse(request["id"].ToString());

            SkillRepository.Deleteskill(skillId);
        }

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

        #region To Json

        private JArray convertTracksToJson(List<Common.Track> tracks)
        {
            JArray arrayTracks = new JArray();

            foreach (var track in tracks)
            {
                JObject jTrack = new JObject();

                jTrack.Add("Id", track.TrackId);

                arrayTracks.Add(jTrack);
            }

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

                language.Add("Id", dbLanguage.Id);
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
            Guid trackId= jEvent.GetValue("TrackId").Value<Guid>();
            Guid groupId = jEvent.GetValue("GroupId").Value<Guid>();

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
