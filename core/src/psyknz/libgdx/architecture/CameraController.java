package psyknz.libgdx.architecture;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class CameraController extends OrthographicCamera implements InputProcessor {
	
	public static final int MAX_TOUCHES = 10;	// Maximum number of unique touch points tracked by the camera.
	
	public static final int FIT_TO_SCREEN = 0;	// When resized the entire area defined by the camera will be inside of the window.
	public static final int FILL_SCREEN = 1;	// When resized the camera will fill entire window while maintaining its aspect ratio.
	public static final int STRETCH_TO_FIT = 2;	// When resized the area defined by the camera will be stretched to the edges of the window.
	
	private int baseWidth, baseHeight, fillType, screenWidth, screenHeight;
	private Rectangle viewArea;
	
	private float zoom = 1.0f;
	private boolean reversableAspectRatio = false;
	
	private Vector3[] touchCoords;
	private Array<Integer> touchOrder;
	
	public CameraController(int width, int height, int fillType) {
		super(width, height);
		
		baseWidth = width;
		baseHeight = height;
		this.fillType = fillType;
		viewArea = new Rectangle();
		
		touchCoords = new Vector3[MAX_TOUCHES];
		for(Vector3 vec : touchCoords) vec = new Vector3();
		touchOrder = new Array<Integer>();
	}
	
	public void resize(int width, int height) {
		screenWidth = width;
		screenHeight = height;
		
		float screenAspectRatio = (float) width / height;			// Determines the aspect ratio of the resized window.
		float cameraAspectRatio = (float) baseWidth / baseHeight;	// Determines the target aspect ratio for the camera.
		
		if(reversableAspectRatio && 
				((screenAspectRatio > 1 && cameraAspectRatio < 1) || 
				(screenAspectRatio < 1 && cameraAspectRatio > 1))) 
				viewArea.setSize(baseHeight / zoom, baseWidth / zoom);
		else viewArea.setSize(baseWidth / zoom, baseHeight / zoom);
		
		cameraAspectRatio = viewArea.width / viewArea.height;
		
		switch(fillType) {
			case FIT_TO_SCREEN:
				if(cameraAspectRatio > screenAspectRatio) viewArea.height = viewArea.width / screenAspectRatio;
				else viewArea.width = viewArea.height * screenAspectRatio;
				break;
				
			case FILL_SCREEN:
				if(cameraAspectRatio > screenAspectRatio) viewArea.width = viewArea.height / screenAspectRatio;
				else viewArea.height = viewArea.width * screenAspectRatio;
				break;
				
			default: break;
		}
		
		viewportWidth = viewArea.width;
		viewportHeight = viewArea.height;
		update();
	}
	
	@Override
	public void update() {
		super.update();
		viewArea.setPosition(position.x - viewArea.width / 2, position.y - viewArea.height / 2);
	}
	
	public void setZoom(float zoom) {
		this.zoom = zoom;
		resize(screenWidth, screenHeight);
	}
	
	public float getZoom() {
		return zoom;
	}
	
	public void setSize(int width, int height) {
		baseWidth = width;
		baseHeight = height;
		resize(screenWidth, screenHeight);
	}
	
	public Rectangle getViewArea() {
		return viewArea;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(pointer >= MAX_TOUCHES) return false;
		touchCoords[pointer].set(screenX, screenY, 0);
		unproject(touchCoords[pointer]);
		touchOrder.add(pointer);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(pointer >= MAX_TOUCHES) return false;
		touchOrder.removeValue(pointer, true);
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(pointer >= MAX_TOUCHES) return false;
		
		Vector3 vecA = new Vector3(screenX, screenY, 0);
		unproject(vecA);
		vecA.sub(touchCoords[pointer]);
		
		if(touchOrder.size >= 2) {
			if(pointer == touchOrder.first()) {
				
			}
			else if(touchOrder.get(1) == pointer) {
			
			}
		}		
		else if(touchOrder.size == 1) position.add(vecA);
		
		touchCoords[pointer].sub(vecA);
		return false;
	}
	
	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}	

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

}
