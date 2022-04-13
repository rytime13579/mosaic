import java.awt.*;
import java.io.*;
public class image_px{
    public image[] faces;
    private int[][] sums = new int[64*64*64][3];
    private Color[][][][][] masterImgList;
    public image_px() throws IOException{
        File main_dir = new File("images/lfw");
        File[] dir = main_dir.listFiles();
        this.faces = new image[dir.length];  
        for (int i = 0; i < dir.length; i++){
            this.faces[i] = new image("images/lfw/" + dir[i].getName(), 16, 16);
        }
        //asign to list
        Color[][] len_for = faces[0].get_pixels();
        Color[][][][][] rgb = new Color[64][64][64][len_for.length][len_for[0].length];
        // gets average of each image pixel and assigns them correctly
        for (int main = 0; main < faces.length; main++) {
       	 try {
	        	 int[] avgs = faces[main].getAvgRGB();
	        	 int r = avgs[0];
	        	 int g = avgs[1];
	        	 int b = avgs[2];
	    		 if(rgb[r/4][g/4][b/4][0][0] == null) {
	    			 rgb[r/4][g/4][b/4] = this.faces[main].get_pixels();
	    		 }
	         }catch (NullPointerException e) {
	        	 System.out.println(main);
	         }
        }
        this.masterImgList = rgb;
        
        // multiplies each red green and blue value together to be used as a variable for image comparision later
        for (int red = 0; red < masterImgList.length; red++) {
			 for (int green = 0; green < masterImgList[red].length; green++) {
				 for (int blue = 0; blue < masterImgList[green].length; blue++) {
					 if (masterImgList[red][green][blue] != null) {
						 int[] temp1 = {red,green,blue};
						 this.sums[red*green*blue] = temp1;
					 }
				  }
			 }
        }
    }
    
     public Color[][][][][] get_pix_pic(){
         return masterImgList;
     }
     // im not going to explain how this works... because i'm far too tired to go through that pain rn
     // but essentially it shifts the color of the red green and blue values of the clossest image to the
     // r, g, and b values passed to it inside masterImgLisst to fill more parts of the final image
     public Color[][] assign_value(int r, int g, int b){
    	 int sum = r * g * b;
    	 Color[][] opt1 = new Color[masterImgList[r][g][b].length][masterImgList[r][g][b][0].length];
    	 Color[][] opt2 = new Color[masterImgList[r][g][b].length][masterImgList[r][g][b][0].length];
    	 int val1 = 0;
    	 int val2 = 0;
    	 int[] opt1rgb = new int[3];
    	 int[] opt2rgb = new int[3];
    	 for (int i = sum; i < sums.length; i++) {
    		 int red = sums[i][0];
    		 int green = sums[i][1];
    		 int blue = sums[i][2];
    		 if (masterImgList[red][green][blue][0][0] != null) {
    			 opt1 = masterImgList[red][green][blue];
    			 opt1rgb[0] = red;
    			 opt1rgb[1] = green;
    			 opt1rgb[2] = blue;
    			 val1 = red*green*blue;
    			 break;
    		 }
    	 }
    	 for (int j = sum; j > 0; j--) {
    		 int red = sums[j][0];
    		 int green = sums[j][1];
    		 int blue = sums[j][2];
    		 if (masterImgList[red][green][blue][0][0] != null) {
    			 opt2 = masterImgList[red][green][blue];
    			 opt2rgb[0] = red;
    			 opt2rgb[1] = green;
    			 opt2rgb[2] = blue;
    			 val2 = red*green*blue;
    			 break;
    		 }
    	 }
    	 Color[][] returnar = new Color[masterImgList[r][g][b].length][masterImgList[r][g][b][0].length];
    	 try { 
	    	 if (Math.abs(val1 - sum) > Math.abs(val2 - sum)) {
	    		 //opt2 is better
	    		 int diffr = Math.abs((r*4) - (opt2rgb[0]*4));
	    		 int diffg = Math.abs((g*4) - (opt2rgb[1]*4));
	    		 int diffb = Math.abs((b*4) - (opt2rgb[2]*4));
	    		 Color[][] temp = new Color[masterImgList[r][g][b].length][masterImgList[r][g][b][0].length];
	    		 for (int i = 0; i < temp.length; i++) {
	    			 for (int j = 0; j < temp[i].length; j++) {
	    				 int ther = opt2[i][j].getRed() + diffr;
	    				 int theg = opt2[i][j].getGreen() + diffg;
	    				 int theb = opt2[i][j].getBlue() + diffb;
	    				 if (ther > 255) {
	    					 ther = 255;
	    				 }if (theg > 255) {
	    					 theg = 255;
	    				 }if (theb > 255) {
	    					 theb = 255;
	    				 }
	    				 temp[i][j] = new Color(ther, theg , theb);
	    			 }
	    		 }
	    		 returnar = temp;
	    	 }else {
	    		 //opt1 is better
	    		 int diffr = Math.abs((r*4) - (opt1rgb[0]*4));
	    		 int diffg = Math.abs((g*4) - (opt1rgb[1]*4));
	    		 int diffb = Math.abs((b*4) - (opt1rgb[2]*4));
	    		 Color[][] temp = new Color[masterImgList[r][g][b].length][masterImgList[r][g][b][0].length];
	    		 for (int i = 0; i < temp.length; i++) {
	    			 for (int j = 0; j < temp[i].length; j++) {
	    				 int ther = opt1[i][j].getRed() - diffr;
	    				 int theg = opt1[i][j].getGreen() - diffg;
	    				 int theb = opt1[i][j].getBlue() - diffb;
	    				 if (ther < 0) {
	    					 ther = 0;
	    				 }if (theg < 0) {
	    					 theg = 0;
	    				 }if (theb < 0) {
	    					 theb = 0;
	    				 }
	    				 temp[i][j] = new Color(ther, theg, theb);
	    			 }
	    		 }
	    		 returnar = temp;
	    	 }
    	 }catch (NullPointerException e) {
    		 // defaults to black if all else fails
    		 for (int i = 0; i < returnar.length; i++) {
    			 for (int j = 0; j < returnar[i].length; j++) {
    				 returnar[i][j] = new Color(0,0,0);
    			 }
    		 }
    	 }
    	 return returnar;
     }
     public Color[][] assign_based_great(int r, int g, int b){
    	 double[] scores = new double[faces.length];
    	 int x = 0;
    	 for (image img : this.faces) {
    		 int[] avg = img.getAvgRGB();
    		 //geometric mean theorem to get image closest to the target rgb value -- credits: my dad
    		 scores[x] = Math.sqrt(Math.pow((r - avg[0]), 2) + Math.pow((g - avg[1]), 2) + Math.pow((b - avg[2]), 2));
    		 x++;
    	 }
    	 double min;
    	 int index = 0;
    	 for (int i = 0; i < scores.length-1; i++) {
    		 if (scores[i] < scores[i+1]) {
    			 min = scores[i];
    			 index = i;
    		 }else {
    			 min = scores[i+1];
    			 index = i+1;
    		 }
    	 }
    	 this.masterImgList[r][g][b] = this.faces[index].get_pixels();
    	 return this.faces[index].get_pixels();
     }
}