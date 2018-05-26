package RayTracing;

import java.awt.Color;

public class Light {
	
	private final Vector position;
	
	private final Color color;
	
	private final double specularIntensity;
	
	private final double shadowIntensity;
	
	private final double lightRadius;

	private Light(Vector position, Color color, double specularIntensity,
			double shadowIntensity, double lightRadius) {
		this.position = position;
		this.color = color;
		this.specularIntensity = specularIntensity;
		this.shadowIntensity = shadowIntensity;
		this.lightRadius = lightRadius;
	}

	public static Light parse(String... params) throws RayTracingParseException {
		if (params.length != 9) {
			throw new RayTracingParseException("Wrong number of parameters");
		}
		Vector position;
		Color color;
		double specularIntensity, shadowIntensity, lightRadius;
		try {
			position = Vector.parse(params[0], params[1], params[2]);
			color = ColorParser.parseColor(params[3], params[4], params[5]);
			specularIntensity = Double.parseDouble(params[6]);
			shadowIntensity = Double.parseDouble(params[7]);
			lightRadius = Double.parseDouble(params[8]);
		} catch (NumberFormatException e) {
			throw new RayTracingParseException(e);
		}
		return new Light(position, color, specularIntensity, shadowIntensity, lightRadius);
		
	}
	
	public Vector getPosition() {
		return position;
	}

	public Color getColor() {
		return color;
	}

	public double getSpecularIntensity() {
		return specularIntensity;
	}

	public double getShadowIntensity() {
		return shadowIntensity;
	}

	public double getLightRadius() {
		return lightRadius;
	}
	
	

}
