package tcp.chatroom2;


import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Author chen.don
 * @date 2019/12/26
 */

//客户端
//输入、输出流分别为处于一个线程
public class SingelClient2 {
    public static void main(String[] args) {
        String serverName = "127.0.0.1";
        int port = 6666;
        Socket client;
        try {
            client = new Socket(serverName,port);
            Thread ouThread = new Thread(new OutToServer2(client));
            Thread inThread = new Thread(new ReadFromServer2(client));
            ouThread.start();
            inThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ReadFromServer2 implements Runnable{
    private Socket client;
    public ReadFromServer2(Socket client) {
        super();
        this.client = client;
    }
    public void run() {
        try {
            Scanner scanner = new Scanner(client.getInputStream());
            scanner.useDelimiter("\n");
            while(scanner.hasNext()) {
                System.out.println("服务器端输入："+scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

class OutToServer2 implements Runnable{
    private Socket client;

    public OutToServer2(Socket client) {
        super();
        this.client = client;
    }

    @Override
    public void run() {
        PrintStream printStream;
        try {
            printStream = new PrintStream(client.getOutputStream(),true);
            Scanner scanner = new Scanner(System.in);
            scanner.useDelimiter("\n");
            while(true) {
                System.out.println("请输入：");
                while(scanner.hasNext()) {
                    String string = scanner.nextLine();
                    printStream.println(string);
                    if(string.equals("拜拜")) {
                        System.out.println("客户端退出");
                        printStream.close();
                        scanner.close();
                        //client.close();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}