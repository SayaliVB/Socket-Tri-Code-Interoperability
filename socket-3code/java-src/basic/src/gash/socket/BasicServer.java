package gash.socket;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Server to manage incoming clients
 * 
 * @author gash
 */
public class BasicServer {
    private static final Logger logger = Logger.getLogger(BasicServer.class.getName());

    private String host;
    private int port;
    private boolean forever = true;
    private AtomicInteger totalRequests = new AtomicInteger(0);
    private AtomicInteger failedRequests = new AtomicInteger(0);

    public BasicServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Start monitoring socket for new connections
     */
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info(String.format("Server Host: %s%n", host));  // Improved formatting
            logger.info(String.format("Server Port: %d%n", port));  // Improved formatting

            while (forever) {
                Socket clientSocket = serverSocket.accept();
                logger.info("--> Server got a client connection");
                var sh = new SessionHandler(clientSocket, host, totalRequests, failedRequests);
                sh.start();
                totalRequests.incrementAndGet();
            }

        } catch (Exception e) {
            logger.warning("Error in the server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Calculate throughput (requests per second)
     *
     * @param startTime   Time when the server started
     * @param currentTime Current time
     * @return Throughput in requests per second
     */
    private double calculateThroughput(long startTime, long currentTime) {
        long elapsedTime = currentTime - startTime;
        return totalRequests.get() / (elapsedTime / 1000.0);
    }

    /**
     * Calculate failure rate (percentage of failed requests)
     *
     * @return Failure rate as a percentage
     */
    private double calculateFailureRate() {
        int total = totalRequests.get();
        int failed = failedRequests.get();
        return (double) (failed * 100) / total;
    }

    /**
     * Main method to run the BasicServer
     */
    public static void main(String[] args) {
        BasicServer server = new BasicServer("localhost", 2000);
        long startTime = System.currentTimeMillis();
        server.start();

        // You can periodically log or display metrics
        while (true) {
            try {
                Thread.sleep(5000); // Log every 5 seconds, adjust as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long currentTime = System.currentTimeMillis();
            double throughput = server.calculateThroughput(startTime, currentTime);
            double failureRate = server.calculateFailureRate();

            logger.info(String.format("Throughput: %.2f req/s, Failure Rate: %.2f%%", throughput, failureRate));
            
        }
    }
}
