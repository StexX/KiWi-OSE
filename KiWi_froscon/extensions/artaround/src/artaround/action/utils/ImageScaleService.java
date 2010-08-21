/*
	Copyright © Rolf Sint, Michael Schneider, Christian Osterrieder, 2010
 */

package artaround.action.utils;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
/**
 * @author coster, www.utilo.eu 
 * Helper class to scale images
 */
public class ImageScaleService {
	
	/**
	 * reads the width of an image
	 * @param url the url of the image
	 * @return the width of the image
	 */
	public static Integer readImageWidth(File url){	

		try {
			Image image = ImageIO.read(url);
			Integer w = image.getWidth(null);
			image.flush(); //speicher freigeben
			return w;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
			
	}
	/**
	 * resize an image
	 * @param origFile the image file to resize
	 * @param newFile the target file
	 * @param width 
	 * @param height
	 * @return true if successful
	 */
	public static boolean resizeImage(File origFile, File newFile,
			int width, int height) {
		
		Logger logger = Logger.getLogger(ImageScaleService.class);

		try {

			Image image = null;
			
			try {

				image = ImageIO.read(origFile);

				// don't scale if the orig image size is to small:
				double origWidth = (double) image.getWidth(null);
				double origHeight = (double) image.getHeight(null);

				if (origWidth == width && origHeight == height) {
					logger.info("the orig image has the correct size -> not scaling.");						
				}
				else{

					if ( (origWidth < width && origHeight < height) || (origWidth > width && origHeight > height) ){
						//picture is to small or to height
						if (origHeight > origWidth){
							//picture is more height as small -> resize the height:
							image = image.getScaledInstance( 
									-1,
									height,
									Image.SCALE_SMOOTH );
							//height is ok, bust is the width also ok?
							origWidth  = (int)(origWidth/(origHeight/height));
							origHeight = height;
							if (origWidth > width){
								image = image.getScaledInstance( 
										width, 
										-1,
										Image.SCALE_SMOOTH);
							}
						}
						else{
							//picture is more width as height -> resize the width:
							image = image.getScaledInstance( 
									width, 
									-1,
									Image.SCALE_SMOOTH);	
							//width is ok but is the height also ok?								
							origHeight= (int)(origHeight/(origWidth/width));
							origWidth = width;
							if (origHeight > height){
								//picture is more height as small -> resize the height:
								image = image.getScaledInstance( 
										-1,
										height,
										Image.SCALE_SMOOTH );
							}
						}
					}
					else if(origHeight > height && origWidth <= width){
						//image is to height, the width is ok -> resize the height
						image = image.getScaledInstance( 
								-1,
								height,
								Image.SCALE_SMOOTH );
					}
					else if(origWidth > width && origHeight <= height){
						//image is to width, the height is ok -> resize the width
						image = image.getScaledInstance( 
								width, 
								-1,
								Image.SCALE_SMOOTH);
					}
					else{
						//image has the correct proportions
						image = image.getScaledInstance( 
								width, 
								height,
								Image.SCALE_SMOOTH);
					}
						
				}	//end else				
							
				// write the file in the cache directory:
				if (image != null) {
									
					BufferedImage bufIm = ImageScaleService.toBufferedImage(image);
					ImageIO.write(bufIm, "jpg", newFile);
					bufIm.flush();

				} else {
					logger.error("error generating the new image BufferdImage is null");
					return false;
				}
				
				image.flush(); //resourcen freigeben

			} catch (IOException e) {
				logger.error("IO Exception reading or writing the image");
				e.printStackTrace();
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;		
		
	}
	
	public static boolean resizeImage(String origuri, String changeduri,
			int width, int height) {
		
		return ImageScaleService.resizeImage(new File(origuri), new File(changeduri), width, height);

	}	

	/**
	 * create a scaled file from the image
	 * @param orig the original filename
	 * @param newFile the new filename
	 * @param width the new width
	 * @param height the new height
	 * @param fixeRatio if true keep ratio of the image
	 * @throws IOException
	 */
	public static void createScaledFile(File orig, File newFile, int width,
			int height, boolean fixeRatio) throws IOException {

		ImageScaleService.resizeImage(orig, newFile, width, height);
		
	}	
	/**
	 * for testing
	 * @param args
	 * @throws IOException
	 */
    public static void main(String[] args) throws IOException {
    		    	
        URL url = new URL("http://www.utilo.eu/joomla15/images/stories/utilologo90pxHeight.gif");
        GraphicsConfiguration gc = getDefaultConfiguration();
        BufferedImage image = toCompatibleImage(ImageIO.read(url), gc);
        final double SCALE = 0.75;
        int w = (int) (SCALE * image.getWidth());
        int h = (int) (SCALE * image.getHeight());
        final BufferedImage resize = getScaledInstance(image, w, h, true);
 
        JPanel p = new JPanel(new GridLayout(1,2));
        p.add(new JLabel(new ImageIcon(image)));
        p.add(new JLabel(new ImageIcon(resize)));
 
        JPanel south = new JPanel();
        JButton save = new JButton("save");
        save.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt) {
                try {
                    ImageIO.write(resize, "jpeg", new File("test.jpeg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        south.add(save);
 
        JFrame f = new JFrame("Example");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container cp = f.getContentPane();
        cp.add(p, BorderLayout.CENTER);
        cp.add(south, BorderLayout.SOUTH);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
    /**
     * The image read in may not be in the best format to be displayed on your monitor: 
     * it may have 256 colors and your monitor could be set to 32 bits colors, for example. 
     * Using [url=http://java.sun.com/j2se/1.5.0/docs/api/java/awt/GraphicsConfiguration.html] 
     * GraphicsConfiguration[/url] will 
     * fix this, as well as returning an image that uses hardware acceleration, when possible
     * @return the graphic configuration of current machine
     */
    protected static GraphicsConfiguration getDefaultConfiguration() {
    	
    	try{
	        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        
	        //check the headless mode
	        if (GraphicsEnvironment.isHeadless()){
	        	
	            // This triggers creation of the toolkit.
	            // Because java.awt.headless property is set to true, this 
	            // will be an instance of headless toolkit.	        	
	        	//Toolkit tk = Toolkit.getDefaultToolkit();
	        	return null;
	        	
	        }
	        
	        GraphicsDevice gd = ge.getDefaultScreenDevice();
	        return gd.getDefaultConfiguration();
    	}
    	catch(HeadlessException  e){
    		return null;
    	}
    }
 
    protected static BufferedImage toCompatibleImage(BufferedImage image, GraphicsConfiguration gc) {
    	
        if (gc == null){
            gc = getDefaultConfiguration();
        }
        if (gc != null){
	        int w = image.getWidth();
	        int h = image.getHeight();
	        int transparency = image.getColorModel().getTransparency();
	        BufferedImage result = gc.createCompatibleImage(w, h, transparency);
	        Graphics2D g2 = result.createGraphics();
	        g2.drawRenderedImage(image, null);
	        g2.dispose();
	        return result;
        }
        
        //for headless configuration
        return image;
        
    }
 
    //returns target
    protected static BufferedImage copy(BufferedImage source, BufferedImage target) {
    	
        Graphics2D g2 = target.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        double scalex = (double) target.getWidth()/ source.getWidth();
        double scaley = (double) target.getHeight()/ source.getHeight();
        AffineTransform xform = AffineTransform.getScaleInstance(scalex, scaley);
        g2.drawRenderedImage(source, xform);
        g2.dispose();
        return target;
        
    }
    protected static BufferedImage copy(BufferedImage source, int width, int height) {
    	
        Graphics2D g2 = source.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        double scalex = (double) source.getWidth()/ width;
        double scaley = (double) source.getHeight()/ height;
        AffineTransform xform = AffineTransform.getScaleInstance(scalex, scaley);
        g2.drawRenderedImage(source, xform);
        g2.dispose();
        return source;
        
    }	    
    /**
     * returns a scaled image
     * @param image the original image to scale
     * @param width the new width	
     * @param height the new hight
     * @param fixeRatio if true keep ratio of the image
     * @return the scaled image
     */
    public static BufferedImage getScaledInstance(BufferedImage image, int width, int height, boolean fixeRatio) {
    	
    	GraphicsConfiguration gc = getDefaultConfiguration();
        int transparency = image.getColorModel().getTransparency();
        if (gc != null){
        	return copy(image, gc.createCompatibleImage(width, height, transparency));
        }
        return copy(image,width,height);
        
    }
    /**
     * returns a scaled image
     * @param image the original image to scale
     * @param width the new width	
     * @param height the new hight
     * @param fixeRatio if true keep ratio of the image
     * @return the scaled image
     */
    public static BufferedImage getScaledInstance(Image image, int width, int height, boolean fixeRatio) {
    	
    	BufferedImage bufImg = toBufferedImage( image );    
		
		double origWidth = (double)image.getWidth(null);
		double origHeight= (double)image.getHeight(null);
		
		if (origWidth == width && origHeight == height){
			return bufImg;
		}
		
		//check if the image is to small:
		if ( (origWidth < width && origHeight < height) || (origWidth > width && origHeight > height) ){
			//if the width is small than the height, scale the width:
			if (origHeight > origWidth){
				//resize the height to maximum:
				bufImg = ImageScaleService.scaleImage(bufImg, (int)(origWidth/(origHeight/height)), height );
				//height is ok, bust is the width also ok?					
				origWidth  = (int)(origWidth/(origHeight/height));
				origHeight = height;
				if (origWidth > width){
					return ImageScaleService.scaleImage(bufImg, width, ( (int)(origHeight/(origWidth/width)) ) );
				}
				return bufImg;
			}
			else{
				//resize the width to maximum if the width is greater thant the height or if both sides has the same lenght
				bufImg = ImageScaleService.scaleImage(bufImg, width, ( (int)(origHeight/(origWidth/width)) ) );
				//width is ok but is the height also ok?					
				origHeight=(int)(origHeight/(origWidth/width));
				origWidth = width;
				if (origHeight > height){
					//picture is more height as small -> resize the height:
					return ImageScaleService.scaleImage(bufImg, (int)(origWidth/(origHeight/height)), height );
				}
				return bufImg;
			}
		}

		//check if the height is to big:
		if (origHeight > height && origWidth <= width){
			//resize the height:
			return ImageScaleService.scaleImage(bufImg, (int)(origWidth/(origHeight/height)), height );
		}
		//check if thw width is to great:
		if (origWidth > width && origHeight <= height){
			return ImageScaleService.scaleImage(bufImg, width, ( (int)(origHeight/(origWidth/width)) ) );
		}	
					
		return ImageScaleService.scaleImage(bufImg, width, height );
		
    }
    

    public static BufferedImage scaleImage(BufferedImage bsrc, int width, int height) {

	   BufferedImage bdest =
	      new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	   Graphics2D g = bdest.createGraphics();
	   AffineTransform at =
	      AffineTransform.getScaleInstance((double)width/bsrc.getWidth(),
	          (double)height/bsrc.getHeight());
	   g.drawRenderedImage(bsrc,at);
	   return bdest;
	   
  }
	   
	/**
	 * This method returns a buffered image with the contents of an image
	 * @see http://www.exampledepot.com/egs/java.awt.image/Image2Buf.html
	 * @param image the image to converd
	 * */
    public static BufferedImage toBufferedImage(Image image) {
    	
    	Logger logger = Logger.getLogger(ImageScaleService.class);
    	logger.info ("get BufferedImage of Image");
    	
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }
    
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
    
        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent Pixels
        //boolean hasAlpha = hasAlpha(image);
    
        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
       
        try {
        	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            /*
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }
            */
    
            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        	logger.warn ("error to get default screen device -> The system does not have a screen?");
        }
    
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            /*
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            */
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
	 * return the width of an image
	 * 
	 * @return the width of an image
	 * @param urlstr
	 *            the url of the image
	 */
	public static Integer calcImageWidth(String urlstr) {

		int i = urlstr.lastIndexOf("/");
		if (i < 0) {
			i = urlstr.lastIndexOf("\\");
		}
		if (i < 0) {
			System.out.println("could not extract the filename from url: " + urlstr);
			return null;
		}
		urlstr = urlstr.substring(i);
		String cacheDir = PropertiesReader
				.getProperty(PropertiesReader.FILE_CACHE);

		File f = new File(cacheDir + urlstr);
		return ImageScaleService.readImageWidth(f);
	}


}
