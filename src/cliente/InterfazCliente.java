package cliente;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cliente.modelo.Comunicacion;
import comun.interfaz.PanelJuego;

public class InterfazCliente extends JFrame {

	private static final long serialVersionUID = 849115524426412246L;
	public static InterfazCliente REF;

	private JTextField txtNombre;
	private JButton btnIniciar;
	private Comunicacion comumincacion;

	public static void main(String[] args) {
		REF = new InterfazCliente();
		REF.iniciar();
		REF.setVisible(true);
	}

	public InterfazCliente() {
		setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		add(new PanelJuego(true), BorderLayout.CENTER);

		JPanel panelOpciones = new JPanel();
		panelOpciones.setLayout(new GridLayout(1, 3));
		txtNombre = new JTextField("Introduce un Nombre");
		txtNombre.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {
				ActualizarNombre(txtNombre.getText());
			}

			@Override
			public void keyReleased(KeyEvent arg0) {

			}

			@Override
			public void keyPressed(KeyEvent arg0) {

			}
		});
		btnIniciar = new JButton("Conectar");
		btnIniciar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Conectar();

			}
		});
		panelOpciones.add(txtNombre);
		panelOpciones.add(btnIniciar);
		add(panelOpciones, BorderLayout.SOUTH);

		pack();
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
}
