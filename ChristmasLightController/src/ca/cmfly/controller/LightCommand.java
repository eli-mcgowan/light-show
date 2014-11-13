package ca.cmfly.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * This class does nothing at the moment.
 * 
 * @author CMfly
 *
 */
public class LightCommand {

	List<LightData> lightData;
	int command = 1;

	public LightCommand() {
		super();
		lightData = new ArrayList<>();
	}

	public LightCommand(List<LightData> lightData) {
		super();
		this.lightData = lightData;
	}

	public LightCommand(LightData lightdata) {
		this.lightData = new ArrayList<>();
		this.lightData.add(lightdata);
	}

	public List<LightData> getLightData() {
		return lightData;
	}

	public void setLightData(List<LightData> lightData) {
		this.lightData = lightData;
	}

	public void addLightData(LightData lightData) {
		if (this.lightData == null) {
			this.lightData = new ArrayList<>();
		}
		this.lightData.add(lightData);
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

}
