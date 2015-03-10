package ca.cmfly.controller.programs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.cmfly.controller.ArduinoColor;
import ca.cmfly.controller.LightController;
import ca.cmfly.controller.colorselection.arduino.ArduinoColorSelectionStrategy;
import ca.cmfly.controller.colorselection.arduino.RainbowOrderColorSelectionStrategy;
import ca.cmfly.controller.commands.FillStrandCommand;
import ca.cmfly.controller.commands.LightCommandGroup;
import ca.cmfly.controller.commands.LightData;

/**
 * Fills entire tree from bottom to top and out every branch. This has lots of options!
 * <br />
 * Options:
 * <ul>
 * <li>TIME_BETWEEN_MOVES - milliseconds between turning on lights</li>
 * <li>MOVES_UNTIL_NEW_COLOR - number of lights that will be filled before a new color starts to filled (from the bottom).</li>
 * <li>Off Color - the color the light turns when the next bulb is lit.</li>
 * </ul>
 *
 */
public class ReverseDrip2 extends LightShow {
	private static final int TIME_BETWEEN_MOVES = 50;
	private static final int MOVES_UNTIL_NEW_COLOR = 20;
	//private Map<Integer, Integer> nextLights;
	private int timeBetweenMoves;
	private Integer offColor;
	private int movesUntilNewColor;
	private ArduinoColorSelectionStrategy colorSelectionStrategy;
	
	private List<LightData> nextLightIds;
	private long nextMoveTimeInMillis;
	private int moveNumber;
	
	public ReverseDrip2() throws IOException {
		this(ArduinoColor.COLOR_BLACK, TIME_BETWEEN_MOVES, MOVES_UNTIL_NEW_COLOR);
	}
	
	public ReverseDrip2(Integer offColor, int timeBetweenMoves, int movesUntilNewColor) throws IOException {
		super();
		//colorSelectionStrategy = new RandomColorSelectionStrategy(this.lc);
		colorSelectionStrategy = new RainbowOrderColorSelectionStrategy();
		this.offColor = offColor;
		this.timeBetweenMoves = timeBetweenMoves;
		this.movesUntilNewColor = movesUntilNewColor;
	}

	@Override
	public void init() throws IOException {
		FillStrandCommand fillCommand = null;
		fillCommand = new FillStrandCommand((byte) 0, ArduinoColor.COLOR_BLACK, 0, 0, 0, 0);
		lc.sendMessage(fillCommand);
		
//		nextLights = new HashMap<>();
//		nextLights.put(13, 0);
		nextLightIds = new ArrayList<>();

		int color = colorSelectionStrategy.getNextColor();
		nextLightIds.add(new LightData(13, 0, color, LightController.MAX_INTENSITY));
		
		nextMoveTimeInMillis = System.currentTimeMillis();
		moveNumber = 0;
	}

