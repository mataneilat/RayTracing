package RayTracing;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scene {
	
	private final Camera camera;
	
	private final Settings settings;
	
	private final List<Material> materials;
	
	private final List<Light> lights;
	
	private final List<Primitive> primitives;

	public Scene(Camera camera, Settings settings, List<Material> materials,
			List<Light> lights, List<Primitive> primitives) {
		this.camera = camera;
		this.settings = settings;
		this.materials = materials;
		this.lights = lights;
		this.primitives = primitives;
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

	public List<Primitive> getPrimitives() {
		return primitives;
	}

	
	public void setImageContext(int imageWidth, int imageHeight) {
		getCamera().setImageContext(imageWidth, imageHeight);
	}
	
	public Color calculateColorForPixel(int i, int j) {
		
		List<Ray> rays = constructRaysThroughPixel(i, j);
		float r = 0;
		float g = 0;
		float b = 0;
		for (Ray ray : rays) {
			Color tracedColor = calculateColor(ray);
			r += tracedColor.getRed();
			g += tracedColor.getGreen();
			b += tracedColor.getBlue();
		}
		float divisor = (float)rays.size() * 255.0f;
		return new Color(r / divisor, g / divisor, b / divisor);
	}
	
	public List<Ray> constructRaysThroughPixel(int i, int j) {
		return getCamera().constructRaysThroughPixel(i, j, getSettings().getSuperSamplingLevel());
	}
	
	public Color calculateColor(Ray viewRay) {
		return calculateColor(viewRay, getSettings().getMaxRecursionLevel(), Color.WHITE);
	}
	
	private Color calculateColor(Ray viewRay, int recursiveCount, Color contribution) {
		try {
			if (recursiveCount <= 0) {
				return getSettings().getBackgroundColor();
			}
			
			Intersection intersection = intersect(viewRay);
			
			if (intersection == null) {
				return getSettings().getBackgroundColor();
			}
			
			Primitive primitive = intersection.getPrimitive();
			
			if (ColorUtils.colorDifference(contribution, Color.BLACK) <= 0.05) {
				return getPrimitiveMaterial(primitive).getDiffuseColor();
			}
			
			Vector intersectionPoint = intersection.getIntersectionPoint();
			
			Vector surfaceNormal = primitive.getPrimitiveLogic().surfaceNormalAtPoint(intersectionPoint);
			
			surfaceNormal = surfaceNormal.dot(viewRay.getDirection().multiply(-1)) > 0 ? surfaceNormal : surfaceNormal.multiply(-1);
			
			Ray reflectionRay = calculateReflectionRay(primitive, surfaceNormal, viewRay.getDirection(), intersectionPoint);
			
			Color color = Color.BLACK;
			
			for (Light light: getLights()) {
				

				float illuminationFactor = (float) hittingRaysRatio(light, intersectionPoint);
				
	//			if (illuminationFactor == 0) {
	//				illuminationFactor = (float) ((float) 1.0 - light.getShadowIntensity());
	//			}
				illuminationFactor = illuminationFactor + (1 - illuminationFactor) * (float) (1 - light.getShadowIntensity());
				
				if (illuminationFactor == 0) {
					continue;
				}
				
				Color diffuseColor = calculateDiffuseColor(primitive, surfaceNormal, intersectionPoint, light);
				
				Color specularColor = calculateSpecularColor(primitive, reflectionRay.getDirection(), intersectionPoint, light);
				
				Color lightContribution = ColorUtils.add(diffuseColor, specularColor);
				
				lightContribution = ColorUtils.multiplyComponents(lightContribution, illuminationFactor);
				
				color = ColorUtils.add(color, lightContribution);
			}
			
			// Calculate Transparency color
			float transparency = (float) getPrimitiveMaterial(primitive).getTransparency();
			if (transparency > 0) {
				Ray transparencyRay = new Ray(intersectionPoint, viewRay.getDirection());
				
				Color transparencyColor = calculateColor(transparencyRay, recursiveCount,
						ColorUtils.multiplyComponents(contribution, transparency));
				
				transparencyColor = ColorUtils.multiplyComponents(transparencyColor, transparency);
				color = ColorUtils.multiplyComponents(color, 1 - transparency);
				color = ColorUtils.add(color, transparencyColor);
			}
			
			// Calculate Reflection color
			Color materialRelectionColor = getPrimitiveMaterial(primitive).getReflectionColor();
			Color reflectionColor = calculateColor(reflectionRay, recursiveCount - 1,
					ColorUtils.multiplyComponents(contribution, materialRelectionColor));
			reflectionColor = ColorUtils.multiplyComponents(reflectionColor, materialRelectionColor);
			
			color = ColorUtils.add(color, reflectionColor);
			
			return color;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	
	private Color calculateSpecularColor(Primitive primitive, Vector reflectionRayDirection, 
											Vector intersectionPoint, Light light) {
		
		Material primitiveMaterial = getPrimitiveMaterial(primitive);
		Ray lightRay = Ray.create(light.getPosition(), intersectionPoint);
		
		float specular = (float) lightRay.getDirection().multiply(-1).dot(reflectionRayDirection);
		if (specular > 0) {
			Color lightSpecularColor = ColorUtils.multiplyComponents(light.getColor(), (float)light.getSpecularIntensity());
			Color c = ColorUtils.multiplyComponents(primitiveMaterial.getSpecularColor(), lightSpecularColor);
			return ColorUtils.multiplyComponents(c, (float) Math.pow(specular, 
					primitiveMaterial.getSpecularityCoefficient()));
		}
		return Color.BLACK;
	}
	
	private Ray calculateReflectionRay(Primitive primitive, Vector surfaceNormal, Vector rayDirection, Vector intersectionPoint) {
		double k = -2.0 * rayDirection.dot(surfaceNormal);
		Vector reflectionDirection = new Vector(k * surfaceNormal.getX() + rayDirection.getX(),
												k * surfaceNormal.getY() + rayDirection.getY(),
												k * surfaceNormal.getZ() + rayDirection.getZ());
		return new Ray(intersectionPoint, reflectionDirection.normalize());
	}
	
	private Color calculateDiffuseColor(Primitive primitive, Vector surfaceNormal, 
			Vector intersectionPoint, Light light) {
		Material primitiveMaterial = getPrimitiveMaterial(primitive);
		Ray lightRay = Ray.create(light.getPosition(), intersectionPoint);
		
		float diffuse = (float) lightRay.getDirection().multiply(-1).dot(surfaceNormal);
		if (diffuse <= 0) {
			return Color.BLACK;
		}
		Color c = ColorUtils.multiplyComponents(primitiveMaterial.getDiffuseColor(), light.getColor());
		return ColorUtils.multiplyComponents(c, diffuse);
	}
	
	private double hittingRaysRatio(Light light, Vector point) {
		List<Ray> softShadowRays = light.getLightsRayToPoint(point, getSettings().getShadowRaysCount());
		return (double)hittingRaysCount(softShadowRays, point) / (double) softShadowRays.size();
	}
	
	private int hittingRaysCount(List<Ray> softShadowRays, Vector point) {
		
		int hitCount = 0;
		
		for (Ray softShadowRay : softShadowRays) {
			Intersection intersection = intersect(softShadowRay);
			if (intersection == null) {
				continue;
			}
			
			double pointDistanceFromLight = point.distanceFrom(softShadowRay.getP0());
			double intersectionDistanceFromLight = intersection.getIntersectionPoint().distanceFrom(softShadowRay.getP0());
			
			if (pointDistanceFromLight < intersectionDistanceFromLight + 0.0001) {
				hitCount++;
			}
		}
		return hitCount;
	}
    
    private Intersection intersect(Ray ray) {
    	double minT = Double.MAX_VALUE;
    	Primitive minPrimitive = null;
    	
    	for (Primitive primitive : getPrimitives()) {
    		List<Double> intersections = primitive.getPrimitiveLogic().intersect(ray);
    		if (intersections == null) {
    			continue;
    		}
    		for (Double t : intersections) {
    			if (t <= 0.00001) {
    				continue;
    			}
    			if (t < minT) {
        			minT = t;
        			minPrimitive = primitive;
        		}
    		}
    		
    	}
    	if (minT == Double.MAX_VALUE) {
    		return null;
    	} 
    	return new Intersection(ray, minT, minPrimitive);
    }
    
    private Material getPrimitiveMaterial(Primitive primitive) {
    	return getMaterials().get(primitive.getMaterialIndex() - 1);
    }
	
}
