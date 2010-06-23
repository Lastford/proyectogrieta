
package pfc;

import jigl.image.ops.morph.*;
import java.awt.image.*;
import javax.swing.ImageIcon;
import java.awt.*;
import java.util.HashSet;
import jigl.image.*;


/**
 * @author Pablo Morillas Olmedo, Daniel Martín Núñez
 */

public class Morfologia {

    /**
     * Devuelve la inversa de la imagen que le metamos como
     * parámetro.
     */
    public static BufferedImage inversa(BufferedImage imagenOrig) {
        BufferedImage imgInv = new BufferedImage(imagenOrig.getWidth(),imagenOrig.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < imagenOrig.getWidth(); i++) {
            for (int j = 0; j < imagenOrig.getHeight(); j++) {
                int pixel = imagenOrig.getRGB(i, j);
                int aux = Color.WHITE.getRGB() - pixel;
                int ap = analizapixel(pixel);
                if (ap >= 0 && ap < 256) {
                    imgInv.setRGB(i, j, aux);
                } else {
                    imgInv.setRGB(i, j, imagenOrig.getRGB(i, j));
                }
            }
        }
        return imgInv;
    }


    public static BufferedImage comodin(BufferedImage imgOrig, selectallTool region, float[][] eltoEstruc, int indice) throws ImageNotSupportedException {

        ROI roi = null;

        if (region.equals(null)) {
            roi = new ROI(0,0,imgOrig.getWidth(),imgOrig.getHeight());
        } else {
            roi = new ROI(region.getStartX(), region.getStartY(), region.getEndX(), region.getEndY());
        }

        java.awt.Image imageOrig = (java.awt.Image) toImage(imgOrig);

        GrayImage imgGris = GrayInitFromImage(imageOrig, 0, 0, imgOrig.getWidth(), imgOrig.getHeight());

        ImageKernel ik = new ImageKernel(eltoEstruc);

        jigl.image.Image imgProc, imgProc1, imgProc2 = null;
        BufferedImage imgRes, imgRes1, imgRes2 = null;

        GDilate gdil = null;
        GErode gero = null;
        GOpen gope = null;
        GClose gclo = null;

        switch (indice) {
            case 0:
                gdil = new GDilate(ik, 1, 1);
                imgProc = gdil.apply(imgGris, roi);
                imgRes = jiglToJava(imgProc);
                break;
            case 1:
                gero = new GErode(ik, 1, 1);
                imgProc = gero.apply(imgGris, roi);
                imgRes = jiglToJava(imgProc);
                break;
            case 2:
                gope = new GOpen(ik, 1, 1);
                imgProc = gope.apply(imgGris, roi);
                imgRes = jiglToJava(imgProc);
                break;
            case 3:
                gclo = new GClose(ik, 1, 1);
                imgProc = gclo.apply(imgGris, roi);
                imgRes = jiglToJava(imgProc);
                break;
            case 4:
                gero = new GErode(ik, 1, 1);
                imgProc1 = gero.apply(imgGris, roi);
                imgRes1 = jiglToJava(imgProc1);

                gdil = new GDilate(ik, 1, 1);
                imgProc2 = gdil.apply(imgGris, roi);
                imgRes2 = jiglToJava(imgProc2);

                imgRes = restaImagenes(imgRes2, imgRes1, region);
                break;
            case 5:
                gope = new GOpen(ik, 1, 1);
                imgProc = gope.apply(imgGris, roi);
                imgRes = restaImagenes(jiglToJava(imgProc), jiglToJava(imgGris), region);
                break;
            case 6:
                gclo = new GClose(ik, 1, 1);
                imgProc = gclo.apply(imgGris, roi);
                imgRes = restaImagenes (jiglToJava(imgProc),jiglToJava(imgGris), region);
                break;
            default:
                gdil = new GDilate(ik, 1, 1);
                imgProc = gdil.apply(imgGris, roi);
                imgRes = jiglToJava(imgProc);
                break;
        }

        System.out.println("Tratamiento realizado...");

        return imgRes;
    }


