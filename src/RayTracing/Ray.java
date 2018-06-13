package RayTracing;

public class Ray {

	private final Vector p0;
	
	private final Vector direction;

	public Ray(Vector p0, Vector direction) {
		this.p0 = p0;
		this.direction = direction;
	}
	
	public static Ray create(Vector fromPoint, Vector toPoint) {
		return new Ray(fromPoint, toPoint.sub(fromPoint).normalize());
	}

	public Vector getP0() {
		return p0;
	}

	public Vector getDirection() {
		return direction;
	}
	
	public Ray transform(Rotation rotation, Translation translation) {
		Matrix rotationMatrix = rotation.createHomogeneousMatrix();
		Matrix translationMatrix = translation.createHomogeneousMatrix();
		
		return new Ray(rotationMatrix.multiply(translationMatrix).homogeneousMultiply(getP0()),
				rotationMatrix.homogeneousMultiply(getDirection()));
	}
	
	@Override
	public String toString() {
		return String.format("[Ray - p0 : %s , direction : %s] ", p0.toString(), direction.toString());
	}
	
	public Vector pointForT(double t) {
		return getP0().add(getDirection().multiply(t));
	}
	
	
}
