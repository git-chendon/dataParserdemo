package tcp.chatRoom.Client;


/**
 * @Author chen.don
 * @date 2019/12/27
 */
public class MainClient {
    public static void main(String[] args) {
        try{
            new TcpClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