    public static BufferedImage dilatacion(BufferedImage imgOrig, selectallTool region, float[][] eltoEstruc) throws ImageNotSupportedException {

        ROI roi = new ROI(region.getStartX(), region.getStartY(), region.getEndX(), region.getEndY());

        java.awt.Image imageOrig = (java.awt.Image) toImage(imgOrig);

        GrayImage imgGris = GrayInitFromImage(imageOrig, 0, 0, imgOrig.getWidth(), imgOrig.getHeight());

        ImageKernel ik = new ImageKernel(eltoEstruc);
        GDilate gdil = new GDilate(ik, 1, 1);

        jigl.image.Image imgProc = gdil.apply(imgGris, roi);
        BufferedImage imgRes = jiglToJava(imgProc);

        System.out.println("Dilatación realizada...");

        return imgRes;
    }

    public static BufferedImage erosion(BufferedImage imgOrig, selectallTool region, float[][] eltoEstruc) throws ImageNotSupportedException {

        ROI roi = new ROI(region.getStartX(), region.getStartY(), region.getEndX(), region.getEndY());

        java.awt.Image imageOrig = (java.awt.Image) toImage(imgOrig);

        GrayImage imgGris = GrayInitFromImage(imageOrig, 0, 0, imgOrig.getWidth(), imgOrig.getHeight());

        ImageKernel ik = new ImageKernel(eltoEstruc);
        GErode ger = new GErode(ik, 1, 1);

        jigl.image.Image imgProc = ger.apply(imgGris, roi);
        BufferedImage imgRes = jiglToJava(imgProc);

        System.out.println("Erosión realizada...");

        return imgRes;
    }


    public static BufferedImage apertura(BufferedImage imgOrig, selectallTool region, float[][] eltoEstruc) throws ImageNotSupportedException {
        
        ROI roi = new ROI(region.getStartX(), region.getStartY(), region.getEndX(), region.getEndY());
                
        java.awt.Image imageOrig = (java.awt.Image) toImage(imgOrig);

        GrayImage imgGris = GrayInitFromImage(imageOrig, 0, 0, imgOrig.getWidth(), imgOrig.getHeight());
        
        ImageKernel ik = new ImageKernel(eltoEstruc);
        GOpen gop = new GOpen(ik, 1, 1);

        jigl.image.Image imgProc = gop.apply(imgGris, roi);
        BufferedImage imgRes = jiglToJava(imgProc);

        System.out.println("Apertura realizada...");
        
        return imgRes;
    }


    public static BufferedImage clausura(BufferedImage imgOrig, selectallTool region, float[][] eltoEstruc) throws ImageNotSupportedException {
        
        ROI roi = new ROI(region.getStartX(), region.getStartY(), region.getEndX(), region.getEndY());

        java.awt.Image imageOrig = (java.awt.Image) toImage(imgOrig);

        GrayImage imgGris = GrayInitFromImage(imageOrig, 0, 0, imgOrig.getWidth(), imgOrig.getHeight());

        ImageKernel ik = new ImageKernel(eltoEstruc);
        GClose gcl = new GClose(ik, 1, 1);

        jigl.image.Image imgProc = gcl.apply(imgGris, roi);
        BufferedImage imgRes = jiglToJava(imgProc);

        System.out.println("Clausura realizada...");
       
        return imgRes;
    }


    public static BufferedImage topHatClara(BufferedImage imgOrig, selectallTool region, float[][] eltoEstruc) throws ImageNotSupportedException {

        ROI roi = new ROI(region.getStartX(), region.getStartY(), region.getEndX(), region.getEndY());

        java.awt.Image imageOrig = (java.awt.Image) toImage(imgOrig);

        GrayImage imgGris = GrayInitFromImage(imageOrig, 0, 0, imgOrig.getWidth(), imgOrig.getHeight());

        ImageKernel ik = new ImageKernel(eltoEstruc);
        GOpen gop = new GOpen(ik, 1, 1);

        jigl.image.Image imgProc = gop.apply(imgGris, roi);
        BufferedImage imgRes = restaImagenes (jiglToJava(imgProc),jiglToJava(imgGris), region);

        System.out.println("Top-Hat(Clara) realizada...");

        return imgRes;
    }


