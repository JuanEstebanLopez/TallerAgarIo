package cliente.modelo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class StreamAudioCliente extends Thread {

	private AudioInputStream audioInputStream;
	private SourceDataLine sourceDataLine;
	public final static String IP = "239.1.2.3";
	public final static int PORT = 5000;

	private MulticastSocket ms;
	private InetAddress grupo;

	private boolean escuchar;

	// private InterfazCliente interfaz;

	public StreamAudioCliente() {
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

	@Override
	public void run() {
		escuchar = true;
		try {
			System.out.println("va a ");
			while (escuchar) {
				recibirAudio();
			}
		} catch (Exception e) {
		}
	}

	private void recibirAudio() {
		try {
			byte[] buf = new byte[6000];
			DatagramPacket dp = new DatagramPacket(buf, buf.length);

			ms.receive(dp);
			try {
				byte audioData[] = dp.getData();
				// audioInputStream = AudioSystem.getAudioInputStream(new
				// File("data/game.wav"));

				InputStream byteInputStream = new ByteArrayInputStream(audioData);
				AudioFormat audioFormat = getAudioFormat();
				audioInputStream = new AudioInputStream(byteInputStream, audioFormat,
						audioData.length / audioFormat.getFrameSize());
				DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);

				sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
				sourceDataLine.open(audioFormat);
				// System.out.println(audioFormat);
				sourceDataLine.start();

				playAudio();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (SocketTimeoutException e) {
		} catch (IOException e) {
			System.err.println(e);

		}

	}

	private AudioFormat getAudioFormat() {
		// PCM_SIGNED 44100.0 Hz, 16 bit, mono, 2 bytes/frame, little-endian

		float sampleRate = 44100.0F;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

	private void playAudio() {
		byte[] buffer = new byte[10000];
		try {
			int count;
			while ((count = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
				if (count > 0) {
					sourceDataLine.write(buffer, 0, count);
				}
			}
		} catch (Exception e) {
		}
	}

}
