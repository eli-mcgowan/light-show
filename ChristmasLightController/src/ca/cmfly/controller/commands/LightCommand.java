package ca.cmfly.controller.commands;

public class LightCommand extends Command {
	LightData lightData;

	public LightCommand() {
		super("0");
	}
	
	public LightCommand(String command) {
		super(command);
	}

	public LightCommand(LightData lightData) {
		super("0");
		this.lightData = lightData;
	}

	public LightData getLightData() {
		return lightData;
	}

	public void setLightData(LightData lightData) {
		this.lightData = lightData;
	}

}
