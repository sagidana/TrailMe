using Newtonsoft.Json.Linq;
using Owin;
using System.Collections.Generic;
using System.IO;

namespace TrailMe.WebServer
{
    public class Startup 
    {
        // List of web resources for this self host server to service.
        public static List<WebResource> Resources;

        // This code configures Web API. The Startup class is specified as a type
        // parameter in the WebApp.Start method.
        public void Configuration(IAppBuilder appBuilder) 
        {
            appBuilder.Run((context) =>
            {
                if (Resources != null)
                {
                    var request = context.Request;
                    foreach (var resource in Resources)
                    { 
                        if (request.Path.Value.Equals(resource.Path, System.StringComparison.OrdinalIgnoreCase) && 
                            request.Method.Equals(resource.Method, System.StringComparison.OrdinalIgnoreCase))
                        {
                            resource.Handler(context);
                        }
                    }
                }

                return context.Response.WriteAsync(string.Empty);
            });
        } 
    } 
}