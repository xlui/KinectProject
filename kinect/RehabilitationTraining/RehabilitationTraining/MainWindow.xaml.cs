using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Microsoft.Kinect;
using System.IO;
namespace RehabilitationTraining
{
    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : Window
    {
        #region Member Variables
        //体感器设备
        private KinectSensor _KinectDevice = null;

        //多种帧读取
        private MultiSourceFrameReader _MultiFrameReader = null;

        //彩色帧描述
        private FrameDescription _ColorFrameDescription = null;

        //位图对象
        private WriteableBitmap _ColorBitmap = null;

        //存放一帧彩色图像的矩形框
        private Int32Rect _ColorBitmapRect;

        //步长，一维彩色像素数组转化为矩形框的步长。
        private int _ColorBitmapStride;

        //存放一帧彩色图像像素
        private Byte[] _ColorPixelData;

        //玩家数据
        private Body[] _Bodies;

        //主要玩家数据
        private Body _PrimaryBody;

        //画骨架图颜色uint[]
        private Brush[] ColorBody = new Brush[]{
            Brushes.Red,Brushes.Green,Brushes.Pink,Brushes.Blue,Brushes.Yellow,Brushes.Orange
        };

        //关节点两两相连
        private JointType[] _JointType = new JointType[]{
            JointType.SpineBase,JointType.SpineMid,
            JointType.SpineMid,JointType.SpineShoulder,
            JointType.SpineShoulder,JointType.Neck,
            JointType.Neck,JointType.Head,

            JointType.SpineShoulder,JointType.ShoulderLeft,
            JointType.ShoulderLeft,JointType.ElbowLeft,
            JointType.ElbowLeft,JointType.WristLeft,
            JointType.WristLeft,JointType.HandLeft,
            JointType.HandLeft,JointType.HandTipLeft,
            JointType.HandLeft,JointType.ThumbLeft,

            JointType.SpineShoulder,JointType.ShoulderRight,
            JointType.ShoulderRight,JointType.ElbowRight,
            JointType.ElbowRight,JointType.WristRight,
            JointType.WristRight,JointType.HandRight,
            JointType.HandRight,JointType.HandTipRight,
            JointType.HandRight,JointType.ThumbRight,

            JointType.SpineBase,JointType.HipLeft,
            JointType.HipLeft,JointType.KneeLeft,
            JointType.KneeLeft,JointType.AnkleLeft,
            JointType.AnkleLeft,JointType.FootLeft,

            JointType.SpineBase,JointType.HipRight,
            JointType.HipRight,JointType.KneeRight,
            JointType.KneeRight,JointType.AnkleRight,
            JointType.AnkleRight,JointType.FootRight
        };

        //数据保存
        //记录手臂角度
        private double _Angele = 0;
        private StreamWriter _SW;
        private string _CoordinatePath;
        private bool _IsSave;

        #endregion Member Variables

        /// <summary>
        /// 构造函数
        /// </summary>
        public MainWindow()
        {
            InitializeComponent();
        }


        #region Methods

        /// <summary>
        /// 彩色和骨骼帧同时触发处理事件
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        void _MultiFrameReader_MultiSourceFrameArrived(object sender, MultiSourceFrameArrivedEventArgs e)
        {
            //打开多种帧
            MultiSourceFrame msf = e.FrameReference.AcquireFrame();

            //不为空
            if (msf != null)
            {
                //打开骨骼帧和彩色帧
                using (BodyFrame bodyFrame = msf.BodyFrameReference.AcquireFrame())
                {
                    using (ColorFrame colorFrame = msf.ColorFrameReference.AcquireFrame())
                    {
                        LeftArmExercise(bodyFrame, colorFrame);
                    }
                }
            }
        }


        /// <summary>
        /// 获得Body对象
        /// </summary>
        /// <param name="bodies"></param>
        /// <returns></returns>
        private Body GetNearBody(Body[] bodies)
        {
            Body body = null;

            if (bodies != null)
            {
                for (int i = 0; i < bodies.Length; i++)
                {
                    if (bodies[i].IsTracked == true)
                    {
                        if (body == null)
                        {
                            body = bodies[i];
                        }
                        else
                        {
                            if (Math.Abs(body.Joints[JointType.SpineBase].Position.Z) > Math.Abs(bodies[i].Joints[JointType.SpineBase].Position.Z))
                            {
                                body = bodies[i];
                            }
                        }
                    }
                }
            }
            return body;
        }


        /// <summary>
        /// 绘制手臂
        /// </summary>
        /// <param name="oneBody">要绘制的Body对象</param>
        private void DrawArm(Body oneBody)
        {
            //通过循环将关节点两两连接
            JointType[] _JointType = new JointType[]
            {
                JointType.HandLeft,JointType.ElbowLeft,
                JointType.ElbowLeft,JointType.ShoulderLeft
            };

            //逐个绘制操作
            for (int j = 0; j < _JointType.Length; j += 2)
            {
                Line line = new Line();
                line.Stroke = Brushes.Red;
                line.StrokeThickness = 5;

                //起点的屏幕坐标和终点的屏幕坐标
                Point StartP = GetJointPointScreen(oneBody.Joints[_JointType[j]]);
                Point EndP = GetJointPointScreen(oneBody.Joints[_JointType[j + 1]]);

                Ellipse sp = new Ellipse();
                Ellipse ep = new Ellipse();

                sp.Width = 10;
                sp.Height = 10;
                sp.HorizontalAlignment = HorizontalAlignment.Left;
                sp.VerticalAlignment = VerticalAlignment.Top;
                sp.Fill = Brushes.Yellow;
                ep.Width = 10;
                ep.Height = 10;
                ep.HorizontalAlignment = HorizontalAlignment.Left;
                ep.VerticalAlignment = VerticalAlignment.Top;
                ep.Fill = Brushes.Yellow;

                sp.Margin = new Thickness(StartP.X - sp.Width / 2, StartP.Y - sp.Height / 2, 0, 0);
                ep.Margin = new Thickness(EndP.X - ep.Width / 2, EndP.Y - ep.Height / 2, 0, 0);

                //设置起点、终点的坐标
                line.X1 = StartP.X;
                line.Y1 = StartP.Y;
                line.X2 = EndP.X;
                line.Y2 = EndP.Y;

                //把起点、终点及连接添加到网格中。
                CanvasBody.Children.Add(sp);
                CanvasBody.Children.Add(ep);
                CanvasBody.Children.Add(line);
            }
        }



        /// <summary>
        /// 绘制骨骼
        /// </summary>
        /// <param name="oneBody">要绘制的Joint对象</param>
        private void DrawBody(Body oneBody)
        {
            //通过循环将关节点两两连接，
            for (int j = 0; j < this._JointType.Length; j += 2)
            {
                Line line = new Line();
                line.Stroke = Brushes.Gray;
                line.StrokeThickness = 5;

                //起点的屏幕坐标和终点的屏幕坐标
                Point StartP = GetJointPointScreen(oneBody.Joints[this._JointType[j]]);
                Point EndP = GetJointPointScreen(oneBody.Joints[this._JointType[j + 1]]);

                Ellipse sp = new Ellipse();
                Ellipse ep = new Ellipse();

                sp.Width = 10;
                sp.Height = 10;
                sp.HorizontalAlignment = HorizontalAlignment.Left;
                sp.VerticalAlignment = VerticalAlignment.Top;
                sp.Fill = Brushes.Yellow;
                ep.Width = 10;
                ep.Height = 10;
                ep.HorizontalAlignment = HorizontalAlignment.Left;
                ep.VerticalAlignment = VerticalAlignment.Top;
                ep.Fill = Brushes.Yellow;

                sp.Margin = new Thickness(StartP.X - sp.Width / 2, StartP.Y - sp.Height / 2, 0, 0);
                ep.Margin = new Thickness(EndP.X - ep.Width / 2, EndP.Y - ep.Height / 2, 0, 0);

                //设置起点、终点的坐标
                line.X1 = StartP.X;
                line.Y1 = StartP.Y;
                line.X2 = EndP.X;
                line.Y2 = EndP.Y;

                //把起点、终点及连接添加到网格中。
                CanvasBody.Children.Add(sp);
                CanvasBody.Children.Add(ep);
                CanvasBody.Children.Add(line);
            }
        }



        /// <summary>
        /// 骨骼坐标转化为彩色图像坐标，再转化为屏幕坐标
        /// </summary>
        /// <param name="oneJoint">骨骼坐标对象</param>
        /// <returns></returns>
        private Point GetJointPointScreen(Joint oneJoint)
        {
            //骨骼坐标转化为彩色图像坐标
            ColorSpacePoint colorPoint = this._KinectDevice.CoordinateMapper.MapCameraPointToColorSpace(oneJoint.Position);

            //彩色图像坐标转化为屏幕坐标

            colorPoint.X = (int)((colorPoint.X * CanvasBody.Width) / 1920);
            colorPoint.Y = (int)((colorPoint.Y * CanvasBody.Height) / 1080);

            //返回Point类型变量
            return new Point(colorPoint.X, colorPoint.Y);
        }


        /// <summary>
        /// 窗口关闭处理
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void Window_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            //关闭处理
            if (this._MultiFrameReader != null)
            {
                this._MultiFrameReader.Dispose();
                this._MultiFrameReader = null;
            }

            //体感器关闭处理
            if (this._KinectDevice != null)
            {
                this._KinectDevice.Close();
                this._KinectDevice = null;
            }
        }


