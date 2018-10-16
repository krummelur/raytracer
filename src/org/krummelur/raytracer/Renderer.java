package org.krummelur.raytracer;

import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Renderer {

    private final double epsilon = 0.00001;
    World world;
    int resolutionX = 300;
    int resolutionY = 300;
    int image[] = new int[resolutionY * resolutionY];
    BufferedImage bi = new BufferedImage(resolutionY, resolutionX, BufferedImage.TYPE_INT_RGB);
    Camera camera;

    public Renderer(World world, Camera camera) {
        this.world = world;
        this.camera = camera;
    }

    BufferedImage getImage() {
        return bi;
    }

    int render(RenderWindow window) {

        for (int performanceLoops = 0; performanceLoops <  1; performanceLoops++) {}
        //a slight optimization, sort objects in order of distance to camera, then render in order until one hits the camera
        long startTime = System.nanoTime();
            for (int y = 0; y < resolutionY; y++) {
                for (int x = 0; x < resolutionX; x++) {
                    double reflectivityFactor = 0.6;
                    //calculate if the ray hit any object, and at what distance
                    Ray cameraRay = new Ray(this.camera.location
                            .add(this.camera.lookPlane.up.multiply((this.camera.orthogonalSize / 2) - ((double) (y) / (double) (resolutionY) * this.camera.orthogonalSize))
                                    .add(this.camera.lookPlane.right.multiply((this.camera.orthogonalSize / 2) - ((double) (x) / (double) (resolutionX) * this.camera.orthogonalSize)))),
                            camera.direction());
                    Vector3 surfaceColor = Vector3.ZERO();
                    int maxIterations = 5;
                    for (int iterations = 0; iterations < maxIterations && cameraRay != null; iterations++) {
                        Pair<Vector3, Ray> results = getColorForRay(cameraRay);
                        cameraRay = results.getValue();
                        surfaceColor = surfaceColor.add(results.getKey().multiply(Math.pow(reflectivityFactor, iterations)));
                        //surfaceColor = results.getKey();
                    }
                    surfaceColor = surfaceColor.clampMaximum(255);
                    image[resolutionX * y + x] = new Color((int) (surfaceColor.x), (int) (surfaceColor.y), (int) (surfaceColor.z)).getRGB();
                }
            }
            bi.setRGB(0, 0, resolutionX, resolutionY, this.image, 0, resolutionX);
            long endTime = System.nanoTime();
            System.out.println(((endTime - startTime) / 1000000));
            window.start();
            return (int)((endTime - startTime) / 1000000);
            /*
            File outputfile = new File("image"  + performanceLoops + ".png");
            try {
                ImageIO.write(bi, "png", outputfile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
    }

    Pair<Vector3, Ray> getColorForRay(Ray cameraRay) {
        //This optimization could possibly be of use when there are lots of small objects.
        //Collections.sort(world.objects, (o1, o2) -> (int)(camera.location.distanceSquared(o1.location)-o1.radius*o1.radius - camera.location.distanceSquared(o2.location)-o2.radius*o2.radius));

        Ray returnRay = null;
        //Get closest point on the surface of an object in ray direction
        Shape3d closestObject = null;
        double closestHit = Integer.MAX_VALUE;
        Vector3 surfaceColor = Vector3.ZERO();

        //we can optimize a bit and sort the spheres after their distance to camera ((distancesqr-radius))
        //then stop the process as soon as we find a hit.
        for (Shape3d o : world.objects()) {
            double hit = o.hit(cameraRay);
            if (hit != -1 && hit < closestHit) {
                closestHit = hit;
                closestObject = o;
                //Corresponding break for the optimization above. unfortunately, the creation of the map is
                //slower than the speedup from the optimization
                //break;
            }
        }

        //if there was a hit calculate lighing
        if (closestHit != Integer.MAX_VALUE) {
            for (Light l : world.lights()) {
                //first check if there is something obstruction the light ray
                Vector3 hitLocation = cameraRay.travel(closestHit);
                boolean lightShadowed = false;
                double lightToSurfaceDistance = l.location.distance(hitLocation);
                Ray lightRay = new Ray(l.location, hitLocation.subtract(l.location).normalize());
                for (int i = 0; i < world.objects().size() && !lightShadowed; i++) {
                    double hit = world.objects.get(i).hit(lightRay);
                    if (hit != -1 && hit < lightToSurfaceDistance - epsilon) {
                        lightShadowed = true;
                    }
                }

                    Vector3 surfaceNormal = closestObject.surfaceNormal(hitLocation);
                if (!lightShadowed) {
                    //start with diffuse light
                    Vector3 lightToSurfaceNormal = hitLocation.subtract(l.location).normalize();
                    double dot = -1 * surfaceNormal.dot(lightToSurfaceNormal);
                    if (dot > 0) {
                        surfaceColor = surfaceColor.add(l.color.multiply(l.strength * 10 / hitLocation.distance(l.location)).multiply(dot).multiply(closestObject.color));

                        //add specular lightz§
                        Vector3 surfaceToCameraDir = hitLocation.subtract(cameraRay.origin).normalize();
                        Vector3 reflectionDirection = lightToSurfaceNormal.reflect(surfaceNormal).normalize();
                        double specular = -1 * surfaceToCameraDir.dot(reflectionDirection);
                        if (specular > 0) {
                            specular = Math.pow(specular, 120);
                            surfaceColor = surfaceColor.add(new Vector3(specular, specular, specular).multiply(l.strength*2));
                            returnRay = new Ray(hitLocation, cameraRay.direction.reflect(surfaceNormal));
                            returnRay.origin=returnRay.travel(0.01);
                        }
                    }
                }
                returnRay = new Ray(hitLocation, cameraRay.direction.reflect(surfaceNormal));
                returnRay.origin=returnRay.travel(epsilon);
            }
            //world.objects.forEach();
        }
        return new Pair<>(surfaceColor,returnRay);
    }
}

