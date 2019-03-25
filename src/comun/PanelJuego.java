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
		JuegoActivo = true;

		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		new Thread(this).start();
	}

	@Override
	public void paint(Graphics g) {
		super.paintComponent(g);
		// g.drawOval((int) (Math.random() * WIDTH), (int) (Math.random() * HEIGHT),
		// 200, 200);

		// if (isCliente)
		// g.drawOval(jugador.getX(), jugador.getY(), jugador.getD(), jugador.getD());

		pintarComida(g);
		pintarJugadores(g);
	}

	public abstract void pintarComida(Graphics g);

	public abstract void pintarJugadores(Graphics g);

	public abstract void update();
	// {
	// if(isCliente)
	// jugador.move(mx, my);
	// }

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