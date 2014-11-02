package psyknz.libgdx.architecture;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/** The GameScreen class implements all the boiler plate code required to produce a libGDX {@link Screen} which is able to display
 * 2D graphics, access a shared asset manager, process in-game events, and pass user generated events to objects extending the
 * {@link InputAdapter} class. * 
 * @author Alex Crowther */
public class GameScreen extends InputAdapter implements Screen, GameElement, GameEventListener {
	
	protected GameCore game; // Reference to the GameCore object managing this Screen.
	
	protected InputMultiplexer input; // The InputMultiplexer used by the screen to process user generated events.
	
	public Color bgColor = new Color(0, 0, 0, 1); // Color used to clear the screen each render call.
	
	protected SpriteBatch batch; // The SpriteBatch used to draw all 2D graphics.
	
	private Rectangle viewport; // Rectangular region of the game that's currently visible.
	
	public GameScreen nextScreen; // The next GameScreen which should be loaded by the game.
	
	protected OrthographicCamera camera; // The camera used to display the 2D game space.
	
	public boolean viewSizeMin = true; // Whether viewSize is the length of the smallest edge (true) or largest edge (false) on the camera.
	
	public float viewSize = Gdx.graphics.getHeight(); // The size that the viewArea should be. By default the smallest edge should be 480 in-game units.
	
	/** Creates a new GameScreen and initialises all non-OpenGL features. 
	 * @param game Reference to the {@link GameCore} object managing this screen. */
	public GameScreen(GameCore game) {
		this.game = game;
		input = new InputMultiplexer();
	}
	
	@Override
	public void show() {
		batch = new SpriteBatch(); // Creates a new SpriteBatch to use each time the screen is shown.
		Gdx.input.setInputProcessor(input); // Registers the GameScreen's InputMultiplexer as the input device when the screen is shown.
	}
	
	@Override
	public void render(float delta) {
		// Clears the screen using the currently set bgColor.
		Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Updates the game logic for all Renderable's in the GameScreen.
		update(delta);
		
		// Draws the screen and all the GameElements it's managing to the screen.
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		draw(batch, getViewport());
		batch.end();
		
		// Changes to the next screen if one has been declared.
		if(nextScreen != null) game.setScreen(nextScreen);
	}
	
	@Override
	public void CallEvent(GameEvent e) {}
	
	@Override
	public void update(float delta) {}
	
	@Override
	public void draw(SpriteBatch batch, Rectangle drawArea) {}
	
	@Override
	public void resize(int width, int height) {
		// Determines how big the camera should be to maintain the screens aspect ratio while maintaining the target viewSize.
		float newWidth, newHeight;
		newWidth = newHeight = viewSize;
		
		// Adjusts the size of the camera with viewSize representing the smallest edge.
		if(viewSizeMin) {
			if(width >= height) newWidth = newWidth * width / height;
			else newHeight = newHeight * height / width;
		}
		
		// Adjusts the size of the camera with viewSize representing the largest edge.
		else {
			if(width >= height) newHeight = newHeight * height / width;
			else newWidth = newWidth * width / height;
		}
		
		camera = new OrthographicCamera(newWidth, newHeight);
		camera.position.x = newWidth / 2;
		camera.position.y = newHeight / 2;
		camera.update();
	}
	
	@Override
	public void pause() {}
	
	@Override
	public void resume() {}
	
	@Override
	public void hide() {
		this.dispose(); // All assets used by the GameScreen are disposed of when it is changed.
	}
	
	@Override
	public void dispose() {
		batch.dispose(); // Disposes of the SpriteBatch if this screen loses focus.
	}
	
	/** Gets an up to date representation of the GameScreens viewport.
	 * @return Reference to the {@link Rectangle} which represents the position and area of the viewport. */
	public Rectangle getViewport() {
		if(camera == null) return null; // If there is no camera then there is no viewport and null is returned immediately.
		if(viewport == null) viewport = new Rectangle(); // If no viewport Rectangle exists a new one is created.
		viewport.setSize(camera.viewportWidth, camera.viewportHeight); // Updates the width and height of the viewport.
		viewport.setCenter(camera.position.x, camera.position.y); // Updates the focal point of the viewport.
		return viewport;
	}
	
	public OrthographicCamera getCamera() {
		return camera;
	}
	
	public GameCore getGame() {
		return game;
	}
}