	@Override
	public void doit() throws IOException {
		long currentTimeInMillis = System.currentTimeMillis();
		
		if(currentTimeInMillis > nextMoveTimeInMillis){
			nextMoveTimeInMillis = currentTimeInMillis + timeBetweenMoves;
			LightCommandGroup lightCommandGroup = new LightCommandGroup();
			
			List<LightData> lightsToAdd = new ArrayList<>();
			List<LightData> lightsToRemove = new ArrayList<>();

			for(LightData lightData: nextLightIds){
				// turn on light;
				lightCommandGroup.addLightData(new LightData(lightData));
				
				// turn off previous light
				if(lightData.getLight() > 0 && offColor != null){
					LightData lightData1 = new LightData(lightData.getString(), (byte)(lightData.getLight() - 1), offColor, 0, 0, 0, 0);
					lightCommandGroup.addLightData(lightData1);
				}

				int color = lightData.getColor();
				lightData.setLight((byte) (lightData.getLight() + 1));
				if(!isValidIndex((int)lightData.getString(), (int)lightData.getLight())){
					lightsToRemove.add(new LightData(lightData.getString(), lightData.getLight(), color, LightController.MAX_INTENSITY));
					// turn off last light
					if(lightData.getLight() > 0 && offColor != null){
						LightData lightData1 = new LightData(lightData.getString(), (byte)(lightData.getLight() - 1), offColor, 0, 0, 0, 0);
						lightCommandGroup.addLightData(lightData1);
					}
				}
				
				// 2014/2015 tree mapping ... gotta do this again.
				if(lightData.getString() == 13 && lightData.getLight() == (34 + 1)){
					lightsToAdd.add(new LightData(5, 0, color, LightController.MAX_INTENSITY));
				}
				if(lightData.getString() == 5 && lightData.getLight() == (11 + 1)){
					lightsToAdd.add(new LightData(6, 0, color, LightController.MAX_INTENSITY));
				}
				if(lightData.getString() == 13 && lightData.getLight() == (35 + 1)){
					lightsToAdd.add(new LightData(9, 0, color, LightController.MAX_INTENSITY));
				}
				if(lightData.getString() == 13 && lightData.getLight() == (45 + 1)){
					lightsToAdd.add(new LightData(12, 0, color, LightController.MAX_INTENSITY));
				}
				if(lightData.getString() == 13 && lightData.getLight() == (45 + 1)){
					lightsToAdd.add(new LightData(2, 0, color, LightController.MAX_INTENSITY));
				}
				if(lightData.getString() == 2 && lightData.getLight() == (8 + 1)){
					lightsToAdd.add(new LightData(10, 0, color, LightController.MAX_INTENSITY));
				}
				if(lightData.getString() == 13 && lightData.getLight() == (46 + 1)){
					lightsToAdd.add(new LightData(4, 0, color, LightController.MAX_INTENSITY));
				}
				if(lightData.getString() == 4 && lightData.getLight() == (1 + 1)){
					lightsToAdd.add(new LightData(1, 0, color, LightController.MAX_INTENSITY));
				}
				if(lightData.getString() == 4 && lightData.getLight() == (3 + 1)){
					lightsToAdd.add(new LightData(3, 0, color, LightController.MAX_INTENSITY));
				}
				if(lightData.getString() == 4 && lightData.getLight() == (4 + 1)){
					lightsToAdd.add(new LightData(7, 0, color, LightController.MAX_INTENSITY));
				}
				if(lightData.getString() == 4 && lightData.getLight() == (6 + 1)){
					lightsToAdd.add(new LightData(11, 0, color, LightController.MAX_INTENSITY));
				}
				if(lightData.getString() == 4 && lightData.getLight() == (8 + 1)){
					lightsToAdd.add(new LightData(8, 0, color, LightController.MAX_INTENSITY));
				}
				
			}

			if(lightCommandGroup.hasCommands()){
				lc.sendMessage(lightCommandGroup);
			}
			
			nextLightIds.removeAll(lightsToRemove);
			nextLightIds.addAll(lightsToAdd);
			
			moveNumber++;
			if(moveNumber == movesUntilNewColor){
				moveNumber = 0;
				int color = colorSelectionStrategy.getNextColor();
				nextLightIds.add(new LightData(13, 0, color, LightController.MAX_INTENSITY));
			}
			
			if(nextLightIds.isEmpty()){
				init();
			}
		}
	}

	private boolean isValidIndex(Integer strandNum, Integer nextBulbIndex) {
		if (nextBulbIndex < 0) {
			return false;
		}
		if (strandNum == 13) {
			return nextBulbIndex < 50;
		}
		return nextBulbIndex < 25;
	}
	
	public static void main(String[] args) throws IOException {
		//ReverseDrip2 ReverseDrip2 = new ReverseDrip2(null, 50, 30);
		ReverseDrip2 ReverseDrip2 = new ReverseDrip2();
		ReverseDrip2.runLightShow();
	}

}
