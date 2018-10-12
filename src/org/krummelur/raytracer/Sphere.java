package org.krummelur.raytracer;

public class Sphere extends Shape3d {
	private final double epsilon = 0.000001;

	double radius;
	
	public Sphere(Vector3 location, double radius) {
		this.radius = radius;
		this.location = location;
	}

	//Ray must be normalized
	@Override 
	double hit(Ray normalizedRay) {
		//some edge cases caused mindistanceRaySohereSquared to become extremely small negative values when passing right through sphere, causing ray miss to be reported
		//Get distance from ray origin to sphere origin
		double rayToSphereSquared = normalizedRay.origin.distanceSquared(this.location);
		double travelDistanceClosestPoint = normalizedRay.direction.dot(this.location.subtract(normalizedRay.origin));
		if(travelDistanceClosestPoint < 0) {
			return -1;
		}
		double minDistanceRaySphereSquared = rayToSphereSquared - (travelDistanceClosestPoint*travelDistanceClosestPoint);
			//if minDistanceRaySquared is < 0, that means that the closest distance is farther than the radius, meaning the ray missed
		if (minDistanceRaySphereSquared < -epsilon) {
			return -1;
		}
		double halfDistanceInsideSphere = Math.sqrt(this.radius*this.radius - minDistanceRaySphereSquared);
		return travelDistanceClosestPoint - halfDistanceInsideSphere;
		//Vector3 intersectionPoint = normalizedRay.travel(rayInstersectionDistance );
	}

	@Override
	Vector3 surfaceNormal(Vector3 point) {
		return point.subtract(this.location).divide(this.radius);
	}
}