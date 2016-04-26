using Microsoft.Owin.Hosting;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using TrailMeServer.Owin;

namespace TrailMeServer.Models
{
    public static class TrailMeManager
    {
        private static List<string> RegisteredClients = new List<string>();
        const string GCM_URL = "https://gcm-http.googleapis.com/gcm/send";

        //public List<string> RegisteredClients { get; set; }

        //public TrailMeManager()
        //{
        //    //RegisteredClients = new List<string>();
        //}

        public static void Start()
        {
            string serviceUrl = "http://+:9900/";

            using (WebApp.Start<Startup>(serviceUrl))
            {
                NotificationLoop();
            }
        }

        public static void Stop()
        {

        }

        private static void NotificationLoop()
        {
            while (true)
            {
                if (RegisteredClients.Count > 0)
                {
                    lock (RegisteredClients)
                    {
                        foreach (string to in RegisteredClients)
                        {
                            PushNotifications(to, "Simple message!");
                        }
                    }
                }

                Thread.Sleep(1000);
            }
        }

        public static void RegisterClient(string id)
        {
            lock (RegisteredClients)
            {
                RegisteredClients.Add(id);

                Console.WriteLine("New client registered - {0}", id);
            }
        }

        public static void PushNotifications(string to, string message)
        {
            try
            {
                WebRequest request = WebRequest.Create(GCM_URL);

                string data = "{\"data\": {\"message\":\"" + message + "\"}, \"to\":\"" + to + "\"}";

                request.Method = "POST";
                request.ContentType = "application/json";
                request.Headers.Add(String.Format("Authorization: key={0}", "AIzaSyCvU8Qs3VbnuwsQZh4LAYaOfogF9pALae0")); // API Key

                request.GetRequestStream().Write(Encoding.UTF8.GetBytes(data), 0, Encoding.UTF8.GetBytes(data).Length);

                using (WebResponse response = request.GetResponse())
                {

                }

                Console.WriteLine("Sending notification to - {0}", to);
            }
            catch
            { }
        }
    }
}
