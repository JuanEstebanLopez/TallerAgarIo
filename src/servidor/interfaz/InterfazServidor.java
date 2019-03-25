package servidor.interfaz;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import comun.Jugador;
import servidor.modelo.Modelo;

public class InterfazServidor extends JFrame {

	public static InterfazServidor REF;

	private Modelo modelo;
	private JuegoServidor juego;

	public static void main(String[] args) {
		REF = new InterfazServidor();
		REF.iniciar();
		REF.setVisible(true);
	}

	public InterfazServidor() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		juego = new JuegoServidor(this);
		add(juego, BorderLayout.CENTER);
		JButton btnIniciar = new JButton("Iniciar Juego");
		btnIniciar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				iniciarPartida();

			}
		});
		add(btnIniciar, BorderLayout.SOUTH);
		pack();
	}

	@Override
	public void dispose() {
		System.out.println("Log Out");
		super.dispose();
		System.exit(EXIT_ON_CLOSE);
	}

	public void iniciar() {
		modelo = new Modelo(this);
	}

	public int agregarJugador(Jugador juga) {
		return juego.agregarJugador(juga);
	}

	public void actualizarPosiciones(String jugadores, String comida) {
		modelo.ActualizarClientes(jugadores, comida);
	}

	public void iniciarPartida() {
		modelo.IniciarJuego();
		juego.iniciarJuego();
	}
}
