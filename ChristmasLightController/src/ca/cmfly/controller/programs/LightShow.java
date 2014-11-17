package ca.cmfly.controller.programs;

import java.io.IOException;
import java.util.List;

import ca.cmfly.controller.LightController;
import ca.cmfly.controller.LightId;

public abstract class LightShow {
	protected LightController lc;
	protected boolean keepGoing;
	protected List<LightId> lightIds;
	
	public LightShow() throws IOException{
		this.lc = new LightController();
		this.keepGoing = true;
		this.lightIds = LightController.getLightIds();
	}
	
	public void runLightShow() throws IOException {
		init();
		while(keepGoing){
			doit();
		}
	}
	
	public abstract void doit() throws IOException;
	
	public void init() throws IOException{
		// Empty implementation allowed
	}
	
}
