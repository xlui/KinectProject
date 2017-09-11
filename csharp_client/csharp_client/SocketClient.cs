using System;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Windows;

namespace csharp_client
{
	class SocketClient
    {
        private IPAddress serverIP = null;
        private int serverPort = 0;

		public SocketClient()
		{
			serverIP = IPAddress.Parse(Config.SERVER_IP);
			// 服务器 IP 地址，在 Config 类中设置
			serverPort = Config.SERVER_PORT;
			// 服务器端口号，在 Config 类中设置
		}

        public string Send(string toSend)
        {
			// Send 方法，发送数据给服务器，并返回服务器的回应。如果出现异常，返回空字符串
			Socket socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
			byte[] buffers = new byte[1024];
			string receive = null;
			
			// connect to server
			try {
				socket.Connect(new IPEndPoint(serverIP, serverPort));
			} catch (Exception) {
				//MessageBox.Show(e.ToString());
				MessageBox.Show("无法连接到服务器！", "Error");
				socket.Close();
				return "";
			}

			// send data to server
			try {
				socket.Send(Encoding.Default.GetBytes(toSend));
			} catch (Exception) {
				//MessageBox.Show(e.ToString());
				MessageBox.Show("与服务器的连接意外中断！", "Error");
				socket.Close();
				return "";
			}

			// receive data from server
			try {
				int byteRec = 0;
				byteRec = socket.Receive(buffers, 1024, SocketFlags.None);
				// 接收服务器回应
				receive = Encoding.Default.GetString(buffers, 0, byteRec).Trim();
				// 将服务器回应从 byte 转换为 String
				// 调用 Trim 函数去掉末尾的 \n
			} catch (Exception) {
				//MessageBox.Show(e.ToString());
				MessageBox.Show("与服务器的连接意外中断！", "Error");
				socket.Close();
				return "";
			}

			socket.Close();

			if (receive.Length != 0) {
				return receive;
			} else {
				return "";
			}
		}
    }
}
