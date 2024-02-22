package gash.socket;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import gash.payload.BasicBuilder;
import gash.payload.Message;

/**
 * Handles the session for an incoming client connection
 * 
 * @author gash
 */
class SessionHandler extends Thread {
    private static final Logger logger = Logger.getLogger(SessionHandler.class.getName());

    private Socket connection;
    private String host;
    private boolean forever = true;
    private AtomicInteger totalRequests;
    private AtomicInteger failedRequests;

    public SessionHandler(Socket connection, String host, AtomicInteger totalRequests, AtomicInteger failedRequests) {
        this.connection = connection;
        this.host = host;
        this.totalRequests = totalRequests;
        this.failedRequests = failedRequests;
        // allow server to exit if
        this.setDaemon(true);
    }

    /**
     * Stops session on the next timeout cycle
     */
    public void stopSession() {
        forever = false;
        if (connection != null && !connection.isClosed()) {
            try {
                connection.close();
            } catch (IOException e) {
                logger.warning("Error while closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Process incoming data
     */
    @Override
    public void run() {
        logger.info("Session for Host " + host + " started");

        try {
            processIncomingData();
        } catch (IOException e) {
            logger.warning("Error during session: " + e.getMessage());
            // Record failed request
            failedRequests.incrementAndGet();
        } finally {
            logger.info("Session for Host " + host + " ending");
            stopSession();
        }
    }

    private void processIncomingData() throws IOException {
        try (var in = new BufferedInputStream(connection.getInputStream())) {
            byte[] raw = new byte[2048];
            BasicBuilder builder = new BasicBuilder();
            while (forever) {
                int len = in.read(raw);
                if (len > 0) {
                    Message msg = builder.decode(new String(raw, 0, len).getBytes());
                    logger.info("[" + host + "] " + msg);
                    // Increment successful requests
                    totalRequests.incrementAndGet();
                } else if (len == -1) {
                    forever = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Record failed request
            failedRequests.incrementAndGet();
        }
    }
}
