package cliente.modelo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class UserChat extends Thread {

	public final static String SEPARADOR = " ";
	public final String SEPARADOR_MENSAJE = ":";
	public final String LISTA_USUARIOS = "players";
	public final String NAME = "user";

	public final int PUERTO = 8080;

	private ManejoComunicacion comunicacion;
	private Socket s;

	private boolean conectado;

	private DataInputStream sIn;
	private DataOutputStream sOut;

	public UserChat(ManejoComunicacion comunicacion) {
		this.comunicacion = comunicacion;
		this.conectado = true;

	}

	@Override
	public void run() {
		String ip = Comunicacion.USED_IP;
		try {
			s = new Socket(ip, PUERTO);
			sOut = new DataOutputStream(s.getOutputStream());
			sIn = new DataInputStream(s.getInputStream());
			// Recibe mensajes del cliente mientras el usuario está conectado
			while (conectado) {
				recibirMensajes();
				sleep(500);
			}
			sOut.close();
			sIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
		}
	}

	public void recibirMensajes() throws IOException {
		String mensaje = sIn.readUTF();
		System.out.println("recibido " + mensaje);
		if (!"".equals(mensaje))
			if (mensaje.contains(SEPARADOR_MENSAJE)) {
				System.out.println("en chat");
				comunicacion.showChatMessage(mensaje);
			} else if (mensaje.startsWith(NAME + SEPARADOR)) {
				comunicacion.updateNameChat(mensaje.split(SEPARADOR)[1]);
				System.out.println("el Nombre");
			} else if (mensaje.startsWith(LISTA_USUARIOS + SEPARADOR)) {
				String usrs = mensaje.split(SEPARADOR, 2)[1];
				comunicacion.Registrarusuarios(usrs);
				System.out.println("users");
			}

	}

	public void enviarMensaje(String mensaje) {
		try {
			sOut.writeUTF(mensaje);
			sOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
