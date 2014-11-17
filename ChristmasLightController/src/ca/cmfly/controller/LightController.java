package ca.cmfly.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ca.cmfly.controller.commands.Command;
import ca.cmfly.controller.commands.FadeCommand;
import ca.cmfly.controller.commands.LightCommand;
import ca.cmfly.controller.commands.LightData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LightController {

	public static int MAX_INTENSITY = 0xcc;
	private InetAddress address;
	private int port;

	public LightController() throws IOException {
		this(ConnectionProperties.getHost(), ConnectionProperties.getPort());
	}

	public LightController(String host, int port) throws IOException {
		super();
		this.port = port;
		// Get the internet address of the specified host
		this.address = InetAddress.getByName(host);

		setAddresses();
	}

	/**
	 * We send an off command to each bulb to make sure they are addressed as the lights may turn on after the arduino setup method.
	 * 
	 * @throws IOException
	 */
	private void setAddresses() throws IOException {
		List<LightId> lightIds = getLightIds();
		for (LightId lightId : lightIds) {
			LightData lightdata = new LightData(lightId.strandNum, lightId.lightNum, ArduinoColor.COLOR_RED, 0, 0, 0, MAX_INTENSITY);
			this.sendMessage(new LightCommand(lightdata));
		}
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
		randomizeLightColors(0, false);
	}

	public void randomizeLightColors(long delay, boolean shuffle) throws IOException {

		List<LightId> lightIds = getLightIds();
		if (shuffle) {
			Collections.shuffle(lightIds);
		}

		for (LightId lightId : lightIds) {
			LightData lightdata = new LightData(lightId.strandNum, lightId.lightNum, randInt(1, 13), 0, 0, 0, randInt(1, MAX_INTENSITY));
			this.sendMessage(new LightCommand(lightdata));
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
			this.sendMessage(new LightCommand(lightdata));
		}
	}

	public void lightsOffQuickly() throws IOException {
		FadeCommand fc = new FadeCommand();
		this.sendMessage(fc);
	}

	public void lightsOffFade(int fadeDelay) throws IOException {
		FadeCommand fc = new FadeCommand();
		fc.setDelay(fadeDelay);
		this.sendMessage(fc);
	}

	public void lightsOffRandom(long delay) throws IOException {

		List<LightId> lightIds = getLightIds();
		Collections.shuffle(lightIds);

		for (LightId lightId : lightIds) {
			LightData lightdata = new LightData(lightId.strandNum, lightId.lightNum, 0, 0, 0, 0, 0);
			this.sendMessage(new LightCommand(lightdata));
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {

			}
		}
	}

	public static List<LightId> getLightIds() {
		List<LightId> lightIds = new ArrayList<LightId>();
		if (ConnectionProperties.isLive()) {

			for (int i = 1; i < 14; i++) {
				int max = 25;
				if (i == 13) {
					max = 50;
				}
				for (int j = 0; j < max; j++) {
					lightIds.add(new LightId(i, j));
				}
			}

		} else {
			for (int i = 0; i < 25; i++) {
				lightIds.add(new LightId(1, i));
			}
		}
		return lightIds;
	}

	public void lightsOffWithDelay(long delay) throws IOException {

		List<LightId> lightIds = getLightIds();
		for (LightId lightId : lightIds) {
			LightData lightdata = new LightData(lightId.strandNum, lightId.lightNum, 0, 0, 0, 0, 0);
			this.sendMessage(new LightCommand(lightdata));
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {

			}
		}
	}

	public String sendMessage(Command command) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String message = mapper.writeValueAsString(command);
		return this.sendMessage(message);
	}

	private String sendMessage(String message) throws IOException {
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
		// System.out.println("FROM SERVER: " + response);
		dsocket.close();
		return response;
	}

	public static void main(String args[]) {
		try {
			LightController sand = new LightController();

			sand.randomizeLightColors();
			sand.lightsOff();

			sand.randomizeLightColors(10, false);
			sand.lightsOffRandom(10);

			sand.randomizeLightColors();
			sand.lightsOffWithDelay(10);

		} catch (Exception e) {
			System.err.println(e);
		}
	}

}
