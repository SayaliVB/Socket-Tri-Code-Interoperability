package gash.socket;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server to manage incoming clients
 * 
 * @author gash
 */
public class BasicServer {
    private String host;
    private int port;
    private ServerSocket socket;
    private boolean forever = true;

    public BasicServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Start monitoring socket for new connections
     */
    public void start() {
        try {
            socket = new ServerSocket(port);

            System.out.println("Server Host: " + host);
            System.out.println("Server Port: " + port);

            while (forever) {
                Socket clientSocket = socket.accept();
                if (!forever) {
                    break;
                }

                System.out.println("--> Server got a client connection");
                var sh = new SessionHandler(clientSocket, host);
                sh.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
