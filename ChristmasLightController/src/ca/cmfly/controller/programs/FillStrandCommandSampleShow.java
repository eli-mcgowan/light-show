package ca.cmfly.controller.programs;

import java.io.IOException;

import ca.cmfly.controller.ArduinoColor;
import ca.cmfly.controller.LightController;
import ca.cmfly.controller.commands.FillStrandCommand;

/**
 * Demonstrates how to use the FillCommand.
 *
 */
public class FillStrandCommandSampleShow extends LightShow {

	public FillStrandCommandSampleShow() throws IOException {
		super();
	}

	@Override
	public void init() throws IOException {
		for (byte i = 1; i < 14; i++) {
			FillStrandCommand fillCommand = null;
			if(i == (byte)13){
				fillCommand = new FillStrandCommand(i, ArduinoColor.COLOR_CYAN, 0, 0, 0, LightController.MAX_INTENSITY, 50);
			} else{
				fillCommand = new FillStrandCommand(i, ArduinoColor.COLOR_CYAN, 0, 0, 0, LightController.MAX_INTENSITY, 25);
			}
			lc.sendMessage(fillCommand);
		}
	}

	@Override
	public void doit() throws IOException {
		keepGoing = false;
	}

	public static void main(String[] args) throws IOException {
		FillStrandCommandSampleShow fillCommandSampleShow = new FillStrandCommandSampleShow();
		fillCommandSampleShow.runLightShow();
	}

}
