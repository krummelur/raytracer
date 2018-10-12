package org.krummelur.raytracer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Sphere_test {

    @Test
    void hit_test() {
        double epsilon = 0.00001;
        Sphere sphere = new Sphere(Vector3.ZERO(), 1);
        Ray ray = new Ray(new Vector3(2,0,0), new Vector3(-1,0,0));
        assertEquals(1, sphere.hit(ray), epsilon);
        ray = new Ray(new Vector3(2,1, 0), new Vector3(-1,0,0));
        assertEquals(2, sphere.hit(ray), epsilon);
        double sqrt3 = Math.sqrt(3);
        ray = new Ray(new Vector3(2/sqrt3,2/sqrt3, 2/sqrt3), new Vector3(-1/sqrt3,-1/sqrt3, -1/sqrt3));
        assertEquals(1, sphere.hit(ray), epsilon);
    }

}
