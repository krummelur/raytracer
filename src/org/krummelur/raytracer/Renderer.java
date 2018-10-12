package org.krummelur.raytracer;

import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Renderer {

    private final double epsilon = 0.00001;
    World world;
    int resolutionX = 1024;
    int resolutionY = 1024;
    int image[] = new int[resolutionY * resolutionY];
    Camera camera;

    public Renderer(World world, Camera camera) {
        this.world = world;
        this.camera = camera;
    }

    void render() {
        for (int performanceLoops = 0; performanceLoops< 10; performanceLoops++) {

            long startTime = System.nanoTime();
            for (int y = 0; y < resolutionY; y++)
                for (int x = 0; x < resolutionX; x++) {
                    double reflectivityFactor = 0.7;
                    //calculate if the ray hit any object, and at what distance
                    Ray cameraRay = new Ray(this.camera.location
                            .add(this.camera.lookPlane.up.multiply((this.camera.orthogonalSize / 2) - ((double) (y) / (double) (resolutionY) * this.camera.orthogonalSize))
                                    .add(this.camera.lookPlane.right.multiply((this.camera.orthogonalSize / 2) - ((double) (x) / (double) (resolutionX) * this.camera.orthogonalSize)))),
                            camera.direction);
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
            long endTime = System.nanoTime();
            System.out.println((endTime - startTime) / 1000000);
        }

        BufferedImage testImage = new BufferedImage(resolutionY, resolutionX, BufferedImage.TYPE_INT_RGB);
        testImage.setRGB(0, 0, resolutionX, resolutionY, image, 0, resolutionX);
        File outputfile = new File("image.png");
        try {
            ImageIO.write(testImage, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Pair<Vector3, Ray> getColorForRay(Ray cameraRay) {

        Ray returnRay = null;
        //Get closest point on the surface of an object in ray direction
        Shape3d closestObject = null;
        double closestHit = Integer.MAX_VALUE;
        Vector3 surfaceColor = Vector3.ZERO();
        for (Shape3d o : world.objects()) {
            double hit = o.hit(cameraRay);
            if (hit != -1 && hit < closestHit) {
                closestHit = hit;
                closestObject = o;
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

                        //add specular light
                        Vector3 surfaceToCameraDir = hitLocation.subtract(cameraRay.origin).normalize();
                        Vector3 reflectionDirection = lightToSurfaceNormal.reflect(surfaceNormal).normalize();
                        double specular = -1 * surfaceToCameraDir.dot(reflectionDirection);
                        if (specular > 0) {
                            specular = Math.abs(specular);
                            specular = Math.pow(specular, 20);
                            specular *= 2;
                            surfaceColor = surfaceColor.add(new Vector3(specular, specular, specular).multiply(l.strength));
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

