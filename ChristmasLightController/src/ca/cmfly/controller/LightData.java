package ca.cmfly.controller;

public class LightData {
	int string;
	int bulb;
	int color;
	int r;
	int g;
	int b;
	int brightness; //MAX_INTENSITY = 0xcc -> 204

	public LightData(int string, int bulb, int color, int r, int g, int b, int brightness) {
		super();
		this.string = string;
		this.bulb = bulb;
		this.color = color;
		this.r = r;
		this.g = g;
		this.b = b;
		this.brightness = brightness;
	}

	public int getString() {
		return string;
	}

	public void setString(int string) {
		this.string = string;
	}

	public int getBulb() {
		return bulb;
	}

	public void setBulb(int bulb) {
		this.bulb = bulb;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public int getBrightness() {
		return brightness;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

}
