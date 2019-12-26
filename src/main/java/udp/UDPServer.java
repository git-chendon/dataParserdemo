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
public class UDPServer {
    public static void main(String[] args) throws Exception{
        try {
            DatagramSocket socket = new DatagramSocket(10000);
            byte[] buffer = new byte[65508];
            DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length);
            while(true){
                System.out.println("UdpRecv: 我在等待信息");
                socket.receive(packet);
                String s = new String(packet.getData(),0,packet.getLength());
                System.out.println(s);
                System.out.println("UdpRecv: 我接收到信息");



                Thread.sleep(1000L);
                System.out.println("UdpRecv: 我要发送信息");
                String str = "hello world 222";
                DatagramPacket dp2 = new DatagramPacket(str.getBytes(),str.length(), InetAddress.getByName("127.0.0.1"),packet.getPort());
                socket.send(dp2);
                System.out.println("UdpRecv: 我发送信息结束");
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
