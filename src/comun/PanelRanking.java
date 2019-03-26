package comun;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class PanelRanking extends JPanel {
	
	public PanelRanking() {
		setPreferredSize(new Dimension(200, 0));
		TitledBorder border = BorderFactory.createTitledBorder("Ranking");
		border.setTitleColor(Color.BLUE);
		setBorder(border);
		setLayout(new GridLayout(1, 5));
		add(new JLabel(""));
	}

}