        private void Window_Loaded(object sender, RoutedEventArgs e)
        {
            //获取默认的连接的体感器
            this._KinectDevice = KinectSensor.GetDefault();

            //多种帧读取彩色帧和骨骼帧
            this._MultiFrameReader = this._KinectDevice.OpenMultiSourceFrameReader(FrameSourceTypes.Color | FrameSourceTypes.Body);

            //多种帧事件
            this._MultiFrameReader.MultiSourceFrameArrived += _MultiFrameReader_MultiSourceFrameArrived;

            //彩色帧描述，宽度和高度，注意括号内的入参，代表这一帧彩色图像像素的格式
            this._ColorFrameDescription = this._KinectDevice.ColorFrameSource.CreateFrameDescription(ColorImageFormat.Bgra);

            //存放彩色图像的字节数组的长度=帧宽度*帧高度*每个像素占用的字节数4
            this._ColorPixelData = new Byte[this._ColorFrameDescription.LengthInPixels * 4];

            //位图初始化，宽度，高度，96.0表示分辨率，像素格式，blue,green,red,alpha,共32位。
            this._ColorBitmap = new WriteableBitmap(this._ColorFrameDescription.Width,
                                                this._ColorFrameDescription.Height, 96.0, 96.0, PixelFormats.Bgra32, null);//注意格式，32位，四个字节

            //存放图像像素的矩形框，起点为（0，0），宽度和高度
            this._ColorBitmapRect = new Int32Rect(0, 0, this._ColorBitmap.PixelWidth, this._ColorBitmap.PixelHeight);

            //步长：宽度*4字节/像素
            this._ColorBitmapStride = this._ColorFrameDescription.Width * 4;

            //UI界面的图片控件和位图关联起来
            colorImage.Source = this._ColorBitmap;

            //玩家骨骼数组长度为6
            this._Bodies = new Body[6];

        }


