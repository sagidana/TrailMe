using Microsoft.Owin.Hosting;
using System;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Threading;
using System.Collections.Generic;
using Newtonsoft.Json.Linq;
using System.IO;
using TrailMe.WebServer;
using TrailMe.Apriori;
using TrailMe.Common;
using TrailMe.DAL;

namespace TrailMe
{
    public delegate void requestHandler(Microsoft.Owin.IOwinContext context);

    public class Program
    {
        static void Main(string[] args)
        {
            TrailMeServer server = new TrailMeServer();

            server.Start();
            
            Console.WriteLine("Server is running!");
            Console.WriteLine("Press Enter to stop the server");
            Console.ReadLine();

            server.Stop();
        }

        #region Examples
        
        private static void DALExample()
        {
            var events = EventRepository.GetEvents();
            var tracks = TrackRepository.GetTracks();
            var users = UserRepository.GetUsers();
            var groups = GroupRepository.GetGroups();
        }

        private static void aprioriExample()
        {
            AprioriAlgorithm apriori = new AprioriAlgorithm();

            List<Track> tracks = new List<Track> {  new Track() { TrackId = Guid.NewGuid() },
                                                    new Track() { TrackId = Guid.NewGuid() },
                                                    new Track() { TrackId = Guid.NewGuid() },
                                                    new Track() { TrackId = Guid.NewGuid() },
                                                    new Track() { TrackId = Guid.NewGuid() },
                                                    new Track() { TrackId = Guid.NewGuid() }};

            for (int i = 0; i < tracks.Count; i++)
                Console.WriteLine("Track {0} - {1}", i + 1, tracks[i].TrackId);

            List<Transaction> transactions = new List<Transaction>() {  new Transaction() { Items = new List<Track>() { tracks[0], tracks[2] } },
                                                                        new Transaction() { Items = new List<Track>() { tracks[0], tracks[2] } },
                                                                        new Transaction() { Items = new List<Track>() { tracks[0], tracks[3] } },
                                                                        new Transaction() { Items = new List<Track>() { tracks[0], tracks[4] } },
                                                                        new Transaction() { Items = new List<Track>() { tracks[0], tracks[5] } }};


            var result = apriori.ProcessTransaction(0.3, 0.6, tracks, transactions);

            foreach (var rule in result.StrongRules)
                Console.WriteLine("{0} => {1} , confidence - {2}", rule.From[0].TrackId, rule.To[0].TrackId, rule.Confidence);
        }
        
        #endregion
    }
}
