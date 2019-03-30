package comun;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class PanelRanking extends JPanel {
	
	private JLabel jugador1, jugador2, jugador3, jugador4, jugador5;
	
	public PanelRanking() {
		setPreferredSize(new Dimension(200, 0));
		TitledBorder border = BorderFactory.createTitledBorder("Ranking");
		border.setTitleColor(Color.BLUE);
		setBorder(border);
		
		jugador1 = new JLabel();
		jugador2 = new JLabel();
		jugador3 = new JLabel();
		jugador4 = new JLabel();
		jugador5 = new JLabel();
		
		setLayout(new GridLayout(1, 5));
		add(jugador1);
		add(jugador2);
		add(jugador3);
		add(jugador4);
		add(jugador5);
	}

}
