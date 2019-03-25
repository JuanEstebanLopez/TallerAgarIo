package servidor.interfaz;

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
		juego = new JuegoServidor();
		add(juego);
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

	public String[] getListaPosiciones() {
		String[] l = { juego.listaJugadores(), juego.listaComida() };
		return l;
	}
}
