package org.krummelur.raytracer;

public class Camera {

    Vector3 direction, location;
    OrthogonalSet lookPlane;
    double fov;
    boolean orthographic = true;
    double orthogonalSize = 3;


    public Camera(Vector3 location, Vector3 direction) {
        this.direction = direction;
        this.location = location;
        lookPlane = new OrthogonalSet(direction);
    }

}
