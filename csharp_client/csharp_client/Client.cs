using System;
using System.IO;
using System.Net;

namespace csharp_client
{
    class Client
    {
        public void Get(String url, String username, String password)
        {
            String username_password = username + ":" + password;
            // user authorization
            String base64_username_password = Convert.ToBase64String(System.Text.Encoding.UTF8.GetBytes(username_password));
            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
            request.Method = "GET";
            request.Headers.Add(HttpRequestHeader.Authorization, "Basic " + base64_username_password);
            // request
            HttpWebResponse response = null;
            // response
            try
            {
                response = (HttpWebResponse)request.GetResponse();
            } catch (Exception e)
            {
                Console.WriteLine(e);
                return;
            }
            int response_length = (int)response.ContentLength;
            byte[] response_byte = new Byte[response.ContentLength];
            Stream stream = response.GetResponseStream();

            try
            {
                stream.Read(response_byte, 0, (int)response.ContentLength);
                String response_string = System.Text.Encoding.UTF8.GetString(response_byte, 0, response_length).ToString();
                Console.WriteLine(response_string);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        public String Post()
        {
            return "";
        }
    }
}
