package ca.cmfly.controller.tools;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

import ca.cmfly.controller.LightController;
import ca.cmfly.controller.commands.LightCommand;
import ca.cmfly.controller.commands.LightData;

public class UDPLightCommandListener implements Callable<LightCommand>{
	
	private InetAddress address;
	private int port;
	
	public UDPLightCommandListener(String host, int port) throws UnknownHostException{
		this.address = InetAddress.getByName(host);
		this.port = port;
	}
	
	@Override
	public LightCommand call() throws Exception {
		// Receive reply
		DatagramSocket dsocket = new DatagramSocket(port, address);
		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		dsocket.receive(receivePacket);
		String response = new String(receivePacket.getData());
		// System.out.println("FROM SERVER: " + response);
		dsocket.close();
		
		return parseLightCommand(response.trim());
	}
	
	private LightCommand parseLightCommand(String str){
		String[] tokens = str.split(",");
		
		LightData lightData = new LightData(Byte.parseByte(tokens[0]), Byte.parseByte(tokens[1]), Byte.parseByte(tokens[2]), 0x00, 0x00, 0x00, LightController.MAX_INTENSITY);
		return new LightCommand(lightData);
	}

}
