package RayTracing;

public class PrimitiveFactory {
	
	public enum PrimitiveType {
		PLANE, SPHERE, TRIANGLE, CYLINDER, DISC
	}
	
	public Primitive create(PrimitiveType type, String... params) throws RayTracingParseException {
		int materialIndex;
		try {
			materialIndex = Integer.parseInt(params[params.length - 1]);
		} catch (NumberFormatException e) {
			throw new RayTracingParseException(e);
		}
		
		PrimitiveLogic logic = null;
		switch (type) {
			case PLANE:
				logic = Plane.parse(params);
				break;
			case SPHERE:
				logic = Sphere.parse(params);
				break;
			case TRIANGLE:
				logic = Triangle.parse(params);
				break;
			case CYLINDER:
				logic = Cylinder.parse(params);
				break;
			case DISC:
				logic = Disc.parse(params);
				break;
		}
		return new DefaultPrimitive(logic, materialIndex);
	}

}
