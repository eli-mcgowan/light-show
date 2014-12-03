package ca.cmfly.controller.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * This class does nothing at the moment.
 * 
 * @author CMfly
 *
 */
public class LightCommandGroup extends Command {

	List<LightData> lightData;

	public LightCommandGroup(List<LightData> lightData) {
		super(1);
		this.lightData = lightData;
	}

	public LightCommandGroup() {
		this(new ArrayList<LightData>());
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

	@Override
	public List<byte[]> getMessage(int maxSize) {
		// I'm sorry
		final int HEADER_SIZE = 2;
		final int LIGHT_DATA_SIZE = 7;
		final int MAX_NUM_ELEMENTS = 200;
		List<byte[]> messageList = new ArrayList<>();
		
		
		int currentNumLightDatas = 0;
		List<Byte> lightDataBytes = new ArrayList<>();
		for (LightData lightDatum : lightData) {
			currentNumLightDatas++;
			lightDataBytes.add(lightDatum.getString());
			lightDataBytes.add(lightDatum.getLight());
			lightDataBytes.add(lightDatum.getColor());
			lightDataBytes.add(lightDatum.getRed());
			lightDataBytes.add(lightDatum.getGreen());
			lightDataBytes.add(lightDatum.getBlue());
			lightDataBytes.add(lightDatum.getIntensity());
			if(currentNumLightDatas >= MAX_NUM_ELEMENTS){
				byte[] message = new byte[HEADER_SIZE + LIGHT_DATA_SIZE * MAX_NUM_ELEMENTS];
				message[0] = getCommand();
				message[1] = (byte) MAX_NUM_ELEMENTS;
				int thisIsHacky = 2;
				for(Byte lightDataByte: lightDataBytes){
					message[thisIsHacky++] = lightDataByte;
				}
				messageList.add(message);
				
				currentNumLightDatas = 0;
				lightDataBytes = new ArrayList<>();
			}
		}

		if(currentNumLightDatas > 0){
			byte[] message = new byte[HEADER_SIZE + LIGHT_DATA_SIZE * currentNumLightDatas];
			message[0] = getCommand();
			message[1] = (byte) currentNumLightDatas;
			int thisIsHacky = 2;
			for(Byte lightDataByte: lightDataBytes){
				message[thisIsHacky++] = lightDataByte;
			}
			messageList.add(message);
		}
		
		return messageList;
	}
	
	
	
}
