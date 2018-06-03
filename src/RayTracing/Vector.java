package RayTracing;

public class Vector {
	
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static Vector createDirection(Vector from, Vector to) {
		return to.sub(from).normalize();
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}
	
	public static Vector parse(String... params) throws RayTracingParseException {
		if (params.length != 3) {
			throw new RayTracingParseException("Wrong number of parameters");
		}
		double x, y, z;
		try {
			x = Double.parseDouble(params[0]);
			y = Double.parseDouble(params[1]);
			z = Double.parseDouble(params[2]);
		} catch (NumberFormatException e) {
			throw new RayTracingParseException(e);
		}
		return new Vector(x, y, z);
	}
	
	public Vector sub(Vector v) {
		return add(v.multiply(-1));
	}
	
	public Vector add(Vector v) {
		return new Vector(x+v.x, y+v.y, z+v.z);
	}
	
	public double distanceFrom(Vector v) {
		return sub(v).norm();
	}
	
	public Vector multiply(double a) {
		return new Vector(a*x, a*y, a*z);
	}
	
	public Vector divide(double a) {
		return multiply(1.0/a);
	}
	
	public Vector normalize() {
		return divide(norm());
	}
	
	public Vector cross(Vector v) {
		return new Vector(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x);	
	}
	
	public double norm() {
		return Math.sqrt(dot(this));
	}
	
	public double dot(Vector v) {
		return x * v.x + y * v.y + z * v.z;
	}
	
	@Override
	public String toString() {
		return String.format("[Vector - x: %f , y: %f, z: %f]", x, y, z);
	}

	private final double x;
	
	private final double y;
	
	private final double z;

}
