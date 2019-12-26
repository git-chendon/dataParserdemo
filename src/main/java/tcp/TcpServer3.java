package tcp;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author chen.don
 * @date 2019/12/25
 * 使用线程池技术定义一个tcp聊天室服务器
 */
public class TcpServer3 {

    public static void main(String[] args) throws Exception {
        ServerSocket sc = new ServerSocket(8001);  //新建服务器

        //线程池
        ThreadPoolExecutor executor;
        executor=(ThreadPoolExecutor) Executors.newCachedThreadPool();
        //executor=(ThreadPoolExecutor)Executors.newFixedThreadPool(5);  //可以规定数量

        while (true){
            System.out.println("等待任务到来");
            Socket s = sc.accept();  //等待连接
            Future<String> future = executor.submit(new Worker2(s));

            System.out.printf("Server: Pool Size: %d\n",executor.getPoolSize());
            System.out.printf("Server: Active Count: %d\n",executor.getActiveCount());
            System.out.printf("Server: Completed Tasks: %d\n",executor.getCompletedTaskCount());
        }

    }
}
