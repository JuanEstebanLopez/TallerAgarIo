package servidor.modelo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import comun.Jugador;

public class Comunicacion extends Thread {

	public final static int SLEEP = 40;

	public final static String SEPARADOR = ":";
	public final static String SEPARADOR_MIN = " ";

	public final static String REGISTRAR = "register";
	public final static String MOVER = "move";
	public final static String DESCONECTAR = "exit";
	public final static String INFO = "info";

	private Socket s;
	private Modelo principal;
	private boolean conectado;

	private DataInputStream sIn;
	private DataOutputStream sOut;
	private String nombre;

	private Jugador juga;

	public Comunicacion(Socket s, Modelo p) {
		this.s = s;
		this.principal = p;
		this.nombre = "";
		conectado = true;
	}

	@Override
	public void run() {
		try {
			sOut = new DataOutputStream(s.getOutputStream());
			sIn = new DataInputStream(s.getInputStream());
			conectado = true;
			while (conectado) {
				recibirMensajes();
				sleep(SLEEP);
			}
			System.out.println("finalizar Servidor");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			principal.desconectarUsuario(this);
		}
	}

	/**
	 * Recibe los mensajes de un cliente y los muestra en el log.
	 * 
	 * @throws IOException
	 */
	public void recibirMensajes() throws IOException {
		String mensaje = nextLineClient();
		System.out.println("Recibido: " + mensaje);
		switch (mensaje) {

		case MOVER:
			moverJugador(nextLineClient());
			break;
		case REGISTRAR:
			setNombre(nextLineClient());
			break;

		default:
			break;
		}

	}

	/**
	 * Lee una línea obteinda del cliente
	 * 
	 * @return String recibido del cliente
	 * @throws IOException
	 */
	public String nextLineClient() throws IOException {
		return sIn.readUTF();
	}

	/**
	 * Envía un mensaje al cliente
	 * 
	 * @param mensaje
	 *            mensajes a enviar
	 * @throws IOException
	 */
	public void enviarMensaje(String... mensaje) throws IOException {
		System.out.println("Se envía mensaje");
		for (int i = 0; i < mensaje.length; i++) {
			sOut.writeUTF(mensaje[i]);
		}
		sOut.flush();
	}

	public void tryEnviarMensaje(String... mensaje) {
		try {
			enviarMensaje(mensaje);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Termina la comunicación entre un usuario y el servidor.
	 * 
	 * @param mensaje
	 *            Mensaje para mostrar al usuario que ha sido desconectado del
	 *            servidor.
	 */
	public void terminarConexion(String mensaje) {
		try {
			if (mensaje != null && !"".equals(mensaje)) {
				enviarMensaje(INFO);
				enviarMensaje(mensaje);
			}
			enviarMensaje(DESCONECTAR);
			conectado = false;
			principal.desconectarUsuario(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void moverJugador(String mov) {
		String[] m = mov.split(SEPARADOR_MIN);
		juga.move(Integer.parseInt(m[0]), Integer.parseInt(m[1]));
	}

	public void actualizarPosiciones(String jugadores, String comida) {
		try {
			enviarMensaje(MOVER, jugadores, comida);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
		principal.agregarCliente(this);
	}

	public String getNombre() {
		return nombre;
	}

	public void setJuga(Jugador juga) {
		this.juga = juga;
	}

	public Jugador getJuga() {
		return juga;
	}

	public boolean isConectado() {
		return conectado;
	}

	public void setConectado(boolean conectado) {
		this.conectado = conectado;
	}

}
