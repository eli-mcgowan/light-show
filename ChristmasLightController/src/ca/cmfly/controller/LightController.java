package ca.cmfly.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LightController {

	public static final int MAX_INTENSITY = 0xcc;
	private static final int PORT = 7654; // 8888
	private static final String HOST_ADDRESS = "localhost"; //"192.168.1.61"
	
	private InetAddress address;
	private int port;

	public LightController(String host, int port) throws UnknownHostException {
		super();
		this.port = port;
		// Get the internet address of the specified host
		this.address = InetAddress.getByName(host);
	}

	/**
	 * Returns a psuedo-random number between min and max, inclusive. The difference between min and max can be at most <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min
	 *            Minimim value
	 * @param max
	 *            Maximim value. Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	private static int randInt(int min, int max) {

		// Usually this can be a field rather than a method variable
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	public void randomizeLightColors() throws JsonProcessingException, IOException {
		for (int i = 1; i < 14; i++) {
			int max = 25;
			if (i == 13) {
				max = 50;
			}
			for (int j = 0; j < max; j++) {
				// LightData lightdata = new LightData(i, j, randInt(0,3), randInt(0,15), randInt(0,15), randInt(0,15), 6);
				LightData lightdata = new LightData(i, j, randInt(1, 13), 0, 0, 0, randInt(1, MAX_INTENSITY));
				this.sendMessage(lightdata);
			}
		}
	}

	public void lightsOnRandom(long delay) throws IOException {

		List<LightId> lightIds = getLightIds();
		Collections.shuffle(lightIds);

		for (LightId lightId : lightIds) {
			LightData lightdata = new LightData(lightId.strandNum, lightId.lightNum, randInt(1, 13), 0, 0, 0, randInt(1, MAX_INTENSITY));
			this.sendMessage(lightdata);
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {

			}
		}
	}

	public void lightsOff() throws IOException {
		List<LightId> lightIds = getLightIds();
		for (LightId lightId : lightIds) {
			LightData lightdata = new LightData(lightId.strandNum, lightId.lightNum, 0, 0, 0, 0, 0);
			this.sendMessage(lightdata);
		}
	}

	public void lightsOffRandom(long delay) throws IOException {

		List<LightId> lightIds = getLightIds();
		Collections.shuffle(lightIds);

		for (LightId lightId : lightIds) {
			LightData lightdata = new LightData(lightId.strandNum, lightId.lightNum, 0, 0, 0, 0, 0);
			this.sendMessage(lightdata);
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {

			}
		}
	}

	private List<LightId> getLightIds() {
		List<LightId> lightIds = new ArrayList<LightId>();
		for (int i = 1; i < 14; i++) {
			int max = 25;
			if (i == 13) {
				max = 50;
			}
			for (int j = 0; j < max; j++) {
				lightIds.add(new LightId(i, j));
			}
		}
		return lightIds;
	}

	public void lightsOffWithDelay(long delay) throws IOException {

		List<LightId> lightIds = getLightIds();
		for (LightId lightId : lightIds) {
			LightData lightdata = new LightData(lightId.strandNum, lightId.lightNum, 0, 0, 0, 0, 0);
			this.sendMessage(lightdata);
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {

			}
		}
	}

	public String sendMessage(LightCommand command) {
		return null;
	}

	public String sendMessage(LightData lightdata) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String message = mapper.writeValueAsString(lightdata);
		System.out.println("Bytes: " + new String(message));
		System.out.println("Size: " + message.length());

		return this.sendMessage(message);
	}

	public String sendMessage(String message) throws IOException {
		// Create a datagram socket, send the packet through it, close it.
		DatagramSocket dsocket = new DatagramSocket();
		byte[] receiveData = new byte[1024];
		// Initialize a datagram packet with data and address and send
		DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), this.address, this.port);
		dsocket.send(packet);

		// Receive reply
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		dsocket.receive(receivePacket);
		String response = new String(receivePacket.getData());
		System.out.println("FROM SERVER: " + response);
		dsocket.close();
		return response;
	}

	public static void main(String args[]) {
		try {
			String host = HOST_ADDRESS;
			int port = PORT;

			LightController sand = new LightController(host, port);

			sand.randomizeLightColors();
			sand.lightsOff();

			sand.lightsOnRandom(10);
			sand.lightsOffRandom(10);

			sand.randomizeLightColors();
			sand.lightsOffWithDelay(10);

		} catch (Exception e) {
			System.err.println(e);
		}
	}

}
