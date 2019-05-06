package servidor.modelo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class StreamAudioServidor extends Thread {

	private TargetDataLine targetDataLine;
	private final byte audioBuffer[] = new byte[10000];

	public final static String IP = "239.1.2.3";
	public final static int PORT = 5000;

	private MulticastSocket ms;
	private InetAddress grupo;

	// private InterfazCliente interfaz;
	private boolean enviar;

	public StreamAudioServidor() {
		// this.interfaz = interfaz;
		conectar();
	}

	public void conectar() {
		try {
			grupo = InetAddress.getByName(IP);
			ms = new MulticastSocket(PORT);
			ms.joinGroup(grupo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {

		enviar = true;
		try {
			setupAudio();
			broadcastAudio();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private AudioFormat getAudioFormat() {
		float sampleRate = 16000F;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

	private void broadcastAudio() {
		try {
			while (enviar) {
				
				int count = targetDataLine.read(audioBuffer, 0, audioBuffer.length);
				if (count > 0) {
					DatagramPacket packet = new DatagramPacket(audioBuffer, audioBuffer.length, grupo, PORT);
					ms.send(packet);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void setupAudio() {
		try {
			AudioFormat audioFormat = getAudioFormat();
			DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
			targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			targetDataLine.open(audioFormat);
			targetDataLine.start();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
	}

}
