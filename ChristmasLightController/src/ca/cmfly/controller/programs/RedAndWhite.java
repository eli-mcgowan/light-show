package ca.cmfly.controller.programs;

import java.io.IOException;
import java.util.Collections;

import ca.cmfly.controller.LightController;
import ca.cmfly.controller.LightId;
import ca.cmfly.controller.commands.LightCommand;
import ca.cmfly.controller.commands.LightData;

public class RedAndWhite extends LightShow{
	private boolean random;
	private int fadeDelay;

	public RedAndWhite() throws IOException {
		super();
	}

	public RedAndWhite(boolean random, int fadeDelay) throws IOException {
		super();
		this.random = random;
		this.fadeDelay = fadeDelay;
	}

	public void doit() throws IOException {
		lc.lightsOffFade(fadeDelay);
		if (random) {
			Collections.shuffle(lightIds);
		}
		
		int colour = 11;
		for (LightId lightId : lightIds) {
			LightCommand lightCommand = new LightCommand();

			LightData lightData = new LightData(lightId.strandNum, lightId.lightNum, colour, 0, 0, 0, LightController.MAX_INTENSITY);
			lightCommand.setLightData(lightData);
//
//			ObjectMapper mapper = new ObjectMapper();
//			String message = mapper.writeValueAsString(lightCommand);
//			System.out.println(message);

			lc.sendMessage(lightCommand);
			if (colour == 11) {
				colour = 2;
			} else {
				colour = 11;
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

		RedAndWhite raw = new RedAndWhite(random, fadeDelay);
		raw.runLightShow();

	}
}