package cliente.modelo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

import cliente.interfaz.InterfazCliente;

public class StreamJuegoCliente implements Runnable {
	public final static int TIEMPO_ESPERA_MULTICAST = 10000;
	public final static String IP = "239.1.2.2";
	public final static int PORT = 5000;

	private MulticastSocket ms;
	private InetAddress grupo;
	private boolean escuchar;

	private InterfazCliente interfaz;

	public StreamJuegoCliente(InterfazCliente interfaz) {
		this.interfaz = interfaz;
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

	public void unirseStream() {
		escuchar = true;
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			while (escuchar) {
				recibir();
				Thread.sleep(Comunicacion.SLEEP);
			}
		} catch (InterruptedException e) {
			System.err.println(e);
		}
	}

	private void recibir() {
		try {

			byte[] buf = new byte[6000];
			DatagramPacket recibe = new DatagramPacket(buf, buf.length);
			// ms.setSoTimeout(TIEMPO_ESPERA_MULTICAST);
			ms.receive(recibe);
			String recibido = new String(recibe.getData());

			System.out.println("RECIBIDO: " + recibido);
			actualizarInterfaz(recibido);
		} catch (SocketTimeoutException e) {
		} catch (IOException e) {
			System.err.println(e);

		}

	}

	public void actualizarInterfaz(String recibido) {
		String[] info = recibido.split(Comunicacion.SEPARADOR_MAX);
		if (info.length == 2) {
			System.out.println("Jugadores: " + info[0]);
			System.out.println("Comida: " + info[1]);
			interfaz.actualizarElementosJuego(info[0], info[1]);
		} else
			System.out.println("Error al recibir stram del juego: " + recibido);
	}

	public void setEscuchar(boolean escuchar) {
		this.escuchar = escuchar;
	}

	public boolean isEscuchar() {
		return escuchar;
	}
}
