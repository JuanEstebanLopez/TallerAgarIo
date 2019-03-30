package servidor.interfaz;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import cliente.modelo.Comunicacion;

import comun.Elemento;
import comun.Jugador;
import comun.PanelJuego;
import servidor.modelo.Modelo;

public class JuegoServidor extends PanelJuego {

	public final static int MIN_COMIDA = 10;
	public final static int MAX_COMIDA = 18;

	private ArrayList<Jugador> jugadores;
	private LinkedList<Elemento> comida;
	private InterfazServidor principal;

	public JuegoServidor(InterfazServidor principal) {
		super();
		this.principal = principal;
		jugadores = new ArrayList<Jugador>(Modelo.CANT_JUGADORES);
		comida = new LinkedList<Elemento>();
	}

	public ArrayList<Color> colorear() {
		ArrayList<Color> colores = new ArrayList<Color>();
		colores.add(Color.BLUE);
		colores.add(Color.RED);
		colores.add(Color.CYAN);
		colores.add(Color.YELLOW);
		colores.add(Color.MAGENTA);
		colores.add(Color.ORANGE);
		colores.add(Color.PINK);
		colores.add(Color.GREEN);
		colores.add(Color.WHITE);
		return colores;
	}

	@Override
	public void pintarComida(Graphics g) {
		Iterator<Elemento> c = comida.iterator();
		while (c.hasNext()) {
			Elemento color = c.next();
			g.setColor(colorear().get(color.getC()));
			g.fillOval(color.getX(), color.getY(), color.getD(), color.getD());

		}
	}

	@Override
	public void pintarJugadores(Graphics g) {
		for (Jugador juga : jugadores) {
			g.setColor(colorear().get(juga.getC()));
			g.fillOval(juga.getX(), juga.getY(), juga.getD(), juga.getD());
		}
	}

	@Override
	public void update() {
		// Actualiza los estados de los jugadores activos
		for (Jugador juga : jugadores) {
			if (juga.isActivo()) {
				// recorre la comida para eliminar la comida que se ha consumido
				Iterator<Elemento> c = comida.iterator();
				while (c.hasNext()) {
					Elemento co = c.next();
					if (juga.collisionWhith(co) > 0) {
						juga.setD(juga.getD() + Jugador.INCREMENT);
						c.remove();
					}
				}
				for (Jugador opo : jugadores) {
					if (juga != opo && opo.isActivo() && juga.collisionWhith(opo) > 0) {
						juga.setD(juga.getD() + Jugador.INCREMENT);
						opo.setD(opo.getD() - Jugador.INCREMENT);
					}
				}
			}
		}
		// Actuliza los jugadores que han perdido
		Iterator<Jugador> jit = jugadores.iterator();
		while (jit.hasNext()) {
			Jugador j = jit.next();
			if (j.isActivo() && j.getD() < Jugador.MIN_DIAMETER)
				DescalificarJugador(j);
		}
		addComida();
		actualizarPosiciones();
	}

	public String TerminarJuego() {
		String puntajes = "";
		for (int i = 0; i < jugadores.size(); i++) {
			puntajes += jugadores.get(i).getPuntaje() + " ";
		}
		this.setJuegoActivo(false);
		return puntajes;
	}

	private void addComida() {
		if (comida.size() < MIN_COMIDA) {
			int add = (MIN_COMIDA - comida.size()) + (int) (Math.random() * (float) (MAX_COMIDA - MIN_COMIDA));
			for (int i = 0; i < add; i++) {
				comida.add(new Elemento(Elemento.COMIDA_DIAMETER));
			}
		}
	}

	public void DescalificarJugador(Jugador juga) {
		juga.setActivo(false);
	}

	public int agregarJugador(Jugador juga) {
		jugadores.add(juga);
		return jugadores.size() - 1;
	}

	public void actualizarPosiciones() {
		principal.actualizarPosiciones(listaJugadores(), listaComida());
	}

	public String listaJugadores() {
		StringBuilder str = new StringBuilder();
		for (Jugador j : jugadores) {
			str.append(j.getC()) // color
					.append(Comunicacion.SEPARADOR_MIN) // separador de atributo
					.append(j.getX()) // Coordenada X
					.append(Comunicacion.SEPARADOR_MIN) // separador de atributo
					.append(j.getY()) // Coordenada Y
					.append(Comunicacion.SEPARADOR_MIN) // separador de atributo
					.append(j.getD()) // Diámetro
					.append(Comunicacion.SEPARADOR_MIN) // separador de jugador
					.append(j.getPuntaje()) // Puntaje
					.append(Comunicacion.SEPARADOR); // separador de jugador
		}
		return str.toString();
	}

	public String listaComida() {
		StringBuilder str = new StringBuilder();
		for (Elemento c : comida) {
			str.append(c.getC()) // color
					.append(Comunicacion.SEPARADOR_MIN) // separador de atributo
					.append(c.getX()) // Coordenada X
					.append(Comunicacion.SEPARADOR_MIN) // separador de atributo
					.append(c.getY()) // Coordenada Y
					.append(Comunicacion.SEPARADOR); // separador entre comida
		}
		return str.toString();
	}

}
