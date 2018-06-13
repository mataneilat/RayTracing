package RayTracing;

import java.util.ArrayList;
import java.util.List;

public class Disc implements PrimitiveLogic {
	
	private final Rotation rotation;
	
	private final Translation translation;
	
	private final double radius;

	public Disc(Rotation rotation, Translation translation, double radius) {
		this.rotation = rotation;
		this.translation = translation;
		this.radius = radius;
	}
	
	public static Disc parse(String... params) throws RayTracingParseException {
		if (params.length < 7) {
			throw new RayTracingParseException("Wrong number of parameters");
		}
		Rotation rotation;
		Translation translation;
		double radius;
		try {
			rotation = Rotation.parse(params[0], params[1], params[2], params[3]);
			translation = Translation.parse(params[4], params[5], params[6]);
			radius = Double.parseDouble(params[7]);
		} catch (NumberFormatException e) {
			throw new RayTracingParseException(e);
		}
		return new Disc(rotation, translation, radius);
	}
	
	@Override
	public List<Double> intersect(Ray ray) {
		
		Ray transformedRay = ray.transform(rotation.inverse(), translation.inverse());
		
		Plane p = new Plane(new Vector(0,1,0) , 0);
		List<Double> tList = p.intersect(transformedRay);
		if (tList == null) {
			return null;
		}
		
		List<Double> retVal = new ArrayList<Double>();
		for (double t : tList) {
			Vector intersectionPoint = transformedRay.pointForT(t);
			if (intersectionPoint.norm() <= radius) {
				retVal.add(t);
			}
		}
		
		return retVal;
	}

	@Override
	public Vector surfaceNormalAtPoint(Vector p) {
		return rotation.createHomogeneousMatrix().multiply(translation.createHomogeneousMatrix()).homogeneousMultiply(new Vector(0,1,0));
	}
	

}
