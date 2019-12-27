package tcp.chatRoom.Client;

import tcp.chatroom1.SocketClient;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @Author chen.don
 * @date 2019/12/25
 */
public class TcpClient extends Socket {

    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 8001;

    private Socket client;
    private PrintWriter out;
    private BufferedReader serverIn;
    private BufferedReader in;

    /**
     * 与服务器连接，并输入发送消息
     */
    public TcpClient() throws Exception {
        super(SERVER_IP, SERVER_PORT);
        client = this;
        out = new PrintWriter(this.getOutputStream(), true);
        serverIn = new BufferedReader(new InputStreamReader(this.getInputStream()));
        in = new BufferedReader(new InputStreamReader(System.in));
        new readLineThread().start();

        while (true) {
            String input = in.readLine();            //拿到用户输入
            out.println(input);
        }
    }

    /**
     * 用于监听服务器端向客户端发送消息线程类
     * 注意，用户输入部分不用使用线程处理。
     */
    class readLineThread extends Thread {

        public readLineThread() {
            try {
                System.out.println(serverIn.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            try {
                while (true) {
                    String result = serverIn.readLine();     //拿到服务器输入
                    if ("byeClient".equals(result)) {        //客户端申请退出，服务端返回确认退出
                        break;
                    } else {//输出服务端发送消息
                        System.out.println(result);
                    }
                }
                in.close();
                out.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
