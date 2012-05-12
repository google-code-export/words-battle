package server.tests;

import java.util.logging.Level;
import java.util.logging.Logger;

import server.core.MultiThreadedServer;

public class ServerClientConnectionTest {
	private final static Logger LOGGER = Logger.getLogger(ServerClientConnectionTest.class.getName());
	public static void main(String[] args) {		
		MultiThreadedServer server = new MultiThreadedServer(6789);
		new Thread(server).start();
		TCPClient client = new TCPClient("localhost", 6789);
		while (true) {
			LOGGER.log(Level.INFO, "trying to send message\n");
			if (client.isConnected()) {
				LOGGER.log(Level.INFO, "is connected\n");
				client.sendMessage("test message\n");
				break;				
			}
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.stop();
		server.stop();
	}
}
