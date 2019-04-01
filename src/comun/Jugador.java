package comun;

public class Jugador extends Elemento {
	public final static int BASE_DIAMETER = 100;
	public final static int MIN_DIAMETER = 30;
	public final static int BASE_VEL = 50;
	public final static int INCREMENT = 10;

	private boolean activo;
	private int puntaje;

	public Jugador() {
		super(BASE_DIAMETER);
		activo = true;
	}

	public void move(float direx, float direy) {
		if (!(direx > 0 && x >= PanelJuego.WIDTH - d) && !(direx < 0 && x <= 0))
			x += direx / BASE_VEL;

		if (!(direy > 0 && y >= PanelJuego.HEIGHT - d) && !(direy < 0 && y <= 0))
			y += direy / BASE_VEL;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
		if (!activo) {
			y = -100;
			x = -100;
			d = 0;
		}
	}

	public boolean isActivo() {
		return activo;
	}

	public int getPuntaje() {
		return (int) ((d - BASE_DIAMETER) / INCREMENT);
	}

}
