package gash.socket;

import java.io.BufferedInputStream;
import java.io.InterruptedIOException;
import java.net.Socket;

import gash.payload.BasicBuilder;
import gash.payload.Message;

/**
 * Handles the session for an incoming client connection
 * 
 * @author gash
 */
class SessionHandler extends Thread {
    private Socket connection;
    private String host;
    private boolean forever = true;

    public SessionHandler(Socket connection, String host) {
        this.connection = connection;
        this.host = host;
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Process incoming data
     */
    public void run() {
        System.out.println("Session for Host " + host + " started");

        try {
            connection.setSoTimeout(2000);
            var in = new BufferedInputStream(connection.getInputStream());

            /**if (in == null) {
                throw new RuntimeException("Unable to get input streams");
            }**/

            byte[] raw = new byte[2048];
            BasicBuilder builder = new BasicBuilder();
            while (forever) {
                try {
                    int len = in.read(raw);
                    if (len == 0) {
                        continue;
                    } else if (len == -1) {
                        break;
                    }

                    Message msg = builder.decode(new String(raw, 0, len).getBytes());
                    System.out.println("[" + host + "] " + msg);

                } catch (InterruptedIOException ioe) {
                    // Handle InterruptedIOException more gracefully if needed
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("Session for Host " + host + " ending");
                System.out.flush();
                stopSession();
            } catch (Exception re) {
                re.printStackTrace();
            }
        }
    }
} // class SessionHandler
