using Newtonsoft.Json.Linq;
using Owin;
using System.IO; 

namespace MockServer
{
    public class Startup 
    { 
        // This code configures Web API. The Startup class is specified as a type
        // parameter in the WebApp.Start method.
        public void Configuration(IAppBuilder appBuilder) 
        {
            appBuilder.Run((context) =>
            {
                if (context.Request.Path == new Microsoft.Owin.PathString("/register"))
                {
                    string body = new StreamReader(context.Request.Body).ReadToEnd();
                    JObject jsonRequest = JObject.Parse(body);
                    string token = jsonRequest.GetValue("registerationID").Value<string>();

                    Program.RegisterClient(token);
                }

                return null;
            });
        } 
    } 
}