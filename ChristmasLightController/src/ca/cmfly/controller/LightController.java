package ca.cmfly.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ca.cmfly.controller.commands.Command;
import ca.cmfly.controller.commands.FadeCommand;
import ca.cmfly.controller.commands.LightCommand;
import ca.cmfly.controller.commands.LightData;

import com.fasterxml.jackson.core.JsonProcessingException;

public class LightController {

	public static byte MAX_INTENSITY = (byte) 0xcc;
	private final int UDP_TX_PACKET_MAX_SIZE =1500;
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
	}

	/**
	 * We send an off command to each bulb to make sure they are addressed as the lights may turn on after the arduino setup method.
	 * 
	 * @throws IOException
	 */
	public void setAddresses() throws IOException {
		List<LightId> lightIds = getLightIds();
		for (LightId lightId : lightIds) {
			LightData lightdata = new LightData(lightId.strandNum, lightId.lightNum, (byte)ArduinoColor.COLOR_RED, (byte)0, (byte)0, (byte)0, MAX_INTENSITY);
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

			for (byte i = 1; i < 14; i++) {
				byte max = 25;
				if (i == 13) {
					max = 50;
				}
				for (byte j = 0; j < max; j++) {
					lightIds.add(new LightId(i, j));
				}
			}

		} else {
			for (byte i = 0; i < 25; i++) {
				lightIds.add(new LightId((byte)1, i));
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

	public void sendMessage(Command command) throws IOException {
//		ObjectMapper mapper = new ObjectMapper();
//		String message = mapper.writeValueAsString(command);
		for(byte[] message: command.getMessage(UDP_TX_PACKET_MAX_SIZE)){
			this.sendMessage(message);
		}
	}

//	private String sendMessage(String message) throws IOException {
//		// Create a datagram socket, send the packet through it, close it.
//		DatagramSocket dsocket = new DatagramSocket();
//		byte[] receiveData = new byte[1024];
//		// Initialize a datagram packet with data and address and send
//		DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), this.address, this.port);
//		dsocket.send(packet);
//
//		// Receive reply
//		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//		dsocket.receive(receivePacket);
//		String response = new String(receivePacket.getData());
//		// System.out.println("FROM SERVER: " + response);
//		dsocket.close();
//		return response;
//	}

	public String sendMessage(byte[] message) throws IOException {
		// Create a datagram socket, send the packet through it, close it.
		DatagramSocket dsocket = new DatagramSocket();
		byte[] receiveData = new byte[1024];
		// Initialize a datagram packet with data and address and send
		DatagramPacket packet = new DatagramPacket(message, message.length, this.address, this.port);
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

//			sand.randomizeLightColors();
//			sand.lightsOff();
//
//			sand.randomizeLightColors(10, false);
//			sand.lightsOffRandom(10);
//
//			sand.randomizeLightColors();
//			sand.lightsOffWithDelay(10);
			
			// command, #commands, string, bulb, color, r, g, b, intensity
			{
				long start = Calendar.getInstance().getTimeInMillis();
				// command, #commands, string, bulb, color, r, g, b, intensity
				byte[] message = {1,25,
						13,0,3,0,0,0,(byte) MAX_INTENSITY,
						13,1,3,0,0,0,(byte) MAX_INTENSITY,
						13,2,3,0,0,0,(byte) MAX_INTENSITY,
						13,3,3,0,0,0,(byte) MAX_INTENSITY,
						13,4,3,0,0,0,(byte) MAX_INTENSITY,
						13,5,3,0,0,0,(byte) MAX_INTENSITY,
						13,6,3,0,0,0,(byte) MAX_INTENSITY,
						13,7,3,0,0,0,(byte) MAX_INTENSITY,
						13,8,3,0,0,0,(byte) MAX_INTENSITY,
						13,9,3,0,0,0,(byte) MAX_INTENSITY,
						13,10,3,0,0,0,(byte) MAX_INTENSITY,
						13,11,3,0,0,0,(byte) MAX_INTENSITY,
						13,12,3,0,0,0,(byte) MAX_INTENSITY,
						13,13,3,0,0,0,(byte) MAX_INTENSITY,
						13,14,3,0,0,0,(byte) MAX_INTENSITY,
						13,15,3,0,0,0,(byte) MAX_INTENSITY,
						13,16,3,0,0,0,(byte) MAX_INTENSITY,
						13,17,3,0,0,0,(byte) MAX_INTENSITY,
						13,18,3,0,0,0,(byte) MAX_INTENSITY,
						13,19,3,0,0,0,(byte) MAX_INTENSITY,
						13,20,3,0,0,0,(byte) MAX_INTENSITY,
						13,21,3,0,0,0,(byte) MAX_INTENSITY,
						13,22,3,0,0,0,(byte) MAX_INTENSITY,
						13,23,3,0,0,0,(byte) MAX_INTENSITY,
						13,24,3,0,0,0,(byte) MAX_INTENSITY};
				
				System.out.println(sand.sendMessage(message));

				
				System.out.println("25 Light: " + (Calendar.getInstance().getTimeInMillis() - start) + "ms");
			}
				
				
				{
				long start = Calendar.getInstance().getTimeInMillis();
				for(int colour : ArduinoColor.COLORS){
				// command, #commands, string, bulb, color, r, g, b, intensity
				byte[] message = {1,25,
						13,0,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,1,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,2,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,3,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,4,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,5,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,6,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,7,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,8,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,9,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,10,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,11,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,12,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,13,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,14,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,15,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,16,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,17,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,18,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,19,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,20,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,21,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,22,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,23,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,24,(byte) colour,0,0,0,(byte) MAX_INTENSITY};
				
				System.out.println(sand.sendMessage(message));
				}

				
				System.out.println("14 x 25 Light: " + (Calendar.getInstance().getTimeInMillis() - start) + "ms");
				}
				
				{
				long start = Calendar.getInstance().getTimeInMillis();
				for(int colour : ArduinoColor.COLORS){
				// command, #commands, string, bulb, color, r, g, b, intensity
				byte[] message2 = {1,50,
						13,0,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,1,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,2,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,3,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,4,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,5,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,6,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,7,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,8,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,9,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,10,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,11,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,12,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,13,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,14,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,15,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,16,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,17,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,18,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,19,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,20,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,21,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,22,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,23,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,24,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,25,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,26,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,27,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,28,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,29,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,30,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,31,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,32,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,33,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,34,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,35,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,36,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,37,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,38,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,39,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,40,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,41,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,42,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,43,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,44,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,45,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,46,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,47,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,48,(byte) colour,0,0,0,(byte) MAX_INTENSITY,
						13,49,(byte) colour,0,0,0,(byte) MAX_INTENSITY
						};
				
				System.out.println(sand.sendMessage(message2));
				}

				
				System.out.println("14 x 50 Light: " + (Calendar.getInstance().getTimeInMillis() - start) + "ms");
				}

		} catch (Exception e) {
			System.err.println(e);
		}
	}

}
