namespace csharp_client
{
    class Program
    {
        static void Main(string[] args)
        {
            Client client = new Client();
            string url = "http://111.231.1.210/api/dev/";
            string username = "1", password = "dev";
            client.Get(url, username, password);
        }
    }
}
