package RayTracing;

public class Intersection {
	
	private final Ray ray;
	
	private final double t;

	private final Primitive primitive;

	public Intersection(Ray ray, double t, Primitive primitive) {
		this.ray = ray;
		this.t = t;
		this.primitive = primitive;
	}
	
	public Ray getRay() {
		return ray;
	}

	public double getT() {
		return t;
	}

	public Primitive getPrimitive() {
		return primitive;
	}
	
	public Vector getIntersectionPoint() {
		return getRay().pointForT(getT());
	}
	
}
