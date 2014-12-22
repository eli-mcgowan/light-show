package ca.cmfly.controller.programs;

import java.io.IOException;
import java.util.Collections;

import ca.cmfly.controller.LightController;
import ca.cmfly.controller.LightId;
import ca.cmfly.controller.commands.LightCommand;
import ca.cmfly.controller.commands.LightData;

public class RandomColorShow extends LightShow {
	private boolean random;

	public RandomColorShow() throws IOException {
		super();
	}

	public RandomColorShow(boolean random) throws IOException {
		super();
		this.random = random;
	}

	public void doit() throws IOException {
		if (random) {
			Collections.shuffle(lightIds);
		}
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
		
		if (random) {
			Collections.shuffle(lightIds);
		}
		for (LightId lightId : lightIds) {
			LightCommand lightCommand = new LightCommand();

			LightData lightData = new LightData(lightId.strandNum, lightId.lightNum, 0, 0, 0, 0, LightController.MAX_INTENSITY);
			lightCommand.setLightData(lightData);

			lc.sendMessage(lightCommand);
			if (color == 13) {
				color = 1;
			} else {
				color++;
			}

		}
	}

	public static void main(String[] args) throws IOException {
		boolean random = true;

		if (args.length > 0) {
			random = Boolean.getBoolean(args[0]);
		}

		RandomColorShow raw = new RandomColorShow(random);
		raw.runLightShow();

	}
}
