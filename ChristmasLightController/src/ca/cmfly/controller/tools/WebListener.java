package ca.cmfly.controller.tools;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import ca.cmfly.controller.ArduinoColor;
import ca.cmfly.controller.commands.FillStrandCommand;
import ca.cmfly.controller.commands.LightCommand;
import ca.cmfly.controller.programs.LightShow;
import ca.cmfly.controller.programs.MultiShowRunner;

public class WebListener extends LightShow {
	private UDPLightCommandListener udpCommandListener;
	private FutureTask<LightCommand> udpCommandTask;
	
	private ExecutorService executor;
	
	public WebListener() throws IOException {
		super();
		
		udpCommandListener = new UDPLightCommandListener(MultiShowRunner.UDP_COMMAND_HOST, MultiShowRunner.UDP_COMMAND_PORT);
		executor = Executors.newFixedThreadPool(2);
	}
	
	@Override
	public void init() throws IOException {
		FillStrandCommand fillCommand = null;
		fillCommand = new FillStrandCommand((byte) 0, ArduinoColor.COLOR_BLACK, 0, 0, 0, 0);
		lc.sendMessage(fillCommand);
		
		startUdpCommandTask();
	}

	private void startUdpCommandTask(){
		if(udpCommandTask != null){
			udpCommandTask.cancel(true);
		}
		udpCommandTask = new FutureTask<LightCommand>(udpCommandListener);
		executor.execute(udpCommandTask);
	}
	
	@Override
	public void doit() throws IOException {
		if(udpCommandTask.isDone()){
			try {
				LightCommand lightCommand = udpCommandTask.get();
				System.out.println(lightCommand);
				lc.sendMessage(lightCommand);
				startUdpCommandTask();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}
	
	public static void main(String[] args) throws IOException {
		WebListener webListener = new WebListener();
		webListener.runLightShow();
	}
}
