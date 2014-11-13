package ca.cmfly.controller;

public class LightData {
	int s;
	int l;
	int c;
	int r;
	int g;
	int b;
	int i; // MAX_INTENSITY = 0xcc -> 204

	public LightData(int s, int l, int c, int r, int g, int b, int i) {
		super();
		this.s = s;
		this.l = l;
		this.c = c;
		this.r = r;
		this.g = g;
		this.b = b;
		this.i = i;
	}

	public int getS() {
		return s;
	}

	public void setS(int s) {
		this.s = s;
	}

	public int getL() {
		return l;
	}

	public void setL(int l) {
		this.l = l;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
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

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

}
