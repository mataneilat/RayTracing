package RayTracing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Square {
	
	private final Ray perpendicularRay;
	
	private final double length;
	
	private Vector topRight = null;
	
	private Vector upDirection = null;
	
	private Vector downDirection = null;
	
	private Vector rightDirection = null;
	
	private Vector leftDirection = null;
	
	public Square(Ray perpendicularRay, double length) {
		this.perpendicularRay = perpendicularRay;
		this.length = length;
	}
	
	public Vector getUpDirection() {
		if (upDirection == null) {
			Vector rayDirection = getPerpendicularRay().getDirection();
			upDirection = new Vector(1, 1, - (rayDirection.getX() + rayDirection.getY()) / rayDirection.getZ());
			upDirection = upDirection.normalize();
		}
		return upDirection;
	}
	
	public Vector getDownDirection() {
		if (downDirection == null) {
			downDirection = getUpDirection().multiply(-1);
		}
		return downDirection;
	}
	
	public Vector getRightDirection() {
		if (rightDirection == null) {
			rightDirection = getPerpendicularRay().getDirection().cross(getUpDirection()).normalize();
		}
		return rightDirection;
	}
	
	public Vector getLeftDirection() {
		if (leftDirection == null) {
			leftDirection = getRightDirection().multiply(-1);
		}
		return leftDirection;
	}

	public Vector getTopRight() {
		if (topRight == null) {
			Vector tRight = getPerpendicularRay().getP0();
			tRight = tRight.add(getUpDirection().multiply(getLength() / 2.0));
			tRight = tRight.add(getRightDirection().multiply(getLength() / 2.0));
			topRight = tRight;
		}
		return topRight;
	}
	
	public List<Vector> randomizedGridPoints(int N) {
		double cellLength = getLength() / N;
		List<Vector> retVal = new ArrayList<Vector>();
		Random rand = new Random();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				
				double leftDelta = rand.nextDouble() * cellLength;
				double downDelta = rand.nextDouble() * cellLength;
				
//				double leftDelta = 0.5 * cellLength;
//				double downDelta = 0.5 * cellLength;
				
				Vector gridPoint = getTopRight();
				gridPoint = gridPoint.add(getLeftDirection().multiply(cellLength * i + leftDelta));
				gridPoint = gridPoint.add(getDownDirection().multiply(cellLength * j + downDelta));
			
				retVal.add(gridPoint);
			}
		}
		return retVal;
		
	}
	
	public Ray getPerpendicularRay() {
		return perpendicularRay;
	}
	
	public double getLength() {
		return length;
	}
	

	

}
