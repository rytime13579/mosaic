import java.io.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
public class out{
	//when i reffer to "image pixels" im refernig to the image that will make up the mosaic
public static void main(String[] args) throws IOException {
		System.out.println("Compiling images");
		//class gets all the images that will be used as pixels
		image_px pixels = new image_px();
		// imports image as buffered image to be used to get width and height for downscalling
		BufferedImage bimg = ImageIO.read(new File("in.jpg"));
		// sends image to image class to be used as a 2D array of pixels as well as the target width and height
		// here the target width and height is the image down scaled by a factor of 10
		image main = new image("in.jpg", (int)bimg.getWidth()/10, (int)bimg.getHeight()/10);
		// gets 2d array of pixles from image class
		Color[][] mainAr = main.get_pixels();
		// creates a blank 4d array that will in turn be used as a matrix for each image used as a pixel
		Color[][][][] mosaic = new Color[mainAr.length]
									[mainAr[0].length]
									[pixels.faces[0].get_pixels().length]
									[pixels.faces[0].get_pixels().length];
		System.out.println("Assigning image positions");
		// yes this is a 5D array but no its not that bad
		// the first index value corisponds to the average red value of our image pixels
		// the second is the green, the third the blue (r g b)
		Color[][][][][] face_px = pixels.get_pix_pic();
		// iterates through the image that will be made into a mosaic
        for (int x = 0; x < mainAr.length; x++) {
        	for (int z = 0; z < mainAr[x].length; z++) {
        		// gets the red, green, and blue value of each pixel then devides it by 8
        		// we devide by 8 becuase using 8 bit color is to much for the heap space to handle 
        		// so instead of 256 possible red, green, and blue values there are only 32 greatly reducing memory usage
        		int r = (mainAr[x][z].getRed())/4;
        		int b = (mainAr[x][z].getBlue())/4;
        		int g = (mainAr[x][z].getGreen())/4;
        		// asigns the image pixel to the matrix if there is a suitable image for that pixel
        		Color[][] pixel = face_px[r][g][b];
        		if (pixel[0][0] != null) {
        			mosaic[x][z] = pixel;
        		}else{
        			// if there is not a suitable image pixel, the program then shifts the color of that image pixel to ]
        			// closer math the color of that pixel
    				pixel = pixels.assign_based_great(r,g,b);
        		}
        		mosaic[x][z] = pixel;
        	}
        }
        System.out.println("Exporting image");
        // this... caused a lot of pain
        // inits the array that will be used as the final image
        Color[][] final_ar = new Color[mainAr.length * pixels.faces[0].get_pixels().length]
        							[mainAr[0].length * pixels.faces[0].get_pixels().length];
        // the program now converts the 4D array of pixels (colors) to a 2D array that can be converted back into
        // png or jpg image formats
        int up = 0;
        int up2 = 0;
        // each for loop points to an individual image in the matrix of images, then each indivudual pixel in each
        // image within the matrix
        // asside from that I cannot explain how it works... because this accedently worked
        for (int x = 0; x < mosaic.length; x++) {
        	for (int y = 0; y < mosaic[x].length; y++) {
        		up = 0;
        		for (int z = 0; z < mosaic[x][y].length; z++){
        			up2 = 0;
        			for (int a = 0; a < mosaic[x][y][z].length; a++) {
        				final_ar[x*16+up][y*16+up2] = mosaic[x][y][z][a];
        				up2++;
        			}
        			up++;
        		}
        	}
        }
        // sends our final image ar to the image class
        image final_out = new image(final_ar);
        // isntructs the class to save the image as "final.png"
        final_out.save("final.png");
        System.out.println("Done!");
	}
}