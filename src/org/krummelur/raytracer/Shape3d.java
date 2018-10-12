package org.krummelur.raytracer;

public abstract class Shape3d extends Object3d{
    Vector3 color;
    abstract double hit(Ray ray);
    abstract Vector3 surfaceNormal(Vector3 point);
}
