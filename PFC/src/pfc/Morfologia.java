/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pfc;

import jigl.image.ops.morph.GOpen;
import java.awt.image.*;
import javax.swing.ImageIcon;
import java.awt.*;
import jigl.image.*;
import jigl.image.ops.morph.GClose;


/**
 *
 * @author Pablo
 */

public class Morfologia {

    public static BufferedImage apertura(BufferedImage img, selectallTool sat) throws ImageNotSupportedException {
        float arr[][] = new float[1][5];

        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 5; j++){
                arr[i][j] = 0;
            }
        }

        System.out.println("Punto inicio: ("+sat.getStartX()+","+sat.getStartY()+")");
        System.out.println("Punto fin: ("+sat.getEndX()+","+sat.getEndY()+")");
        
        ROI roi = new ROI(sat.getStartX(), sat.getStartY(), sat.getEndX(), sat.getEndY());
        
        java.awt.Image image = (java.awt.Image) toImage(img);


        GrayImage grayimg = GrayInitFromImage(image, 0, 0, img.getWidth()-1, img.getHeight()-1);

        

        ImageKernel ik = new ImageKernel(arr);
        GOpen gop = new GOpen(ik, 0, 0);

        jigl.image.Image im = gop.apply(grayimg, roi);

        System.out.println("Apertura realizada");
        MemoryImageSource imp = (MemoryImageSource) getJavaImage((GrayImage) (jigl.image.Image) im);
        java.awt.Image imageres = Toolkit.getDefaultToolkit().createImage(imp);
        BufferedImage res = toBufferedImage(imageres);
        return res;
    }


    public static BufferedImage clausura(BufferedImage img, selectallTool sat) throws ImageNotSupportedException {
        float arr[][] = new float[1][9];

        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 9; j++){
                arr[i][j] = 0;
            }
        }

        System.out.println("Punto inicio: ("+sat.getStartX()+","+sat.getStartY()+")");
        System.out.println("Punto fin: ("+sat.getEndX()+","+sat.getEndY()+")");

        ROI roi = new ROI(sat.getStartX(), sat.getStartY(), sat.getEndX(), sat.getEndY());

        java.awt.Image image = (java.awt.Image) toImage(img);

        GrayImage grayimg = GrayInitFromImage(image, 0, 0, img.getWidth()-1, img.getHeight()-1);

        ImageKernel ik = new ImageKernel(arr);
        GClose gcl = new GClose(ik, 0, 0);

        jigl.image.Image im = gcl.apply(grayimg, roi);

        System.out.println("Clausura realizada...");
        MemoryImageSource imp = (MemoryImageSource) getJavaImage((GrayImage) (jigl.image.Image) im);
        java.awt.Image imageres = Toolkit.getDefaultToolkit().createImage(imp);
        BufferedImage res = toBufferedImage(imageres);
        return res;
    }


