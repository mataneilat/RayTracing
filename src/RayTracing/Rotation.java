package RayTracing;

public class Rotation implements AffineTransformation<Rotation> {
	
	private final Vector aboutAxis;
	
	private final double angle;

	public Rotation(Vector aboutAxis, double angle) {
		this.aboutAxis = aboutAxis;
		this.angle = angle;
	}
	
	public static Rotation parse(String... params) throws RayTracingParseException {
		if (params.length < 4) {
			throw new RayTracingParseException("Wrong number of parameters");
		}
		Vector aboutAxis;
		double angle;
		try {
			aboutAxis = Vector.parse(params[0], params[1], params[2]);
			angle = Double.parseDouble(params[3]);
		} catch (NumberFormatException e) {
			throw new RayTracingParseException(e);
		}
		return new Rotation(aboutAxis, angle);
	}
	
	public Vector getAboutAxis() {
		return aboutAxis;
	}

	public double getAngle() {
		return angle;
	}

	public static Rotation noRotation() {
		return new Rotation(new Vector(1, 0, 0), 0);
	}
	
	@Override
	public Matrix createHomogeneousMatrix() {
		double[][] matrix = new double[4][4];
		double l = aboutAxis.getX();
		double m = aboutAxis.getY();
		double n = aboutAxis.getZ();
		
		double cos_a = Math.cos(angle);
		double sin_a = Math.sin(angle);
		
		matrix[0][0] = l * l * (1 - cos_a) + cos_a;
		matrix[0][1] = m * l * (1 - cos_a) + n * sin_a;
		matrix[0][2] = n * l * (1 - cos_a) + m * sin_a;
		matrix[0][3] = 0;
		
		matrix[1][0] = l * m * (1 - cos_a) + n * sin_a;
		matrix[1][1] = m * m * (1 - cos_a) + cos_a;
		matrix[1][2] = n * m * (1 - cos_a) - l * sin_a;
		matrix[1][3] = 0;
		
		matrix[2][0] = l * n * (1 - cos_a) - m * sin_a;
		matrix[2][1] = m * n * (1 - cos_a) + l * sin_a;
		matrix[2][2] = n * n * (1 - cos_a) + cos_a;
		matrix[2][3] = 0;
		
		matrix[3][0] = 0;
		matrix[3][1] = 0;
		matrix[3][2] = 0;
		matrix[3][3] = 1;
		
		return new Matrix(matrix);
	}

	@Override
	public Rotation inverse() {
		return new Rotation(getAboutAxis(), -getAngle());
	}
	
	

}
