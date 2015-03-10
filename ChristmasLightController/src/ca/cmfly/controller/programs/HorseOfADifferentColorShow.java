package ca.cmfly.controller.programs;

import java.io.IOException;

import ca.cmfly.controller.ArduinoColor;
import ca.cmfly.controller.LightController;
import ca.cmfly.controller.RGB;
import ca.cmfly.controller.colorselection.rgb.ColorWheelColorSelectionStrategy;
import ca.cmfly.controller.colorselection.rgb.RGBColorSelectionStrategy;
import ca.cmfly.controller.commands.FillStrandCommand;

/**
 * FIlls all lights rotating through the color wheel.
 * <br />
 * Options:
 * <ul>
 * <li>Increasing the COLOR_STEP will increase the speed the color changes. Note that faster speeds may look choppy.</li>
 * </ul>
 *
 */
public class HorseOfADifferentColorShow extends LightShow {
	private static final int LIGHT_OFF_COLOR = ArduinoColor.COLOR_BLACK;
	private static final int COLOR_STEP = 4;
	
	RGBColorSelectionStrategy colorSelectionStrategy;

	public HorseOfADifferentColorShow() throws IOException {
		super();
		colorSelectionStrategy = new ColorWheelColorSelectionStrategy(COLOR_STEP);
	}

	@Override
	public void init() throws IOException {
		FillStrandCommand fillCommand = null;
		fillCommand = new FillStrandCommand((byte) 0, LIGHT_OFF_COLOR, 0, 0, 0, LightController.MAX_INTENSITY);
		lc.sendMessage(fillCommand);
	}



	@Override
	public void doit() throws IOException {
		RGB rgb = colorSelectionStrategy.getNextColor();
		
		FillStrandCommand fillCommand = null;
		fillCommand = new FillStrandCommand((byte) 0, 0, rgb.getArduinoRed(), rgb.getArduinoGreen(), rgb.getArduinoBlue(), LightController.MAX_INTENSITY);
		lc.sendMessage(fillCommand);
	}
	
	
	
	public static void main(String[] args) throws IOException {
		HorseOfADifferentColorShow horseOfADifferentColorShow = new HorseOfADifferentColorShow();
		horseOfADifferentColorShow.runLightShow();
	}

}
