/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pfc;

import java.awt.image.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;

/**
 *
 * @author alumno
 */
public class MiCanvas extends JPanel {

    /**
     * Representan el ancho y el alto de nuestro canvas.
     */
    int ancho, alto;
    
    /**
     * Representa la imagen que dibujamos en nuestro canvas.
     */
    public BufferedImage miImagen = null;

    public BufferedImage temp_x;

    public boolean seleccion = false;
    
    public JScrollPane pictureScrollPane = new JScrollPane();

    public ScrollablePicture picture;

    public ImageIcon miIcon = null;

    Graphics2D gd;

    AffineTransform t;

    public double zoomFactor = 1.0;

    static double oldZoomFactor = 1.0;


    public MiCanvas(){
        ancho = 700;
        alto = 500;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        clear();
        add(pictureScrollPane);
    }

    @Override
    public void paintComponent(Graphics g) {
        System.out.println("Entramos en paintComponent...");
        
        gd.drawImage(miImagen, null, 0, 0);
        pictureScrollPane.paintAll(g);
    }

    public void setBufferedImage(BufferedImage buffImg) {
        miImagen = buffImg;
	ancho = buffImg.getWidth();
	alto = buffImg.getHeight();
	buffImg.flush();	
	repaint();
    }

    public void clear() {
        System.out.println("Entramos en el clear...");

        ancho = 700;
        alto = 500;
        miImagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        gd = miImagen.createGraphics();
	gd.setColor(Color.white);
	gd.fillRect(0,0,miImagen.getWidth(),miImagen.getHeight());        
    }

    public void zoom() {
        System.out.println("Entramos en zoom...");

        gd = miImagen.createGraphics();
        miIcon = new ImageIcon((Image) miImagen);        
        picture = new ScrollablePicture(miIcon, 1);
        pictureScrollPane.setViewportView(picture);        
    }

    
    public BufferedImage getBufferedImage(){    
    	return getCopy(miImagen);
    }

    
    public BufferedImage getCopy(BufferedImage in){
       BufferedImage temp = new BufferedImage(in.getWidth(), in.getHeight(),BufferedImage.TYPE_INT_RGB);
       Graphics2D g2d = temp.createGraphics();
       g2d.drawImage(in,null,0,0);

       return temp;
    }

    public void setZoom (double factor) {
        zoomFactor = factor;
        t = new AffineTransform();
        t.setToScale(zoomFactor, zoomFactor);
        if ((int) (miImagen.getWidth() * zoomFactor) > 0 & (int) (miImagen.getHeight() * zoomFactor) > 0) {
            temp_x = new BufferedImage((int) (miImagen.getWidth() * zoomFactor),
                    (int) (miImagen.getHeight() * zoomFactor), BufferedImage.TYPE_INT_RGB);
        }

        gd = temp_x.createGraphics();
        miIcon = new ImageIcon((Image) temp_x);

        picture = new ScrollablePicture(miIcon, 1);

        int oldX = (int) pictureScrollPane.getViewport().getViewPosition().getX();
        int oldY = (int) pictureScrollPane.getViewport().getViewPosition().getY();
        Point old = new Point(oldX, oldY);
        pictureScrollPane.setViewportView(picture);
        pictureScrollPane.getViewport().setViewPosition(old);
    }

    @Override
    public void paint(Graphics g) {
        if (miImagen != null
                && (int) (miImagen.getWidth() * zoomFactor * miImagen.getHeight() * zoomFactor) < 12000000) {

            if (miImagen != null) {
                gd.drawImage(miImagen, t, null);
                pictureScrollPane.paintAll(g);
            }
        } else {
            zoomFactor = oldZoomFactor;
        }
    }

    public double getZoom(){
	return(zoomFactor);
    }

}
