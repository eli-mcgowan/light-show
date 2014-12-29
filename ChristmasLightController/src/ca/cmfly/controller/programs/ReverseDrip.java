package ca.cmfly.controller.programs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.cmfly.controller.ArduinoColor;
import ca.cmfly.controller.LightController;
import ca.cmfly.controller.commands.FillStrandCommand;
import ca.cmfly.controller.commands.LightCommandGroup;
import ca.cmfly.controller.commands.LightData;

public class ReverseDrip extends LightShow {
	private static final int TIME_BETWEEN_MOVES = 50;
	private Map<Integer, Integer> nextLights;
	private int timeBetweenMoves;
	private long nextMoveTimeInMillis;
	private int color;
	
	public ReverseDrip() throws IOException {
		super();
		timeBetweenMoves = TIME_BETWEEN_MOVES;
	}
	
	

	@Override
	public void init() throws IOException {
		FillStrandCommand fillCommand = null;
		fillCommand = new FillStrandCommand((byte) 0, ArduinoColor.COLOR_BLACK, 0, 0, 0, 0);
		lc.sendMessage(fillCommand);
		
		nextLights = new HashMap<>();
		nextLights.put(13, 0);
		nextMoveTimeInMillis = System.currentTimeMillis();
		color = lc.getRandomColor();
	}



	@Override
	public void doit() throws IOException {
		long currentTimeInMillis = System.currentTimeMillis();
		
		if(currentTimeInMillis > nextMoveTimeInMillis){
			nextMoveTimeInMillis = currentTimeInMillis + timeBetweenMoves;
			LightCommandGroup lightCommandGroup = new LightCommandGroup();
			
			List<Integer> keys = new ArrayList<>(nextLights.keySet());
			for(Integer strandNum: keys){
				Integer nextBulbIndex = nextLights.get(strandNum);
				
				// turn on light;
				LightData lightData0 = new LightData(strandNum.byteValue(), nextBulbIndex.byteValue(), color, 0, 0, 0, LightController.MAX_INTENSITY);
				lightCommandGroup.addLightData(lightData0);
				
				// turn off previous light
				if(nextBulbIndex > 0){
					LightData lightData1 = new LightData(strandNum.byteValue(), (byte)(nextBulbIndex.byteValue() - 1), ArduinoColor.COLOR_BLACK, 0, 0, 0, 0);
					lightCommandGroup.addLightData(lightData1);
				}
				
				nextBulbIndex++;
				if(isValidIndex(strandNum, nextBulbIndex)){
					nextLights.put(strandNum, nextBulbIndex);
				} else{
					nextLights.remove(strandNum);
				}
				
				if(strandNum == 13 && nextBulbIndex == (34 + 1)){
					nextLights.put(5, 0);
				}
				if(strandNum == 5 && nextBulbIndex == (11 + 1)){
					nextLights.put(6, 0);
				}
				if(strandNum == 13 && nextBulbIndex == (35 + 1)){
					nextLights.put(9, 0);
				}
				if(strandNum == 13 && nextBulbIndex == (45 + 1)){
					nextLights.put(12, 0);
				}
				if(strandNum == 13 && nextBulbIndex == (45 + 1)){
					nextLights.put(2, 0);
				}
				if(strandNum == 2 && nextBulbIndex == (8 + 1)){
					nextLights.put(10, 0);
				}
				if(strandNum == 13 && nextBulbIndex == (46 + 1)){
					nextLights.put(4, 0);
				}
				if(strandNum == 4 && nextBulbIndex == (1 + 1)){
					nextLights.put(1, 0);
				}
				if(strandNum == 4 && nextBulbIndex == (3 + 1)){
					nextLights.put(3, 0);
				}
				if(strandNum == 4 && nextBulbIndex == (4 + 1)){
					nextLights.put(7, 0);
				}
				if(strandNum == 4 && nextBulbIndex == (6 + 1)){
					nextLights.put(11, 0);
				}
				if(strandNum == 4 && nextBulbIndex == (8 + 1)){
					nextLights.put(8, 0);
				}
				
			}
			
			if(lightCommandGroup.hasCommands()){
				lc.sendMessage(lightCommandGroup);
			}
			
			if(nextLights.isEmpty()){
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
		ReverseDrip reverseDrip = new ReverseDrip();
		reverseDrip.runLightShow();
	}

}
