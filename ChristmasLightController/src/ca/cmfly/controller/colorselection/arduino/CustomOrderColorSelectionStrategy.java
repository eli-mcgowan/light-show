package ca.cmfly.controller.colorselection.arduino;

import java.util.List;

public class CustomOrderColorSelectionStrategy implements ArduinoColorSelectionStrategy {

	private List<Integer> colors;
	private int nextColorIndex;
	
	public CustomOrderColorSelectionStrategy(List<Integer> colors){
		this.colors = colors;
		this.nextColorIndex = 0;
	}
	
	@Override
	public int getNextColor() {
		int nextColor = colors.get(nextColorIndex);
		nextColorIndex ++;
		if(nextColorIndex >= colors.size()){
			nextColorIndex = 0;
		}
		return nextColor;
	}

}
