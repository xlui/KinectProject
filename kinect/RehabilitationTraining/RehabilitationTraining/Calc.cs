using Microsoft.Kinect;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RehabilitationTraining
{
    class Calc
    {
        /// <summary>
        /// 计算两组两个关节的夹角
        /// </summary>
        /// <param name="AStart">A起点关节</param>
        /// <param name="AEnd">A终点关节</param>
        /// <param name="BStart">B起点关节</param>
        /// <param name="BEnd">B终点关节</param>
        /// <returns></returns>
        static public double GetAngle(Joint AStart, Joint AEnd, Joint BStart, Joint BEnd)
        {
            double x1 = AEnd.Position.X - AStart.Position.X;
            double y1 = AEnd.Position.Y - AStart.Position.Y;
            double z1 = AEnd.Position.Z - AStart.Position.Z;
            double x2 = BEnd.Position.X - BStart.Position.X;
            double y2 = BEnd.Position.Y - BStart.Position.Y;
            double z2 = BEnd.Position.Z - BStart.Position.Z;

            double angle;
            angle = Math.Acos((x1 * x2 + y1 * y2 + z1 * z2)
                / (Math.Sqrt(x1 * x1 + y1 * y1 + z1 * z1) * Math.Sqrt(x2 * x2 + y2 * y2 + z2 * z2)))
                / Math.PI * 180;
            return angle;
        }
    }
}
