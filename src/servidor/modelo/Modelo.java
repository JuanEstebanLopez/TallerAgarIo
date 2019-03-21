package servidor.modelo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;

public class Modelo {

	public static final String KEYSTORE_LOCATION = "agario.store";
	public static final String KEYSTORE_PASSWORD = "redes2019";

	public final static int PUERTO = 8080;
	public final static int CANT_JUGADORES = 5;
	public final static String SALA_LLENA = "La sala está llena";
	public boolean juegoIniciado;
	public ArrayList<Comunicacion> clientes;

	public Modelo() {
		juegoIniciado = false;
		clientes = new ArrayList<Comunicacion>(5);
		aceptarClientes();
	}

	public void aceptarClientes() {
		new Thread() {
			@Override
			public void run() {
				juegoIniciado = false;
				System.setProperty("javax.net.ssl.keyStore", KEYSTORE_LOCATION);
				System.setProperty("javax.net.ssl.keyStorePassword", KEYSTORE_PASSWORD);

				SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
				ServerSocket ss;
				try {
					ss = ssf.createServerSocket(PUERTO);

					while (!juegoIniciado && clientes.size() < CANT_JUGADORES) {
						Socket s = ss.accept();
						Comunicacion c = new Comunicacion(s, Modelo.this);
						c.start();
						System.out.println("Recibido un cliente");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}.start();
	}

	public void agregarCliente(Comunicacion cliente) {
		if (clientes.size() < CANT_JUGADORES)
			clientes.add(cliente);
		else
			cliente.terminarConexion(SALA_LLENA);
	}

	public void desconectarUsuario(Comunicacion cliente) {
		clientes.remove(cliente);

	}

}
