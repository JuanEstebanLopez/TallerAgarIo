package comun;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import cliente.modelo.Comunicacion;

public abstract class PanelJuego extends JPanel implements Runnable {
	private static final long serialVersionUID = -7954891210811247751L;
	public final static int WIDTH = 1000;
	public final static int HEIGHT = 600;

	private boolean JuegoActivo;

	public PanelJuego() {
		JuegoActivo = false;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}

	public void IniciarJuego() {
		if (!JuegoActivo) {
			JuegoActivo = true;
			new Thread(this).start();
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paintComponent(g);
		pintarComida(g);
		pintarJugadores(g);
	}

	/**
	 * Pinta los elementos de comida del juego.
	 * 
	 * @param g
	 */
	public abstract void pintarComida(Graphics g);

	/**
	 * pinta los jugadores del juego.
	 * 
	 * @param g
	 */
	public abstract void pintarJugadores(Graphics g);

	/**
	 * Actualiza las posiciones y elementos del juego.
	 */
	public abstract void update();

	public boolean isJuegoActivo() {
		return JuegoActivo;
	}

	public void setJuegoActivo(boolean juegoActivo) {
		JuegoActivo = juegoActivo;
	}

	@Override
	public void run() {
		while (JuegoActivo) {
			update();
			repaint();
			try {
				Thread.sleep(Comunicacion.SLEEP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
