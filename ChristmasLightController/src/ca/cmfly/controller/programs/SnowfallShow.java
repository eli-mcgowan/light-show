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
	private static final int NUM_ACTIVE_STRANDS = 5;
	private static final int TIME_BETWEEN_MOVES = 250;
	private static final int LIGHT_COLOR = ArduinoColor.COLOR_WHITE;
	private static final int LIGHT_OFF_COLOR = ArduinoColor.COLOR_BLACK;
	
	private final int timeBetweenMoves;
	private final int numLightsAtATime;
	private final int numActiveStrands;
	private final int lightColor;
	private final int lightOffColor;

	private List<Integer> availableStrandIndices;
	private List<SnowfallActiveStrand> activeStrands;
	private long nextStrandStartTime;

	
	public SnowfallShow() throws IOException {
		this(NUM_LIGHTS, TIME_BETWEEN_MOVES, NUM_ACTIVE_STRANDS, LIGHT_COLOR, LIGHT_OFF_COLOR);
	}
	
	public SnowfallShow(int numLightsAtATime, int timeBetweenMoves, int numActiveStrands, int lightColor, int lightOffColor) throws IOException {
		super();
		this.numLightsAtATime = numLightsAtATime;
		this.numActiveStrands = numActiveStrands;
		this.timeBetweenMoves = timeBetweenMoves;
		this.lightColor = lightColor;
		this.lightOffColor = lightOffColor;
		availableStrandIndices = new ArrayList<>();
		activeStrands = new ArrayList<>();
	}
	
	@Override
	public void init() throws IOException {
		FillStrandCommand fillCommand = null;
		fillCommand = new FillStrandCommand((byte) 0, lightOffColor, 0, 0, 0, LightController.MAX_INTENSITY);
		lc.sendMessage(fillCommand);
		nextStrandStartTime = System.currentTimeMillis();
	}

	@Override
	public void doit() throws IOException {
		long currentTimeInMillis = System.currentTimeMillis();

		List<SnowfallActiveStrand> strandsToRemove = new ArrayList<>();
		
		if(activeStrands.size() < numActiveStrands && currentTimeInMillis > nextStrandStartTime){
			// Init Strand
			SnowfallActiveStrand activeStrand = new SnowfallActiveStrand();
			activeStrand.strandNum = getNextStrand();
			activeStrand.lowBulb = (byte) (25 - numLightsAtATime);
			activeStrand.nextMoveTimeInMillis = currentTimeInMillis + timeBetweenMoves;
			activeStrands.add(activeStrand);
			turnFirstSetOfLightsOn(activeStrand);
			
			final int NUM_BULBS = 25;
			int getAverageTimeBetweenStrands = ((NUM_BULBS - numLightsAtATime) * timeBetweenMoves) / numActiveStrands;
			nextStrandStartTime = currentTimeInMillis + LightController.randInt((int)(getAverageTimeBetweenStrands * 0.5), (int)(getAverageTimeBetweenStrands * 1.5));
		} 
		for(SnowfallActiveStrand activeStrand: activeStrands){
			if(currentTimeInMillis > activeStrand.nextMoveTimeInMillis){
				activeStrand.nextMoveTimeInMillis = currentTimeInMillis + timeBetweenMoves;
				activeStrand.lowBulb--;
				if(activeStrand.lowBulb >= 0){
					shiftLightsOne(activeStrand);
				} else{
					shiftLightsOne(activeStrand);
					turnOffLights(activeStrand);
					strandsToRemove.add(activeStrand);
				}
			}
		}
		
		for(SnowfallActiveStrand strandToRemove: strandsToRemove){
			activeStrands.remove(strandToRemove);
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

	private void turnFirstSetOfLightsOn(SnowfallActiveStrand activeStrand) throws IOException{
		LightCommandGroup lightCommandGroup = new LightCommandGroup();
		
		for(int i=0; i<numLightsAtATime; i++){
			LightData lightData = new LightData((byte)activeStrand.strandNum, (byte)(activeStrand.lowBulb + i), lightColor, 0, 0, 0, LightController.MAX_INTENSITY);
			lightCommandGroup.addLightData(lightData);
		}
		
		lc.sendMessage(lightCommandGroup);
	}

	private void shiftLightsOne(SnowfallActiveStrand activeStrand) throws IOException {
		LightCommandGroup lightCommandGroup = new LightCommandGroup();
		
		// Turn off the high bulb
		LightData lightData4 = new LightData((byte)activeStrand.strandNum, (byte)(activeStrand.lowBulb + numLightsAtATime), lightOffColor, 0, 0, 0, LightController.MAX_INTENSITY);
		lightCommandGroup.addLightData(lightData4);

		LightData lightData0 = new LightData((byte)activeStrand.strandNum, (byte)(activeStrand.lowBulb + 0), lightColor, 0, 0, 0, LightController.MAX_INTENSITY);
		lightCommandGroup.addLightData(lightData0);

		lc.sendMessage(lightCommandGroup);
	}

	private void turnOffLights(SnowfallActiveStrand activeStrand) throws IOException {
		LightCommandGroup lightCommandGroup = new LightCommandGroup();
		for(int i=0; i<numLightsAtATime; i++){
			LightData lightData = new LightData((byte)activeStrand.strandNum, (byte)(activeStrand.lowBulb + i + 1), lightOffColor, 0, 0, 0, LightController.MAX_INTENSITY);
			lightCommandGroup.addLightData(lightData);
		}
		
		lc.sendMessage(lightCommandGroup);
	}
	
	public static void main(String[] args) throws IOException {
		SnowfallShow show = new SnowfallShow();
		show.runLightShow();
	}
	
	public class SnowfallActiveStrand {
		int strandNum;
		int lowBulb;
		long nextMoveTimeInMillis;
	}

}
