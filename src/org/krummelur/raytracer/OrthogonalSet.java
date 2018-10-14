package org.krummelur.raytracer;


public class OrthogonalSet {

    final double Deg_To_Rad = Math.PI/180;
    final double Rad_To_Deg = 180/Math.PI;
    Vector3 forward, up, right;
    public OrthogonalSet(Vector3 forward) {
        this.forward = forward;
        recalculate();
    }

    void rotate(Vector3 degrees) {
        //yaw, pitch, roll
        degrees  = degrees.multiply(Deg_To_Rad);
        //degrees.x = Math.PI/2;
        double x = Math.sin(Math.asin(this.forward.x)+degrees.x*Deg_To_Rad);
        double y = Math.cos(Math.acos(this.forward.y)+degrees.y*Deg_To_Rad);
        x = this.forward.x *Math.cos(degrees.x) - this.forward.y*Math.sin(degrees.x);
        y = this.forward.x *Math.sin(degrees.x) + this.forward.y*Math.cos(degrees.x);
        this.forward.x = x;
        this.forward.y = y;
        recalculate();
    }

    private void recalculate() {
        //Make up a right vector that is on the xy plane (roll not necessary)
        Vector3 normalizedforwardXY = new Vector3(forward.x, forward.y, 0).normalize();
        double thetaForward = Math.asin(normalizedforwardXY.y);
        thetaForward = Math.atan2(normalizedforwardXY.x, normalizedforwardXY.y);
        //2 cos / sin, could be performance hog
        right = new Vector3(Math.sin(thetaForward-(Math.PI / 2)),Math.cos(thetaForward-(Math.PI / 2)), 0).normalize();
        double thetaRight = Math.atan2(right.x, right.y);
   //     System.out.println((thetaForward)*Rad_To_Deg);
   //     System.out.println((thetaRight)*Rad_To_Deg);
        //System.out.println(this);

//        double rx = normalizedforwardXY.x * Math.cos(90) - normalizedforwardXY.y * Math.sin(90);
//        double ry = normalizedforwardXY.x * Math.sin(90) + normalizedforwardXY.y * Math.cos(90);
//        right.x = rx;
//        right.y =ry;

        //right = new Vector3(thetaForward,thetaForward, 0);


        //this.right = new Vector3(forward.x*xSign, forward.y*ySign, 0).normalize();

        //up vector is gotten by crossing forward with right, making up a new proper (with real yaw) up vector
        this.up = forward.cross(right).normalize();
    }



    @Override
    public String toString() {
        return "{\n  forward: " + forward + "\n  up: " + up + "\n  " + "right: " + right + "\n}";
    }
}
