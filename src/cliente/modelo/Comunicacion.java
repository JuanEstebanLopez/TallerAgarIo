package cliente.modelo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;
import javax.swing.JOptionPane;

import cliente.InterfazCliente;

public class Comunicacion extends Thread {
	public static final String TRUSTTORE_LOCATION = "agario.store";
	public final static int SLEEP = 40;

	public final static String REGISTRAR = "register";
	public final static String MOVER = "move";
	public final static String DESCONECTAR = "exit";
	public final static String INFO = "info";

	public final static int PUERTO = 8080;
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
			// s = new Socket(ip, PUERTO);
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
			enviarMensaje(REGISTRAR);
			System.out.println(nombre);
			enviarMensaje(nombre);
			while (conectado) {
				recibirMensajes();
				sleep(SLEEP);
			}
			sOut.close();
			sIn.close();
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
		System.out.println(mensaje);

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
	public void enviarMensaje(String mensaje) throws IOException {
		sOut.writeUTF(mensaje);
		sOut.flush();
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

}
