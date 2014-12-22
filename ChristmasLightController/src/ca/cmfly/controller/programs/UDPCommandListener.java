package ca.cmfly.controller.programs;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

public class UDPCommandListener implements Callable<MultiShowCommand>{
	
	private InetAddress address;
	private int port;
	
	public UDPCommandListener(String host, int port) throws UnknownHostException{
		this.address = InetAddress.getByName(host);
		this.port = port;
	}
	
	@Override
	public MultiShowCommand call() throws Exception {
		// Receive reply
		DatagramSocket dsocket = new DatagramSocket(port, address);
		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		dsocket.receive(receivePacket);
		String response = new String(receivePacket.getData());
		// System.out.println("FROM SERVER: " + response);
		dsocket.close();
		
		return MultiShowCommand.fromString(response.trim());
	}

}
