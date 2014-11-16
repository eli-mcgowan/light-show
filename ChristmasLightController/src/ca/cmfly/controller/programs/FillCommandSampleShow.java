package ca.cmfly.controller.programs;

import java.io.IOException;
import java.net.UnknownHostException;

import ca.cmfly.controller.ArduinoColor;
import ca.cmfly.controller.LightController;
import ca.cmfly.controller.commands.FillCommand;

/**
 * Demonstrates how to use the FillCommand.
 *
 */
public class FillCommandSampleShow extends LightShow {

	public FillCommandSampleShow() throws UnknownHostException {
		super();
	}

	@Override
	public void init() throws IOException {
		FillCommand fillCommand = new FillCommand(1, ArduinoColor.COLOR_RED, 0, 0, 0, LightController.MAX_INTENSITY, 25);
		lc.sendMessage(fillCommand);
	}
	
	@Override
	public void doit() throws IOException {
		keepGoing = false;
	}
	

	public static void main(String[] args) throws IOException {
		FillCommandSampleShow fillCommandSampleShow = new FillCommandSampleShow();
		fillCommandSampleShow.runLightShow();
	}

}
