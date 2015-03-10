package ca.cmfly.controller.tools;

import ca.cmfly.controller.RGB;

public class ColorWheel {

	// Input a value 0 to 255 to get a color value.
	// The colors are a transition r - g - b - back to r.
	public static RGB wheel(int wheelPos) {
		wheelPos = wheelPos % 255;
		wheelPos = 255 - wheelPos;
		if (wheelPos < 85) {
			return new RGB(255 - wheelPos * 3, 0, wheelPos * 3);
		} else if (wheelPos < 170) {
			wheelPos -= 85;
			return new RGB(0, wheelPos * 3, 255 - wheelPos * 3);
		} else {
			wheelPos -= 170;
			return new RGB(wheelPos * 3, 255 - wheelPos * 3, 0);
		}
	}
}
