package RayTracing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Sphere implements SceneObject {
	
	private final Vector center;
	
	private final double radius;
	
	private final int materialIndex;

	private Sphere(Vector center, double radius, int materialIndex) {
		this.center = center;
		this.radius = radius;
		this.materialIndex = materialIndex;
	}
	
	public static Sphere parse(String... params) throws RayTracingParseException {
		if (params.length != 5) {
			throw new RayTracingParseException("Wrong number of parameters");
		}
		Vector center;
		double radius;
		int materialIndex;
		try {
			center = Vector.parse(params[0], params[1], params[2]);
			radius = Double.parseDouble(params[3]);
			materialIndex = Integer.parseInt(params[4]);
		} catch (NumberFormatException e) {
			throw new RayTracingParseException(e);
		}
		return new Sphere(center, radius, materialIndex);
	}

	public Vector getCenter() {
		return center;
	}

	public double getRadius() {
		return radius;
	}

	public int getMaterialIndex() {
		return materialIndex;
	}

	@Override
	public List<Double> intersect(Ray ray) {
		Vector L = getCenter().sub(ray.getP0());
		double tca = L.dot(ray.getDirection());
		if (tca < 0) {
			return null;
		}
		double d_square = L.dot(L) - tca * tca;
		double r_square = getRadius() * getRadius(); 
		if (d_square > r_square) {				
			return null;
		}
		double thc = Math.sqrt(r_square - d_square);
		return Arrays.asList(tca - thc, tca + thc);
	}
	
	

}
