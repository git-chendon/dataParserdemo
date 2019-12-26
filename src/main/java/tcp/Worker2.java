package tcp;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * @Author chen.don
 * @date 2019/12/25
 * 聊天室功能worker
 */
public class Worker2 implements Callable<String> {
    Socket s;

    public Worker2(Socket s) {
        this.s = s;
    }

    @Override
    public String call() throws Exception {

        InputStream ips = s.getInputStream();
        OutputStream ops = s.getOutputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(ips));
        DataOutputStream dos = new DataOutputStream(ops);
//        PrintStream ps = new PrintStream(ops); //两种方法

        String strIn = null;
        while (true) {
            String strWord = br.readLine();
            strIn = strWord;
            System.out.println("client said:" + strWord +":" + strWord.length());
            if (strWord.equalsIgnoreCase("quit"))
                break;
            String strEcho = strWord + " 666";
            // dos.writeBytes(strWord +"---->"+ strEcho +"\r\n");
            System.out.println("server said:" + strWord + "---->" + strEcho);
            dos.writeBytes(strWord + "---->" + strEcho + System.getProperty("line.separator"));
        }
        br.close();
        // 关闭包装类，会自动关闭包装类中所包装的底层类。所以不用调用ips.close()
        dos.close();
        s.close();
        return strIn;
    }
    //实现接受到的消息群发



}
