package ca.cmfly.controller.programs;

import java.io.IOException;

import ca.cmfly.controller.ArduinoColor;
import ca.cmfly.controller.LightController;
import ca.cmfly.controller.RGB;
import ca.cmfly.controller.commands.FillStrandCommand;

public class HorseOfADifferentColorShow extends LightShow {
	private static final int LIGHT_OFF_COLOR = ArduinoColor.COLOR_BLACK;
	private static final int COLOR_STEP = 2;
	
	int currentColor = 0;

	public HorseOfADifferentColorShow() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	@Override
	public void init() throws IOException {
		FillStrandCommand fillCommand = null;
		fillCommand = new FillStrandCommand((byte) 0, LIGHT_OFF_COLOR, 0, 0, 0, LightController.MAX_INTENSITY);
		lc.sendMessage(fillCommand);
	}



	@Override
	public void doit() throws IOException {
		RGB rgb = Wheel(currentColor);
		
		FillStrandCommand fillCommand = null;
		fillCommand = new FillStrandCommand((byte) 0, 0, rgb.getArduinoRed(), rgb.getArduinoGreen(), rgb.getArduinoBlue(), LightController.MAX_INTENSITY);
		lc.sendMessage(fillCommand);
		
		currentColor+=COLOR_STEP;
		currentColor = currentColor % 255;
	}
	
	// Input a value 0 to 255 to get a color value.
	// The colours are a transition r - g - b - back to r.
	RGB Wheel(int WheelPos) {
		WheelPos = 255 - WheelPos;
		if (WheelPos < 85) {
			return new RGB(255 - WheelPos * 3, 0, WheelPos * 3);
		} else if (WheelPos < 170) {
			WheelPos -= 85;
			return new RGB(0, WheelPos * 3, 255 - WheelPos * 3);
		} else {
			WheelPos -= 170;
			return new RGB(WheelPos * 3, 255 - WheelPos * 3, 0);
		}
	}
	
	public static void main(String[] args) throws IOException {
		HorseOfADifferentColorShow horseOfADifferentColorShow = new HorseOfADifferentColorShow();
		horseOfADifferentColorShow.runLightShow();
	}

}
