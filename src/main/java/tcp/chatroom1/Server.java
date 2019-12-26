package tcp.chatroom1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author chen.don
 * @date 2019/12/26
 */
public class Server extends ServerSocket {
    private static final int SERVER_PORT = 5566;
    private static boolean isPrint = false;// 是否输出消息标志
    private static List<String> user_list = new ArrayList<String>();// 登录用户集合
    private static List<ServerThread> thread_list = new ArrayList<ServerThread>();// 服务器已启用线程集合
    private static LinkedList<Message> message_list = new LinkedList<Message>();// 存放用户消息的队列

    /**
     * 创建服务端Socket,创建向客户端发送消息线程,监听客户端请求并处理
     */
    public Server() throws IOException {
        super(SERVER_PORT);// 创建ServerSocket
        new PrintOutThread();// 处理所有客户端发送的消息
        System.out.println("服务器已启动");
        try {
            while (true) {// 监听客户端请求，启个线程处理
                Socket socket = accept();
                new ServerThread(socket);
            }
        } catch (Exception e) {
        } finally {
            close();
        }
    }

    /**
     * 监听是否有消息在队列里的线程类,向除自己之外的客户端发送消息
     */

    class PrintOutThread extends Thread {

        public PrintOutThread() {
            start();
        }

        @Override
        public void run() {
            while (true) {
                //如果消息队列没有消息则暂停当前线程，把cpu片段让出给其他线程,提高性能
                if (!isPrint) {
                    try {
                        Thread.sleep(500);
                        sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    continue;
                }
                // 将缓存在队列中的消息按顺序发送到各客户端，并从队列中清除。
                Message message = (Message) message_list.getFirst();
                // 对所有的用户的线程遍历，如果不是自己发的消息就广播给其他人
                for (int i = 0; i < thread_list.size(); i++) {
                    // 由于添加线程和用户是一起的，所以i所对应的用户就是i所对应的线程，可以根据这个判断是不是自己的线程
                    ServerThread thread = thread_list.get(i);
                    if (message.getName() != user_list.get(i)) {
                        thread.sendMessage(message);
                    }
                }
                message_list.removeFirst();
                isPrint = message_list.size() > 0 ? true : false;

            }
        }
    }

    /**
     * 服务器线程类
     */
    class ServerThread extends Thread {
        // 客户端
        private Socket client;
        // 打印流
        private PrintWriter out;
        // 读取客户端发的消息的缓冲流
        private BufferedReader in;
        // 客户名字
        private String name;

        public ServerThread(Socket s) throws IOException {
            client = s;
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    client.getInputStream()));
            in.readLine();

            start();
        }

        @Override
        public void run() {
            out.println("成功连上聊天室,请输入你的名字：");
            System.out.println(getName());
            try {
                int flag = 0;
                String line = in.readLine();
                while (!"byeClient".equals(line)) {

                    // 查看在线用户列表
                    if ("showuser".equals(line)) {
                        out.println(this.listOnlineUsers());
                        line = in.readLine();
                    }
                    if ("showmessage".equals(line)) {
                        out.println(this.listmassage());
                        line = in.readLine();
                    }

                    // 第一次进入，保存名字
                    if (flag == 0) {
                        name = line;
                        user_list.add(name);
                        thread_list.add(this);
                        out.println(name + "你好,可以开始聊天了...");
                        System.out.println(name + "连接服务器");
                        pushMessage(name, "进入聊天室");
                    } else {
                        pushMessage(name, line);
                    }
                    flag++;
                    line = in.readLine();
                    System.out.println(name + ":" + line);
                }
                out.println("byeClient");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {// 用户退出聊天室
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                thread_list.remove(this);
                user_list.remove(name);
                pushMessage(name, "退出了聊天室");
            }
        }

        // 放入消息队列末尾，准备发送给客户端
        public void pushMessage(String name, String msg) {
            Message message = new Message(name, msg);
            // 放入用户信息
            message_list.addLast(message);
            // 表示可以向其他用户发送消息
            isPrint = true;
        }

        // 向客户端发送一条消息
        public void sendMessage(Message message) {
            out.println(message.getName() + ":" + message.getMessage());
        }

        // 统计在线用户列表
        private String listOnlineUsers() {
            String s = "--- 在线用户列表 ---\015\012";
            for (int i = 0; i < user_list.size(); i++) {
                s += "[" + user_list.get(i) + "]\015\012";
            }
            s += "--------------------";
            return s;
        }

        // 统计在线用户列表
        private String listmassage() {
            String s = "--- 消息列表 ---\015\012";
            for (int i = 0; i < message_list.size(); i++) {
                s += "[" + message_list.get(i) + "]\015\012";
            }
            s += "--------------------";
            return s;
        }
    }

    public static void main(String[] args) throws IOException {
        new Server();// 启动服务端
    }
}

// 消息类，用户以及用户发的一条消息
class Message {
    // 用户名
    String client;
    // 消息
    String message;

    public Message() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Message(String client, String message) {
        super();
        this.client = client;
        this.message = message;
    }

    public String getName() {
        return client;
    }

    public void setName(String name) {
        this.client = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message [client=" + client + ", message=" + message + "]";
    }

}
