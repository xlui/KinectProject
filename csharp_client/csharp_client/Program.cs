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

            string latestUrl = "http://111.231.1.210/api/dev/latest";
            string updateUrl = "http://111.231.1.210/api/dev/update";

            // 用户认证
            string username = "1", password = "dev";
            string authorization = Convert.ToBase64String(System.Text.Encoding.UTF8.GetBytes(username + ":" + password));

            // 对要发送给服务器的数据进行包装
            Dictionary<string, string> state = new Dictionary<string, string>();
            state.Add("state", "TEST");
            // 转换成 Json 格式。注：服务器仅接受 Json 格式的数据，其他格式数据会返回错误
            string json = JsonConvert.SerializeObject(state);

            // 获取最新手势
            client.Get(latestUrl, authorization);
            // 向服务器发送新手势
            client.Post(updateUrl, json, authorization);
            // 以上两个函数的实现细节在 Client.cs 中
        }
    }
}
