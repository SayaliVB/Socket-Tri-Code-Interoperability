package gash.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import gash.socket.BasicClient;

/**
 * ClientApp - basic chat construct. This varies from our Python and C++ versions
 * as it prompts the user for messages.
 * 
 * @author gash
 * 
 */
public class ClientApp {
    private static final Logger logger = Logger.getLogger(ClientApp.class.getName());
    private BasicClient myClient;

    public BasicClient getMyClient() {
        return myClient;
    }

    public void setMyClient(BasicClient myClient) {
        this.myClient = myClient;
    }

    /**
     * Constructor - Add a comment explaining why this method is empty or complete the implementation.
     */
    public ClientApp() {
        // Add a comment explaining why this method is empty or complete the implementation.
    }

    public static void main(String[] args) {
        var myClient = new BasicClient("app", "127.0.0.1", 2000);
        myClient.connect();
        myClient.join("pets/dogs");

        var br = new BufferedReader(new InputStreamReader(System.in));
        boolean exitLoop = false; // Introduce a flag to control loop exit
        while (!exitLoop) {
            try {
                logger.info("\nenter message ('exit' to quit): "); // Replace System.out with logger
                var m = br.readLine();
                if (m.length() == 0 || "exit".equalsIgnoreCase(m)) {
                    exitLoop = true;
                } else {
                    myClient.sendMessage(m);
                }
            } catch (Exception ex) {
                exitLoop = true;
            }
        }
    }
}
