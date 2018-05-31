package RayTracing;

import java.util.List;

public interface Primitive {
	
	public int getMaterialIndex();
	
	public List<Double> intersect(Ray ray);
	
	public Vector surfaceNormalAtPoint(Vector p);

}
