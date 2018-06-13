package RayTracing;

public class Matrix {
	
	private final double[][] matrix;
	
	private boolean isTransposed = false;
	
	public Matrix(double[][] matrix) {
		this(matrix, false);
	}
	
	private Matrix(double[][] matrix, boolean isTransposed) {
		this.matrix = matrix;
		this.isTransposed = isTransposed;
	}
	
	public int getWidth() {
		return isTransposed ? matrix[0].length : matrix.length; 
	}
	
	public int getHeight() {
		return isTransposed ? matrix.length : matrix[0].length;
	}
	
	public Matrix transpose() {
		return new Matrix(matrix, !isTransposed);
	}
	
	public double get(int x, int y) {
		return isTransposed ? matrix[y][x] : matrix[x][y];
	}
	
	public void set(int x, int y, double element) {
		if (isTransposed) {
			matrix[y][x] = element; 
		} else {
			matrix[x][y] = element;
		}
	}
	
	public Vector homogeneousMultiply(Vector v) {
		if (getWidth() != 4 || getHeight() != 4) {
			return null;
		}
		double[] retXYZ = new double[3];
		double[] vectorXYZ = v.asArray();
		
		for (int i = 0; i < 3; i++) {
			retXYZ[i] = get(i, 3);
			for (int j = 0; j < 3; j++) {
				retXYZ[i] = retXYZ[i] + get(i, j) * vectorXYZ[j];
			}
		}
		return Vector.fromArray(retXYZ);
	}
	
	public Matrix multiply(Matrix mat) {
		if (getHeight() != mat.getWidth()) {
			return null;
		}
		
		double[][] newMatrix = new double[getWidth()][mat.getHeight()];

		for (int i = 0; i < getWidth(); i++) {
			for (int j = 0; j < mat.getHeight(); j++) {
				newMatrix[i][j] = 0;
				for (int k = 0; k < getHeight(); k++) {
					newMatrix[i][j] += get(i, k) * mat.get(k, j);
				}
			}
		}
		return new Matrix(newMatrix);
	}

}
