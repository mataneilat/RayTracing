package RayTracing;

import java.util.ArrayList;
import java.util.List;

public class Triangle implements Primitive {

	private final Vector v1;
	
	private final Vector v2;
	
	private final Vector v3;
	
	private final int materialIndex;
	
	private Plane trianglePlane;
	
	public Triangle(Vector v1, Vector v2, Vector v3, int materialIndex) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.materialIndex = materialIndex;
	}
	
	public Vector getV1() {
		return v1;
	}

	public Vector getV2() {
		return v2;
	}

	public Vector getV3() {
		return v3;
	}
	
	public Plane getTrianglePlane() {
		if (trianglePlane == null) {
			trianglePlane = Plane.create(getV1(), getV2(), getV3());
		}
		return trianglePlane;
	}

	@Override
	public int getMaterialIndex() {
		return materialIndex;
	}

	public static Triangle parse(String... params) throws RayTracingParseException {
		if (params.length != 10) {
			throw new RayTracingParseException("Wrong number of parameters");
		}
		Vector v1, v2, v3;
		int materialIndex;
		try {
			v1 = Vector.parse(params[0], params[1], params[2]);
			v2 = Vector.parse(params[3], params[4], params[5]);
			v3 = Vector.parse(params[6], params[7], params[8]);
			materialIndex = Integer.parseInt(params[9]);
		} catch (NumberFormatException e) {
			throw new RayTracingParseException(e);
		}
		return new Triangle(v1, v2, v3, materialIndex);
	}

	@Override
	public List<Double> intersect(Ray ray) {
		Plane trianglePlane = getTrianglePlane();
		List<Double> planeIntersections = trianglePlane.intersect(ray);
		List<Double> triangleIntersections = new ArrayList<Double>();
		for (double planeIntersection : planeIntersections) {
			Vector intersectionPoint = ray.pointForT(planeIntersection);
			if (pointInTriangle(intersectionPoint, getV1(), getV2(), getV3())) {
				triangleIntersections.add(planeIntersection);
			}
		}
		return triangleIntersections;
		
	}
	
	private boolean pointInTriangle(Vector p , Vector a, Vector b, Vector c) {
		return (sameSide(p, a, b, c) && sameSide(p, b, a, c) && sameSide(p, c, a, b));
	}
	
	private boolean sameSide(Vector p1, Vector p2, Vector a, Vector b) {
		Vector ab = b.sub(a);
		Vector cp1 = ab.cross(p1.sub(a));
		Vector cp2 = ab.cross(p2.sub(a));
		return (cp1.dot(cp2) >= 0);
	}
	
//	private boolean isPointInside(Vector point) {
//		Vector AB = getV2().sub(getV1());
//		Vector AC = getV3().sub(getV1());
//		Vector PA = getV1().sub(point);
//		Vector PB = getV2().sub(point);
//		Vector PC = getV3().sub(point);
//		double triangleArea = AB.cross(AC).norm() / 2.0;
//		double alpha = PB.cross(PC).norm() / (2.0 * triangleArea);
//		double beta = PC.cross(PA).norm() / (2.0 * triangleArea);
//		double gamma = 1.0 - alpha - beta;
//		return alpha >= 0 && alpha <= 1 && beta >= 0 && beta <= 1 && gamma >= 0 && gamma <= 1;
//	}

	@Override
	public Vector surfaceNormalAtPoint(Vector p) {
		return getTrianglePlane().getNormal();
	}

}