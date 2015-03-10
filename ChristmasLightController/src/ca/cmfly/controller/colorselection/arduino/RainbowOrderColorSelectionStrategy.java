package ca.cmfly.controller.colorselection.arduino;

import java.util.ArrayList;
import java.util.List;

import ca.cmfly.controller.ArduinoColor;

public class RainbowOrderColorSelectionStrategy implements ArduinoColorSelectionStrategy {

	private CustomOrderColorSelectionStrategy customOrderColorSelectionStrategy; 
	
	public RainbowOrderColorSelectionStrategy(){
		List<Integer> rainbowOrder = new ArrayList<>();
		//ROYGBIV
		rainbowOrder.add(ArduinoColor.COLOR_RED);
		rainbowOrder.add(ArduinoColor.COLOR_ORANGE);
		rainbowOrder.add(ArduinoColor.COLOR_YELLOW);
		rainbowOrder.add(ArduinoColor.COLOR_GREEN);
		rainbowOrder.add(ArduinoColor.COLOR_BLUE);
		rainbowOrder.add(ArduinoColor.COLOR_VIOLET);
		customOrderColorSelectionStrategy = new CustomOrderColorSelectionStrategy(rainbowOrder);
	}
	
	@Override
	public int getNextColor() {
		return customOrderColorSelectionStrategy.getNextColor();
	}

}
