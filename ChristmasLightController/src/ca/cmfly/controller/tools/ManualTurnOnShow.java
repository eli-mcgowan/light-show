package ca.cmfly.controller.tools;

import java.io.IOException;
import java.util.Scanner;

import ca.cmfly.controller.ArduinoColor;
import ca.cmfly.controller.LightController;
import ca.cmfly.controller.commands.FillStrandCommand;
import ca.cmfly.controller.commands.LightCommand;
import ca.cmfly.controller.commands.LightData;
import ca.cmfly.controller.programs.LightShow;

public class ManualTurnOnShow extends LightShow {

	private Scanner in;
	
	public ManualTurnOnShow() throws IOException {
		super();
		in = new Scanner(System.in);
	}

	@Override
	public void init() throws IOException{
		// Clear all
		FillStrandCommand fillCommand = null;

		fillCommand = new FillStrandCommand((byte) 0, ArduinoColor.COLOR_BLACK, 0, 0, 0, 0);
		lc.sendMessage(fillCommand);
	}
	
	@Override
	public void doit() throws IOException {
		// TODO Auto-generated method stub
		Integer strand = null;
		while(strand == null){
			strand = getStrand();
		}
		
		Integer bulb = null;
		while(bulb == null){
			bulb = getBulb();
		}
		
		Integer color = null;
		while(color == null){
			color = getColor();
		}

		LightData lightData = new LightData(strand.byteValue(), bulb.byteValue(), color.byteValue(), 0, 0, 0, LightController.MAX_INTENSITY);
		LightCommand lightCommand = new LightCommand();
		lightCommand.setLightData(lightData);

		lc.sendMessage(lightCommand);
	}

	private Integer getColor() {
		System.out.println("COLOR_WHITE = 1");
		System.out.println("COLOR_RED = 2");
		System.out.println("COLOR_GREEN = 3");
		System.out.println("COLOR_BLUE = 4");
		System.out.println("COLOR_CYAN = 5");
		System.out.println("COLOR_MAGENTA = 6");
		System.out.println("COLOR_YELLOW = 7");
		System.out.println("COLOR_PURPLE = 8");
		System.out.println("COLOR_ORANGE = 9");
		System.out.println("COLOR_PALE_ORANGE = 10");
		System.out.println("COLOR_WARMWHITE = 11");
		System.out.println("COLOR_INDIGO = 12");
		System.out.println("COLOR_VIOLET = 13");
		System.out.println("COLOR_BLACK = 14");

		System.out.print("Enter color: ");
		int num = in.nextInt();
		if(num < 1 || num > 14){
			System.out.println("Invalid color.");
			return null;
		}
		return num;
	}

	private Integer getBulb() {
		System.out.print("Enter bulb index: ");
		int num = in.nextInt();
		return num;
	}

	public Integer getStrand(){
		System.out.print("Enter strand #: ");
		int num = in.nextInt();
		if(num < 1 || num > 13){
			System.out.println("Invalid strand #.");
			return null;
		}
		return num;
	}
	
	public static void main(String[] args) throws IOException {
		ManualTurnOnShow manualTurnOnShow = new ManualTurnOnShow();
		manualTurnOnShow.runLightShow();
	}
}
