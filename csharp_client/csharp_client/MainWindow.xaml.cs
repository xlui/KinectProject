using System;
using System.Windows;
using System.Windows.Controls;

namespace csharp_client
{
	/// <summary>
	/// MainWindow.xaml 的交互逻辑
	/// </summary>
	public partial class MainWindow : Window
	{
		public string username { get; set; }
		// 定义该变量用于登录对话框向主对话框传递用户名

		public MainWindow()
		{
			InitializeComponent();
			send.Focus();				// 将发送框设为焦点
		}

		private void Submit_Click(object sender, RoutedEventArgs e)
		{
			// 点击按钮 Submit 事件
			Button btn = sender as Button;
			string toSend = send.Text;

			// 本地处理非法数据格式
			try
			{
				int.Parse(toSend);
				//MessageBox.Show(i.ToString());
			}
			catch (FormatException)
			{
				MessageBox.Show("发送数据的格式非法（仅能发送数字）！", "Error");
				SetTextBoxEmpty();
				return;
			}
			catch (Exception exp)
			{
				MessageBox.Show(exp.ToString(), "Exception");
				return;
			}

			// 事件
			if (btn.Name == "Submit")
			{
				SocketClient socketClient = new SocketClient();
				string receive = null;

				receive = socketClient.Send("code:" + toSend + "username:" + username);
				// Send 函数将发送框的数据发送给服务器，并接受服务器的回应 
				// code 代表不同的手势，username 是登录的用户
				// 请注意：目前安卓客户端只响应 code:1 code:2 code3
				// 所以 Send 的内容为： 1 2 3 时，安卓会响应；其他安卓不做响应。
				
				if (receive.Length != 0) 
					MessageBox.Show("服务器回应： " + receive, "Response");
				else
					MessageBox.Show("服务器连接异常！", "Error");
				SetTextBoxEmpty();
			}
		}

		private void Cancel_Click(object sender, RoutedEventArgs e)
		{
			// 点击 Cancel 按钮事件
			SetTextBoxEmpty();
			// 将发送框设置为空
		}

		private void SetTextBoxEmpty() 
		{
			send.Text = "";
			send.Focus();
			// 设置输入框为焦点
		}

        private void Logout_Click(object sender, RoutedEventArgs e)
        {
            // 退出登录事件
            (new LoginWindow()).Show();
            Close();
        }
    }
}
