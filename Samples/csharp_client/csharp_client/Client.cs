using System;
using System.IO;
using System.Net;

namespace csharp_client
{
    class Client
    {
        public void Get(String url, String authorization)
        {
            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
            request.Method = "GET";
            request.Headers.Add(HttpRequestHeader.Authorization, authorization);

            ShowResponse(request);
        }

        public void Post(String url, String state, String authorization)
        {
            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
            byte[] data = System.Text.Encoding.UTF8.GetBytes(state);
            request.Method = "POST";
            request.Headers.Add(HttpRequestHeader.Authorization, authorization);
            request.ContentType = "application/json";

            using (Stream requestStream = request.GetRequestStream())
            {
                requestStream.Write(data, 0, data.Length);
                requestStream.Close();
            }

            ShowResponse(request);
        }

        private void ShowResponse(HttpWebRequest request)
        {
            HttpWebResponse response = null;

            try
            {
                response = (HttpWebResponse)request.GetResponse();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                return;
            }

            byte[] response_byte = new Byte[response.ContentLength];
            // 接受 response stream

            using (Stream responseStream = response.GetResponseStream())
            {
                responseStream.Read(response_byte, 0, (int)response.ContentLength);
                responseStream.Close();
            }

            try
            {
                Console.WriteLine(System.Text.Encoding.ASCII.GetString(response_byte));
                // 使用 ASCII 编码避免返回值是下划线时乱码
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        public void SendPicture(String url, String filename, String authorization)
        {
            using (var wc = new WebClient())
            {
                wc.Headers.Add(HttpRequestHeader.Authorization, authorization);
                byte[] response = wc.UploadFile(url, filename);
                Console.WriteLine(System.Text.Encoding.ASCII.GetString(response));
            }
        }
    }
}
