package tcp.chatRoom.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author chen.don
 * @date 2019/12/25
 * 使用线程池技术定义一个tcp聊天室服务器
 */
public class TcpServer {

    private ServerSocket sc;
    private ThreadPoolExecutor executor; //线程池
    private Map<String, PrintStream> storeInfo = new HashMap<>();  //存放私聊信息
    private Integer port = 8001;   //指定服务器监听端口

    public TcpServer() {
        try {
            sc = new ServerSocket(port);  //新建服务器
            executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            while (true) {
                System.out.println("等待任务到来");
                Socket s = sc.accept();  //等待连接
                System.out.println("连接成功");
                executor.execute(new Client(s));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 广播群聊
     */
    public void groupSending(String myName, String message) {
        Map<String, PrintStream> storeInfoNew = storeInfo;
        storeInfoNew.entrySet()
                .stream()
                .filter(p -> !p.getKey().equals(myName))
                .forEach(p -> p.getValue().println(myName + ":" + message));
    }


    class Client implements Runnable {

        private Socket s;   //客户端对象
        private String name;  //客户端名称
        private BufferedReader br;  //输入通道
        private PrintStream ps;    //输出通道

        public Client(Socket s) {
            this.s = s;
            try {
                br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                ps = new PrintStream(s.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            try {
                setName();          //设置用户名
                String strWord = br.readLine();   //客户端输入
                while (!strWord.equalsIgnoreCase("quit")) {
                    ps.println(name + ":" + strWord);
                    System.out.println(name + ":" + strWord);
                    groupSending(name, strWord);               //群发消息
                    strWord = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();   // 关闭包装类，会自动关闭包装类中所包装的底层类。所以不用调用ips.close()
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        private void setName() {
            try {
                ps.println("请输入你的名字:");
                this.name = br.readLine();
                storeInfo.put(this.name, ps);  //这里存储映射输出，还可以存储映射线程。
                System.out.println(name + "进入聊天室");
                ps.println("可以开始聊天了");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
