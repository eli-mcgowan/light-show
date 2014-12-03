package ca.cmfly.controller.commands;


public class LightData {
	byte string;
	byte light;
	byte color;
	byte red;
	byte green;
	byte blue;
	byte intensity; // MAX_byteENSITY = 0xcc -> 204

	public LightData(byte s, byte l, byte c, byte r, byte g, byte b, byte i) {
		super();
		this.string = s;
		this.light = l;
		this.color = c;
		this.red = r;
		this.green = g;
		this.blue = b;
		this.intensity = i;
	}
	
	public LightData(byte s, byte l, int c, int r, int g, int b, int i) {
		super();
		this.string = s;
		this.light = l;
		this.color = (byte) c;
		this.red = (byte) r;
		this.green = (byte) g;
		this.blue = (byte) b;
		this.intensity = (byte) i;
	}

	public byte getString() {
		return string;
	}

	public void setString(byte string) {
		this.string = string;
	}

	public byte getLight() {
		return light;
	}

	public void setLight(byte light) {
		this.light = light;
	}

	public byte getColor() {
		return color;
	}

	public void setColor(byte color) {
		this.color = color;
	}

	public byte getRed() {
		return red;
	}

	public void setRed(byte red) {
		this.red = red;
	}

	public byte getGreen() {
		return green;
	}

	public void setGreen(byte green) {
		this.green = green;
	}

	public byte getBlue() {
		return blue;
	}

	public void setBlue(byte blue) {
		this.blue = blue;
	}

	public byte getIntensity() {
		return intensity;
	}

	public void setIntensity(byte intensity) {
		this.intensity = intensity;
	}

}
