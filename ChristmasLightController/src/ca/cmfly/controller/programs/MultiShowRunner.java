package ca.cmfly.controller.programs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class MultiShowRunner extends LightShow{
	public static final String UDP_COMMAND_HOST = "localhost";
	public static final int UDP_COMMAND_PORT = 8765;
	
	private List<LightShow> lightShows;
	private int currentLightShowIndex;
	
	private LightShowTimer lightShowTimer;
	private FutureTask<String> timeOutTask;
	
	private UDPCommandListener udpCommandListener;
	private FutureTask<MultiShowCommand> udpCommandTask;
	
	private ExecutorService executor;
	
	public MultiShowRunner() throws IOException {
		super();
		lightShows = new ArrayList<>();

		lightShows.add(new HorseOfADifferentColorShow());
		//lightShows.add(new ChristmasFillShow());
		lightShows.add(new RandomColorShow(true));
		lightShows.add(new Twinkle());
		lightShows.add(new FillArmsShow());
		lightShows.add(new SnowfallShow());
		lightShows.add(new RedAndWhite(true, 1));
		
		currentLightShowIndex = 0;
		lightShowTimer = new LightShowTimer(60000);
		udpCommandListener = new UDPCommandListener(UDP_COMMAND_HOST, UDP_COMMAND_PORT);
		executor = Executors.newFixedThreadPool(2);
	}

	@Override
	public void init() throws IOException {
		initLightShow();
		startUdpCommandTask();
	}
	
	private void startUdpCommandTask(){
		if(udpCommandTask != null){
			udpCommandTask.cancel(true);
		}
		udpCommandTask = new FutureTask<MultiShowCommand>(udpCommandListener);
		executor.execute(udpCommandTask);
	}
	

	@Override
	public void doit() throws IOException {
		LightShow lightShow = lightShows.get(currentLightShowIndex);
		lightShow.doit();
		
		if(udpCommandTask.isDone()){
			try {
				switch(udpCommandTask.get()){
					case NEXT:
						nextLightShow();
						startUdpCommandTask();
						break;
					case PREVIOUS:
						previousLightShow();
						startUdpCommandTask();
						break;
					default:
						break;
				}
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(timeOutTask.isDone()){
			nextLightShow();
		}
	}
	
	private void nextLightShow() throws IOException{
		currentLightShowIndex++;
		if(currentLightShowIndex >= lightShows.size()){
			currentLightShowIndex = 0;
		}
		initLightShow();
	}
	
	private void previousLightShow() throws IOException{
		currentLightShowIndex--;
		if(currentLightShowIndex < 0){
			currentLightShowIndex = lightShows.size() - 1;
		}
		initLightShow();
	}
	
	private void initLightShow() throws IOException{
		LightShow lightShow = lightShows.get(currentLightShowIndex);
		lightShow.init();
		
		if(timeOutTask != null){
			timeOutTask.cancel(true);
		}
		
		timeOutTask = new FutureTask<String>(lightShowTimer);
		executor.execute(timeOutTask);
	}
	
	public static void main(String[] args) throws IOException {
		MultiShowRunner multiShowRunner = new MultiShowRunner();
		multiShowRunner.runLightShow();
	}
	
}
