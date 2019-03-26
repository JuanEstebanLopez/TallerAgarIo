package comun;

public class Elemento {
	public final static int BASE_DIAMETER = 100;
	public final static int COMIDA_DIAMETER = 20;
	protected float x, y, d;
	protected int c;

	public Elemento() {
		this(BASE_DIAMETER);
	}

	public Elemento(int d) {
		this.d = d;
		this.x = (float) (Math.random() * (PanelJuego.WIDTH - d)) + d / 2;
		this.y = (float) (Math.random() * (PanelJuego.HEIGHT - d)) + d / 2;
		c = (int) (Math.random() * 9);
	}

	public byte collisionWhith(Elemento e) {
		if (dist(e.getXR(), e.getYR()) < (d + e.d) / 2)
			if (d > e.d)
				return 1;
			else
				return -1;
		return 0;
	}

	public int dist(float dx, float dy) {
		return (int) Math.sqrt(Math.pow(getXR() - dx, 2) + Math.pow(getYR() - dy, 2));

	}

	public int getX() {
		return (int) (x);
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return (int) (y);
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getD() {
		return (int) d;
	}

	/**
	 * @return Coordenada x del centro el elemento
	 */
	public int getXR() {
		return (int) (x + d / 2);
	}

	/**
	 * @return Coordenada y del centro del elemento
	 */
	public int getYR() {
		return (int) (y + d / 2);
	}

	public void setD(int d) {
		this.d = d;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

}
