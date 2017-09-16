/* 在该文件中设置：
 *		服务器 IP 地址
 *		服务器端口号
 * 引用：
 *		Config.SERVER_IP
 *		Config.SERVER_PORT
 */
using System;

namespace csharp_client
{
	class Config
	{
        // 部署用的服务器 IP 地址
        // public const String SERVER_IP = "111.231.1.210";

        // 测试用的本机 IP 地址
        public const String SERVER_IP = "10.100.67.235";
        public const int SERVER_PORT = 21567;
	}
}
