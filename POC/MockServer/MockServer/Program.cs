using Microsoft.Owin.Hosting;
using System;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Threading;
using System.Collections.Generic;
using Newtonsoft.Json.Linq;

namespace MockServer
{
    public class Program
    {
        static public List<string> registeredClients = new List<string>();
        static string GCM_URL = "https://gcm-http.googleapis.com/gcm/send";

        static void Main(string[] args)
        {
            string serviceUrl = "http://+:9900/";

            using (WebApp.Start<Startup>(serviceUrl))
            {
                notificationLoop();
            }
        }
        
        static private void notificationLoop()
        {
            while (true)
            {
                if (registeredClients.Count > 0)
                {
                    lock (registeredClients)
                    {
                        foreach (string to in registeredClients)
                        {
                            PushNotifications(to, "Simple message!");
                        }
                    }
                }

                Thread.Sleep(1000);
            }
        }

        static public void RegisterClient(string id)
        {
            lock(registeredClients)
            {
                registeredClients.Add(id);

                Console.WriteLine("New client registered - {0}", id);
            }
        }

        static public void PushNotifications(string to, string message)
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
