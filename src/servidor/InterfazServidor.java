package servidor;

import javax.swing.JFrame;

import comun.interfaz.PanelJuego;
import servidor.modelo.Modelo;

public class InterfazServidor extends JFrame {

	public static InterfazServidor REF;
	
	private Modelo modelo;
	
	
	public static void main(String[] args) {
		REF = new InterfazServidor();
		REF.iniciar();
		REF.setVisible(true);
	}

	public InterfazServidor() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		add(new PanelJuego(false));
		pack();
	}

	@Override
	public void dispose() {
		System.out.println("Log Out");
		super.dispose();
		System.exit(EXIT_ON_CLOSE);
	}

	public void iniciar() {
		modelo= new Modelo();
	}

}
