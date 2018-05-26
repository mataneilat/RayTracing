package RayTracing;

import java.awt.Color;

public class Material {

	private final Color diffuseColor;
	
	private final Color specularColor;
	
	private final double specularityCoefficient;
	
	private final Color reflectionColor;
	
	private final double transparency;

	private Material(Color diffuseColor, Color specularColor,
			double specularityCoefficient, Color reflectionColor,
			double transparency) {
		this.diffuseColor = diffuseColor;
		this.specularColor = specularColor;
		this.specularityCoefficient = specularityCoefficient;
		this.reflectionColor = reflectionColor;
		this.transparency = transparency;
	}
	
	public static Material parse(String... params) throws RayTracingParseException {
		if (params.length != 11) {
			throw new RayTracingParseException("Wrong number of parameters");
		}
		Color diffuseColor, specularColor, reflectionColor;
		double specularityCoefficient, transparency;
		try {
			diffuseColor = ColorParser.parseColor(params[0], params[1], params[2]);
			specularColor = ColorParser.parseColor(params[3], params[4], params[5]);
			reflectionColor = ColorParser.parseColor(params[6], params[7], params[8]);
			specularityCoefficient = Double.parseDouble(params[9]);
			transparency = Double.parseDouble(params[10]);
		} catch (NumberFormatException e) {
			throw new RayTracingParseException(e);
		} 
		return new Material(diffuseColor, specularColor, specularityCoefficient, 
				reflectionColor, transparency);
	}

	public Color getDiffuseColor() {
		return diffuseColor;
	}

	public Color getSpecularColor() {
		return specularColor;
	}

	public double getSpecularityCoefficient() {
		return specularityCoefficient;
	}

	public Color getReflectionColor() {
		return reflectionColor;
	}

	public double getTransparency() {
		return transparency;
	}
	
	
	
}
