package ca.cmfly.controller.programs;

import java.io.IOException;
import java.util.Calendar;

import ca.cmfly.controller.ArduinoColor;
import ca.cmfly.controller.ChristmasColor;
import ca.cmfly.controller.LightController;
import ca.cmfly.controller.commands.FillStrandCommand;

public class FillAllStrandsShow extends LightShow {
	
	public FillAllStrandsShow() throws IOException {
		super();
	}

	@Override
	public void init() throws IOException {
		
	}

	@Override
	public void doit() throws IOException {
		long start = Calendar.getInstance().getTimeInMillis();
		for (int color: ChristmasColor.COLORS) {
			FillStrandCommand fillCommand = null;
			fillCommand = new FillStrandCommand((byte) 0, color, 0, 0, 0, LightController.MAX_INTENSITY);
			lc.sendMessage(fillCommand);
		}
		System.out.println("14 x ALL Light: " + (Calendar.getInstance().getTimeInMillis() - start) + "ms");
	}

	public static void main(String[] args) throws IOException {
		FillAllStrandsShow fillAllStrandsShow = new FillAllStrandsShow();
		fillAllStrandsShow.runLightShow();
	}

}
