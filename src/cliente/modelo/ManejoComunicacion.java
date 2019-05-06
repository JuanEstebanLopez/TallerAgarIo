package cliente.modelo;

import cliente.interfaz.InterfazCliente;

public class ManejoComunicacion {

	private InterfazCliente interfaz;
	private StreamJuegoCliente streamJuego;
	private StreamAudioCliente streamAudio;
	private UserChat userChat;

	public ManejoComunicacion(InterfazCliente interfazCliente) {
		this.interfaz = interfazCliente;
		this.streamAudio = new StreamAudioCliente();
		this.streamAudio.start();
	}

	public void iniciarStreamJuego() {
		streamJuego = new StreamJuegoCliente(interfaz);
		streamJuego.unirseStream();
		userChat = new UserChat(this);
		userChat.start();
	}

	public void sendChatMessage(String message) {
		if (userChat != null)
			userChat.enviarMensaje(message);
	}

	public void showChatMessage(String message) {
		interfaz.addTextChat(message);
	}

	public void updateNameChat(String name) {
		interfaz.setNameChat(name);
	}

	public void Registrarusuarios(String usrs) {
		interfaz.registrarUsuarios(usrs.split(UserChat.SEPARADOR));
	}
}
