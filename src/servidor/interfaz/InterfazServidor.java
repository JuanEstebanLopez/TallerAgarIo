package servidor.interfaz;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

import comun.Jugador;
import comun.PanelRanking;
import comun.StopWatch;
import servidor.modelo.Comunicacion;
import servidor.modelo.Modelo;

public class InterfazServidor extends JFrame {

	public static InterfazServidor REF;

	private Modelo modelo;
	private JuegoServidor juego;
	private PanelRanking panelRanking;
	private StopWatch reloj;
	public boolean juegoIniciado;

	private JButton btnIniciar;

	private Timer timerEsperaDeUsuarios;
	private TimerTask taskEsperaDeUsuarios;

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
				if (timerEsperaDeUsuarios != null)
					timerEsperaDeUsuarios.cancel();
			}
		});
		add(btnIniciar, BorderLayout.SOUTH);
		btnIniciar.setEnabled(false);
		pack();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {

		}

		taskEsperaDeUsuarios = new TimerTask() {
			private int min = 2;
			private int seg = 0;

			@Override
			public void run() {
				seg--;
				if (seg < 0) {
					seg = 59;
					min--;
				}
				updateTimeEsperaDeUsuarios(min, seg);
			}
		};

		timerEsperaDeUsuarios = new Timer();
		timerEsperaDeUsuarios.schedule(taskEsperaDeUsuarios, 1000, 1000);

	}

	public void updateTimeEsperaDeUsuarios(int min, int seg) {
		if (min <= 0 && seg <= 0) {
			iniciarPartida();
			reloj.start();
			timerEsperaDeUsuarios.cancel();
		} else {
			System.out.println("Partida inicia en " + min + ":" + seg);
			btnIniciar.setText("Iniciar (" + min + ":" + seg + ")");
		}
	}

	public void refrecarRanking() {

	}

	/**
	 * Manda señal de acabar la partida
	 * 
	 * @param forzarFin
	 *            true si se acaba la partida sin esperar a que se acabe el límite
	 *            de tiempo.
	 */
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

	/**
	 * Habilita el botón para iniciar la partida.
	 */
	public void puedeIniciar() {
		btnIniciar.setEnabled(true);
	}

	/**
	 * Indica si el juego ya está corriendo.
	 * 
	 * @return true si el juego ha comenzado, false en caso contrario.
	 */
	public boolean juegoIniciado() {
		return juegoIniciado;
	}

	/**
	 * Agrega un nuevo jugador.
	 * 
	 * @param juga
	 * @return
	 */
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
		if (panelRanking.needUpdate())
			panelRanking.mostrarPuntajes(jugadores.split(Comunicacion.SEPARADOR));
	}

	/**
	 * Inicia el juego.
	 */
	public void iniciarPartida() {
		juegoIniciado = true;
		String usrs = modelo.IniciarJuego();
		juego.IniciarJuego();
		panelRanking.setNombres(usrs.split(Comunicacion.SEPARADOR_MIN));
		btnIniciar.setVisible(false);
	}
}
