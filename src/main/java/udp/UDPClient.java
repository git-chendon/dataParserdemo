package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

/**
 * @Author chen.don
 * @date 2019/12/24
 */
public class UDPClient {
    public static void main(String[] args) throws Exception{
        try {

            DatagramSocket socket = new DatagramSocket();
            String s = "这是测试数据11111";
            byte[] buffer = s.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("127.0.0.1"), 10000);
            System.out.println("我要发送消息");
            socket.send(packet);
            System.out.println("我发送消息结束");



            Thread.sleep(1000L);
            byte[] buf = new byte[1024];
            DatagramPacket dp2 = new DatagramPacket(buf, buf.length);
            System.out.println("等待消息");
            socket.receive(dp2);
            System.out.println("成功收到消息");


            /**
             * 属性检查
             */
            System.out.println(packet.getAddress().toString());
            System.out.println(System.getProperties());  //拿到系统属性，包括编码方式。看到式GBK
            System.out.println(Arrays.toString(buffer));
            System.out.println("UTF-8:" + Arrays.toString(s.getBytes("UTF-8")));
            System.out.println("GBK:" + Arrays.toString(s.getBytes("GBK")));

            socket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
