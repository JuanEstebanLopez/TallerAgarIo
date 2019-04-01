package servidor.interfaz;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

import comun.Jugador;
import comun.PanelRanking;
import comun.StopWatch;
import servidor.modelo.Modelo;

public class InterfazServidor extends JFrame {

	public static InterfazServidor REF;

	private Modelo modelo;
	private JuegoServidor juego;
	private PanelRanking panelRanking;
	private StopWatch reloj;
	public boolean juegoIniciado;

	private JButton btnIniciar;

	public static void main(String[] args) {
		REF = new InterfazServidor();
		REF.iniciar();
		REF.setVisible(true);
	}

	public InterfazServidor() {
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Agario");
		setLayout(new BorderLayout());
		juegoIniciado = false;
		panelRanking = new PanelRanking();
		juego = new JuegoServidor(this);
		reloj = new StopWatch();
		add(panelRanking, BorderLayout.EAST);
		add(juego, BorderLayout.CENTER);
		btnIniciar = new JButton("Iniciar Juego");
		btnIniciar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				iniciarPartida();
				reloj.start();
			}
		});
		add(btnIniciar, BorderLayout.SOUTH);
		btnIniciar.setEnabled(false);
		pack();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {

		}

	}

	public void refrecarRanking() {

	}

	public void TerminarPartida(boolean forzarFin) {
		System.out.println(reloj.getElapsedTimeSecs());
		if (forzarFin || reloj.getElapsedTimeSecs() >= 300) {
			modelo.TerminarJuego(juego.TerminarJuego());

		}
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

	public void puedeIniciar() {
		btnIniciar.setEnabled(true);
	}

	public boolean juegoIniciado() {
		return juegoIniciado;
	}

	public int agregarJugador(Jugador juga) {
		return juego.agregarJugador(juga);
	}

	/**
	 * Actualiza las coordenadas de los elementos del juego
	 * 
	 * @param jugadores
	 *            String con la información de los jugadores
	 * @param comida
	 *            String con la información de la comida
	 */
	public void actualizarPosiciones(String jugadores, String comida) {
		modelo.ActualizarClientes(jugadores, comida);
	}

	public void iniciarPartida() {
		juegoIniciado = true;
		modelo.IniciarJuego();
		juego.IniciarJuego();
	}
}
