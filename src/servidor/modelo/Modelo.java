package servidor.modelo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.net.ssl.SSLServerSocketFactory;

import comun.Jugador;
import servidor.interfaz.InterfazServidor;

public class Modelo {

	public static final String KEYSTORE_LOCATION = "agario.store";
	public static final String KEYSTORE_PASSWORD = "redes2019";

	public final static int PUERTO = 8000;
	public final static int CANT_JUGADORES = 5;
	public final static String SALA_LLENA = "La sala está llena";
	public boolean juegoIniciado;
	public ArrayList<Comunicacion> clientes;

	public InterfazServidor principal;

	public Modelo(InterfazServidor principal) {
		this.principal = principal;
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

	public void ActualizarClientes(String jugadores, String comida) {

		for (Comunicacion comu : clientes) {
			if (comu.isConectado()) {
				comu.actualizarPosiciones(jugadores, comida);
			}
		}
	}

	public boolean IniciarJuego() {
		boolean enviadoATodos = true;
		for (Comunicacion comu : clientes) {
			if (comu.isConectado()) {
				enviadoATodos &= comu.tryEnviarMensaje(Comunicacion.START);
			}
		}
		return enviadoATodos;
	}
	public boolean TerminarJuego(String mensaje) {
		boolean enviadoATodos = true;
		for (Comunicacion comu : clientes) {
			if (comu.isConectado()) {
				enviadoATodos &= comu.tryEnviarMensaje(Comunicacion.DESCONECTAR, mensaje);
			}
		}
		return enviadoATodos;
	}
	public void agregarCliente(Comunicacion cliente) {
		if (clientes.size() < CANT_JUGADORES) {
			clientes.add(cliente);
			Jugador juga = new Jugador();
			cliente.setJuga(juga);
			int jIndex = principal.agregarJugador(juga);
			cliente.tryEnviarMensaje(Comunicacion.REGISTRAR, jIndex + "");
			System.out.println("Se ha agregado el jugador " + cliente.getNombre());
		} else
			cliente.terminarConexion(SALA_LLENA);
	}

	public void desconectarUsuario(Comunicacion cliente) {
		clientes.remove(cliente);

	}

}
