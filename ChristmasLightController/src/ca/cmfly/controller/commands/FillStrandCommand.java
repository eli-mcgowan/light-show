package ca.cmfly.controller.commands;

import java.util.ArrayList;
import java.util.List;


public class FillStrandCommand extends Command {
	byte strand;
	byte color;
	byte red;
	byte green;
	byte blue;
	byte intensity; // MAX_byteENSITY = 0xcc -> 204
	
	public FillStrandCommand(byte s, int c, int r, int g, int b, int i){
		super((byte) 3);
		strand = s;
		color = (byte) c;
		red = (byte) r;
		green = (byte) g;
		blue = (byte) b;
		intensity = (byte) i;
	}

	@Override
	public List<byte[]> getMessage(int maxSize) {
		List<byte[]> messageList = new ArrayList<>();
		byte[] message = {getCommand(),
				strand,
				color,
				red,
				green,
				blue,
				intensity};
		messageList.add(message);
		return messageList;
	}
}
