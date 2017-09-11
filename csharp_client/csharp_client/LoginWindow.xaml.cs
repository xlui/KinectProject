using System;
using System.Windows;
using System.Windows.Controls;

namespace csharp_client
{
	/// <summary>
	/// LoginWindow.xaml 的交互逻辑
	/// </summary>
	public partial class LoginWindow : Window
	{
		public LoginWindow()
		{
			InitializeComponent();
			username.Focus();
			// 设置用户名输入框为焦点
		}

		private void login_Click(object sender, RoutedEventArgs e)
		{
			// 点击登录按钮事件
			Button button = sender as Button;
			string input_username = username.Text;
			string receive = null;

			// 在本地处理不规范的 username
			try
			{
				int.Parse(input_username);
				//MessageBox.Show(i.ToString());
			}
			catch (FormatException)
			{
				// 通过类型转换来判断用户名是否非法
				MessageBox.Show("用户名不能包含非法字符（数字以外的字符）！", "Error");
				username.Text = "";
				username.Focus();
				return;
			}
			catch (Exception exp)
			{
				// 捕捉未处理的异常
				MessageBox.Show(exp.ToString(), "Exception");
				return;
			}


			// 处理登录事件
			if (button.Name.Equals("login"))
			{
				SocketClient socketClient = new SocketClient();
				receive = socketClient.Send("csharp:" + input_username);
				// 对要发送的消息进行修饰，便于服务器的识别

				if (receive.Equals("success"))
				{
					MessageBox.Show("登录成功！", "提示");
					MainWindow mainWindow = new MainWindow
					{
						username = input_username
					};
					mainWindow.Show();
					Close();
				}
				else if (receive.Equals("failed"))
				{
					MessageBox.Show("用户名未注册！", "Error");
					username.Text = "";
					username.Focus();
				}
				else
				{
					//MessageBox.Show("Connection Exception", "Error");
					username.Text = "";
					username.Focus();
				}
			}
		}
	}
}
