package RayTracing;

public class Intersection {
	
	private final double t;

	private final SceneObject primitive;

	public Intersection(double t, SceneObject primitive) {
		this.t = t;
		this.primitive = primitive;
	}

	public double getT() {
		return t;
	}

	public SceneObject getPrimitive() {
		return primitive;
	}
	
}
