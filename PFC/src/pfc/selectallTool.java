package pfc;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

/** The class selectallTool uses the ourTool interface. It selects a rectangular region.
 * See also selectTool
 *
 * It should work with all operating systems and hardware.
 * There are no variances and no security constraints.
 *
 * @author TerpPaint
 * @version 2.0
 */
public class selectallTool implements ourTool {
     /** Holds the starting X,Y and ending X,Y.
     */
    private int startX, startY, endX, endY;

    /** Used as a flag to determine if the mouse was dragged.
     */
    private boolean dragged = false;
    
    /** Holds the current, backup, selected and pasted image.
     */
    BufferedImage curImage;

    /** Holds the current, backup, selected and pasted image.
     */
    BufferedImage backupImage;

    /** Holds the current, backup, selected and pasted image.
     */
    BufferedImage selectedImage;

    /** Holds the current, backup, selected and pasted image.
     */
    BufferedImage  pastedImage;

    /** Holds the temp image.
     */
    BufferedImage tempImage;

    /**Holds a Graphics2D.
     */
    private Graphics2D g2D;
  
    /** Flag to determine is it has been selected.
     */
    private boolean selected = false;

    /** Flag to determine is it has been pasted.
     */
    private boolean pasted = false;

    /** Holds the stroke that is selected.
     */
    private BasicStroke selectStroke;

    /** Holds the stroke that is selected for the white part.
    */
    private BasicStroke selectStrokeW;

    /** x coord of where to be pasted.
     */
    private int pasteX;

    /** y of where to be pasted.
     */
    private int	 pasteY;

    /** Flag used to determine if drawOpaque is on or not.
     */
    public boolean drawOpaque = true;

    
    /** Creates a selectallTool and sets the Stroke.
     * It takes in no parameters or null arguments.  It does not return anything.
     * There are no algorithms of any kind and no variances and OS dependencies.
     * There should not be any exceptions or security constraints.
     */
    public selectallTool() {
	super();
	selectStroke = new BasicStroke(1.f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,8.f,new float[]{6.f,6.f},0.f);	
	selectStrokeW = new BasicStroke(1.f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,8.f,new float[]{6.f,6.f},6.f);		
    }

    /** Allows the user to click on the canvas and select all of the image.
     * A click in valid x,y coordinates initializes everything.
     * A click on invalid areas does nothing.
     * @param mevt MosueEvent upon initial click
     * @param theCanvas current main_canvas
     */
    public void clickAction(MouseEvent mevt, MiCanvas theCanvas) {

        int x, y;
        int temp;

        Rectangle boundRect;

        System.out.println("En clickAction...");
        dragged = false;
        if (selected == true) {
            x = mevt.getX();
            y = mevt.getY();
            System.out.println("endX..."+endX);
            if (endX < startX) {                
                temp = endX;
                endX = startX;
                startX = temp;
            }
            if (endY < startY) {
                temp = endY;
                endY = startY;
                startY = temp;
            }
            boundRect = new Rectangle(startX, startY, Math.abs(startX - endX), Math.abs(startY - endY));
            if (boundRect.contains(x, y)) {
                g2D.drawImage(backupImage, null, 0, 0);
                pasted = true;
                selected = true;
                pasteX = startX;
                pasteY = startY;
                pastedImage = selectedImage;
            } else {
                g2D.drawImage(backupImage, null, 0, 0);
                selected = false;
            }
        } else {
            System.out.println("En clickAction inicial...");

            backupImage = theCanvas.getBufferedImage();
            curImage = theCanvas.getBufferedImage();
            theCanvas.setBufferedImage(curImage);
            g2D = curImage.createGraphics();
        }
        startX = mevt.getX();
        startY = mevt.getY();
        theCanvas.repaint();
    }

