package ca.cmfly.controller.commands;


public class FillStrandCommand extends LightCommandGroup {

	public FillStrandCommand(byte s, int c, int r, int g, int b, int i, int numLights){
		for(byte lightIndex=0; lightIndex<numLights; lightIndex++){
			addLightData(new LightData(s, lightIndex, c, r, g, b, i));
		}
	}
}
