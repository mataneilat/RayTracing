package RayTracing;

public class Ray {

	private final Vector p0;
	
	private final Vector direction;

	public Ray(Vector p0, Vector direction) {
		this.p0 = p0;
		this.direction = direction;
	}

	public Vector getP0() {
		return p0;
	}

	public Vector getDirection() {
		return direction;
	}
	
	@Override
	public String toString() {
		return String.format("[Ray - p0 : %s , direction : %s] ", p0.toString(), direction.toString());
	}
	
	
}
