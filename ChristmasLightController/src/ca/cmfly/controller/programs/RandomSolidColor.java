package ca.cmfly.controller.programs;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import ca.cmfly.controller.LightController;
import ca.cmfly.controller.LightId;
import ca.cmfly.controller.commands.LightCommand;
import ca.cmfly.controller.commands.LightData;

public class RandomSolidColor {
	private LightController lc;
	private boolean random;
	private int fadeDelay;

	public RandomSolidColor() throws IOException {
		super();
		lc = new LightController();
	}

	public RandomSolidColor(boolean random, int fadeDelay) throws IOException {
		super();
		lc = new LightController();
		this.random = random;
		this.fadeDelay = fadeDelay;
	}

	public RandomSolidColor(String host, int port, boolean random, int fadeDelay) throws IOException {
		super();
		lc = new LightController(host, port);
		this.random = random;
		this.fadeDelay = fadeDelay;
	}

	public void doit() throws IOException {
		List<LightId> lightIds = LightController.getLightIds();
		if (random) {
			Collections.shuffle(lightIds);
		}

		while (true) {
			for (int color = 1; color <= 13; color++) {
				lc.lightsOffFade(fadeDelay);

				for (LightId lightId : lightIds) {
					LightCommand lightCommand = new LightCommand();

					LightData lightData = new LightData(lightId.strandNum, lightId.lightNum, color, 0, 0, 0, LightController.MAX_INTENSITY);
					lightCommand.setLightData(lightData);

					lc.sendMessage(lightCommand);

				}
			}
			lc.lightsOffFade(fadeDelay);
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

			}
			
		}
	}

	public static void main(String[] args) throws IOException {
		boolean random = true;
		int fadeDelay = 25;

		if (args.length > 0) {
			random = Boolean.getBoolean(args[0]);
			fadeDelay = Integer.parseInt(args[1]);
		}

		RandomSolidColor raw = new RandomSolidColor(random, fadeDelay);
		raw.doit();

	}
}
