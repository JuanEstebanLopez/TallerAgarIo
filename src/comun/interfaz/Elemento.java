package comun.interfaz;

public class Elemento {
	public final static int BASE_DIAMETER = 100;

	protected float x, y, d;
	protected Color c;

	public Elemento() {
		d = BASE_DIAMETER;
		x = (float) (Math.random() * PanelJuego.WIDTH - d) + d / 2;
		y = (float) (Math.random() * PanelJuego.HEIGHT - d) + d / 2;
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
		return (int) (x - d / 2);
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return (int) (y - d / 2);
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
