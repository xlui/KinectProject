using Microsoft.Kinect;
using System;
using System.Collections.Generic;
using System.Windows;

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
		//private String lastLeftHandState = "";
		private String lastRightHandState = "";
		public string username { get; set; }
		// 定义该变量用于登录对话框向主对话框传递用户名

		#endregion

		#region Constructor

		public MainWindow()
        {
            InitializeComponent();
        }

        #endregion

		private void Send(int toSend) 
		{
			SocketClient socketClient = new SocketClient();
			string receive = null;

			receive = socketClient.Send("code:" + toSend.ToString() + "username:" + username);
			// Send 函数将发送框的数据发送给服务器，并接受服务器的回应 
			// code 代表不同的手势，username 是登录的用户
			// 请注意：目前安卓客户端只响应 code:1 code:2 code3 只检测右手状态
			// 所以 Send 的内容为： 1 2 3 时，安卓会响应；其他安卓不做响应。

			if (receive.Length == 0)
				MessageBox.Show("服务器连接异常！", "Error");
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

                                // Draw hands and thumbs
                                canvas.DrawHand(handRight, _sensor.CoordinateMapper);
                                canvas.DrawHand(handLeft, _sensor.CoordinateMapper);
                                canvas.DrawThumb(thumbRight, _sensor.CoordinateMapper);
                                canvas.DrawThumb(thumbLeft, _sensor.CoordinateMapper);

                                // Find the hand states
                                string rightHandState = "-";
								string leftHandState = "-";
								// record hand state code
								int rightHandStateCode = 0;

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
                                        break;
                                    case HandState.NotTracked:
                                        rightHandState = "Not tracked";
                                        break;
                                    default:
                                        break;
                                }

                                switch (body.HandLeftState)
                                {
                                    case HandState.Open:
                                        leftHandState = "Open";
                                        break;
                                    case HandState.Closed:
                                        leftHandState = "Closed";
                                        break;
                                    case HandState.Lasso:
                                        leftHandState = "Lasso";
                                        break;
                                    case HandState.Unknown:
                                        leftHandState = "Unknown...";
                                        break;
                                    case HandState.NotTracked:
                                        leftHandState = "Not tracked";
                                        break;
                                    default:
                                        break;
                                }

                                tblRightHandState.Text = rightHandState;
								tblLeftHandState.Text = leftHandState;

								if (!rightHandState.Equals(lastRightHandState))
								{
									// 如果当前手势与之前记录的不同，则发送当前手势的代码到服务器端
									lastRightHandState = rightHandState;
									Send(rightHandStateCode);
								}
                            }
                        }
                    }
                }
            }
        }

        #endregion
    }
}
