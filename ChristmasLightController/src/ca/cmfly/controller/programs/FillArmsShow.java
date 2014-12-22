package ca.cmfly.controller.programs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.cmfly.controller.ArduinoColor;
import ca.cmfly.controller.LightController;
import ca.cmfly.controller.commands.FillStrandCommand;

/**
 * Fill random (each once) entire strand on/off as fast as possible
 *
 */
public class FillArmsShow extends LightShow {
	private static final int TIME_BETWEEN_MOVES = 500;

	List<Integer> strandIndexes = new ArrayList<>();
	Integer lastStrandIndex;
	private int timeBetweenMoves;
	private long nextMoveTimeInMillis;
	
	public FillArmsShow() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws IOException {
		lc.lightsOffWithDelay(0);
		timeBetweenMoves = TIME_BETWEEN_MOVES;
		nextMoveTimeInMillis = System.currentTimeMillis();
	}

	@Override
	public void doit() throws IOException {
		long currentTimeInMillis = System.currentTimeMillis();
		
		if(strandIndexes.isEmpty()){
			for(int i=1; i<=13; i++){
				strandIndexes.add(i);
			}
			Collections.shuffle(strandIndexes);
		}
		
		if(currentTimeInMillis > nextMoveTimeInMillis){
			nextMoveTimeInMillis = currentTimeInMillis + timeBetweenMoves;
			
			Integer lightToTurnOffIndex = null;
			
			if(lastStrandIndex != null){
				lightToTurnOffIndex = lastStrandIndex.intValue();
			}
			
			lastStrandIndex = strandIndexes.remove(0);
			FillStrandCommand fillCommand = new FillStrandCommand(lastStrandIndex.byteValue(), lastStrandIndex, 0, 0, 0, LightController.MAX_INTENSITY);
			lc.sendMessage(fillCommand);
			
			if(lightToTurnOffIndex != null){
				FillStrandCommand fillOffCommand = new FillStrandCommand(lightToTurnOffIndex.byteValue(), ArduinoColor.COLOR_BLACK, 0, 0, 0, 0);
				lc.sendMessage(fillOffCommand);
			}
		}
		
	}

	public static void main(String[] args) throws IOException {
		FillArmsShow fillArmsShow = new FillArmsShow();
		fillArmsShow.runLightShow();
	}
}
