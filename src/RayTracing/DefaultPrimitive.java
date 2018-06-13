package RayTracing;

public class DefaultPrimitive implements Primitive {

	private final PrimitiveLogic logic;
	
	private final int materialIndex;
	
	public DefaultPrimitive(PrimitiveLogic logic, int materialIndex) {
		this.logic = logic;
		this.materialIndex = materialIndex;
	}

	@Override
	public int getMaterialIndex() {
		return materialIndex;
	}

	@Override
	public PrimitiveLogic getPrimitiveLogic() {
		return logic;
	}

	
}
