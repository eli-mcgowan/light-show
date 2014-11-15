package ca.cmfly.controller.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * This class does nothing at the moment.
 * 
 * @author CMfly
 *
 */
public class LightCommandGroup extends Command{

	List<LightData> lightData;
	
	public LightCommandGroup() {
		super("1");
		lightData = new ArrayList<>();
	}

	public LightCommandGroup(List<LightData> lightData) {
		super();
		this.lightData = lightData;
	}

	public LightCommandGroup(LightData lightdata) {
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
}
