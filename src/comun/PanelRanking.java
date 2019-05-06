package comun;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import cliente.modelo.Comunicacion;

public class PanelRanking extends JPanel {
	public final static int UPDATETIME = 300;
	private String[] nombres;
	private long lastUpdate;

	public boolean needUpdate() {
		return System.currentTimeMillis() - lastUpdate >= UPDATETIME;
	}

	public PanelRanking() {
		lastUpdate = 0;
		setPreferredSize(new Dimension(200, 0));
		TitledBorder border = BorderFactory.createTitledBorder("Ranking");
		border.setTitleColor(Color.BLUE);
		setBorder(border);
		setLayout(new GridLayout(1, 5));

	}

	/**
	 * Obtiene los puntajes de la información del jugador.
	 * 
	 * @param jugadores
	 */
	public void mostrarPuntajes(String[] jugadores) {
		int[] puntajes = new int[jugadores.length];
		for (int i = 0; i < puntajes.length; i++) {
			String[] infoJuga = jugadores[i].split(Comunicacion.SEPARADOR_MIN);
			puntajes[i] = Integer.parseInt(infoJuga[infoJuga.length - 1]);
		}
		mostrarPuntajes(puntajes);
	}

	/**
	 * Actualiza en la interfaz la lista de putajes de mayor a menor.
	 * 
	 * @param puntajes
	 */
	public void mostrarPuntajes(int[] puntajes) {

		int[][] indices = new int[puntajes.length][2];
		for (int i = 0; i < indices.length; i++) {
			indices[i][0] = puntajes[i];
			indices[i][1] = i;
		}
		Comparator<int[]> com = new Comparator<int[]>() {
			@Override
			public int compare(int[] o1, int[] o2) {
				return o2[0] - o1[0];
			}

		};
		Arrays.sort(indices, com);
		removeAll();
		setPreferredSize(new Dimension(200, 0));
		TitledBorder border = BorderFactory.createTitledBorder("Ranking");
		border.setTitleColor(Color.BLUE);
		setBorder(border);
		setLayout(new GridLayout(indices.length, 1));
		// System.out.println(nombres + ": " + Arrays.toString(nombres));
		if (nombres != null)
			for (int i = 0; i < indices.length; i++) {
				String nom = nombres[indices[i][1]];
				// System.out.println(Arrays.toString(indices[i]) + " " + nom);
				add(new JLabel((i + 1) + ") " + nom + ": " + indices[i][0]));
			}
		updateUI();
		lastUpdate = System.currentTimeMillis();
	}

	public void setNombres(String[] nombres) {
		this.nombres = nombres;
	}

	public String[] getNombres() {
		return nombres;
	}
}
