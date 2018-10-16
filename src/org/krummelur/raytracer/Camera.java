package org.krummelur.raytracer;

public abstract class Camera implements BehaviourObject {

    Vector3 location;
    OrthogonalSet lookPlane;
    double fov;
    boolean orthographic = true;
    double orthogonalSize = 3;
    Vector3 center = Vector3.ZERO();


    Vector3 direction() {
        return this.lookPlane.forward;
    }
    public Camera(Vector3 location, Vector3 direction) {
        this.location = location;
        lookPlane = new OrthogonalSet(direction);
        this.location = this.center.subtract(this.direction().multiply(100));

    }


    public void rotate(Vector3 rotation) {
        this.lookPlane.rotate(rotation);
        //this.lookPlane.forward = this.lookPlane.forward.normalize();
        //if the camera is orthographic, move it 'infinitely' far away from the center, in the opposite of it's direction. Set to 1000 since we will lose precision as the camera moves away
        /*
        this.lookPlane.forward = new Vector3(-0.9446110269413361, 0.0, 0.32819202522128077).normalize();
        this.lookPlane.up= new Vector3( 0.32819202560887367, -4.0191931366759337E-17, 0.9446110280569164);
        this.lookPlane.right= new Vector3( -1.2246467991473532E-16, -1.0, 0.0);
        this.location = new Vector3( 94.46110269413361, 0.0, -32.81920252212808);
        */
        if (this.orthographic) {
        this.location = this.center.subtract(this.direction().multiply(100));
        }
        //System.out.println("LOOKPLANE " + this.lookPlane);
        //System.out.println("CENTER: " + this.center);
        //System.out.println("LOCATION: " + this.location);
    }

}
