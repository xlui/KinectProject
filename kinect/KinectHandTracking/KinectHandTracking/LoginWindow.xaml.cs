using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace RehabilitationTraining
{
    /// <summary>
    /// LoginWindow.xaml 的交互逻辑
    /// </summary>
    public partial class LoginWindow : Window
    {
        public LoginWindow()
        {
            InitializeComponent();
            AllowsTransparency = true;
            WindowStyle = WindowStyle.None;
        }

        /// <summary>
        /// 登陆事件
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btn_login_Click(object sender, RoutedEventArgs e)
        {
            //用户名为空
            if (txt_UserName.Text == "")
            {
                MessageBox.Show("请输入用户名！");
            }
            //密码为空
            else if (txt_Pwd.Password == "")
            {
                MessageBox.Show("请输入密码！");
            }
            else
            {
                this.Hide();

                //登陆成功进入主界面
                KinectHandTracking.MainWindow main = new KinectHandTracking.MainWindow();
                this.Close();
                main.Show();
                //try
                //{
                //    //数据库连接操作
                //    string source = "server=localhost;integrated security=SSPI;database=Rehabilitation system";
                //    SqlConnection conTvm = new SqlConnection(source);
                //    conTvm.Open();

                //    //如果数据库连接成功
                //    if (conTvm.State == ConnectionState.Open)
                //    {
                //        //数据库查询
                //        string pwdCheck = "select dbo.func_PasswordCheck('" + txt_UserName.Text + "','" + txt_Pwd.Password + "')";
                //        SqlCommand cmdCheck = new SqlCommand(pwdCheck, conTvm);
                //        SqlDataReader drStatus = cmdCheck.ExecuteReader();

                //        if (drStatus.Read())
                //        {
                //            //密码正确
                //            if (1 == int.Parse((drStatus[0]).ToString()))
                //            {
                //                drStatus.Close();
                //                conTvm.Close();
                //                this.Hide();
                //                MessageBox.Show("登录成功，欢迎！");

                //                //登陆成功进入主界面
                //                MainWindow mainWindow = new MainWindow();
                //                this.Close();
                //                mainWindow.Show();

                //            }
                //            //密码错误
                //            else
                //            {
                //                MessageBox.Show("用户名或密码错误，请重新输入！");
                //                this.txt_Pwd.Password = "";
                //            }
                //        }
                //    }
                //    //如果数据库连接失败，抛出异常
                //    else
                //    {
                //        throw new Exception("连接数据库失败！");
                //    }
                //}
                //catch (Exception ex)
                //{
                //    MessageBox.Show(ex.ToString());
                //}
            }
        }

        /// <summary>
        /// 退出按钮单击事件
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btn_exit_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
            Environment.Exit(0);
        }
    }
}
