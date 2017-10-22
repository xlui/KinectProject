using System;
using System.Collections.Generic;
using Newtonsoft.Json;

namespace csharp_client
{
    class Program
    {
        static void Main(string[] args)
        {
            Client client = new Client();

            string latestUrl = "http://111.231.1.210/api/dev/";
            string updateUrl = "http://111.231.1.210/api/dev/update";

            string username = "1", password = "dev";
            string authorization = Convert.ToBase64String(System.Text.Encoding.UTF8.GetBytes(username + ":" + password));

            Dictionary<string, string> state = new Dictionary<string, string>();
            state.Add("state", "TEST");
            string json = JsonConvert.SerializeObject(state);

            client.Get(latestUrl, authorization);
            // get latest state
            client.Post(updateUrl, json, authorization);
            // post a new state
        }
    }
}
