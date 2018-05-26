package RayTracing;

import java.util.Arrays;
import java.util.List;

public class Plane implements SceneObject {
	
	private final Vector normal;
	
	private final double offset;
	
	private final int materialIndex;

	private Plane(Vector normal, double offset, int materialIndex) {
		this.normal = normal;
		this.offset = offset;
		this.materialIndex = materialIndex;
	}
	
	public static Plane parse(String... params) throws RayTracingParseException {
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
		return new Plane(normal, offset, materialIndex);
	}

	public Vector getNormal() {
		return normal;
	}

	public double getOffset() {
		return offset;
	}

	public int getMaterialIndex() {
		return materialIndex;
	}

	@Override
	public List<Double> intersect(Ray ray) {
		return Arrays.asList(-(ray.getP0().dot(getNormal()) - getOffset()) / (double) ray.getDirection().dot(getNormal()));
	}
	
	
	

}
