package servidor.modelo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class BaseDatos implements Serializable {
	public final static String BD = "bd/datos.txt";
	private HashMap<String, UsuarioRegistrado> datos;

	public BaseDatos() {
		leer();
	}

	/**
	 * Agrega un nuevo usuario.
	 * 
	 * @param usuario
	 * @param password
	 * @param email
	 * @return true si el usuario se pudo registrar, false si el usuario existe o no
	 *         se pudo guardar el archivo.
	 */
	public boolean registrarUsuario(String usuario, String password, String email) {
		if (verificarUsuarioExistente(email)) {
			return false;
		} else {
			UsuarioRegistrado registrado = new UsuarioRegistrado(usuario, email, password);
			datos.put(email, registrado);
			return guardar();
		}
	}

	/**
	 * Verifica si un usuario existe.<br>
	 * Se usa para saber si un usuario se puede registrar.
	 * 
	 * @param email
	 * @return true si el usuario ya existe, false en caso contrario.
	 */
	public boolean verificarUsuarioExistente(String email) {
		return datos.containsKey(email);
	}

	/**
	 * Verifica si el usuario tiene su contrase�a correcta.<br>
	 * Se usa para verificar si un usuario puede iniciar sesi�n.
	 * 
	 * @param email
	 * @param pass
	 * @return true si el usuario es v�lido, false en caso contrario.
	 */
	public boolean verificarInicioSesion(String email, String pass) {
		return datos.containsKey(email) && datos.get(email).getPassword().equals(pass);
	}

	public void leer() {
		try {
			ObjectInputStream br = new ObjectInputStream(new FileInputStream(BD));
			datos = (HashMap<String, UsuarioRegistrado>) br.readObject();
			br.close();
		} catch (FileNotFoundException e) {
			datos = new HashMap<String, UsuarioRegistrado>();
			guardar();
			leer();
			e.printStackTrace();
		} catch (IOException s) {
			s.printStackTrace();
		} catch (ClassNotFoundException m) {
			m.printStackTrace();
		}
		System.out.println(datos);
	}

	public boolean guardar() {
		try {
			System.out.println(datos);
			ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(BD, false));
			salida.writeObject(datos);
			salida.flush();
			salida.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private class UsuarioRegistrado implements Serializable {

		private String nombre;
		private String email;
		private String password;

		public UsuarioRegistrado(String nombre, String email, String password) {
			this.nombre = nombre;
			this.email = email;
			this.password = password;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

	}

}
