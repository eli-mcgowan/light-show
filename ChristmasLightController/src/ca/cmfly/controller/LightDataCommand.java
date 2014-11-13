package ca.cmfly.controller;

public class LightDataCommand {
	int command = 0;
	LightData lightData;

	public LightDataCommand() {
		super();
	}

	public LightDataCommand(LightData lightData) {
		super();
		this.lightData = lightData;
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public LightData getLightData() {
		return lightData;
	}

	public void setLightData(LightData lightData) {
		this.lightData = lightData;
	}

}
