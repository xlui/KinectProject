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
            request.Headers.Add(HttpRequestHeader.Authorization, "Basic " + authorization);
            // request
            HttpWebResponse response = null;
            // response
            try
            {
                response = (HttpWebResponse)request.GetResponse();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                return;
            }
            int response_length = (int)response.ContentLength;
            byte[] response_byte = new Byte[response.ContentLength];

            using (Stream responseStream = response.GetResponseStream())
            {
                responseStream.Read(response_byte, 0, (int)response.ContentLength);
            }

            try
            {
                String response_string = System.Text.Encoding.UTF8.GetString(response_byte, 0, response_length).ToString();
                Console.WriteLine(response_string);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        public void Post(String url, String state, String authorization)
        {
            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
            HttpWebResponse response = null;
            byte[] data = System.Text.Encoding.UTF8.GetBytes(state);
            request.Method = "POST";
            request.Headers.Add(HttpRequestHeader.Authorization, "Basic " + authorization);
            request.ContentType = "application/json";
            //request.ContentLength = data.Length;
            using (Stream requestStream = request.GetRequestStream())
            {
                requestStream.Write(data, 0, data.Length);
                requestStream.Close();
            }

            try
            {
                response = (HttpWebResponse)request.GetResponse();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                return;
            }
            int response_length = (int)response.ContentLength;
            byte[] response_byte = new Byte[response.ContentLength];

            using (Stream responseStream = response.GetResponseStream())
            {
                responseStream.Read(response_byte, 0, (int)response.ContentLength);
                responseStream.Close();
            }

            try
            {
                String response_string = System.Text.Encoding.UTF8.GetString(response_byte, 0, response_length).ToString();
                Console.WriteLine(response_string);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
    }
}