//    protected static ColorImage ColorInitFromImage(java.awt.Image img, int x, int y, int w, int h) {
//        ColorImage result = new ColorImage(w, h);
//        int pixels[] = new int[w * h];
//        PixelGrabber pg = new PixelGrabber(img, x, y, w, h, pixels, 0, w);
//        try {
//            pg.grabPixels();
//        } catch (InterruptedException e) {
//            System.err.println("interrupted waiting for pixels!");
//            return result;
//        }
//        if ((pg.status() & ImageObserver.ABORT) != 0) {
//            System.err.println("image fetch aborted or errored");
//            return result;
//        }
//
//        // convert from grabbed pixels
//        int red = 0;
//        int green = 0;
//        int blue = 0;
//        int index = 0;
//        for (int iy = 0; iy < result.Y(); iy++) {
//            for (int ix = 0; ix < result.X(); ix++) {
//                red = 0x0FF & pixels[index] >> 16;
//                green = 0x0FF & pixels[index] >> 8;
//                blue = 0x0FF & pixels[index];
//                (result.plane(0)).set(ix, iy, (short) red);
//                (result.plane(1)).set(ix, iy, (short) green);
//                (result.plane(2)).set(ix, iy, (short) blue);
//                index++;
//            }
//        }
//
//        return result;
//    }


    protected static GrayImage GrayInitFromImage(java.awt.Image img, int x, int y, int w, int h) {
        GrayImage result = new GrayImage(w, h);

        int pixels[] = new int[w * h];
        PixelGrabber pg = new PixelGrabber(img, x, y, w, h, pixels, 0, w);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            System.err.println("interrupted waiting for pixels!");
            return result;
        }
        if ((pg.status() & ImageObserver.ABORT) != 0) {
            System.err.println("image fetch aborted or errored");
            return result;
        }

        // convert from grabbed pixels
        int red = 0;
        int green = 0;
        int blue = 0;
        int index = 0;
        for (int iy = 0; iy < result.Y(); iy++) {
            for (int ix = 0; ix < result.X(); ix++) {
                red = 0x0FF & pixels[index] >> 16;
                green = 0x0FF & pixels[index] >> 8;
                blue = 0x0FF & pixels[index];
                result.set(
                        ix,
                        iy,
                        ((short) ((double) red * 0.299
                        + (double) green * 0.587
                        + (double) blue * 0.114)));

                index++;
            }
        }

        return result;

	}

    protected static ImageProducer getJavaImage(ColorImage img) {
        // get range of this image
        double min = (double) img.min();
        double max = (double) img.max();

        // keep byte images in original range
        if (min >= 0 && max <= 255) {
            min = 0.0;
            max = 255.0;
        }
        double range = max - min;

        // convert jigl image to java image
        int pix[] = new int[img.X() * img.Y()];
        int index = 0;
        int red = 0;
        int green = 0;
        int blue = 0;
        int[] color = new int[3];
        for (int y = 0; y < img.Y(); y++) {
            for (int x = 0; x < img.X(); x++) {
                // map image values
                color = img.get(x, y);
                red = (int) ((255.0 / range) * ((double) color[0] - min));
                green = (int) ((255.0 / range) * ((double) color[1] - min));
                blue = (int) ((255.0 / range) * ((double) color[2] - min));

                // take lower 8 bits
                red = 0x00FF & red;
                green = 0x00FF & green;
                blue = 0x00FF & blue;

                // put this pixel in the java image
                pix[index] = (0xFF << 24) | (red << 16) | (green << 8) | blue;
                index++;
            }
        }

        // return java image
        return new MemoryImageSource(img.X(), img.Y(), pix, 0, img.X());
    }

    protected static ImageProducer getJavaImage(GrayImage img) {
        // get range of this image
        int min = img.min();
        int max = img.max();

        // keep byte images in original range
        if (min >= 0 && max <= 255) {
            min = 0;
            max = 255;
        }
        int range = max - min;

        // convert jigl image to java image
        int pix[] = new int[img.X() * img.Y()];
        int index = 0;
        int value = 0;
        for (int y = 0; y < img.Y(); y++) {
            for (int x = 0; x < img.X(); x++) {

                // scale image values
                value = (int) ((255.0 / range) * ((float) img.get(x, y) - min));

                value = 0x00FF & value;	// take lower 8 bits

                // put this pixel in the java image
                pix[index] = (0xFF << 24) | (value << 16) | (value << 8) | value;
                index++;
            }
        }

        // return java image
        return new MemoryImageSource(img.X(), img.Y(), pix, 0, img.X());
    }


    // This method returns an Image object from a buffered image
    public static java.awt.Image toImage(BufferedImage bufferedImage) {
        return  (java.awt.Image) Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
    }

    public static BufferedImage toBufferedImage(java.awt.Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
     
        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;

            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }


    public static BufferedImage pasarabyn(BufferedImage bf, selectallTool sat) {
        BufferedImage img = new BufferedImage(bf.getWidth(), bf.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int i = sat.getStartX(); i < sat.getEndX()+1; i++) {
            for (int j = sat.getStartY(); j < sat.getEndY()+1; j++) {
                int p = bf.getRGB(i, j);
                if (analizapixel(p) > 150) {
                    img.setRGB(i, j, Color.BLACK.getRGB());
                } // all pixels opaque white
                else {
                    img.setRGB(i, j, Color.WHITE.getRGB());
                }
            }
        }
        return img;
    }

    public static int analizapixel(int pixel) {
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;

        return (red + green + blue) / 3;
    }

    public static BufferedImage verResultados(BufferedImage imagenOrig, BufferedImage imagenBin, selectallTool sat) {
        BufferedImage img = new BufferedImage(imagenOrig.getWidth(), imagenOrig.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(imagenOrig,null,0,0);
        for (int i = sat.getStartX(); i < sat.getEndX(); ++i) {
            for (int j = sat.getStartY(); j < sat.getEndY(); ++j) {
                int p = imagenBin.getRGB(i, j);                
                if (analizapixel(p) == 0) { //cuando los pixeles son negros
                    img.setRGB(i, j, Color.RED.getRGB());
                } 
                else {                    
                    img.setRGB(i, j, imagenOrig.getRGB(i, j));
                }
            }
        }
        return img;
    }

    protected static BufferedImage getCopy(BufferedImage in){
       BufferedImage temp = new BufferedImage(in.getWidth(), in.getHeight(),BufferedImage.TYPE_INT_RGB);
       Graphics2D g2d = temp.createGraphics();
       g2d.drawImage(in,null,0,0);

       return temp;
    }


public static BufferedImage inversa(BufferedImage imagenOrig, selectallTool sat) {
        BufferedImage img = new BufferedImage(imagenOrig.getWidth(),imagenOrig.getHeight(), BufferedImage.TYPE_INT_ARGB);
        System.out.println("Altura: "+imagenOrig.getHeight());
        System.out.println("Anchura: "+imagenOrig.getWidth());
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(imagenOrig,null,0,0);
        for (int i = sat.getStartX(); i < sat.getEndX()+1; i++) {
            for (int j = sat.getStartY(); j < sat.getEndY()+1; j++) {
                int p = imagenOrig.getRGB(i, j);                
                int aux = Color.BLACK.getRGB() - p;                
                int ap = analizapixel(p);
                if (ap >= 0 && ap < 256) {                     
                    img.setRGB(i, j, aux);
                }
                else {
                    img.setRGB(i, j, imagenOrig.getRGB(i, j));
                }
            }
        }
        return img;
    }


}