using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.Linq;
using System.ServiceProcess;
using System.Text;
using System.Threading.Tasks;
using TrailMeServer.Models;

namespace TrailMeServer
{
    public partial class TrailMeService : ServiceBase
    {
        //private TrailMeManager m_Manager;

        public TrailMeService()
        {
            //m_Manager = new TrailMeManager();

            InitializeComponent();
        }

        protected override void OnStart(string[] args)
        {
            //m_Manager.Start();
            TrailMeManager.Start();
        }

        protected override void OnStop()
        {
            //m_Manager.Stop();
            TrailMeManager.Stop();
        }
    }
}
