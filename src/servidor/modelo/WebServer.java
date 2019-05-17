package servidor.modelo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer implements Runnable {
	private boolean active;

	public WebServer() {
		active = true;

	}

	public void runServer() {
		active = true;
		new Thread(this).start();
	}

	@Override
	public void run() {

		System.out.println("Webserver Started");
		try {
			ServerSocket serverSocket = new ServerSocket(80);
			while (active) {
				System.out.println("Waiting for the client request");
				Socket remote = serverSocket.accept();
				System.out.println("Connection made");
				new Thread(new WebCllientHandler(remote)).start();

			}
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
