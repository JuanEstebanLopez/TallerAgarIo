package servidor.modelo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.SSLServerSocketFactory;

import comun.Jugador;
import servidor.interfaz.InterfazServidor;

public class Modelo {

	public static final String KEYSTORE_LOCATION = "agario.store";
	public static final String KEYSTORE_PASSWORD = "redes2019";

	public final static int PUERTO = 8000;
	public final static int CANT_JUGADORES = 5;
	public final static String SALA_LLENA = "La sala está llena";
	public final static String JUEGO_INICIADO = "El juego ya ha iniciado";
	public boolean juegoIniciado;
	public ArrayList<Comunicacion> clientes;

	private StreamJuegoServidor streamJuego;
	private StreamAudioServidor streamAudio;
	private WebServer web;

	private SalaChat salaChat;
	private BaseDatos baseDatos;

	public InterfazServidor principal;

	public long initialTime;
	public long finalTime;

	public Modelo(InterfazServidor principal) {
		this.principal = principal;
		juegoIniciado = false;
		clientes = new ArrayList<Comunicacion>(5);
		baseDatos = new BaseDatos();
		aceptarClientes();
		streamJuego = new StreamJuegoServidor();
		streamAudio = new StreamAudioServidor();
		streamAudio.start();

		salaChat = new SalaChat(this);
		salaChat.aceptarClientes();

		web = new WebServer();
		web.runServer();
	}

	/**
	 * Recibe petición de registro de un usuario
	 * 
	 * @param nombre
	 * @param email
	 * @param password
	 * @return
	 */
	public boolean registrar(String nombre, String email, String password) {
		return baseDatos.registrarUsuario(nombre, email, password);
	}

	/**
	 * Recibe petición de inicio de sesión de un usuario
	 * 
	 * @param nombre
	 * @param password
	 * @return
	 */
	public boolean iniciarSesion(String nombre, String password) {
		return baseDatos.verificarInicioSesion(nombre, password);
	}

	// public String refrescarRanking() {
	// String rank = "";
	// ArrayList jugadores = new ArrayList<>();
	// for(int i = 0; i < clientes.size(); i++) {
	// Comunicacion cliente = clientes.get(i);
	//
	// jugadores.add(cliente.getName()+cliente.getJuga().getPuntaje());
	// Collections.sort(players, new Comparator<player>() {
	// public int compare(playerball p1, playerball p2) {
	// return p2.getmass - p1.getmass;
	// }
	// });
	// }
	// return rank;
	// }

	/**
	 * Hilo que acepta nuevos jugadores.
	 */
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

	/**
	 * Envía a los jugadores las actalizaciones de las posiciones en el juego.
	 * 
	 * @param jugadores
	 * @param comida
	 */
	public void ActualizarClientes(String jugadores, String comida) {
		// Envía la información por TCP
		for (int i = clientes.size() - 1; i >= 0; i--) {
			Comunicacion comu = clientes.get(i);
			if (comu.isConectado()) {
				if (comu.getJuga().isActivo())
					comu.actualizarPosiciones(jugadores, comida);
				else
					comu.terminarConexion("Has sido descalificado, has quedado de puesto: " + clientes.size());
			}
		}
		// Envía la cadena por UDP
		StringBuilder udpSTRB = new StringBuilder(jugadores.length() + comida.length() + 1);
		boolean neddCorrection = jugadores.endsWith(":");
		String udpSTR = neddCorrection ? jugadores.substring(0, jugadores.length() - 1) : jugadores;
		udpSTRB.append(udpSTR).append(Comunicacion.SEPARADOR_MIN).append(Comunicacion.SEPARADOR_MAX);
		neddCorrection = comida.endsWith(":");
		udpSTR = neddCorrection ? comida.substring(0, comida.length() - 1) : comida;
		udpSTRB.append(udpSTR).append(Comunicacion.SEPARADOR_MIN);
		streamJuego.enviar(udpSTRB.toString());
		// System.out.println("enviado: " + udpSTRB.toString());
	}

	/**
	 * @return Lista de los nombres de los jugadores.
	 */
	public String getListaJugadores() {
		StringBuilder usuarios = new StringBuilder();
		for (Comunicacion comu : clientes) {
			usuarios.append(comu.getNombre()).append(Comunicacion.SEPARADOR_MIN);
		}
		return usuarios.toString();
	}

	/**
	 * Inicia el juego y envía a los clientes los nombres de los usuarios
	 * conectados.
	 * 
	 * @return
	 */
	public String IniciarJuego() {
		initialTime = System.currentTimeMillis();
		boolean enviadoATodos = true;
		String usrs = getListaJugadores();
		for (Comunicacion comu : clientes) {
			if (comu.isConectado()) {
				enviadoATodos &= comu.tryEnviarMensaje(Comunicacion.START, usrs);
			}
		}
		salaChat.initialPlayers(usrs);
		return usrs;
	}

	/**
	 * Envía a los jugadores la señal de juego finalizado y los puntajes finales.
	 * 
	 * @param mensaje
	 */
	public void TerminarJuego(String mensaje) {
		for (int i = clientes.size() - 1; i >= 0; i--) {
			Comunicacion comu = clientes.get(i);
			comu.partidaTerminada(mensaje);

		}

		// TODO

		finalTime = System.currentTimeMillis();

		long total = finalTime - initialTime;
		Jugador[] jugas = new Jugador[clientes.size()];

		int max = Integer.MIN_VALUE;
		for (int i = 0; i < jugas.length; i++) {
			Jugador jd = clientes.get(i).getJuga();
			jugas[i] = jd;
			if (jd.getPuntaje() > max)
				;
			max = jd.getPuntaje();
		}
		for (int i = 0; i < jugas.length; i++) {
			if (jugas[i].getPuntaje() >= max)
				jugas[i].setGanador(true);
		}

		Date dt = new Date();

		baseDatos.registrarPartida(fecha, total, jugas);
	}

	/**
	 * Agrega un nuevo cliente.
	 * 
	 * @param cliente
	 */
	public void agregarCliente(Comunicacion cliente) {
		if (principal.juegoIniciado()) {
			cliente.terminarConexion(JUEGO_INICIADO);
		} else if (clientes.size() < CANT_JUGADORES) {
			clientes.add(cliente);
			Jugador juga = new Jugador();
			cliente.setJuga(juga);
			int jIndex = principal.agregarJugador(juga);
			cliente.tryEnviarMensaje(Comunicacion.REGISTRAR, jIndex + "");
			System.out.println("Se ha agregado el jugador " + cliente.getNombre());
			if (clientes.size() >= 2) {
				principal.puedeIniciar();
			}
		} else
			cliente.terminarConexion(SALA_LLENA);
	}

	public boolean isJuegoIniciado() {
		return juegoIniciado;
	}

	public void desconectarUsuario(Comunicacion cliente) {
		clientes.remove(cliente);

	}

}
