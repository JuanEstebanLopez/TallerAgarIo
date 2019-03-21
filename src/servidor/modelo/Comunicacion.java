package servidor.modelo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Comunicacion extends Thread {

	public final static int SLEEP = 40;

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

	public Comunicacion(Socket s, Modelo p) {
		this.s = s;
		this.principal = p;
		this.nombre = "";
	}

	@Override
	public void run() {
		try {
			sOut = new DataOutputStream(s.getOutputStream());
			sIn = new DataInputStream(s.getInputStream());
			enviarMensaje("Bienvenido");
			while (conectado) {
				recibirMensajes();
				sleep(SLEEP);
			}
			sOut.close();
			sIn.close();
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
		System.out.println(mensaje);
		switch (mensaje) {
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
	 *            mensaje a enviar
	 * @throws IOException
	 */
	public void enviarMensaje(String mensaje) throws IOException {
		sOut.writeUTF(mensaje);
		sOut.flush();
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
			if(mensaje!= null && !"".equals(mensaje)) {
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

	public void setNombre(String nombre) {
		this.nombre = nombre;
		principal.agregarCliente(this);
	}

	public String getNombre() {
		return nombre;
	}
}
