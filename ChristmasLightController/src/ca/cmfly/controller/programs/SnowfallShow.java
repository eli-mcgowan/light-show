package ca.cmfly.controller.programs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.cmfly.controller.ArduinoColor;
import ca.cmfly.controller.LightController;
import ca.cmfly.controller.commands.FillStrandCommand;
import ca.cmfly.controller.commands.LightCommandGroup;
import ca.cmfly.controller.commands.LightData;

/**
 * 1. Pick a strand (not trunk)
 * 2. Light 3 lights at the end of the strand
 * 3. Move the lights down the strand 1 bulb move at a time, all the way to the base
 * 4. Turn off
 * Repeat
 *
 */
public class SnowfallShow extends LightShow {
	private static final int NUM_LIGHTS = 3;
	private static final int NUM_ACTIVE_STRANDS = 3;
	private static final int TIME_BETWEEN_MOVES = 250;
	private static final int LIGHT_COLOR = ArduinoColor.COLOR_WHITE;
	private static final int LIGHT_OFF_COLOR = ArduinoColor.COLOR_BLACK;
	
	List<Integer> availableStrandIndices;
	private byte[] activeStrandIndices;
	private byte lowBulb;
	private int timeBetweenMoves;
	private long nextMoveTimeInMillis;
	private int numLightsAtATime;
	private boolean startNewStrands;
	
	public SnowfallShow() throws IOException {
		super();
		numLightsAtATime = NUM_LIGHTS;
		timeBetweenMoves = TIME_BETWEEN_MOVES;
		availableStrandIndices = new ArrayList<>();
	}
	
	@Override
	public void init() throws IOException {
		FillStrandCommand fillCommand = null;
		fillCommand = new FillStrandCommand((byte) 0, LIGHT_OFF_COLOR, 0, 0, 0, LightController.MAX_INTENSITY);
		lc.sendMessage(fillCommand);
		//lc.lightsOffWithDelay(0);
		activeStrandIndices = new byte[NUM_ACTIVE_STRANDS];
		startNewStrands = true;
		nextMoveTimeInMillis = System.currentTimeMillis();
	}

	@Override
	public void doit() throws IOException {
		long currentTimeInMillis = System.currentTimeMillis();

		if(startNewStrands){
			startNewStrands = false;
			lowBulb = (byte) (25 - numLightsAtATime);
			
			// Init Strands
			for(int i=0; i<activeStrandIndices.length; i++){
				activeStrandIndices[i] = getNextStrand();
			}
			
			// turn strand lights on
			for(int i=0; i<activeStrandIndices.length; i++){
				turnFirstSetOfLightsOn(activeStrandIndices[i]);
			}
		} else if(currentTimeInMillis > nextMoveTimeInMillis){
			nextMoveTimeInMillis = currentTimeInMillis + timeBetweenMoves;
			
			lowBulb--;
			if(lowBulb >= 0){
				for(int i=0; i<activeStrandIndices.length; i++){
					shiftLightsOne(activeStrandIndices[i]);
				}
			} else{
				for(int i=0; i<activeStrandIndices.length; i++){
					shiftLightsOne(activeStrandIndices[i]);
					turnOffLights(activeStrandIndices[i]);
				}
				
				startNewStrands = true;
			}
			
		}
		
	}

	private byte getNextStrand() {
		if(availableStrandIndices.isEmpty()){
			for(int i=1; i<=13; i++){
				availableStrandIndices.add(i);
			}
			Collections.shuffle(availableStrandIndices);
		}
		return availableStrandIndices.remove(0).byteValue();
	}

	private void turnFirstSetOfLightsOn(int strandIndex) throws IOException{
		LightCommandGroup lightCommandGroup = new LightCommandGroup();
		
		for(int i=0; i<numLightsAtATime; i++){
			LightData lightData = new LightData((byte)strandIndex, (byte)(lowBulb + i), LIGHT_COLOR, 0, 0, 0, LightController.MAX_INTENSITY);
			lightCommandGroup.addLightData(lightData);
		}
		
		lc.sendMessage(lightCommandGroup);
	}

	private void shiftLightsOne(int strandIndex) throws IOException {
		LightCommandGroup lightCommandGroup = new LightCommandGroup();
		
		// Turn off the high bulb
		LightData lightData4 = new LightData((byte)strandIndex, (byte)(lowBulb + numLightsAtATime), LIGHT_OFF_COLOR, 0, 0, 0, LightController.MAX_INTENSITY);
		lightCommandGroup.addLightData(lightData4);

		LightData lightData0 = new LightData((byte)strandIndex, (byte)(lowBulb + 0), LIGHT_COLOR, 0, 0, 0, LightController.MAX_INTENSITY);
		lightCommandGroup.addLightData(lightData0);

		lc.sendMessage(lightCommandGroup);
	}

	private void turnOffLights(byte strandIndex) throws IOException {
		LightCommandGroup lightCommandGroup = new LightCommandGroup();
		for(int i=0; i<numLightsAtATime; i++){
			LightData lightData = new LightData(strandIndex, (byte)(lowBulb + i + 1), LIGHT_OFF_COLOR, 0, 0, 0, LightController.MAX_INTENSITY);
			lightCommandGroup.addLightData(lightData);
		}
		
		lc.sendMessage(lightCommandGroup);
	}
	
	public static void main(String[] args) throws IOException {
		SnowfallShow show = new SnowfallShow();
		show.runLightShow();
	}
}
