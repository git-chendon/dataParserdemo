package tcp.chatRoom;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * @Author chen.don
 * @date 2019/12/25
 * 聊天室功能worker
 */
public class Client implements Runnable {

    Socket s;   //客户端对象

    String name;  //客户端名称

    BufferedReader br;  //输入通道

    DataOutputStream dos;  //输出通道

    public Client(Socket s) {

        this.s = s;

        try {
            InputStream ips = s.getInputStream();
            OutputStream ops = s.getOutputStream();

            br = new BufferedReader(new InputStreamReader(ips));
            dos = new DataOutputStream(ops);
            //        PrintStream ps = new PrintStream(ops); //两种方法

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run()  {
        while (true)
            try {
                String  strWord = br.readLine();
                System.out.println("client said:" + strWord +":" + strWord.length());
                if (strWord.equalsIgnoreCase("quit"))
                    break;
                String strEcho = strWord + " 666";
                // dos.writeBytes(strWord +"---->"+ strEcho +"\r\n");
                System.out.println("server said:" + strWord + "---->" + strEcho);
                dos.writeBytes(strWord + "---->" + strEcho + System.getProperty("line.separator"));

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();   // 关闭包装类，会自动关闭包装类中所包装的底层类。所以不用调用ips.close()
                    dos.close();
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



    }
    //实现接受到的消息群发



}
