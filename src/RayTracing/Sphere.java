package RayTracing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Sphere implements PrimitiveLogic {
	
	private final Vector center;
	
	private final double radius;

	private Sphere(Vector center, double radius) {
		this.center = center;
		this.radius = radius;
	}
	
	public static Sphere parse(String... params) throws RayTracingParseException {
		if (params.length < 4) {
			throw new RayTracingParseException("Wrong number of parameters");
		}
		Vector center;
		double radius;
		try {
			center = Vector.parse(params[0], params[1], params[2]);
			radius = Double.parseDouble(params[3]);
		} catch (NumberFormatException e) {
			throw new RayTracingParseException(e);
		}
		return new Sphere(center, radius);
	}

	public Vector getCenter() {
		return center;
	}

	public double getRadius() {
		return radius;
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

	@Override
	public Vector surfaceNormalAtPoint(Vector p) {
		return p.sub(getCenter()).normalize();
	}
	
	
	

}
