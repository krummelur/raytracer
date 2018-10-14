package org.krummelur.raytracer;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        new Main().start();
    }

    void start() {
        ArrayList<BehaviourObject> updatable = new ArrayList<>();
        //Camera camera = new Camera(new Vector3(-1/sqrt3,-1/sqrt3, -1/sqrt3), new Vector3(1/sqrt3,1/sqrt3, 1/sqrt3));

        //Spinning camera
        Camera camera = new Camera(new Vector3(1,0,0), new Vector3(-1,0,0)) {
            @Override
            public void update(double deltaTime) {
                Vector3 rotationDelta = new Vector3(0.05, 0.05,0);
                rotate(rotationDelta.multiply(deltaTime));
            }
        };
        updatable.add(camera);
        Renderer renderer = new Renderer(new World(), camera);

        renderer.world.addObject3d(new Sphere(new Vector3(0,0, 0), 0.5, new Vector3(0,1,0)));
        renderer.world.addObject3d(new Sphere(new Vector3(0,1,0), 0.5, new Vector3(1,1,0)));
        renderer.world.addObject3d(new Sphere(new Vector3(0,-1,0), 0.5, new Vector3(0,1,1)));
        renderer.world.addObject3d(new Sphere(new Vector3(0,0,1), 0.5, new Vector3(1,1,1)));
        renderer.world.addObject3d(new Sphere(new Vector3(0,0,-1), 0.5, new Vector3(1,0,1)));
        PointLight light1 = new PointLight(new Vector3(0, 5,0),50, new Vector3(0.7,1,0));
        PointLight light2 = new PointLight(new Vector3(10,0 ,0),50, new Vector3(1,1,1));
        renderer.world.addLight(light1);
        renderer.world.addLight(light2);
        OrthogonalSet ortho = new OrthogonalSet(new Vector3(1,0,0));
        System.out.println(ortho);

        RenderWindow window = new RenderWindow(renderer.getImage());
        loop(renderer, window, updatable);
    }

    int draw(Renderer ren, RenderWindow rw) {
        return ren.render(rw);
    }

    void update(double deltaTime, ArrayList<BehaviourObject> updatable) {
        updatable.forEach(u-> u.update(deltaTime));
    }

    void loop(Renderer ren, RenderWindow rw, ArrayList<BehaviourObject> updatable) {
            int deltaTime = 60;
        while(true){
            update(deltaTime, updatable);
            deltaTime = draw(ren, rw);
        }
    }
}
