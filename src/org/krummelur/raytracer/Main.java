package org.krummelur.raytracer;

import java.util.ArrayList;

public class Main {
    int elapsedTime = 0;
    public static void main(String[] args) {
        new Main().start();
    }

    void start() {
        ArrayList<BehaviourObject> updatable = new ArrayList<>();

        //Spinning camera
        Camera camera = new Camera(new Vector3(1,0,0), new Vector3(-1,0,0)) {
            @Override
            public void update(double deltaTime) {
                Vector3 rotationDelta = new Vector3(0.1, 0.0,0);
                rotate(rotationDelta.multiply(deltaTime));
            }
        };

        updatable.add(camera);
        Renderer renderer = new Renderer(new World(), camera);

        renderer.world.addObject3d(new Sphere(new Vector3(0,0, 0), 0.5, new Vector3(0,1,0)));
        renderer.world.addObject3d(new Sphere(new Vector3(0,1,0), 0.5, new Vector3(1,1,0)));
        renderer.world.addObject3d(new Sphere(new Vector3(0,-1,0), 0.5, new Vector3(0,1,1)));
        Sphere spinningSphere2 = new Sphere(new Vector3(0,0,1), 0.5, new Vector3(1,0,1)) {
            @Override
            public void update(double deltaTime) {
               this.location = new Vector3(Math.sin(elapsedTime/1200.0+Math.PI), 0, Math.cos(elapsedTime/1200.0+Math.PI));
            }
        };
        Sphere spinningSphere = new Sphere(new Vector3(0,0,-1), 0.5, new Vector3(1,0,1)) {
            @Override
            public void update(double deltaTime) {
              this.location = new Vector3(Math.sin(elapsedTime/1200.0), 0, Math.cos(elapsedTime/1200.0));
            }
        };
        updatable.add(spinningSphere);
        updatable.add(spinningSphere2);
        renderer.world.addObject3d(spinningSphere);
        renderer.world.addObject3d(spinningSphere2);
        PointLight light1 = new PointLight(new Vector3(0, 5,0),50, new Vector3(0.7,1,0)){
            @Override
            public void update(double deltaTime) {
                this.location = new Vector3(-Math.sin(elapsedTime/750.0)*5, 0, Math.cos(elapsedTime/750.0)*5);
            }


        };
        PointLight light2 = new PointLight(new Vector3(10,0 ,0),50, new Vector3(1,1,1));
        PointLight light3 = new PointLight(new Vector3(0, 5,0),50, new Vector3(0.7,1,0)){
            @Override
            public void update(double deltaTime) {
            double rot= 1000;
            double offset = -Math.PI/2;
                this.location = new Vector3(-Math.sin(offset+(elapsedTime/rot))*20, Math.cos(offset+(elapsedTime/rot))*20, 0);
            }
        };
        updatable.add(light1);
        renderer.world.addLight(light1);
        renderer.world.addLight(light2);
        renderer.world.addLight(light3);
        updatable.add(light3);

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
            elapsedTime+=deltaTime;
            update(deltaTime, updatable);
            deltaTime = draw(ren, rw);
        }
    }
}
