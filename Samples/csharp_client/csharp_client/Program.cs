using System;
using System.Collections.Generic;
//using Newtonsoft.Json;

namespace csharp_client
{
    class Program
    {
        static void Main(string[] args)
        {
            Client client = new Client();

            string latestUrl = "https://nxmup.com/api/dev/latest";
            string updateUrl = "https://nxmup.com/api/dev/update";
            string uploadUrl = "https://nxmup.com/api/dev/upload";

            // 用户认证
            string username = "1", password = "dev";
            string authorization = "Basic " + Convert.ToBase64String(System.Text.Encoding.UTF8.GetBytes(username + ":" + password));

            // 对要发送给服务器的数据进行包装
            Dictionary<string, string> state = new Dictionary<string, string>
            {
                { "state", "open_open" },
            };
            // 转换成 Json 格式。注：服务器仅接受 Json 格式的数据，其他格式数据会返回错误
            //string json = JsonConvert.SerializeObject(state);

            // 以下几个函数的实现细节在 Client.cs 中
            // 获取最新手势
            //client.Get(latestUrl, authorization);
            // 向服务器发送新手势
            //client.Post(updateUrl, json, authorization);
            // 发送图片
            String filename = @"F:\屏幕截图\hello.png";
            client.SendPicture(uploadUrl, filename, authorization);
        }
    }
}
