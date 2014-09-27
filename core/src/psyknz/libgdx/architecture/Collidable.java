package psyknz.libgdx.architecture;

import com.badlogic.gdx.math.Rectangle;

/** Interface used to represent an object which can collide with other objects. Has a function to return its bounding box and a function
 *  to process collisions with other Collidable objects. * 
 * @author Alex 'PsyK' Crowther. */
public interface Collidable {
	
	/** @return {@link Rectangle} representing the Collidables bounding box. */
	public Rectangle getBounds();
	
	/** Should be called if a collision has occured between this Collidable and another. Typically collision involves a comparison
	 *  between this object and others bounding boxes.
	 * @param collider Reference to the other Collider this object has collided with. */
	public void collision(Collidable collider);

}
