package ca.cmfly.controller.commands;

import java.util.ArrayList;
import java.util.List;

public class FadeCommand extends Command {

	private int delay;

	public FadeCommand(int delay) {
		super(2);
		this.delay = delay;
	}
	
	public FadeCommand() {
		this(0);
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	@Override
	public List<byte[]> getMessage(int maxSize) {
		List<byte[]> messageList = new ArrayList<>();
		
		byte[] message = {getCommand(),(byte) delay};
		
		messageList.add(message);
		return messageList;
	}

}
