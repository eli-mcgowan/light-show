package ca.cmfly.controller.programs;

public enum MultiShowCommand {
	NEXT("NEXT"), PREVIOUS("PREVIOUS");

	private String text;

	MultiShowCommand(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public static MultiShowCommand fromString(String text) {
		if (text != null) {
			for (MultiShowCommand b : MultiShowCommand.values()) {
				if (text.equalsIgnoreCase(b.text)) {
					return b;
				}
			}
		}
		return null;
	}
}
