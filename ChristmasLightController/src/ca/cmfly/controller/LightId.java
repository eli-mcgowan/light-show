package ca.cmfly.controller;

public class LightId {
	public byte strandNum;
	public byte lightNum;

	public LightId(byte strandNum, byte lightNum) {
		this.strandNum = strandNum;
		this.lightNum = lightNum;
	}
	
	public LightId(int strandNum, int lightNum) {
		this.strandNum = (byte) strandNum;
		this.lightNum = (byte) lightNum;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lightNum;
		result = prime * result + strandNum;
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
		LightId other = (LightId) obj;
		if (lightNum != other.lightNum)
			return false;
		if (strandNum != other.strandNum)
			return false;
		return true;
	}
	
	
}