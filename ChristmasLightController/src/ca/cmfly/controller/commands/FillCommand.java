package ca.cmfly.controller.commands;


public class FillCommand extends LightCommandGroup {

	public FillCommand(int s, int c, int r, int g, int b, int i, int numLights){
		if(numLights > 25){
			throw new RuntimeException("Max 25 bulbs supported by LightCommandGroup");
		}
		for(int lightIndex=0; lightIndex<numLights; lightIndex++){
			addLightData(new LightData(s, lightIndex, c, r, g, b, i));
		}
	}
}
