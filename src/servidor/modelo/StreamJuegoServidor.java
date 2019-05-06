package servidor.modelo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class StreamJuegoServidor {

	public final static String IP = "239.1.2.2";
	public final static int PORT = 5000;

	private MulticastSocket ms;
	private InetAddress grupo;

	public StreamJuegoServidor() {
		conectar();
	}

	public void conectar() {
		try {
			grupo = InetAddress.getByName(IP);
			ms = new MulticastSocket(PORT);
			ms.joinGroup(grupo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param mensaje
	 */
	public void enviar(String mensaje) {
		byte[] b = mensaje.getBytes();
		DatagramPacket dPacket = new DatagramPacket(b, b.length, grupo, PORT);
		try {
			ms.send(dPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
