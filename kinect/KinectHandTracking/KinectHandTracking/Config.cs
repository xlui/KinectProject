/* 在该文件中设置：
 *		服务器 IP 地址
 *		服务器端口号
 * 引用：
 *		Config.SERVER_IP
 *		Config.SERVER_PORT
 */
using System;

namespace KinectHandTracking
{
	class Config
	{
		public const String SERVER_IP = "192.168.1.165";        // 测试用的本地 IP 地址
        // public const String SERVER_IP = "192.168.1.165";        // 部署用的远程 IP 地址
        public const int SERVER_PORT = 21567;
	}
}
