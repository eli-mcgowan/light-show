package ca.cmfly.controller.programs;

import java.io.IOException;
import java.util.Calendar;

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
		
	}

	@Override
	public void doit() throws IOException {
		long start = Calendar.getInstance().getTimeInMillis();
		for (byte i = 1; i <= 14; i++) {
			//long startInner = Calendar.getInstance().getTimeInMillis();
			FillStrandCommand fillCommand = null;
			if(i == (byte)13){
				fillCommand = new FillStrandCommand(i, i, 0, 0, 0, LightController.MAX_INTENSITY, 50);
			} else{
				fillCommand = new FillStrandCommand(i, i, 0, 0, 0, LightController.MAX_INTENSITY, 25);
			}
			lc.sendMessage(fillCommand);
			//System.out.println("ALL Light: " + (Calendar.getInstance().getTimeInMillis() - startInner) + "ms");
		}
		System.out.println("14 x ALL Light: " + (Calendar.getInstance().getTimeInMillis() - start) + "ms");
	}

	public static void main(String[] args) throws IOException {
		FillStrandCommandSampleShow fillCommandSampleShow = new FillStrandCommandSampleShow();
		fillCommandSampleShow.runLightShow();
	}

}
