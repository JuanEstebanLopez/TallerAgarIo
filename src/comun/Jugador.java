package comun;

public class Jugador extends Elemento {
	public final static int BASE_DIAMETER = 100;
	public final static int MIN_DIAMETER = 30;
	public final static int BASE_VEL = 50;
	public final static int INCREMENT = 10;

	private boolean activo;
	private int puntaje;
	private String nombre;

	private int alimentosConsumidos;

	private boolean ganador;
	private int puesto;

	public Jugador() {
		super(BASE_DIAMETER);
		activo = true;
		alimentosConsumidos = 0;
		puntaje = 0;

		ganador = false;
		puesto = 5;
	}

	/**
	 * Mueve el jugador en la dirección dada.
	 * 
	 * @param direx
	 * @param direy
	 */
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

	/**
	 * Calcula el puntaje del jugador en base a su tamaño.
	 * 
	 * @return
	 */
	public int getPuntaje() {
		return (int) ((d - BASE_DIAMETER) / INCREMENT);
	}

	public void comer(boolean isComida) {
		puntaje += INCREMENT;
		if (isComida)
			alimentosConsumidos++;
	}

	public int getAlimentosConsumidos() {
		return alimentosConsumidos;
	}

	public void setAlimentosConsumidos(int alimentosConsumidos) {
		this.alimentosConsumidos = alimentosConsumidos;
	}

	public void setPuntajeTotal(int puntaje) {
		this.puntaje = puntaje;
	}

	public int getPuntajeTotal() {
		return puntaje;
	}

	public boolean isGanador() {
		return ganador;
	}

	public void setGanador(boolean ganador) {
		this.ganador = ganador;
	}

	public int getPuesto() {
		return puesto;
	}

	public void setPuesto(int puesto) {
		this.puesto = puesto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
