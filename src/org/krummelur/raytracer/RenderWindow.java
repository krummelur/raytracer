package org.krummelur.raytracer;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.*;

public class RenderWindow {

        public static JFrame frame;
        BufferedImage img;

        void setImage(BufferedImage img) {
            this.img = img;
        }

        public RenderWindow(BufferedImage img) {
            this.img = img;
            frame = new JFrame("WINDOW");
            frame.setVisible(true);

            this.start();
            frame.add(new JLabel(new ImageIcon(this.getImage())));

            frame.pack();
//      frame.setSize(WIDTH, HEIGHT);
            // Better to DISPOSE than EXIT
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        public Image getImage() {
            return img;
        }

        public void start(){

            boolean running=false;
                BufferStrategy bs=frame.getBufferStrategy();
                if(bs==null){
                    frame.createBufferStrategy(4);
                    return;
                }

                int border = 12;
                Graphics g= bs.getDrawGraphics();
                g.drawImage(img, frame.getWidth()/2-img.getWidth()/2, frame.getHeight()/2-img.getHeight()/2+border, img.getWidth(), img.getHeight(), null);
                g.dispose();
                bs.show();
        }
    }
