package org.krummelur.raytracer;

public abstract class Camera implements BehaviourObject {

    Vector3 direction, location;
    OrthogonalSet lookPlane;
    double fov;
    boolean orthographic = true;
    double orthogonalSize = 3;
    Vector3 center = Vector3.ZERO();



    public Camera(Vector3 location, Vector3 direction) {
        this.direction = direction;
        this.location = location;
        lookPlane = new OrthogonalSet(direction);
    }

    public void rotate(Vector3 rotation) {
        this.lookPlane.rotate(rotation);
        //if the camera is orthographic, move it 'infinitely' far away from the center, in the opposite of it's direction. Set to 1000 since we will lose precision as the camera moves away
        if (this.orthographic) {
            this.location = this.center.subtract(this.direction.multiply(1000));
        }
    }

}
