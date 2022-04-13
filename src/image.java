import java.awt.image.*;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
public class image {
    private File img;
    private BufferedImage in;
    private Color[][] image_ar;
    private boolean passed_ar;
    // main constructor takes a filename and allows it to be converted to a 2D array of color objects
    public image(String filename, int width, int height) throws IOException{
        this.img = new File(filename);
        BufferedImage newImage = ImageIO.read(img);
        this.in = resizeImage(newImage, width, height);
        this.image_ar = new Color[in.getWidth()][in.getHeight()];
        for (int x = 0; x < image_ar.length; x++){
            for (int z = 0; z < image_ar[x].length; z++){
                Color c = new Color(in.getRGB(x,z), true);
                image_ar[x][z] = c;
            }
        }
        this.passed_ar = true;
    }
    
    public image(Color[][] ar) {
    	this.image_ar = ar;
    	this.passed_ar = false;
    }
    
     public Color[][] get_pixels(){
        return this.image_ar;
    }
    
     public int[] getAvgRGB() {
    	 int r = 0;
    	 int g = 0;
    	 int b = 0;
    	 int dev = 0;
    	 for (Color[] ar : image_ar) {
    		 for (Color px : ar) {
    			 r+= px.getRed();
    			 g+= px.getGreen();
    			 b+= px.getBlue();
    			 dev++;
    		 }
    	 }
    	 int[] temp = {r/dev,g/dev,b/dev};
    	 return temp;
     }
     
    public void save(String name) throws IOException{
    	if (passed_ar) {
	        BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_RGB);
	        for (int x = 0; x < image_ar.length; x++){
	            for (int z = 0; z < image_ar[x].length; z++){
	                Color color = new Color(image_ar[x][z].getRGB());
	                out.setRGB(x,z,color.getRGB());
	            }
	        }
	        File outputfile = new File(name);
	        ImageIO.write(out, "png", outputfile);
    	}else {
    		BufferedImage out = new BufferedImage(image_ar.length, image_ar[0].length, BufferedImage.TYPE_INT_RGB);
    		Color color;
	        for (int x = 0; x < image_ar.length; x++){
	            for (int z = 0; z < image_ar[x].length; z++){
	            	if (image_ar[x][z] == null) {
	            		color = new Color(0,0,0);
	            	}else {
	            		color = new Color(image_ar[x][z].getRGB());
	            	}
	                
	                out.setRGB(x,z,color.getRGB());
	            }
	        }
	        File outputfile = new File(name);
	        ImageIO.write(out, "png", outputfile);
    	}
    }
    
    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }
}
