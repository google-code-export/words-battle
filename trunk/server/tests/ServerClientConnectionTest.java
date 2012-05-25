package server.tests;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import server.core.MultiThreadedServer;

public class ServerClientConnectionTest {
	private final static Logger LOGGER = Logger.getLogger(ServerClientConnectionTest.class.getName());
	
	public static void main(String[] args) {
		// Log4j config
		BasicConfigurator.configure();
		
		MultiThreadedServer server = new MultiThreadedServer(6789);
		new Thread(server).start();
		TCPClient client = new TCPClient("localhost", 6789);
		while(true) {
			LOGGER.info("trying to send message");
			if (client.isConnected()) {
				LOGGER.info("is connected");
				client.sendMessage("test message");
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