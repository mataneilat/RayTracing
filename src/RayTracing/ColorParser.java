package RayTracing;

import java.awt.Color;

public class ColorParser {
	
	public static Color parseColor(String... params) throws RayTracingParseException {
		if (params.length != 3) {
			throw new RayTracingParseException("Wrong number of parameters");
		}
		float r,g,b;
		try {
			r = Float.parseFloat(params[0]);
			g = Float.parseFloat(params[1]);
			b = Float.parseFloat(params[2]);
		} catch (NumberFormatException e) {
			throw new RayTracingParseException(e);
		}
		return new Color(r,g,b);
	}

}
