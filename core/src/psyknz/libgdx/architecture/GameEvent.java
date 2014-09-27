package psyknz.libgdx.architecture;


/** Object which represents an in-game event. Contains details about the event and its source * 
 * @author Alex Crowther */
public class GameEvent {

	public final String id; // The source of the GameEvent
	
	public GameEvent(String id) {
		this.id = id;
	}
}
