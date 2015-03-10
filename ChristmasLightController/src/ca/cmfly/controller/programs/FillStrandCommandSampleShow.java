package ca.cmfly.controller.programs;

import java.io.IOException;
import java.util.Calendar;

import ca.cmfly.controller.LightController;
import ca.cmfly.controller.commands.FillStrandCommand;

/**
 * Demonstrates how to use the FillCommand.
 *
 */
public class FillStrandCommandSampleShow extends LightShow {

	public FillStrandCommandSampleShow() throws IOException {
		super();
	}

	@Override
	public void init() throws IOException {
		
	}

	@Override
	public void doit() throws IOException {
		long start = Calendar.getInstance().getTimeInMillis();
		for (byte strand = 1; strand <= 13; strand++) {
			//long startInner = Calendar.getInstance().getTimeInMillis();
			byte color = strand;
			FillStrandCommand fillCommand = null;
			fillCommand = new FillStrandCommand(strand, color, 0, 0, 0, LightController.MAX_INTENSITY);
			lc.sendMessage(fillCommand);
//			try {
//			//	Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			//System.out.println("ALL Light: " + (Calendar.getInstance().getTimeInMillis() - startInner) + "ms");
		}
		System.out.println("14 x ALL Light: " + (Calendar.getInstance().getTimeInMillis() - start) + "ms");
	}

	public static void main(String[] args) throws IOException {
		FillStrandCommandSampleShow fillCommandSampleShow = new FillStrandCommandSampleShow();
		fillCommandSampleShow.runLightShow();
	}

}
