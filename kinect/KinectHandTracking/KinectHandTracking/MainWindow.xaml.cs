using Microsoft.Kinect;
using System;
using System.Collections.Generic;
using System.Windows;
using Newtonsoft.Json;
using System.Drawing;
using System.IO;
using System.Windows.Media.Imaging;
using System.Net;
using System.Text;
using System.Net.Http;

namespace KinectHandTracking
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        #region Members

        KinectSensor _sensor;
        MultiSourceFrameReader _reader;
        private int flag = 0;           //开启屏幕和关闭屏幕的变量, 1为开启，0为关闭                         

        private String leftHandState = "---";     
        private String rightHandState = "---";
        private String lastHandState = "---";      //the hand states
        private String AllHandState = "---";

        private int leftHandStateCode = 0, rightHandStateCode = 0;   // record hand state code
        IList<Body> _bodies;
        Client client = new Client();
        System.Timers.Timer t = new System.Timers.Timer(10000);   //实例化Timer类，设置间隔时间为10000毫秒；
        #endregion

        #region Constructor

        public MainWindow()
        {
            InitializeComponent();
        }

        #endregion

        private void Send_State(String HandState)
        {
            String updateUrl = "http://111.231.1.210/api/dev/update";

            String username = "1", password = "dev";
            String authorization = Convert.ToBase64String(System.Text.Encoding.UTF8.GetBytes(username + ":" + password));

            // 对要发送给服务器的数据进行包装
            Dictionary<String, String> state = new Dictionary<String, String>();
            state.Add("state", AllHandState);
            // 转换成 Json 格式。
            string json = JsonConvert.SerializeObject(state);
            // 向服务器发送新手势
            client.Post(updateUrl, json, authorization);
        }

        private void Send_Img(String filePath)
        {
            String url = "https://nxmup.com/api/dev/upload";
            String username = "1";
            String password = "dev";
            String authorization = Convert.ToBase64String(System.Text.Encoding.UTF8.GetBytes(username + ":" + password));
            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
            request.Headers.Add(HttpRequestHeader.Authorization, "Basic " + authorization);
            request.Method = "POST";
            request.ContentType = "image/jpeg";//表头的格式必须要写,否则请求响应的页面得不到要传递的值 

            Stream oStream = new FileStream("F:/test/hello.jpg", FileMode.Open, FileAccess.Read);
            BinaryReader oReader = new BinaryReader(oStream);
            byte[] imgdata = oReader.ReadBytes(Convert.ToInt32(oStream.Length));

            request.ContentLength = imgdata.Length;

            using (Stream newStream = request.GetRequestStream())
            {
                newStream.Write(imgdata, 0, imgdata.Length);
                newStream.Close();
            }

            HttpWebResponse httpWebResponse = null;
            httpWebResponse = (HttpWebResponse)request.GetResponse();
            Stream s = httpWebResponse.GetResponseStream();
            StreamReader reader = new StreamReader(s, Encoding.GetEncoding("utf-8"));
            string respText = reader.ReadToEnd();
            s.Close();
            Console.WriteLine(respText);

        }

        private void BmpToJpg(Bitmap bmp) //图片转jpg
        {

            if (!Directory.Exists(" F:\\屏幕截图"))  //判断目录是否存在,不存在就创建 
            {
                DirectoryInfo directoryInfo = new DirectoryInfo(" F:\\屏幕截图");
                directoryInfo.Create();
            }

            String time = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss:fff");//获得系统时间
            time = System.Text.RegularExpressions.Regex.Replace(time, @"[^0-9]+", "");//提取数字
            String fileName = time + ".bmp"; //创建文件名
            bmp.Save("F:\\屏幕截图\\" + fileName); //保存为文件.
            bmp.Dispose(); //关闭对象

            String BMPFiles = "F:\\屏幕截图\\" + fileName;
            BitmapImage BitImage = new BitmapImage(new Uri(BMPFiles, UriKind.Absolute));
            JpegBitmapEncoder encoder = new JpegBitmapEncoder();
            encoder.Frames.Add(BitmapFrame.Create(BitImage));
            String JpegImage = time + ".png";

            //JPG文件件路径
            FileStream fileStream = new FileStream("F:\\屏幕截图\\" + JpegImage, FileMode.Create, FileAccess.ReadWrite);
            encoder.Save(fileStream);

            fileStream.Close();
        }
        #region Event handlers

        private void Window_Loaded(object sender, RoutedEventArgs e)
        {
            _sensor = KinectSensor.GetDefault();

            if (_sensor != null)
            {
                _sensor.Open();

                _reader = _sensor.OpenMultiSourceFrameReader(FrameSourceTypes.Color | FrameSourceTypes.Depth | FrameSourceTypes.Infrared | FrameSourceTypes.Body);
                _reader.MultiSourceFrameArrived += Reader_MultiSourceFrameArrived;
            }
        }

        private void Window_Closed(object sender, EventArgs e)
        {
            if (_reader != null)
            {
                _reader.Dispose();
            }

            if (_sensor != null)
            {
                _sensor.Close();
            }
        }


        void Reader_MultiSourceFrameArrived(object sender, MultiSourceFrameArrivedEventArgs e)
        {
            var reference = e.FrameReference.AcquireFrame();

            if (1 == flag)   //开启影像帧
            {
                t.Elapsed += new System.Timers.ElapsedEventHandler(theout); //到达时间的时候执行事件；   
                t.AutoReset = true;   //设置是执行一次（false）还是一直执行(true)；   
                t.Enabled = true;     //是否执行System.Timers.Timer.Elapsed事件；


                // Color
                using (var frame = reference.ColorFrameReference.AcquireFrame())
                {
                    if (frame != null)
                    {
                        camera.Source = frame.ToBitmap();
                    }
                }
            }
            else
            {
                camera.Source = null;
            }


            // Body
            using (var frame = reference.BodyFrameReference.AcquireFrame())
            {
                if (frame != null)
                {
                    canvas.Children.Clear();
                    _bodies = new Body[frame.BodyFrameSource.BodyCount];
                    frame.GetAndRefreshBodyData(_bodies);



                    foreach (var body in _bodies)
                    {
                        if (body != null)
                        {
                            if (body.IsTracked)
                            {
                                if (1 == flag)
                                {
                                    // Find the joints
                                    Joint handRight = body.Joints[JointType.HandRight];
                                    Joint thumbRight = body.Joints[JointType.ThumbRight];

                                    Joint handLeft = body.Joints[JointType.HandLeft];
                                    Joint thumbLeft = body.Joints[JointType.ThumbLeft];

                                    Joint ElbowLeft = body.Joints[JointType.ElbowLeft];
                                    Joint ElbowRight = body.Joints[JointType.ElbowRight];

                                    // Draw hands and thumbs and wrist and ELbow
                                    canvas.DrawHand(handRight, _sensor.CoordinateMapper);
                                    canvas.DrawHand(handLeft, _sensor.CoordinateMapper);
                                    canvas.DrawThumb(thumbRight, _sensor.CoordinateMapper);
                                    canvas.DrawThumb(thumbLeft, _sensor.CoordinateMapper);
                                    canvas.DrawPoint(ElbowLeft, _sensor.CoordinateMapper);
                                    canvas.DrawPoint(ElbowRight, _sensor.CoordinateMapper);


                                    //tblRightHandState.Text = rightHandState;
                                    //tblLeftHandState.Text = leftHandState;
                                    tblhandState.Text = AllHandState;

                                }



                                switch (body.HandRightState)
                                {
                                    case HandState.Open:
                                        rightHandState = "Open";
                                        rightHandStateCode = 1;
                                        break;
                                    case HandState.Closed:
                                        rightHandState = "Closed";
                                        rightHandStateCode = 2;
                                        break;
                                    case HandState.Lasso:
                                        rightHandState = "Lasso";
                                        rightHandStateCode = 3;
                                        break;
                                    case HandState.Unknown:
                                        rightHandState = "Unknown...";
                                        rightHandStateCode = 0;
                                        break;
                                    case HandState.NotTracked:
                                        rightHandState = "Not tracked";
                                        rightHandStateCode = -1;
                                        break;
                                    default:
                                        break;
                                }

                                switch (body.HandLeftState)
                                {
                                    case HandState.Open:
                                        leftHandState = "Open";
                                        leftHandStateCode = 1;
                                        break;
                                    case HandState.Closed:
                                        leftHandState = "Closed";
                                        leftHandStateCode = 2;
                                        break;
                                    case HandState.Lasso:
                                        leftHandState = "Lasso";
                                        leftHandStateCode = 3;
                                        break;
                                    case HandState.Unknown:
                                        leftHandState = "Unknown...";
                                        leftHandStateCode = 0;
                                        break;
                                    case HandState.NotTracked:
                                        leftHandState = "Not tracked";
                                        leftHandStateCode = -1;
                                        break;
                                    default:
                                        break;
                                }


                                if (leftHandStateCode == 1 && rightHandStateCode == 1)
                                {
                                    AllHandState = "设备已开启";
                                    tbltips.Text = "双手做剪刀动作使设备关闭";
                                    flag = 1;
                                }
                                else if (leftHandStateCode == 1 && rightHandStateCode == 2)
                                {
                                    AllHandState = "口渴";
                                }
                                else if (leftHandStateCode == 1 && rightHandStateCode == 3)
                                {
                                    AllHandState = "上厕所";
                                }
                                else if (leftHandStateCode == 2 && rightHandStateCode == 1)
                                {
                                    AllHandState = "signal4";
                                }
                                else if (leftHandStateCode == 2 && rightHandStateCode == 2)
                                {
                                    AllHandState = "signal5";
                                }
                                else if (leftHandStateCode == 2 && rightHandStateCode == 3)
                                {
                                    AllHandState = "联系亲友";
                                }
                                else if (leftHandStateCode == 3 && rightHandStateCode == 1)
                                {
                                    AllHandState = "饥饿";
                                }
                                else if (leftHandStateCode == 3 && rightHandStateCode == 2)
                                {
                                    AllHandState = "身体不适";
                                }
                                else if (leftHandStateCode == 3 && rightHandStateCode == 3)
                                {
                                    AllHandState = "设备已关闭";
                                    tblhandState.Text = AllHandState;
                                    tbltips.Text = "双手打开使设备开启";
                                    flag = 0;
                                }


                                if (!AllHandState.Equals(lastHandState))
                                {
                                    lastHandState = AllHandState;
                                    Send_State(AllHandState);
                                }
                            }
                        }
                    }
                }
            }

            void theout(object source, System.Timers.ElapsedEventArgs e1)
            {
                //Scrennshot
                using (var frame = reference.ColorFrameReference.AcquireFrame())
                {
                    if (frame != null)
                    {
                        System.IO.MemoryStream ms = new System.IO.MemoryStream();
                        BmpBitmapEncoder encoder = new BmpBitmapEncoder();
                        encoder.Frames.Add(BitmapFrame.Create((BitmapSource)frame.ToBitmap()));
                        encoder.Save(ms);
                        Bitmap bp = new Bitmap(ms);
                        BmpToJpg(bp);

                        ms.Close();
                    }
                }
            }
        }
        #endregion
    }

}
