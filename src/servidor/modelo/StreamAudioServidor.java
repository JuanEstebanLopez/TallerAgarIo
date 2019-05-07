package servidor.modelo;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class StreamAudioServidor extends Thread {

	private SourceDataLine sourceDataLine;
	private final byte audioBuffer[] = new byte[10000];

	public final static String IP = "239.1.2.3";
	public final static int PORT = 5000;

	private MulticastSocket ms;
	private InetAddress grupo;

	// private InterfazCliente interfaz;
	private boolean enviar;

	private AudioInputStream au;

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
		float sampleRate = 44100.0F;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

	private void broadcastAudio() {
		try {
			while (enviar) {
				byte audioBuffer[] = new byte[10000];
				// int count = targetDataLine.read(audioBuffer, 0, audioBuffer.length);
				// int count = au.read(audioBuffer);

				int count = au.read(audioBuffer, 0, audioBuffer.length);
				System.out.println(count);
				if (count > 0) {
					sourceDataLine.write(audioBuffer, 0, count);
					DatagramPacket packet = new DatagramPacket(audioBuffer, count, grupo, PORT);
					ms.send(packet);

				} else {
					setupAudio();

				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void setupAudio() {
		try {

			au = AudioSystem.getAudioInputStream(new File("data/game.wav"));
			AudioFormat audioFormat = getAudioFormat();
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);

			sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			sourceDataLine.open(audioFormat);
			sourceDataLine.start();

			// Clip sonido = AudioSystem.getClip();
			// sonido.open(AudioSystem.getAudioInputStream(new File("data/game.wav")));
			// sonido.loop(Clip.LOOP_CONTINUOUSLY);
			// sonido.start();

			//
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
	}

}
