package cliente.modelo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;
import javax.swing.JOptionPane;

import cliente.interfaz.InterfazCliente;

public class Comunicacion extends Thread {
	public static final String TRUSTTORE_LOCATION = "agario.store";
	public final static int SLEEP = 40;

	public final static String SEPARADOR = ":";
	public final static String SEPARADOR_MIN = " ";

	public final static String REGISTRAR = "register";
	public final static String REGISTO_FALLIDO = "register faild";

	public final static String INICIAR_SESION = "login";
	public final static String INICIO_SESION_FALLIDO = "login faild";

	public final static String START = "start";
	public final static String MOVER = "move";
	public final static String DESCONECTAR = "exit";
	public final static String FIN_PARTIDA = "fin";
	public final static String INFO = "info";

	public final static int PUERTO = 8000;
	public final static String IP_DEFAULT = "127.0.0.1";

	private Socket s;
	private boolean conectado;

	private DataInputStream sIn;
	private DataOutputStream sOut;

	private String nombre;
	private InterfazCliente interfaz;

	public Comunicacion(InterfazCliente interfaz) {
		conectado = true;
		this.interfaz = interfaz;
	}

	/**
	 * Inicializa la comunicación con el servidor y crea el hilo que espera los
	 * mensajes.
	 */
	public void crearComunicacion() {
		try {
			String ip = JOptionPane.showInputDialog("Ingrese la IP del servidor");
			if (ip == null || "".equals(ip))
				ip = IP_DEFAULT;
			System.setProperty("javax.net.ssl.trustStore", TRUSTTORE_LOCATION);
			SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
			s = sf.createSocket(ip, PUERTO);
			sOut = new DataOutputStream(s.getOutputStream());

			sIn = new DataInputStream(s.getInputStream());

			// Ciclo para obtener información desde el servidor
			start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			conectado = true;
			// enviarMensaje(REGISTRAR, nombre);
			System.out.println("Esperando mensajes...");
			while (conectado) {
				recibirMensajes();
				sleep(SLEEP);
			}
			System.out.println("finalizar Cliente");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Recibe los mensajes de un cliente y los muestra en el log.
	 * 
	 * @throws IOException
	 */
	public void recibirMensajes() throws IOException {
		String mensaje = nextLineServer();
		System.out.println("Recibido: " + mensaje);
		switch (mensaje) {
		case MOVER:
			String jugadores = nextLineServer();
			String comida = nextLineServer();
			interfaz.actualizarElementosJuego(jugadores, comida);
			break;
		case INFO:
			mensaje = nextLineServer();
			JOptionPane.showMessageDialog(interfaz, mensaje);
			break;
		case DESCONECTAR:
			this.conectado = false;
			interfaz.terminarJuego();
			System.out.println("Se ha deconectado del servidor.");
			break;
		case FIN_PARTIDA:
			this.conectado = false;
			String puntajesFinales = nextLineServer();
			interfaz.actualizarPuntajeFinal(puntajesFinales);
			System.out.println("Se ha terminado el juego.\n Puntajes: " + puntajesFinales);
			interfaz.terminarJuego();
			break;
		case REGISTRAR:
			int index = Integer.parseInt(nextLineServer());
			interfaz.actualizarJugadorIndex(index);
			System.out.println("Registrado como " + index);
			break;
		case REGISTO_FALLIDO:
		case INICIO_SESION_FALLIDO:
			String mensaje2 = nextLineServer();
			JOptionPane.showMessageDialog(null, mensaje2);
			break;
		case START:
			iniciarJuego();
			String usuarios = nextLineServer();
			interfaz.registrarUsuarios(usuarios.split(SEPARADOR_MIN));
			break;

		default:
			break;
		}

	}

	/**
	 * Lee una línea obteinda del servidor
	 * 
	 * @return String recibido del servidor
	 * @throws IOException
	 */
	public String nextLineServer() throws IOException {
		return sIn.readUTF();
	}

	/**
	 * Envía un mensaje al cliente
	 * 
	 * @param mensaje
	 *            mensaje a enviar
	 * @throws IOException
	 */
	public void enviarMensaje(String... mensaje) throws IOException {
		for (int i = 0; i < mensaje.length; i++) {
			System.out.println(mensaje[i] + "__" + mensaje.length);
			sOut.writeUTF(mensaje[i]);
		}
		sOut.flush();
	}

	public boolean tryEnviarMensaje(String... mensaje) {
		boolean enviado = true;
		try {
			enviarMensaje(mensaje);
		} catch (IOException e) {
			e.printStackTrace();
			enviado = false;
		}
		return enviado;
	}

	/**
	 * Inicia el juego en la interfaz
	 */
	public void iniciarJuego() {
		interfaz.iniciarJuego();
	}

	/**
	 * Envía el mensaje paraa mover el jugador
	 * 
	 * @param x
	 * @param y
	 */
	public void moverJugador(int x, int y) {
		try {
			enviarMensaje(MOVER, x + SEPARADOR_MIN + y);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}
}
