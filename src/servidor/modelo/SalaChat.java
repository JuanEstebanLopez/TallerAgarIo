package servidor.modelo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;

public class SalaChat extends Thread {

	public final String SEPARADOR = " ";
	public final String SEPARADOR_MENSAJE = ":";
	public final String LISTA_USUARIOS = "players";
	public final String NAME = "user";

	public final int PUERTO = 8080;
	private ServerSocket ss;

	private Modelo principal;
	private LinkedList<UserChat> usuarios;

	private boolean aceptarClietes;
	private StringBuilder log;

	public SalaChat(Modelo principal) {
		this.principal = principal;
		usuarios = new LinkedList<UserChat>();
		log = new StringBuilder(1000);
	}

	public void aceptarClientes() {
		aceptarClietes = true;
		try {
			ss = new ServerSocket(PUERTO);
			start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void run() {
		System.out.println("Esperando usuarios del chat...");
		while (aceptarClietes) {
			try {
				Socket s = ss.accept();
				UserChat con = new UserChat(SalaChat.this, s);
				con.start();
				usuarios.add(con);
				sleep(500);
				String players = principal.isJuegoIniciado() ? principal.getListaJugadores() : "";
				con.initialValues(usuarios.size(), log.toString(), players);
				System.out.println("Registrado un uevo usuario al chat");
				sleep(500);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

	public synchronized void repartirMensaje(UserChat user, String mensaje) {
		log.append(mensaje).append("\n");
		Iterator<UserChat> it = usuarios.iterator();
		while (it.hasNext()) {
			UserChat u = it.next();
			if (user != u) {
				u.enviarMensaje(mensaje);
			}
		}
	}

	public synchronized void initialPlayers(String players) {
		String mensaje = LISTA_USUARIOS + SEPARADOR + "Usuario" + players;
		Iterator<UserChat> it = usuarios.iterator();
		while (it.hasNext()) {
			UserChat u = it.next();
			u.enviarMensaje(mensaje);
		}
	}

	public String getLog() {
		return log.toString();
	}

	private class UserChat extends Thread {
		private SalaChat sala;
		private Socket s;

		private boolean conectado;

		private DataInputStream sIn;
		private DataOutputStream sOut;

		public UserChat(SalaChat sala, Socket s) {
			this.sala = sala;
			this.s = s;
			this.conectado = true;
		}

		@Override
		public void run() {
			try {
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

		public void initialValues(int ind, String log, String players) {
			enviarMensaje(NAME + SEPARADOR + "Usuario" + ind);
			if (!"".equals(log))
				enviarMensaje(log);
			if (!"".equals(players))
				enviarMensaje(LISTA_USUARIOS + SEPARADOR + "Usuario" + players);
		}

		public void recibirMensajes() throws IOException {
			String mensaje = sIn.readUTF();
			sala.repartirMensaje(this, mensaje);
			System.out.println("recibido " + mensaje);
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

}
