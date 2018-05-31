package RayTracing;

import java.awt.Color;

public class ColorUtils {
	
	public static Color multiplyComponents(Color c1, Color c2) {
		
		float r1 = c1.getRed() / 255.0f;
		float g1 = c1.getGreen() / 255.0f;
		float b1 = c1.getBlue() / 255.0f;

		float r2 = c2.getRed() / 255.0f;
		float g2 = c2.getGreen() / 255.0f;
		float b2 = c2.getBlue() / 255.0f;
		
		return new Color(r1 * r2, g1 * g2, b1 * b2);
	}
	
	public static Color allComponentsColor(float a) {
		if (a > 1 || a < 0) {
			return null;
		}
		return new Color(a, a, a);
	}
	
	public static float colorDifference(Color c1, Color c2) {
		return Math.abs(c1.getRed() - c2.getRed()) + 
				Math.abs(c1.getBlue() - c2.getBlue() +
						Math.abs(c1.getGreen() - c2.getGreen()));
	}
	
	public static Color multiplyComponents(Color c, float a) {
		if (a > 1 || a < 0) {
			return null;
		}
		float r = c.getRed() / 255.0f;
		float g = c.getGreen() / 255.0f;
		float b = c.getBlue() / 255.0f;

		return new Color(a*r, a*g, a*b);
	}
	
	public static Color add(Color c1, Color c2) {
		return new Color(clamp(c1.getRed() + c2.getRed(), 0, 255), 
				clamp(c1.getGreen() + c2.getGreen(), 0, 255),
				clamp(c1.getBlue() + c2.getBlue(), 0, 255));
	}
	
	private static int clamp(int value, int min, int max) {
		return Math.max(min, Math.min(value, max));
	}

}