        /// <summary>
        /// 开始训练选项
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void StartEx_Click(object sender, RoutedEventArgs e)
        {
            //训练操作
            if (StartEx.Header.ToString() == "开始训练")
            {
                //启动体感器
                this._KinectDevice.Open();
                StartEx.Header = "终止训练";
                colorImage.Source = this._ColorBitmap;
            }

            //终止训练操作
            else if (StartEx.Header.ToString() == "终止训练")
            {
                this._KinectDevice.Close();
                CanvasBody.Children.Clear();
                StartEx.Header = "开始训练";
                colorImage.Source = null;              
                lbl_Tips.Content = "";

            }
            //出错
            else
            {
                throw new Exception("出现致命错误！请重启！");
            }
        }

        #endregion Methods



        #region 不同训练方法


        /// <summary>
        /// 训练左手手臂的方法
        /// </summary>
        /// <param name="bodyFrame">身体框架</param>
        /// <param name="colorFrame">彩色框架</param>
        protected void LeftArmExercise(BodyFrame bodyFrame, ColorFrame colorFrame)
        {
            //骨骼帧和彩色帧都不为空
            if (bodyFrame != null && colorFrame != null)
            {
                //彩色帧拷贝到数组
                colorFrame.CopyConvertedFrameDataToArray(this._ColorPixelData, ColorImageFormat.Bgra);

                //数组写到位图
                this._ColorBitmap.WritePixels(this._ColorBitmapRect, this._ColorPixelData, this._ColorBitmapStride, 0);

                //骨骼数据拷贝到数组
                bodyFrame.GetAndRefreshBodyData(this._Bodies);

                //清空上次画的骨架
                CanvasBody.Children.Clear();

                bodyFrame.GetAndRefreshBodyData(this._Bodies);
                _PrimaryBody = GetNearBody(_Bodies);

                if (_PrimaryBody != null)
                {
                    DrawBody(_PrimaryBody);
                    DrawArm(_PrimaryBody);

                    Joint head = _PrimaryBody.Joints[JointType.Head];
                    Joint neck = _PrimaryBody.Joints[JointType.Neck];
                    Joint spineShoulder = _PrimaryBody.Joints[JointType.SpineShoulder];
                    Joint shoulderLeft = _PrimaryBody.Joints[JointType.ShoulderLeft];
                    Joint shoulderRight = _PrimaryBody.Joints[JointType.ShoulderRight];
                    Joint elbowLeft = _PrimaryBody.Joints[JointType.ElbowLeft];
                    Joint elbowRight = _PrimaryBody.Joints[JointType.ElbowRight];
                    Joint handLeft = _PrimaryBody.Joints[JointType.HandLeft];
                    Joint handRight = _PrimaryBody.Joints[JointType.HandRight];
                    Joint spineMid = _PrimaryBody.Joints[JointType.SpineMid];

                    _Angele = Calc.GetAngle(spineShoulder, spineMid, shoulderLeft, elbowLeft);             

                    if (_Angele < 10.0)
                    {
                        lbl_Tips.Content = "现在，请慢慢抬起您的手臂";
                    }
                    else if (_Angele > 87)
                    {
                        lbl_Tips.Content = "很好，请慢慢放下您的手臂";
                    }
                }
            }
        }

        /// <summary>
        /// 单击训练设置
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void ExSetting_Click(object sender, RoutedEventArgs e)
        {
            ExerciseSettingWindow exSettingWindow = new ExerciseSettingWindow();
            exSettingWindow.ShowDialog();
        }

        #endregion


    }
}
