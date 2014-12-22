package ca.cmfly.controller.programs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ca.cmfly.controller.ArduinoColor;
import ca.cmfly.controller.LightController;
import ca.cmfly.controller.LightId;
import ca.cmfly.controller.commands.LightCommand;
import ca.cmfly.controller.commands.LightData;


public class Twinkle extends LightShow {
	
	class LightTwinkle{
		public LightId lightId;
		public long twinkleDownTime;
	}
	
	private int maxTimeBetweenTwinkles;
	private long nextTwinkleTimeInMillis;
	private List<LightTwinkle> lightTwinkles;

	public Twinkle() throws IOException {
		super();
		lightTwinkles = new ArrayList<LightTwinkle>();
		maxTimeBetweenTwinkles = 250;
	}

	@Override
	public void init() throws IOException {
		for (LightId lightId : lightIds) {
			LightCommand lightCommand = new LightCommand();

			LightData lightData = new LightData(lightId.strandNum, lightId.lightNum, ArduinoColor.COLOR_WARMWHITE, 0, 0, 0, 80);
			lightCommand.setLightData(lightData);

			lc.sendMessage(lightCommand);

		}
		
		nextTwinkleTimeInMillis = System.currentTimeMillis();
	}
	
	@Override
	public void doit() throws IOException {
		long currentTimeInMillis = System.currentTimeMillis();
		
		if(currentTimeInMillis > nextTwinkleTimeInMillis && moreTwinklesRequired()){
			Random rand = new Random();
			LightId lightId = lightIds.get(rand.nextInt(lightIds.size()));
			
			LightTwinkle lightTwinkle = new LightTwinkle();
			lightTwinkle.lightId = lightId;
			lightTwinkle.twinkleDownTime = currentTimeInMillis + 50;
			lightTwinkles.add(lightTwinkle);
	
			LightCommand lightCommand = new LightCommand();
			LightData lightData = new LightData(lightId.strandNum, lightId.lightNum, ArduinoColor.COLOR_WARMWHITE, 0, 0, 0, LightController.MAX_INTENSITY);
			lightCommand.setLightData(lightData);
			lc.sendMessage(lightCommand);
			nextTwinkleTimeInMillis = currentTimeInMillis + rand.nextInt(maxTimeBetweenTwinkles);
		}
		
		List<LightTwinkle> completedLightTwinkles = new ArrayList<LightTwinkle>();
		for(LightTwinkle lightTwinkle: lightTwinkles){
			if(currentTimeInMillis > lightTwinkle.twinkleDownTime){
				// return to normal
				LightId lightId = lightTwinkle.lightId;
				LightCommand lightCommand2 = new LightCommand();
				LightData lightData2 = new LightData(lightId.strandNum, lightId.lightNum, ArduinoColor.COLOR_WARMWHITE, 0, 0, 0, 80);
				lightCommand2.setLightData(lightData2);
				lc.sendMessage(lightCommand2);
				completedLightTwinkles.add(lightTwinkle);
			}
		}
		
		// Clean up completedTwinles
		lightTwinkles.removeAll(completedLightTwinkles);
	}
	
	private boolean moreTwinklesRequired() {
		return lightTwinkles.size() < 20;
	}

	public static void main(String[] args) throws IOException {
		Twinkle twinkle = new Twinkle();
		twinkle.runLightShow();
	}

}