    /** Selects a rectangle according to movement of mouse, as specified by the
     * coordinates of MouseEvent.
     * @param mevt MosueEvent dragging mouse
     * @param theCanvas the current main_canvas
     */
    public void dragAction(MouseEvent mevt, MiCanvas theCanvas) {

        int x, y, width, height;

        System.out.println("En dragAction...");
        g2D.drawImage(backupImage, null, 0, 0);
        dragged = true;
        endX = mevt.getX();
        endY = mevt.getY();
        x = startX;
        y = startY;
        if (endX < startX) {
            x = endX;
        }
        if (endY < startY) {
            y = endY;
        }

        width = Math.abs(startX - endX);
        height = Math.abs(startY - endY);

        /* Para pintar el rectangulo de seleccion en los
         * colores negro y blanco.
         */
        g2D.setColor(Color.black);
        g2D.setStroke(selectStroke);
        g2D.draw(new Rectangle(x, y, width, height));

        g2D.setColor(Color.white);
        g2D.setStroke(selectStrokeW);
        g2D.draw(new Rectangle(x, y, width, height));

        theCanvas.repaint();
    }

    /** Allows the user to release the mouse and stop selecting.
     * Sets the flag dragged to false and slected to true.
     * @param mevt MouseEvent release of click
     * @param theCanvas the current main_canvas
     */
    public void mouseReleaseAction(MouseEvent mevt, MiCanvas theCanvas) {

        int x, y, width, height;

        if (dragged) {
            System.out.println("En mouseReleaseAction...");
            g2D.drawImage(backupImage, null, 0, 0);
            endX = mevt.getX();
            endY = mevt.getY();
            x = startX;
            y = startY;
            if (endX < startX) {
                x = endX;
                endX = startX;
                startX = x;
            }
            if (endY < startY) {
                y = endY;
                endY = startY;
                startY = y;
            }
            width = Math.abs(startX - endX);
            height = Math.abs(startY - endY);
            System.out.println("X:" + x + " and width:" + width);
            System.out.println("Y:" + y + " and height:" + height);

            selectedImage = theCanvas.getCopy(curImage).getSubimage(x, y, width, height);
            pastedImage = selectedImage;

            /* Para pintar el rectangulo de seleccion en los
            * colores negro y blanco.
            */
            g2D.setColor(Color.black);
            g2D.setStroke(selectStroke);
            g2D.draw(new Rectangle(x, y, width, height));

            g2D.setColor(Color.white);
            g2D.setStroke(selectStrokeW);
            g2D.draw(new Rectangle(x, y, width, height));

            pasteX = startX;
            pasteY = startY;
            theCanvas.repaint();
            dragged = false;
            selected = true;
        }
    }

    /** Allows the user to select the entire main_canvas.
     * It draws a rectangle around the entire BufferedImage and sets the
     * flag dragged to false, selected to true, and pasted to false.
     * The starting coordinates of the selection are at 0, 0.
     * The ending coordinates of the selection are at the width and height of the
     * image.
     * @param theCanvas main_canvas object
     */
    public void selectAll(MiCanvas theCanvas) {

        pasteX = 0;
        pasteY = 0;

        selectedImage = theCanvas.getBufferedImage();
        backupImage = theCanvas.getBufferedImage();
        curImage = theCanvas.getBufferedImage();
        pastedImage = theCanvas.getBufferedImage();
        tempImage = theCanvas.getBufferedImage();
        g2D = curImage.createGraphics();
        g2D.setColor(Color.black);
        g2D.setStroke(selectStroke);
        g2D.draw(new Rectangle(0, 0, selectedImage.getWidth() - 1, selectedImage.getHeight() - 1));

        g2D.setColor(Color.white);
        g2D.setStroke(selectStrokeW);
        g2D.draw(new Rectangle(0, 0, selectedImage.getWidth() - 1, selectedImage.getHeight() - 1));

        theCanvas.setBufferedImage(curImage);

        theCanvas.repaint();
        dragged = false;
        selected = true;
        pasted = false;

        startX = 0;
        startY = 0;
        endX = selectedImage.getWidth();
        endY = selectedImage.getHeight();
    }

