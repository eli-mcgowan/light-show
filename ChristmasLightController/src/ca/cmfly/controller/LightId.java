package ca.cmfly.controller;

public class LightId {
	public byte strandNum;
	public byte lightNum;

	public LightId(byte strandNum, byte lightNum) {
		this.strandNum = strandNum;
		this.lightNum = lightNum;
	}
}