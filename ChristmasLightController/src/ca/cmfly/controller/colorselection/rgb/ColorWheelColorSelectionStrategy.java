package ca.cmfly.controller.colorselection.rgb;

import ca.cmfly.controller.RGB;
import ca.cmfly.controller.tools.ColorWheel;

public class ColorWheelColorSelectionStrategy implements RGBColorSelectionStrategy {

	private int currentColor;
	private int colorStep;
	
	public ColorWheelColorSelectionStrategy(int colorStep){
		this.colorStep = colorStep;
		currentColor = 0;
	}
	
	@Override
	public RGB getNextColor() {
		RGB rgb = ColorWheel.wheel(currentColor);
		currentColor+=colorStep;
		return rgb;
	}

}