    public static BufferedImage topHatOscura(BufferedImage imgOrig, selectallTool region, float[][] eltoEstruc) throws ImageNotSupportedException {

        ROI roi = new ROI(region.getStartX(), region.getStartY(), region.getEndX(), region.getEndY());

        java.awt.Image imageOrig = (java.awt.Image) toImage(imgOrig);

        GrayImage imgGris = GrayInitFromImage(imageOrig, 0, 0, imgOrig.getWidth(), imgOrig.getHeight());

        ImageKernel ik = new ImageKernel(eltoEstruc);
        GClose gcl = new GClose(ik, 1, 1);

        jigl.image.Image imgProc = gcl.apply(imgGris, roi);
        BufferedImage imgRes = restaImagenes (jiglToJava(imgProc),jiglToJava(imgGris), region);

        System.out.println("Top-Hat(Oscura) realizada...");

        return imgRes;
    }


    public static BufferedImage gradienteMorfologico(BufferedImage imgOrig, selectallTool region, float[][] eltoEstruc) throws ImageNotSupportedException {

        ROI roi = new ROI(region.getStartX(), region.getStartY(), region.getEndX(), region.getEndY());

        java.awt.Image imageOrig = (java.awt.Image) toImage(imgOrig);
        GrayImage imgGris = GrayInitFromImage(imageOrig, 0, 0, imgOrig.getWidth(), imgOrig.getHeight());
        ImageKernel ik = new ImageKernel(eltoEstruc);

        jigl.image.ops.morph.GErode ge = new GErode(ik, 1, 1);
        jigl.image.Image imgProc1 = ge.apply(imgGris, roi);
        BufferedImage imgRes1 = jiglToJava(imgProc1);

        jigl.image.ops.morph.GDilate gd = new GDilate(ik, 1, 1);
        jigl.image.Image imgProc2 = gd.apply(imgGris, roi);
        BufferedImage imgRes2 = jiglToJava(imgProc2);

        BufferedImage imgRes = restaImagenes(imgRes2, imgRes1, region);

        return imgRes;
    }


