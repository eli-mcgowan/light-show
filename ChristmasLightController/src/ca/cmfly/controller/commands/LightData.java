package ca.cmfly.controller.commands;


public class LightData {
	byte string;
	byte light;
	byte color;
	byte red;
	byte green;
	byte blue;
	byte intensity; // MAX_byteENSITY = 0xcc -> 204

	public LightData(int s, int l, int c, int i) {
		super();
		this.string = (byte) s;
		this.light = (byte) l;
		this.color = (byte) c;
		this.red = 0;
		this.green = 0;
		this.blue = 0;
		this.intensity = (byte) i;
	}
	
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
	
	public LightData(LightData lightData){
		this(lightData.string, lightData.light, lightData.color, lightData.red, lightData.green, lightData.blue, lightData.intensity);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + blue;
		result = prime * result + color;
		result = prime * result + green;
		result = prime * result + intensity;
		result = prime * result + light;
		result = prime * result + red;
		result = prime * result + string;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LightData other = (LightData) obj;
		if (blue != other.blue)
			return false;
		if (color != other.color)
			return false;
		if (green != other.green)
			return false;
		if (intensity != other.intensity)
			return false;
		if (light != other.light)
			return false;
		if (red != other.red)
			return false;
		if (string != other.string)
			return false;
		return true;
	}

}
