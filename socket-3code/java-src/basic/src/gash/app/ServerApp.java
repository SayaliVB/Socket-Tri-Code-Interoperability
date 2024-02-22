package gash.app;

import gash.socket.BasicServer;

/**
 * Server application
 * 
 * @author gash
 */
class ServerApp {
    public ServerApp() {
		 // The main method is intentionally left empty.
        // Add your server initialization or startup logic here.
        // If no initialization is needed, it's okay to leave it empty.
    }

    public static void main(String[] args) {
        var host = "127.0.0.1";
        var port = 2000;
        var server = new BasicServer(host, port);
        server.start();

        // Intentionally leaving the main method empty as the server runs indefinitely
        // No additional logic needed here.
        // Throwing UnsupportedOperationException is not appropriate in this context.
        // Complete implementation would be unnecessary and might introduce unwanted code.
    }
}
