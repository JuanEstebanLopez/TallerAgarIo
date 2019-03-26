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
import javax.swing.JPanel;
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
	private JTextField txtPassword;
	private JButton btnIniciar;
	private JButton btnRegistrar;
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

		JPanel panelOpciones = new JPanel();
		panelOpciones.setLayout(new GridLayout(1, 6));
		TitledBorder border = BorderFactory.createTitledBorder("Datos del usuario");
		border.setTitleColor(Color.BLUE);
		panelOpciones.setBorder(border);
		labNombre = new JLabel("             Digite su Nickname: ");
		txtNombre = new JTextField();
		labPassword = new JLabel("                     Contraseña: ");
		txtPassword = new JTextField();
		
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
				registrarUsuario();
			}
		});
		btnIniciar = new JButton("Conectar");
		btnIniciar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ActualizarNombre(txtNombre.getText());
				Conectar();

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
		
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){	
			
		}
	}
	
	public void registrarUsuario() {
		
	}

	@Override
	public void dispose() {
		System.out.println("Log Out");
		super.dispose();
		System.exit(EXIT_ON_CLOSE);
	}

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

	public void Conectar() {
		comumincacion.crearComunicacion();
	}

	public void iniciarJuego() {
		juego.iniciarJuego();
	}

	public void actualizarPosiciones(int x, int y) {
		comumincacion.moverJugador(x, y);
	}

	public void actualizarJugadorIndex(int ind) {
		juego.setJugadorIndex(ind);
	}

	public void actualizarElementosJuego(String jugadores, String comida) {
		juego.actualizarElementosJuego(jugadores, comida);
	}
}