    /**
     * Convierte una imagen en color a una imagen en escala de grises, que
     * necesitamos para hacer los diferentes tratamientos.
     */
    protected static GrayImage GrayInitFromImage(java.awt.Image img, int x, int y, int w, int h) {
        GrayImage result = new GrayImage(w, h);

        int pixels[] = new int[w * h];
        PixelGrabber pg = new PixelGrabber(img, x, y, w, h, pixels, 0, w);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            System.err.println("Interrupted waiting for pixels!");
            return result;
        }
        if ((pg.status() & ImageObserver.ABORT) != 0) {
            System.err.println("Image fetch aborted or errored");
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


    /**
     * Devuelve una ImageProducer a partir de una GrayImage, usado para
     * convertir una imagen de Jigl a Java.
     */
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


    /**
     * Devuelve una Image a partir de una BufferedImage, necesario para
     * pasarla despues a GrayImage.
     */
    public static java.awt.Image toImage(BufferedImage bufferedImage) {
        return  (java.awt.Image) Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
    }


    /**
     * Devuelve una BufferedImage a partir de una Image, usado para
     * convertir una imagen de Jigl a Java.
     */
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


    /**
     * Devuelve una imagen binaria a partir de cualquier imagen, ya sea
     * a color o en escala de grises. Usado para resaltar la grieta.
     */
    public static BufferedImage pasarabyn(BufferedImage imgProc, selectallTool region, int valorUmbral) {
        BufferedImage imgRes = new BufferedImage(imgProc.getWidth(), imgProc.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int i = region.getStartX(); i < region.getEndX()+1; i++) {
            for (int j = region.getStartY(); j < region.getEndY()+1; j++) {
                int p = imgProc.getRGB(i, j);
                if (analizapixel(p) > valorUmbral) { //La grieta es blanca
                    imgRes.setRGB(i, j, Color.BLACK.getRGB());
                } else {
                    imgRes.setRGB(i, j, Color.WHITE.getRGB());
                }
            }
        }
        return imgRes;
    }


    /**
     * Devuelve el entero correspondiente al valor RGB del píxel introducido
     * como parámetro.
     */
    public static int analizapixel(int pixel) {
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;

        return (red + green + blue) / 3;
    }


    /**
     * Devuelve una la imagen final con píxeles coloreados en rojo
     * correspondientes a las grietas detectadas en la imagen.
     */
    public static BufferedImage verResultados(BufferedImage imagenOrig, BufferedImage imagenBin, selectallTool region) {
        BufferedImage imgRes = new BufferedImage(imagenOrig.getWidth(), imagenOrig.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imgRes.createGraphics();
        g2d.drawImage(imagenOrig, null, 0, 0);

        for (int i = region.getStartX()+2; i < region.getEndX()-1; i++) {
            for (int j = region.getStartY()+2; j < region.getEndY()-1; j++) {
                int p = imagenBin.getRGB(i, j);                
                if (analizapixel(p) == 0) { //los píxeles son negros
                    imgRes.setRGB(i, j, Color.RED.getRGB());
                } else {                    
                    imgRes.setRGB(i, j, imagenOrig.getRGB(i, j));
                }
            }
        }
        return imgRes;
    }


    
    
    public static BufferedImage restaImagenes(BufferedImage b1, BufferedImage b2, selectallTool sat) {
        BufferedImage res = new BufferedImage(b2.getWidth(), b2.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = res.createGraphics();
        g2d.drawImage(b2, null, 0, 0);
        int redR;
        int greenR;
        int blueR;        
        
        if ((b1.getWidth() == b2.getWidth()) && (b1.getHeight() == b2.getHeight())) {
            for (int i = sat.getStartX(); i < sat.getEndX()+1; i++) {
                for (int j = sat.getStartY(); j < sat.getEndY()+1; j++) {

                    int pixel1 = b1.getRGB(i, j);
                    int pixel2 = b2.getRGB(i, j);

                    int red1 = (pixel1 >> 16) & 0xff;
                    int green1 = (pixel1 >> 8) & 0xff;
                    int blue1 = (pixel1) & 0xff;

                    int red2 = (pixel2 >> 16) & 0xff;
                    int green2 = (pixel2 >> 8) & 0xff;
                    int blue2 = (pixel2) & 0xff;

                    redR = red1 - red2;
                    greenR = green1 - green2;
                    blueR = blue1 - blue2;

                    redR = (redR + 255)/2;
                    greenR = (greenR + 255)/2;
                    blueR = (blueR + 255)/2;

                    Color color = new Color(redR, greenR, blueR);
                    res.setRGB(i, j, color.getRGB());
                }
            }
        } else {
            System.out.println("Imagenes de dimensiones diferentes");
        }

        return res;
    }


    public static int escala(int gris, int n) {
        int c = 256 / n;
        int res = 0;

        for (int i = 0; i < n; i++) {
            if ((gris > i * c) && (gris <= (i + 1) * c)) {
                res = (i * c) + ((i + 1) * c);
            }
        }
        return (res / 2);
    }


    public static BufferedImage jiglToJava(jigl.image.Image img) {

        MemoryImageSource imp = (MemoryImageSource) getJavaImage((GrayImage) (jigl.image.Image) img);
        java.awt.Image imageres = Toolkit.getDefaultToolkit().createImage(imp);
        BufferedImage res = toBufferedImage(imageres);
        return res;

    }



}