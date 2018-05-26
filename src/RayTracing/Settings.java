package RayTracing;

import java.awt.Color;

public class Settings {
	
	private final Color backgroundColor;
	
	private final int shadowRaysCount;
	
	private final int maxRecursionLevel;
	
	private final int superSamplingLevel;

	private Settings(Color backgroundColor, int shadowRaysCount,
			int maxRecursionLevel, int superSamplingLevel) {
		this.backgroundColor = backgroundColor;
		this.shadowRaysCount = shadowRaysCount;
		this.maxRecursionLevel = maxRecursionLevel;
		this.superSamplingLevel = superSamplingLevel;
	}
	
	public static Settings parse(String... params) throws RayTracingParseException {
		if (params.length != 6) {
			throw new RayTracingParseException("Wrong number of parameters");
		}
		Color backgroundColor;
		int shadowRaysCount, maxRecursionLevel, superSamplingLevel;
		try {
			backgroundColor = ColorParser.parseColor(params[0], params[1], params[2]);
			shadowRaysCount = Integer.parseInt(params[3]);
			maxRecursionLevel = Integer.parseInt(params[4]);
			superSamplingLevel = Integer.parseInt(params[5]);
		} catch (NumberFormatException e) {
			throw new RayTracingParseException(e);
		}
		return new Settings(backgroundColor, shadowRaysCount, maxRecursionLevel, superSamplingLevel);
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public int getShadowRaysCount() {
		return shadowRaysCount;
	}

	public int getMaxRecursionLevel() {
		return maxRecursionLevel;
	}

	public int getSuperSamplingLevel() {
		return superSamplingLevel;
	}
	
	
	

}
