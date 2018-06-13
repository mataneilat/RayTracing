package RayTracing;

import java.util.Arrays;
import java.util.List;

public class Plane implements PrimitiveLogic {
	
	private final Vector normal;
	
	private final double offset;

	public Plane(Vector normal, double offset) {
		this.normal = normal;
		this.offset = offset;
	}
	
	public static Plane parse(String... params) throws RayTracingParseException {
		if (params.length < 4) {
			throw new RayTracingParseException("Wrong number of parameters");
		}
		Vector normal;
		double offset;
		try {
			normal = Vector.parse(params[0], params[1], params[2]);
			offset = Double.parseDouble(params[3]);
		} catch (NumberFormatException e) {
			throw new RayTracingParseException(e);
		}
		return new Plane(normal, offset);
	}
	
	public static Plane create(Vector p1, Vector p2, Vector p3) {
		Vector v1 = Vector.createDirection(p1, p2);
		Vector v2 = Vector.createDirection(p1, p3);
		Vector normal = v1.cross(v2).normalize();
		return new Plane(normal, p1.dot(normal));
		
	}
	
	public Vector getNormal() {
		return normal;
	}

	public double getOffset() {
		return offset;
	}

	public List<Double> intersect(Ray ray) {
		return Arrays.asList(-(ray.getP0().dot(getNormal()) - getOffset()) / (double) ray.getDirection().dot(getNormal()));
	}

	public Vector surfaceNormalAtPoint(Vector p) {
		return getNormal();
	}	
	

}
