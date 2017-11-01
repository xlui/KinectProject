using Microsoft.Kinect;
using System;
using System.Collections.Generic;
using System.Windows;
using Newtonsoft.Json;
using System.Drawing;
using System.IO;
using System.Threading;
using System.Windows.Media.Imaging;

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
        IList<Body> _bodies;
        Client client = new Client();
        System.Timers.Timer t = new System.Timers.Timer(10000);   //实例化Timer类，设置间隔时间为10000毫秒；

        private String lastHandState = "-";
        private String AllHandState = "-";
       
        #endregion

        #region Constructor

        public MainWindow()
        {
            InitializeComponent();
        }

        #endregion

        private void Send(String HandState)
        {
            //string latestUrl = "http://111.231.1.210/api/dev/latest";
            string updateUrl = "http://111.231.1.210/api/dev/update";
            String url = "https://nxmup.com/api/dev/upload";

            string username = "1", password = "dev";
            string authorization = Convert.ToBase64String(System.Text.Encoding.UTF8.GetBytes(username + ":" + password));

            // 对要发送给服务器的数据进行包装
            Dictionary<string, string> state = new Dictionary<string, string>();
            state.Add("state", AllHandState);
            // 转换成 Json 格式。
            string json = JsonConvert.SerializeObject(state);
            // 向服务器发送新手势
            client.Post(updateUrl, json, authorization);
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
            FileStream fileStream = new FileStream("F:\\屏幕截图\\"+JpegImage, FileMode.Create, FileAccess.ReadWrite);
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

                                // Find the hand states
                                string rightHandState = "-";
								string leftHandState = "-";
                                
								// record hand state code
								int leftHandStateCode = 0, rightHandStateCode = 0;

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
                                    AllHandState = "打开设备";
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
                                    AllHandState = "关闭设备";
                                }

                                tblRightHandState.Text = rightHandState;
                                tblLeftHandState.Text = leftHandState;
                                tblhandState.Text = AllHandState;
                              
                                if (!AllHandState.Equals(lastHandState))
                                {
                                    lastHandState = AllHandState;
                                    Send(AllHandState);
                                    
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
