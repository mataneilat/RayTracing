package RayTracing;

import java.util.Arrays;
import java.util.List;

public class PlanePrimitive implements Primitive {
	
	private final Plane plane;
	
	private final int materialIndex;
	
	public PlanePrimitive(Plane plane, int materialIndex) {
		this.plane = plane;
		this.materialIndex = materialIndex;
	}

	public static PlanePrimitive parse(String... params) throws RayTracingParseException {
		if (params.length != 5) {
			throw new RayTracingParseException("Wrong number of parameters");
		}
		Vector normal;
		double offset;
		int materialIndex;
		try {
			normal = Vector.parse(params[0], params[1], params[2]);
			offset = Double.parseDouble(params[3]);
			materialIndex = Integer.parseInt(params[4]);
		} catch (NumberFormatException e) {
			throw new RayTracingParseException(e);
		}
		return new PlanePrimitive(new Plane(normal, offset), materialIndex);
	}

	public int getMaterialIndex() {
		return materialIndex;
	}
	
	public Vector getNormal() {
		return plane.getNormal();
	}

	public double getOffset() {
		return plane.getOffset();
	}

	@Override
	public List<Double> intersect(Ray ray) {
		return plane.intersect(ray);
	}

	@Override
	public Vector surfaceNormalAtPoint(Vector p) {
		return getNormal();
	}
	

}
