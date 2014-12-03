package ca.cmfly.controller.commands;

import java.util.List;

public abstract class Command {
	private byte command;

	public Command(byte command) {
		super();
		this.command = command;
	}
	
	public Command(int command) {
		super();
		this.command = (byte) command;
	}

	public byte getCommand() {
		return command;
	}

	public void setCommand(byte command) {
		this.command = command;
	}

	/**
	 * 
	 * @param maxSize	The maximum number of bytes allowed in a single byte[]
	 * @return
	 */
	public abstract List<byte[]> getMessage(int maxSize);
}
