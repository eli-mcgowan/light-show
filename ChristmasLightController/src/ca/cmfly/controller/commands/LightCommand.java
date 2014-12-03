package ca.cmfly.controller.commands;

import java.util.ArrayList;
import java.util.List;

public class LightCommand extends Command {
	LightData lightData;

	public LightCommand() {
		super((byte) 0);
	}

	public LightCommand(LightData lightData) {
		this();
		this.lightData = lightData;
	}

	public LightData getLightData() {
		return lightData;
	}

	public void setLightData(LightData lightData) {
		this.lightData = lightData;
	}

	@Override
	public List<byte[]> getMessage(int maxSize) {
		List<byte[]> messageList = new ArrayList<>();
		byte[] message = {getCommand(),1,
				lightData.getString(),
				lightData.getLight(),
				lightData.getColor(),
				lightData.getRed(),
				lightData.getGreen(),
				lightData.getBlue(),
				lightData.getIntensity()};
		messageList.add(message);
		return messageList;
	}

	
}
