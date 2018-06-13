package RayTracing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cylinder implements PrimitiveLogic {
	
	private final Rotation rotation;
	
	private final Translation translation;
	
	private final double radius;
	
	private final double yLength;

	public Cylinder(Rotation rotation, Translation translation, double radius, double yLength) {
		this.rotation = rotation;
		this.translation = translation;
		this.radius = radius;
		this.yLength = yLength;
	}
	
	public static Cylinder parse(String... params) throws RayTracingParseException {
		if (params.length < 9) {
			throw new RayTracingParseException("Wrong number of parameters");
		}
		Rotation rotation;
		Translation translation;
		double radius;
		double yLength;
		try {
			rotation = Rotation.parse(params[0], params[1], params[2], params[3]);
			translation = Translation.parse(params[4], params[5], params[6]);
			radius = Double.parseDouble(params[7]);
			yLength = Double.parseDouble(params[8]);
		} catch (NumberFormatException e) {
			throw new RayTracingParseException(e);
		}
		return new Cylinder(rotation, translation, radius, yLength);
	}

	@Override
	public List<Double> intersect(Ray ray) {
		
		Ray transformedRay = ray.transform(rotation.inverse(), translation.inverse());
		
		double x_d = transformedRay.getDirection().getX();
		double y_d = transformedRay.getDirection().getY();
		double z_d = transformedRay.getDirection().getZ();
		
		double x_e = transformedRay.getP0().getX();
		double y_e = transformedRay.getP0().getY();
		double z_e = transformedRay.getP0().getZ();
		
		double a = x_d * x_d + z_d * z_d;
		double b = 2 * x_e * x_d + 2 * z_e * z_d;
		double c = x_e * x_e + z_e * z_e - radius * radius;
		
		double discr = b * b - 4 * a * c;
		if (discr < 0) {
			return null;
		}
		
		List<Double> retVal = new ArrayList<Double>();
		
		if (discr == 0) {
			double t = -b / (2 * a);
			if (Math.abs(y_e + t * y_d) < yLength) {
				retVal.add(t);
			}
		} else {
			double sqrtDisc = Math.sqrt(discr);
			
			double t1 = (-b + sqrtDisc) / (2 * a);
			double t2 = (-b - sqrtDisc) / (2 * a);
			
			if (Math.abs(y_e + t1 * y_d) < yLength) {
				retVal.add(t1);
			}
			if (Math.abs(y_e + t2 * y_d) < yLength) {
				retVal.add(t2);
			}
		}

		
		// Caps intersection
		Disc topCap = new Disc(Rotation.noRotation(), new Translation(new Vector(0, yLength, 0)), 
				radius);
		Disc bottomCap = new Disc(Rotation.noRotation(), new Translation(new Vector(0, -yLength, 0)), 
				radius);
		
		List<Double> topCapIntersections = topCap.intersect(transformedRay);
		if (topCapIntersections != null) {
			retVal.addAll(topCapIntersections);
		}
		List<Double> bottomCapIntersections = bottomCap.intersect(transformedRay);
		if (bottomCapIntersections != null) {
			retVal.addAll(bottomCapIntersections);
		}
		
		return retVal;
	}
	
	@Override
	public Vector surfaceNormalAtPoint(Vector p) {
		Matrix inverseRotationMatrix = rotation.inverse().createHomogeneousMatrix();
		Matrix inverseTranslationMatrix = translation.inverse().createHomogeneousMatrix();
		
		Matrix transformationMatrix = rotation.createHomogeneousMatrix().multiply(translation.createHomogeneousMatrix());
		Matrix inverseTransformationMatrix = inverseRotationMatrix.multiply(inverseTranslationMatrix);
		
		Vector transformedPoint = inverseTransformationMatrix.homogeneousMultiply(p);
		
		if (Utils.approximatelyEquals(transformedPoint.getY(), yLength) || 
				Utils.approximatelyEquals(transformedPoint.getY(), -yLength)) {
			return transformationMatrix.homogeneousMultiply(new Vector(0, 1, 0)).normalize();
		}
		Vector circleCenter = new Vector(0,0, transformedPoint.getY());
		return transformationMatrix.homogeneousMultiply(p.sub(circleCenter).normalize()).normalize();
	}
	
	

}
