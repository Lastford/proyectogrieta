/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pfc;

import java.awt.*;
import java.awt.image.*;

/**
 *
 * @author alumno
 */
public class ImagenUtil {

    public static BufferedImage getBufferedImage(String imageFile, Component c){
	Image image = c.getToolkit().getImage(imageFile);
	waitForImage(image,c);
	BufferedImage bufferedImage = new BufferedImage(image.getWidth(c),image.getHeight(c),BufferedImage.TYPE_INT_RGB);
	Graphics2D g2d = bufferedImage.createGraphics();
	g2d.drawImage(image,0,0,c);
	return (bufferedImage);
    }


    public static boolean waitForImage(Image image, Component c){
	MediaTracker tracker = new MediaTracker(c);
	tracker.addImage(image,0);
	try {
            tracker.waitForAll();
	} catch(InterruptedException e){
            System.out.println ("Me han interrumpido");
            System.out.println ("Puede que la imagen todavía no haya terminado de cargarse");
        }
	return(!tracker.isErrorAny());
    }
}
