package RayTracing;

import java.util.List;

public interface PrimitiveLogic {

	public List<Double> intersect(Ray ray);
	
	public Vector surfaceNormalAtPoint(Vector p);
	
}
