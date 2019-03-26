package servidor.interfaz;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

import comun.Jugador;
import comun.PanelRanking;
import servidor.modelo.Modelo;

public class InterfazServidor extends JFrame {

	public static InterfazServidor REF;

	private Modelo modelo;
	private JuegoServidor juego;
	private PanelRanking panelRanking;

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
		panelRanking = new PanelRanking();
		juego = new JuegoServidor(this);
		add(panelRanking, BorderLayout.EAST);
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
		
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){	
			
		}
	}
	
	public void refrecarRanking(String[] arreglo) {
		
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
		juego.IniciarJuego();
	}
}
