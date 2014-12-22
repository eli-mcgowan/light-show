package ca.cmfly.controller;

public class RGB {
	private int r;
	private int g;
	private int b;
	
	public RGB(int r, int g, int b) {
		super();
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public byte getArduinoRed(){
		return scale(r);
	}

	public byte getArduinoGreen(){
		return scale(g);
	}
	
	public byte getArduinoBlue(){
		return scale(b);
	}
	
	private static byte scale(final int colorValue){
		return scale(colorValue, 0, 255, 0, 15);
	}
	
	public static byte scale(final int valueIn, final int baseMin, final int baseMax, final int limitMin, final int limitMax) {
        return (byte)(Math.round(((limitMax - limitMin) * (valueIn - baseMin) / (baseMax - baseMin)) + limitMin));
    }
	
	public static void main(String[] args) {
		// Tests!
		System.out.println("scale(255): " + scale(255));
		System.out.println("scale(127): " + scale(127));
		System.out.println("scale(0): " + scale(0));
	}
	
}
