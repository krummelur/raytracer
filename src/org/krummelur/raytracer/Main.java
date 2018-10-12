package org.krummelur.raytracer;

public class Main {

    public static void main(String[] args) {
	Sphere sphere = new Sphere(Vector3.ZERO(), 1);
	Ray ray = new Ray(new Vector3(2,0,0), new Vector3(-1,0,0));
    //System.out.println(sphere.hit(ray));
    ray = new Ray(new Vector3(2,1, 0), new Vector3(-1,0,0));
    //System.out.println(sphere.hit(ray));
    double sqrt3 = Math.sqrt(3);
    ray = new Ray(new Vector3(2/sqrt3,2/sqrt3, 2/sqrt3), new Vector3(-1/sqrt3,-1/sqrt3, -1/sqrt3));
    //System.out.println(sphere.hit(ray));

    OrthogonalSet orth = new OrthogonalSet(new Vector3 (1/sqrt3,1/sqrt3, 1/sqrt3));
    System.out.println(orth);
    Camera camera = new Camera(new Vector3(2,0, 0), new Vector3(-1,0,0));
    Renderer renderer = new Renderer(new World(), camera);
    renderer.world.addObject3d(new Sphere(new Vector3(0,0, 0), 1));
    renderer.world.addObject3d(new Sphere(new Vector3(0.5,1,0), 0.5));
    renderer.world.addObject3d(new Sphere(new Vector3(-0.5,-1,0), 0.5));
    renderer.world.addObject3d(new Sphere(new Vector3(0.5,0,1), 0.5));
    renderer.world.addObject3d(new Sphere(new Vector3(-0.5,0,-1), 0.5));
    PointLight light1 = new PointLight(new Vector3(0, 5,0),100, new Vector3(0,1,0));
    PointLight light2 = new PointLight(new Vector3(10,0 ,0),50, new Vector3(0,1,0.5));
    renderer.world.addLight(light1);
    renderer.world.addLight(light2);
    OrthogonalSet ortho = new OrthogonalSet(new Vector3(1,0,0));
    System.out.println(ortho);
    renderer.render();
    }
}
