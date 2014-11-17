package ca.cmfly.controller.programs;

import java.io.IOException;

import ca.cmfly.controller.LightController;
import ca.cmfly.controller.commands.FillCommand;

/**
 * Demonstrates how to use the FillCommand.
 *
 */
public class FillCommandSampleShow extends LightShow {

	public FillCommandSampleShow() throws IOException {
		super();
	}

	@Override
	public void init() throws IOException {
		for (int i = 1; i < 14; i++) {
			FillCommand fillCommand = new FillCommand(i, i, 0, 0, 0, LightController.MAX_INTENSITY, 25);
			lc.sendMessage(fillCommand);
		}
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
