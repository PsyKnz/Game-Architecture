package psyknz.libgdx.architecture;

import com.badlogic.gdx.math.Rectangle;

public interface UIFeature {
	
	public void setBounds(float x, float y, float width, float height);
	
	public Rectangle getBounds();
	
	public void setPosition(float x, float y);
	
}
