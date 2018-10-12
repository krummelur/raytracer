package org.krummelur.raytracer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Vector3_test{

    @Test
    void Distance_test() {
        double epsilon = 0.0001;
        Vector3 vector1 = Vector3.ZERO();
        Vector3 vector2 = new Vector3(2,0,0);
        assertEquals(vector1.distance(vector2), 2);
        assertEquals(vector1.distanceSquared(vector2), 4);

        Vector3 vector3 = new Vector3(1/Math.sqrt(3), 1/Math.sqrt(3), 1/Math.sqrt(3));

        assertEquals(vector3.distance(vector1), 1, epsilon);
        assertEquals(vector3.distanceSquared(vector1), 1, epsilon);

        Vector3 vector4 = new Vector3(2/Math.sqrt(3), 2/Math.sqrt(3), 2/Math.sqrt(3));

        assertEquals(2, vector1.distance(vector4), epsilon);
        assertEquals(4, vector1.distanceSquared(vector4), epsilon);

        Vector3 vector5 = new Vector3(-2/Math.sqrt(3), 2/Math.sqrt(3), -2/Math.sqrt(3));

        assertEquals(2, vector1.distance(vector5), epsilon);
        assertEquals(4, vector1.distanceSquared(vector5), epsilon);

        Vector3 vector6 = new Vector3(-2/Math.sqrt(3), -2/Math.sqrt(3), -2/Math.sqrt(3));

        assertEquals(vector1.distance(vector6), 2, epsilon);
        assertEquals(vector1.distanceSquared(vector6), 4, epsilon);
    }
}
