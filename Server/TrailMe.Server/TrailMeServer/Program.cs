using System;
using System.Collections.Generic;
using System.Linq;
using System.ServiceProcess;
using System.Text;
using System.Threading.Tasks;
using TrailMeServer.Models;

namespace TrailMeServer
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        static void Main()
        {
            if (Environment.UserInteractive)
            {
                TrailMeManager manager = new TrailMeManager();
                manager.Start();
                Console.WriteLine("Press any key to stop the service");
                Console.ReadLine();
                manager.Stop();
            }
            else
            {
                ServiceBase[] ServicesToRun;
                ServicesToRun = new ServiceBase[] 
                { 
                    new TrailMeService() 
                };

                ServiceBase.Run(ServicesToRun);
            }

        }
    }
}