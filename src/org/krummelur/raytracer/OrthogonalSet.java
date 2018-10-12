package org.krummelur.raytracer;

public class OrthogonalSet {
    Vector3 forward, up, right;
    public OrthogonalSet(Vector3 forward) {
        this.forward = forward;
        recalculate();
    }

    private void recalculate() {
        //Make up a right vector that is on the xy plane (roll not necessary)
        Vector3 normalizedforwardXY = new Vector3(forward.x, forward.y, 0);//.normalize();
        double thetaForward = Math.asin(normalizedforwardXY.y);
        //2 cos / sin, could be performance hog
        right = new Vector3(Math.cos((Math.PI / 2)+thetaForward),Math.sin((Math.PI / 2)+thetaForward), 0);
        //right = new Vector3(thetaForward,thetaForward, 0);


        //this.right = new Vector3(forward.x*xSign, forward.y*ySign, 0).normalize();

        //up vector is gotten by crossing forward with right, making up a new proper (with real yaw) up vector
        this.up = forward.cross(right);
        }



    @Override
    public String toString() {
        return "{\n  forward: " + forward + "\n  up: " + up + "\n  " + "right: " + right + "\n}";
    }
}
