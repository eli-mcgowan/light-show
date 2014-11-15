package ca.cmfly.controller.commands;

public class FadeCommand extends Command {

	private int delay;

	public FadeCommand() {
		super("2");
		this.delay = 0;
	}

	public FadeCommand(int delay) {
		super("2");
		this.delay = delay;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

}
