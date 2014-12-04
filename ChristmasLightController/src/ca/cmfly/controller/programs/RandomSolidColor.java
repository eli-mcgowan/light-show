package ca.cmfly.controller.programs;

import java.io.IOException;
import java.util.Collections;

import ca.cmfly.controller.LightController;
import ca.cmfly.controller.LightId;
import ca.cmfly.controller.commands.LightCommand;
import ca.cmfly.controller.commands.LightData;

public class RandomSolidColor extends LightShow {
	private boolean random;
	private int fadeDelay;

	public RandomSolidColor() throws IOException {
		super();
	}

	public RandomSolidColor(boolean random, int fadeDelay) throws IOException {
		super();
		this.random = random;
		this.fadeDelay = fadeDelay;
	}

	public void doit() throws IOException {
		if (random) {
			Collections.shuffle(lightIds);
		}
//		for (int color = 1; color <= 13; color++) {
//			lc.lightsOffFade(fadeDelay);
//
//			for (LightId lightId : lightIds) {
//				LightCommand lightCommand = new LightCommand();
//
//				LightData lightData = new LightData(lightId.strandNum, lightId.lightNum, color, 0, 0, 0, LightController.MAX_INTENSITY);
//				lightCommand.setLightData(lightData);
//
//				lc.sendMessage(lightCommand);
//
//			}
//		}
		//lc.lightsOffFade(fadeDelay);
		int color = 1;
		for (LightId lightId : lightIds) {
			LightCommand lightCommand = new LightCommand();

			LightData lightData = new LightData(lightId.strandNum, lightId.lightNum, color, 0, 0, 0, LightController.MAX_INTENSITY);
			lightCommand.setLightData(lightData);

			lc.sendMessage(lightCommand);
			if (color == 13) {
				color = 1;
			} else {
				color++;
			}
			if(color == 8 || color == 10) {
				color++;
			}

		}
		
//		if (random) {
//			Collections.shuffle(lightIds);
//		}
//		for (LightId lightId : lightIds) {
//			LightCommand lightCommand = new LightCommand();
//
//			LightData lightData = new LightData(lightId.strandNum, lightId.lightNum, 0, 0, 0, 0, LightController.MAX_INTENSITY);
//			lightCommand.setLightData(lightData);
//
//			lc.sendMessage(lightCommand);
//			if (color == 13) {
//				color = 1;
//			} else {
//				color++;
//			}
//
//		}
	}

	public static void main(String[] args) throws IOException {
		boolean random = true;
		int fadeDelay = 25;

		if (args.length > 0) {
			random = Boolean.getBoolean(args[0]);
			fadeDelay = Integer.parseInt(args[1]);
		}

		RandomSolidColor raw = new RandomSolidColor(random, fadeDelay);
		raw.runLightShow();

	}
}
