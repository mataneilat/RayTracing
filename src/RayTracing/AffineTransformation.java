package RayTracing;

public interface AffineTransformation<T extends AffineTransformation<?>> {
	
	public Matrix createHomogeneousMatrix();
	
	public T inverse();

}
