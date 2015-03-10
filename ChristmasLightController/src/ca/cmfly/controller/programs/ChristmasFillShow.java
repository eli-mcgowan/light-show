package ca.cmfly.controller.programs;

import java.io.IOException;
import java.util.Calendar;

import ca.cmfly.controller.ArduinoColor;
import ca.cmfly.controller.ChristmasColor;
import ca.cmfly.controller.LightController;
import ca.cmfly.controller.commands.FillStrandCommand;

/**
 * Alternates between white, red and green fill of all lights.
 * <br />
 * Options:
 * <ul>
 * <li>Can set the lights to turn black between colors<li>
 * </ul>
 *
 */
public class ChristmasFillShow extends LightShow {
	static final boolean BLACK_BETWEEN_COLORS = false;
	static int black_time = 0; 

public ChristmasFillShow() throws IOException {
	super();
}

@Override
public void init() throws IOException {
	
}

@Override
public void doit() throws IOException {
	long start = Calendar.getInstance().getTimeInMillis();
	for (int color: ChristmasColor.COLORS) {
		if(BLACK_BETWEEN_COLORS){
			black_time++;
			//long startInner = Calendar.getInstance().getTimeInMillis();
			if(black_time % 1 == 0){
				black_time = 0;
				FillStrandCommand fillCommand = null;
				fillCommand = new FillStrandCommand((byte) 0, ArduinoColor.COLOR_BLACK, 0, 0, 0, 0);
				lc.sendMessage(fillCommand);
				try {
					Thread.sleep(500); // FIXME: Eliminate sleeps!
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		FillStrandCommand fillCommand = null;
		fillCommand = new FillStrandCommand((byte) 0, color, 0, 0, 0, LightController.MAX_INTENSITY);
		lc.sendMessage(fillCommand);
		try {
			Thread.sleep(2000); // FIXME: Eliminate sleeps!
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	System.out.println("14 x ALL Light: " + (Calendar.getInstance().getTimeInMillis() - start) + "ms");
	
}

public static void main(String[] args) throws IOException {
	ChristmasFillShow christmasFillShow = new ChristmasFillShow();
	christmasFillShow.runLightShow();
}

}
