package cliente.interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import cliente.modelo.Comunicacion;
import comun.PanelRanking;

public class InterfazCliente extends JFrame {

	private static final long serialVersionUID = 849115524426412246L;
	public static InterfazCliente REF;

	private JLabel labNombre;
	private JTextField txtNombre;
	private JLabel labPassword;
	private JPasswordField txtPassword;
	private JButton btnIniciar;
	private JButton btnRegistrar;

	private JPanel panelOpciones;
	private Comunicacion comumincacion;

	private PanelRanking panelRanking;

	private JuegoCliente juego;

	public static void main(String[] args) {
		REF = new InterfazCliente();
		REF.iniciar();
		REF.setVisible(true);
	}

	public InterfazCliente() {
		setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("Agario");
		panelRanking = new PanelRanking();
		juego = new JuegoCliente(this);
		add(juego, BorderLayout.CENTER);
		add(panelRanking, BorderLayout.EAST);

		panelOpciones = new JPanel();
		panelOpciones.setLayout(new GridLayout(1, 6));
		TitledBorder border = BorderFactory.createTitledBorder("Datos del usuario");
		border.setTitleColor(Color.BLUE);
		panelOpciones.setBorder(border);
		labNombre = new JLabel("             Digite su email: ");
		txtNombre = new JTextField();
		labPassword = new JLabel("                     Contrase�a: ");
		txtPassword = new JPasswordField();

		txtNombre.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				ActualizarNombre(txtNombre.getText());
			}

			@Override
			public void keyPressed(KeyEvent arg0) {

			}
		});

		btnRegistrar = new JButton("Registrar");
		btnRegistrar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Conectar();
				registrarUsuario();
			}
		});
		btnIniciar = new JButton("Conectar");
		btnIniciar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// ActualizarNombre(txtNombre.getText());
				Conectar();
				iniciarSesion();

			}
		});

		panelOpciones.add(labNombre);
		panelOpciones.add(txtNombre);
		panelOpciones.add(labPassword);
		panelOpciones.add(txtPassword);
		panelOpciones.add(btnIniciar);
		panelOpciones.add(btnRegistrar);
		add(panelOpciones, BorderLayout.SOUTH);

		pack();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {

		}
	}

	/**
	 * Env�a �peticion de inicio de sesi�n al servidor.
	 */
	public void iniciarSesion() {
		String nombre = txtNombre.getText();
		String password = new String(txtPassword.getPassword());
		comumincacion.tryEnviarMensaje(Comunicacion.INICIAR_SESION, nombre, password);
	}

	/**
	 * Env�a peticion de registro al servidor
	 */
	public void registrarUsuario() {
		String nombre = "";
		String email = "";
		String password = "";
		while (nombre.equals("")) {
			nombre = JOptionPane.showInputDialog(this, "Digite el nombre de usuario");
		}
		if (!nombre.equals("")) {
			while (email.equals("")) {
				email = JOptionPane.showInputDialog(this, "Digite su e-mail");
			}
			if (!email.equals("")) {
				while (password.equals("")) {
					password = JOptionPane.showInputDialog(this, "Digite su contrase�a");
				}
				System.out.println(Comunicacion.REGISTRAR + " " + nombre + " " + email + " " + password);
				comumincacion.tryEnviarMensaje(Comunicacion.REGISTRAR, nombre, email, password);
			}
		}
	}

	/**
	 * Actualiza el mensaje de la parte inferior de la pantalla.
	 * 
	 * @param mensaje
	 */
	public void actualizarEstadoJuego(String mensaje) {
		panelOpciones.removeAll();
		panelOpciones.setLayout(new GridLayout(1, 1));
		panelOpciones.add(new JLabel(mensaje));
		pack();
	}

	@Override
	public void dispose() {
		// JOptionPane.showMessageDialog(this, "Log Out");
		super.dispose();
		System.exit(EXIT_ON_CLOSE);
	}

	/**
	 * Inicia la comunicaci�n con el servidor.
	 */
	public void iniciar() {
		comumincacion = new Comunicacion(this);
	}

	public void ActualizarNombre(String nom) {
		if (nom == null || "".equals(nom)) {
			btnIniciar.setEnabled(false);
		} else {
			comumincacion.setNombre(nom);
			btnIniciar.setEnabled(true);
		}
	}

	/**
	 * Agrega los nombres de usuario al ranking.
	 * 
	 * @param usuarios
	 */
	public void registrarUsuarios(String[] usuarios) {
		panelRanking.setNombres(usuarios);
	}

	public void Conectar() {
		if (comumincacion.getState().equals(Thread.State.NEW))
			comumincacion.crearComunicacion();
	}

	/**
	 * inicia el juego
	 */
	public void iniciarJuego() {
		juego.IniciarJuego();
	}

	/**
	 * Env�a la se�al para mover al jugador
	 * 
	 * @param x
	 * @param y
	 */
	public void actualizarPosiciones(int x, int y) {
		comumincacion.moverJugador(x, y);
	}

	/**
	 * Actualiza el �ndice del jugador
	 * 
	 * @param ind
	 */
	public void actualizarJugadorIndex(int ind) {
		juego.setJugadorIndex(ind);
		actualizarEstadoJuego("Jugador " + (ind + 1) + ": " + comumincacion.getNombre());
	}

	/**
	 * Mensaje para el final de la partida.
	 * 
	 * @param puntajes
	 */
	public void actualizarPuntajeFinal(String puntajes) {
		int i = juego.getJugadorIndex();
		int p = 1;
		String[] pts = puntajes.split(" ");
		int pt = Integer.parseInt(pts[i]);
		for (int j = 0; j < pts.length; j++) {
			if (Integer.parseInt(pts[j]) > pt)
				p++;
		}
		String men = "Partida teminada, " + comumincacion.getNombre() + ", puesto:" + p + " puntaje:" + pt;
		actualizarEstadoJuego(men);
		JOptionPane.showMessageDialog(null, men);

	}

	public void terminarJuego() {
		juego.setJuegoActivo(false);
	}

	/**
	 * Actualiza en la interfaz los elementos del juego.
	 * 
	 * @param jugadores
	 * @param comida
	 */
	public void actualizarElementosJuego(String jugadores, String comida) {
		juego.actualizarElementosJuego(jugadores, comida);
		if (panelRanking.needUpdate())
			panelRanking.mostrarPuntajes(jugadores.split(Comunicacion.SEPARADOR));
	}
}
