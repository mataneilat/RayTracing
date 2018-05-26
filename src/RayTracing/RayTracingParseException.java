package RayTracing;

public class RayTracingParseException extends Exception {

	public RayTracingParseException(String message) {
		super(message);
	}
	
	public RayTracingParseException(String message, Exception e) {
		super(message, e);
	}
	
	public RayTracingParseException(Exception e) {
		super(e);
	}
	
}
