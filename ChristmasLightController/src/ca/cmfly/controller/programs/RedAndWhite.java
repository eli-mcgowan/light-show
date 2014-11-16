package ca.cmfly.controller.programs;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import ca.cmfly.controller.ConnectionConstants;
import ca.cmfly.controller.LightController;
import ca.cmfly.controller.LightId;
import ca.cmfly.controller.commands.LightCommand;
import ca.cmfly.controller.commands.LightData;

public class RedAndWhite {
	private LightController lc;
	private boolean live = true;
	private boolean random;
	private int fadeDelay;

	public RedAndWhite() throws IOException {
		super();
		lc = new LightController();
	}

	public RedAndWhite(boolean random, int fadeDelay) throws IOException {
		super();
		lc = new LightController();
		this.random = random;
		this.fadeDelay = fadeDelay;
	}

	public RedAndWhite(String host, int port, boolean random, int fadeDelay) throws IOException {
		super();
		lc = new LightController(host, port);
		this.random = random;
		this.fadeDelay = fadeDelay;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public void doit() throws IOException {
		List<LightId> lightIds = LightController.getLightIds(live);
		if (random) {
			Collections.shuffle(lightIds);
		}
		while (true) {
			lc.lightsOffFade(fadeDelay);

			int colour = 11;
			for (LightId lightId : lightIds) {
				LightCommand lightCommand = new LightCommand();

				LightData lightData = new LightData(lightId.strandNum, lightId.lightNum, colour, 0, 0, 0, LightController.MAX_INTENSITY);
				lightCommand.setLightData(lightData);

//				ObjectMapper mapper = new ObjectMapper();
//				String message = mapper.writeValueAsString(lightCommand);
//				System.out.println(message);

				lc.sendMessage(lightCommand);
				if (colour == 11) {
					colour = 2;
				} else {
					colour = 11;
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		String host = ConnectionConstants.getHost();
		int port = ConnectionConstants.getPort();
		boolean random = true;
		int fadeDelay = 25;
		boolean live = true;

		if (args.length > 0) {
			host = args[0];
			port = Integer.parseInt(args[1]);
			random = Boolean.getBoolean(args[2]);
			fadeDelay = Integer.parseInt(args[3]);
			live = Boolean.getBoolean(args[4]);
		}

		RedAndWhite raw = new RedAndWhite(host, port, random, fadeDelay);
		raw.setLive(live);
		raw.doit();

	}
}