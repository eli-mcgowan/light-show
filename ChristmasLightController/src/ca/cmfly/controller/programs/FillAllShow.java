package ca.cmfly.controller.programs;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import ca.cmfly.controller.ArduinoColor;
import ca.cmfly.controller.LightController;
import ca.cmfly.controller.LightId;
import ca.cmfly.controller.commands.LightCommandGroup;
import ca.cmfly.controller.commands.LightData;

/**
 * Fills all lights with the same color. Loops through Arduino colors.
 * <br />
 * Set to stop after first pass.
 *
 */
public class FillAllShow extends LightShow {

	public FillAllShow() throws IOException {
		super();
	}

	@Override
	public void init() throws IOException {
		fillAllColor(ArduinoColor.COLOR_PURPLE);
	}

	@Override
	public void doit() throws IOException {
		for(int color: ArduinoColor.COLORS){
			fillAllColor(color);
			try {
				// FIXME: Eliminate Thread sleeps from doit loop
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		keepGoing = false;
	}
	
	private void  fillAllColor(int color) throws IOException{
		LightCommandGroup lightCommandGroup = new LightCommandGroup();
		List<LightId> lightIds = LightController.getLightIds();
		Collections.shuffle(lightIds);
		for(LightId lightId: lightIds){
			LightData lightData = new LightData(lightId.strandNum, lightId.lightNum, color, 0, 0, 0, LightController.MAX_INTENSITY);
			lightCommandGroup.addLightData(lightData);
		}
		lc.sendMessage(lightCommandGroup);
	}

	public static void main(String[] args) throws IOException {
		FillAllShow fillAllShow = new FillAllShow();
		fillAllShow.runLightShow();
	}

}
