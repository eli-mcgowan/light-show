package ca.cmfly.controller.commands;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LightData {
	int string;
	int light;
	int color;
	int red;
	int green;
	int blue;
	int intensity; // MAX_INTENSITY = 0xcc -> 204

	public LightData(int s, int l, int c, int r, int g, int b, int i) {
		super();
		this.string = s;
		this.light = l;
		this.color = c;
		this.red = r;
		this.green = g;
		this.blue = b;
		this.intensity = i;
	}

	@JsonProperty("s")
	public int getString() {
		return string;
	}

	public void setString(int string) {
		this.string = string;
	}

	@JsonProperty("l")
	public int getLight() {
		return light;
	}

	public void setLight(int light) {
		this.light = light;
	}

	@JsonProperty("c")
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	@JsonProperty("r")
	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	@JsonProperty("g")
	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	@JsonProperty("b")
	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}

	@JsonProperty("i")
	public int getIntensity() {
		return intensity;
	}

	public void setIntensity(int intensity) {
		this.intensity = intensity;
	}
}
