package org.krummelur.raytracer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Renderer {


    World world;
    int resolutionX = 512;
    int resolutionY = 512;
    int image[] = new int[resolutionY * resolutionY];
    Camera camera;

    public Renderer(World world, Camera camera) {
        this.world = world;
        this.camera = camera;
    }

    void render() {
        long startTime = System.nanoTime();
        for (int y = 0; y < resolutionY; y++)
            for (int x = 0; x < resolutionX; x++) {
                Ray ray = new Ray(this.camera.location
                        .add(this.camera.lookPlane.up.multiply((this.camera.orthogonalSize / 2) - ((double) (y) / (double) (resolutionY) * this.camera.orthogonalSize))
                                .add(this.camera.lookPlane.right.multiply((this.camera.orthogonalSize / 2) - ((double) (x) / (double) (resolutionX) * this.camera.orthogonalSize)))),
                        camera.direction);

                //Get closest point on the surface of an object in ray direction
                Shape3d closestObject = null;
                double closestHit = Integer.MAX_VALUE;
                Vector3 surfaceColor = Vector3.ZERO();
                for (Shape3d o : world.objects()) {
                    double hit = o.hit(ray);
                    if (hit != -1 && hit < closestHit) {
                        closestHit = hit;
                        closestObject = o;
                    }
                }
                //if there was a hit, calculate lighing
                if (closestHit != Integer.MAX_VALUE) {
                    for (Light l : world.lights()) {
                        Vector3 hitLocation = ray.travel(closestHit);
                        Vector3 surfaceNormal = closestObject.surfaceNormal(hitLocation);
                        Vector3 lightToSurfaceNormal = hitLocation.subtract(l.location).normalize();
                        //surfaceColor = 1/hitLocation.distance(l.location.normalize())*camera.location.dot(hitLocation.normalize());
                        double dot = -1 * surfaceNormal.dot(lightToSurfaceNormal);
                        if (dot > 0) {
                            surfaceColor = surfaceColor.add(l.color.multiply(l.strength*10/ hitLocation.distance(l.location)).multiply(dot));
                        }

                    }
                    image[resolutionX * y + x] = new Color((int) (surfaceColor.x), (int) (surfaceColor.y), (int) (surfaceColor.z)).getRGB();
                    //world.objects.forEach();
                }
            }
        long endTime = System.nanoTime();
        System.out.print((endTime - startTime) / 1000000);

        BufferedImage testImage = new BufferedImage(resolutionY, resolutionX, BufferedImage.TYPE_INT_RGB);
        testImage.setRGB(0, 0, resolutionX, resolutionY, image, 0, resolutionX);
        File outputfile = new File("image.png");
        try {
            ImageIO.write(testImage, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