    /** Returns the selected rectangle.
     * @param theCanvas the current main_canvas
     * @return BufferedImage of selected rectangle image
     */
    public BufferedImage getCopyImage(MiCanvas theCanvas) {
	if ((pasted == true) || (selected == true)){
	    g2D.drawImage(backupImage,null,0,0);

	    //myDrawImage(theCanvas.right_color);
	    g2D.setColor(Color.black);
	    g2D.setStroke(selectStroke);
	    g2D.draw(new Rectangle(pasteX,pasteY,pastedImage.getWidth(),pastedImage.getHeight()));
	    
	    g2D.setColor(Color.white);
	    g2D.setStroke(selectStrokeW);
	    g2D.draw(new Rectangle(pasteX,pasteY,pastedImage.getWidth(),pastedImage.getHeight()));
	    
	    theCanvas.repaint();
	    return pastedImage;
	} else {
	    return new BufferedImage(10,10,BufferedImage.TYPE_INT_RGB);
	}
    }

    /** Pastes image into selected area but does not update canvas -- see setPastedImage.
     * @param rightColor Color of right-click
     */
    public void myDrawImage(Color rightColor) {

	if (drawOpaque == true) {
		g2D.drawImage(pastedImage,null,pasteX,pasteY);
	} else {
		for (int countX = 0;countX < pastedImage.getWidth(); countX++) {
			for (int countY = 0;countY < pastedImage.getHeight(); countY++) {
				if (rightColor.getRGB() != pastedImage.getRGB(countX,countY)) {
					try {
						curImage.setRGB(pasteX+countX,pasteY+countY,pastedImage.getRGB(countX,countY));
					} catch (Exception e) {}
				}
			}
		}
	}
    }
    

    /** Resets canvas to backup image and resets all data members and flags to original values.
     * @param theCanvas main_canvas
     */
    public void deSelect(MiCanvas theCanvas) {
	if (selected == true) {
		g2D.drawImage(backupImage,null,0,0);
		if (drawOpaque == false) {
//			myDrawImage(theCanvas.right_color);
		} else {
			g2D.drawImage(pastedImage,null,pasteX,pasteY);
		}
		selected = false;
		pasted = false;
		theCanvas.repaint();
	}
    }

    /** This function returns the current state of the flag selected.
     * @return boolean representing the state of the flag selected
     */
    public boolean getSelected(){
        return selected;
    }


    /** This function returns the current state of the flag pasted.
     * @return boolean representing the flag pasted.
     */
    public boolean getPasted(){
        return pasted;
    }
    
    /** This function returns the current X-position of subimage selected.
     * @return boolean representing the state of the flag selected
     */
    public int getStartX(){
        return startX;
    }
    
    /** This function returns the current Y-position of subimage selected.
     * @return boolean representing the state of the flag selected
     */
    public int getStartY(){
        return startY;
    }

    public int getEndX(){
        return endX;
    }

    public int getEndY(){
        return endY;
    }


    /** sets the opaque flag.
     * @param value boolean opaqueness passed in
     * @see #getOpaque
     */
    public void setOpaque(boolean value) { 
        drawOpaque = value;
    }

    /** This function returns true if it is set to opaque, false
     * otherwise.
     * @return boolean representing opaque
     * @see #setOpaque
     */
    public boolean getOpaque(){
        return drawOpaque;
    }
    
   /** This function returns the Color of the g2d.
    * @return Color representing that of g2D
    */
   public Color getG2dColor(){
       return g2D.getColor();
   }

   /** This function returns the current state of the flag dragged.
    * @return boolean representing the flag dragged
    */
   public boolean getDragged(){
       return dragged;
   }
   
    /** This function the state of anything being selected.
    * @return boolean true if area is selected, false if not.
    */
   public boolean isSelected(){ 
       return selected;
   }

}
