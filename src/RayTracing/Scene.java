package RayTracing;

import java.util.List;

public class Scene {
	
	private final Camera camera;
	
	private final Settings settings;
	
	private final List<Material> materials;
	
	private final List<Light> lights;
	
	private final List<SceneObject> sceneObjects;

	public Scene(Camera camera, Settings settings, List<Material> materials,
			List<Light> lights, List<SceneObject> sceneObjects) {
		this.camera = camera;
		this.settings = settings;
		this.materials = materials;
		this.lights = lights;
		this.sceneObjects = sceneObjects;
	}

	public Camera getCamera() {
		return camera;
	}

	public Settings getSettings() {
		return settings;
	}

	public List<Material> getMaterials() {
		return materials;
	}

	public List<Light> getLights() {
		return lights;
	}

	public List<SceneObject> getSceneObjects() {
		return sceneObjects;
	}
	
	
}
