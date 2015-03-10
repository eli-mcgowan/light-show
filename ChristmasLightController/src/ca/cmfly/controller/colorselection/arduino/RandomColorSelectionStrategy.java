package ca.cmfly.controller.colorselection.arduino;

import ca.cmfly.controller.LightController;

public class RandomColorSelectionStrategy implements ArduinoColorSelectionStrategy {

	// FIXME: remove random color from lightController and put it in here?
	private LightController lc;
	
	public RandomColorSelectionStrategy(LightController lc){
		this.lc = lc;
	}
	
	@Override
	public int getNextColor() {
		return lc.getRandomColor();
	}

}
