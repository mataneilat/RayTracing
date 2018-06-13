package RayTracing;

public class Translation implements AffineTransformation<Translation> {
	
	private final Vector t;

	public Translation(Vector t) {
		this.t = t;
	}
	
	public Vector getT() {
		return t;
	}
	
	public static Translation parse(String... params) throws RayTracingParseException {
		if (params.length < 3) {
			throw new RayTracingParseException("Wrong number of parameters");
		}
		return new Translation(Vector.parse(params));
	}

	@Override
	public Matrix createHomogeneousMatrix() {
		double[][] matrix = new double[4][4];
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (i == j) {
					matrix[i][j] = 1;
				} else {
					matrix[i][j] = 0;
				}
			}
		}
		
		matrix[3][0] = 0;
		matrix[3][1] = 0;
		matrix[3][2] = 0;
		
		matrix[0][3] = t.getX();
		matrix[1][3] = t.getY();
		matrix[2][3] = t.getZ();
		
		matrix[3][3] = 1;
		
		return new Matrix(matrix);
	}

	@Override
	public Translation inverse() {
		return new Translation(getT().multiply(-1));
	}
	
	
	

}
