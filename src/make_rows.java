import java.awt.*;
public class make_rows{
	public Color[][][] mosaic;
	public make_rows(Color[][] mainAr, image_px ar, int index){
		this.mosaic = new Color[mainAr[index].length][ar.faces[0].get_pixels().length][ar.faces[0].get_pixels().length];
	}
	public void update_row(Color[][] update, int pos) {
		this.mosaic[pos] = update;
	}
}