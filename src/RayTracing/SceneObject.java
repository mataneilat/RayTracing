package RayTracing;

import java.util.List;

public interface SceneObject {
	
	public int getMaterialIndex();
	
	public List<Double> intersect(Ray ray);

}
