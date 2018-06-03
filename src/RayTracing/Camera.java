package RayTracing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Camera {

	private static class ImageContext {
		
		private final int imageWidth;
		
		private final int imageHeight;
		
		public ImageContext(int imageWidth, int imageHeight) {
			this.imageWidth = imageWidth;
			this.imageHeight = imageHeight;
		}

		public int getImageWidth() {
			return imageWidth;
		}

		public int getImageHeight() {
			return imageHeight;
		}
		
		public double getAspectRatio() {
			return (double)imageHeight / (double)imageWidth;
		}
		
	}
	
	private final Vector position;
	
	private final Vector lookAtPoint;
	
	private final Vector rawUpVector;
	
	private final double screenDistance;
	
	private final double screenWidth;
	
	/*** Calculated Fields ***/
	
	private Vector towards = null;
	
	private ImageContext imageContext = null;
	
	private double screenHeight;
	
	private Vector upVector;

	private Camera(Vector position, Vector lookAtPoint, Vector rawUpVector,
			double screenDistance, double screenWidth) {
		this.position = position;
		this.lookAtPoint = lookAtPoint;
		this.rawUpVector = rawUpVector;
		this.screenDistance = screenDistance;
		this.screenWidth = screenWidth;
	}
	
	public static Camera parse(String[] params) throws RayTracingParseException {
		if (params.length != 11) {
			throw new RayTracingParseException("Wrong number of parameters");
		}
		
		Vector position, lookAtPoint, upVector;
		double screenDistance, screenWidth;
		
		try {
			position = Vector.parse(params[0], params[1], params[2]);
			lookAtPoint = Vector.parse(params[3], params[4], params[5]);
			upVector = Vector.parse(params[6], params[7], params[8]);
			screenDistance = Double.parseDouble(params[9]);
			screenWidth = Double.parseDouble(params[10]);
		} catch (NumberFormatException e) {
			throw new RayTracingParseException(e);
		}
		return new Camera(position, lookAtPoint, upVector, screenDistance, screenWidth);
	}

	public Vector getTowardsDirection() {
		if (towards == null) {
			towards = lookAtPoint.sub(position).normalize();
		}
		return towards;
	}
	
	public Vector getRightDirection() {
		return getTowardsDirection().cross(getUpVector()).normalize();
	}
	
	public Vector getLeftDirection() {
		return getRightDirection().multiply(-1);
	}
	
	public Vector getDownDirection() {
		return getUpVector().multiply(-1);
	}
	
	public Vector screenTopRight() {
		// TODO: NMAKE LAZY?
		Vector topRight = getPosition().add(getTowardsDirection().multiply(getScreenDistance()));
		topRight = topRight.add(getUpVector().multiply((double)getScreenHeight() / 2.0));
		return topRight.add(getRightDirection().multiply((double)getScreenWidth() / 2.0));
	}
	
	public void setImageContext(int imageWidth, int imageHeight) {
		imageContext = new ImageContext(imageWidth, imageHeight);
	}
	
	public List<Ray> constructRayThroughPixel(int i, int j) {
		return constructRaysThroughPixel(i, j, 1);
	}
	
	public List<Ray> constructRaysThroughPixel(int i, int j, int superSampling) {
		if (imageContext == null) {
			return null;
		}
		
		double deltaX = getScreenWidth() / (double) imageContext.getImageWidth();
		double deltaY = getScreenHeight() / (double) imageContext.getImageHeight();
		
		if (superSampling <= 1) {
			Vector p = screenTopRight();
			p = p.add(getLeftDirection().multiply((double)i * deltaX));
			p = p.add(getDownDirection().multiply((double)j * deltaY));
			return Arrays.asList(Ray.create(getPosition(), p));
		}
		List<Ray> retVal = new ArrayList<Ray>();
		Random rand = new Random();
		
		double superMaxDeltaX = deltaX / superSampling;
		double superMaxDeltaY = deltaY / superSampling;
		
		
		for (int x = 0; x < superSampling; x++) {
			for (int y = 0; y < superSampling; y++) {
				Vector p = screenTopRight();
				
				double superDeltaX = rand.nextDouble() * superMaxDeltaX;
				double superDeltaY = rand.nextDouble() * superMaxDeltaY;
				
				p = p.add(getLeftDirection().multiply((double)(i + (double)x / superSampling) * deltaX + superDeltaX));
				p = p.add(getDownDirection().multiply((double)(j + (double)y / superSampling) * deltaY + superDeltaY));
				
				retVal.add(Ray.create(getPosition(), p));
			}
		}
		return retVal;
	}
	
	public Vector getPosition() {
		return position;
	}

	public Vector getLookAtPoint() {
		return lookAtPoint;
	}

	public Vector getUpVector() {
		if (upVector == null) {
			Vector cross = getTowardsDirection().cross(rawUpVector).normalize();
			upVector = cross.cross(getTowardsDirection()).normalize();
		}
		return upVector;
	}

	public double getScreenDistance() {
		return screenDistance;
	}

	public double getScreenWidth() {
		return screenWidth;
	}
	
	public double getScreenHeight() {
		return imageContext.getAspectRatio() * getScreenWidth();
	}
	
}
