package gash.socket;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import gash.payload.BasicBuilder;
import gash.payload.Message;

public class BasicClient {
    private static final Logger logger = Logger.getLogger(BasicClient.class.getName());
    private static final int TIMEOUT_THRESHOLD = 500; // Set your desired threshold for response time (in milliseconds)

    private String name;
    private String ipaddr;
    private int port;
    private String group = "public";

    private Socket clt;

    public BasicClient(String name) {
        this(name, "127.0.0.1", 2000);
    }

    public BasicClient(String name, String ipaddr, int port) {
        this.name = name;
        this.ipaddr = ipaddr;
        this.port = port;
    }

    public void stop() {
        if (this.clt != null) {
            try {
                this.clt.close();
            } catch (IOException e) {
                throw new ConnectionCloseException("Error while closing the connection", e);
            }
        }
        this.clt = null;
    }

    public class ConnectionCloseException extends RuntimeException {
        public ConnectionCloseException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public void join(String group) {
        this.group = group;
    }

    public void connect() {
        if (this.clt != null) {
            return;
        }

        try {
            this.clt = new Socket(this.ipaddr, this.port);
            logger.info("Connected to " + clt.getInetAddress().getHostAddress());
        } catch (Exception e) {
            logger.warning("Error while connecting: " + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        if (this.clt == null) {
            logger.warning("No connection, text not sent");
            return;
        }

        try {
            BasicBuilder builder = new BasicBuilder();
            long startTime = System.currentTimeMillis();

            byte[] msg = builder.encode(new Message(name, group, message)).getBytes();
            this.clt.getOutputStream().write(msg);

            long endTime = System.currentTimeMillis();
            long responseTime = endTime - startTime;
            // Record response time
            logger.info("Response Time: " + responseTime + " ms");

            // Record successful request
            if (responseTime <= TIMEOUT_THRESHOLD) {
                // Assuming response time below the threshold is considered successful
                // You may adjust this condition based on your specific requirements
                logger.info("Successful Request");
            } else {
                logger.warning("Request Exceeded Threshold, Considered Failed");
                // Record failed request
                
            }

        } catch (IOException e) {
            logger.warning("Error while sending message: " + e.getMessage());
            // Record failed request
        }
    }

    
}
