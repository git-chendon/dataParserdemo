package tcp.chatroom2;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author chen.don
 * @date 2019/12/26
 */

//服务器端
/**
 * Map<String,Socket>
 * 用户注册：username:yyy
 * 群聊：G:hello
 * 私聊：P:yyy-hhh
 */
public class SingleServer2 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6666);
        //存取用户信息（用户名和Socket）
        Map<String, Socket> map = new HashMap<String, Socket>();
        //线程池，线程大小为20
        ExecutorService executorService =Executors.newFixedThreadPool(20);
        System.out.println("等待客户连接中...");
        try {
            for(int i = 0;i < 20;i ++) {
                Socket socket = serverSocket.accept();
                System.out.println("有新的用户连接："+socket.getInetAddress()+socket.getPort());
                executorService.execute(new ExcuteClientServer1(socket,map));
            }
            executorService.shutdown();
            serverSocket.close();
        } catch (Exception e) {
        }
    }
}

class  ExcuteClientServer1 implements Runnable{
    private Socket client;
    private Map<String,Socket> clientMap;
    public ExcuteClientServer1(Socket client, Map<String, Socket> clientMap) {
        super();
        this.client = client;
        this.clientMap = clientMap;
    }

    public void run() {
        try {
            //拿到客户端输入流，读取用户信息
            Scanner scanner = new Scanner(client.getInputStream());
            String string = null;
            while(true){
                if(scanner.hasNext()) {
                    string = scanner.nextLine();
                    Pattern pattern = Pattern.compile("\r\n|\r|\n");
                    Matcher matcher = pattern.matcher(string);
                    string = matcher.replaceAll("");
                    //用户注册
                    if(string.startsWith("user")) {
                        //获取用户名
                        String useName = string.split("\\:")[1];
                        userRegist(useName, client);
                        continue;
                    }
                    //群聊
                    else if(string.startsWith("G")) {
                        String message = string.split("\\:")[1];
                        gropChat(message);
                        continue;
                    }
                    //私聊
                    else if(string.startsWith("P")) {
                        String temp = string.split("\\:")[1];
                        //取得用户名
                        String useName = temp.split("\\-")[0];
                        //取得消息内容
                        String message = temp.split("\\-")[1];
                        privateChat(useName, message);
                        continue;
                    }
                    //用户退出
                    else if(string.contains("拜拜")) {
                        //先根据Socket知道用户名
                        String useName = getUseName(client);
                        System.out.println("用户名为"+useName +"的用户下线了！！！");
                        clientMap.remove(useName);
                        continue;
                    }
                }
            }
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //获取key值（即由端口号找到用户名）
    public String getUseName(Socket socket) {
        String useName = null;
        for(String getKey : clientMap.keySet()) {
            if(clientMap.get(getKey).equals(socket)) {
                useName = getKey;
            }
        }
        return useName;
    }
    //注册实现
    public void userRegist(String useName,Socket client) {
        System.out.println("用户姓名为：" + useName);
        System.out.println("用户socket为：" + client);
        System.out.println("用户名为"+ useName +"的用户上线了！");
        System.out.println("当前用户数为："+ (clientMap.size()+1) +"人");
        clientMap.put(useName, client);
    }
    //群聊实现
    public void gropChat(String message) {
        Iterator<Entry<String, Socket>> iterable = clientMap.entrySet().iterator();
        for(Map.Entry<String, Socket> stringSocketEntry:clientMap.entrySet()) {
            try {
                Socket socket = stringSocketEntry.getValue();
                PrintStream printStream = new PrintStream(socket.getOutputStream(),true);
                System.out.println("用户说"+message);
                printStream.println(message);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
    //私聊实现
    public void privateChat(String useName,String message) {
        //根据对应的useName找到对应的Socket
        Socket privateSocket = clientMap.get(useName);
        try {
            PrintStream printStream = new PrintStream(privateSocket.getOutputStream());
            printStream.println("用户名为"+getUseName(client)+"的用户对你说："+message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}