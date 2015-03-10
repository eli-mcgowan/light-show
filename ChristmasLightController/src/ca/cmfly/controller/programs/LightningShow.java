package ca.cmfly.controller.programs;

import java.io.IOException;

import ca.cmfly.controller.ArduinoColor;

/**
 * Yellow shoots from the ends of the lights down toward the trunk extremely quickly.
 *
 */
public class LightningShow extends SnowfallShow {

	public LightningShow() throws IOException {
		super(7, 10, 2, ArduinoColor.COLOR_YELLOW, ArduinoColor.COLOR_BLACK);
	}

	
	public static void main(String[] args) throws IOException {
		LightningShow show = new LightningShow();
		show.runLightShow();
	}
}
