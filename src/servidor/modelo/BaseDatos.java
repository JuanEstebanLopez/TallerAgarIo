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

public class BaseDatos {
	
	private HashMap<String, UsuarioRegistrado> datos;
	
	public BaseDatos() {
		datos = new HashMap<String, UsuarioRegistrado>();
	}
	
	public boolean registrarUsuario(String usuario, String password, String email) {
		if(verificarUsuarioExistente(usuario)) {
			return false;
		}else {
			try {
				ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream("baseDatos/datos.txt", true));
				UsuarioRegistrado registrado = new UsuarioRegistrado(usuario, email, password);
				salida.writeObject(registrado);
				salida.close();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
	}
	
	/**
	 * Verifica si un usuario existe.<br>
	 * Se usa para saber si un usuario se puede registrar.
	 * @param usuario
	 * @return true si el usuario ya existe, false en caso contrario.
	 */
	public boolean verificarUsuarioExistente(String usuario) {
		return datos.containsKey(usuario);
	}
	
	/**
	 * Verifica si el usuario tiene su contraseña correcta.<br>
	 * Se usa para verificar si un usuario puede iniciar sesión.
	 * @param usuario
	 * @param pass
	 * @return true si el usuario es válido, false en caso contrario.
	 */
	public boolean verificarInicioSesion(String usuario, String pass) {
		return datos.containsKey(usuario) && datos.get(usuario).getPassword().equals(pass);
	}
	
	public void leer() {
		try {
			ObjectInputStream br = new ObjectInputStream(new FileInputStream("baseDatos/datos.txt"));
			datos = (HashMap<String, UsuarioRegistrado>) br.readObject();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException s) {
			s.printStackTrace();
		} catch (ClassNotFoundException m) {
			m.printStackTrace();
		}
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
