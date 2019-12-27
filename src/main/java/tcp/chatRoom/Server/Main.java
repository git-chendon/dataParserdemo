package tcp.chatRoom.Server;

import java.io.IOException;

/**
 * @Author chen.don
 * @date 2019/12/27
 */
public class Main {
    public static void main(String[] args) throws IOException {
        TcpServer server = new TcpServer();
        server.start();
    }
}
