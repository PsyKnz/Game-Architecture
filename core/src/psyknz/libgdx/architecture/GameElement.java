package psyknz.libgdx.architecture;



import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/** Interface used to make an object usable in a {@link GameScreen}'s render cycle. 
 * @author Alex Crowther */
public interface GameElement {
	
	/** The update function is called by a {@link GameScreen} to update the Renderable's game logic. Renderable must be added to
	 * the GameScreen's elements {@link Array}.
	 * @param delta The time in milliseconds since the last rendering cycle was completed. */
	public void update(float delta);
	
	/** The draw function is called by a {@link GameScreen} to draw the Renderable to the screen. Renderable must be added to
	 * the GameScreen's elements {@link Array}. 
	 * @param batch The {@link SpriteBatch} being used to draw this renderable to the screen. */
	public void draw(SpriteBatch batch, Rectangle drawArea);

}
