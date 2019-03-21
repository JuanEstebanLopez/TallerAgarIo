package comun.interfaz;

public class Jugador extends Elemento {
	public final static int BASE_DIAMETER = 100;
	public final static int MIN_DIAMETER = 30;
	public final static int BASE_VEL = 50;

	public Jugador() {
		d = BASE_DIAMETER;
		x = (float) (Math.random() * PanelJuego.WIDTH - d) + d / 2;
		y = (float) (Math.random() * PanelJuego.HEIGHT - d) + d / 2;
	}	

	public void move(int mx, int my) {
		x += (mx - x) / BASE_VEL;
		y += (my - y) / BASE_VEL;
	}
	
}
