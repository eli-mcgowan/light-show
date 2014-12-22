package ca.cmfly.controller.tools;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import ca.cmfly.controller.programs.MultiShowRunner;

public class NextShow {

	public static void main(String[] args) throws IOException {
		NextShow.sendMessage("NEXT");
	}

	private static void sendMessage(String message) throws IOException {

		InetAddress address = InetAddress.getByName(MultiShowRunner.UDP_COMMAND_HOST);
		// Create a datagram socket, send the packet through it, close it.
		DatagramSocket dsocket = new DatagramSocket();
		// Initialize a datagram packet with data and address and send
		DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), address, MultiShowRunner.UDP_COMMAND_PORT);
		dsocket.send(packet);

		// System.out.println("FROM SERVER: " + response);
		dsocket.close();
	}
}
