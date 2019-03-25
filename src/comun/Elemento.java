package comun;

public class Elemento {
	public final static int BASE_DIAMETER = 100;
	public final static int COMIDA_DIAMETER = 20;
	protected float x, y, d;
	protected Color c;

	public Elemento() {
		this(BASE_DIAMETER);
	}

	public Elemento(int d) {
		this.d = d;
		this.x = (float) (Math.random() * (PanelJuego.WIDTH - d)) + d / 2;
		this.y = (float) (Math.random() * (PanelJuego.HEIGHT - d)) + d / 2;
	}

	public byte collisionWhith(Elemento e) {
		if (dist(e.x, e.y) < (d + e.d) / 2)
			if (d > e.d)
				return 1;
			else
				return -1;
		return 0;
	}

	public int dist(float dx, float dy) {
		return (int) Math.sqrt(Math.pow(x - dx, 2) + Math.pow(y - dy, 2));

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

	public void setD(int d) {
		this.d = d;
	}

	public Color getC() {
		return c;
	}

	public void setC(Color c) {
		this.c = c;
	}

}
