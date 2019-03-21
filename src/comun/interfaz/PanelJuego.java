package comun.interfaz;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JPanel;

public class PanelJuego extends JPanel implements Runnable {
	private static final long serialVersionUID = -7954891210811247751L;
	public final static int WIDTH = 1000;
	public final static int HEIGHT = 600;

	private Jugador jugador;
	private boolean JuegoActivo;

	private ArrayList<Jugador> jugadores;
	private LinkedList<Elemento> comida;

	public int mx, my;
	
	private boolean isCliente;

	public PanelJuego(boolean isClient) {
		JuegoActivo = true;
		this.isCliente = isClient;
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		jugadores= new ArrayList<Jugador>();
		comida = new LinkedList<Elemento>();
		if (isClient) {
			jugador = new Jugador();
			this.addMouseMotionListener(new MouseMotionListener() {

				@Override
				public void mouseMoved(MouseEvent e) {
					mx = e.getX();
					my = e.getY();

				}

				@Override
				public void mouseDragged(MouseEvent e) {
				}
			});
		}
		new Thread(this).start();
	}

	@Override
	public void paint(Graphics g) {
		super.paintComponent(g);
		// g.drawOval((int) (Math.random() * WIDTH), (int) (Math.random() * HEIGHT),
		// 200, 200);
		
		Iterator<Elemento> c= comida.iterator();
		while(c.hasNext()) {
			Elemento color = c.next();
			g.drawOval(color.getX(), color.getY(), color.getD(), color.getD());
		}
		
		if(isCliente)
		g.drawOval(jugador.getX(), jugador.getY(), jugador.getD(), jugador.getD());
	}

	public void update() {
		if(isCliente)
		jugador.move(mx, my);
	}

	@Override
	public void run() {
		while (JuegoActivo) {
			update();
			repaint();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
