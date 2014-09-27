package psyknz.libgdx.architecture;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/** The GameCore class provides all basic boilerplate shared functionality required of a game. This includes custom loaders for .ttf
 * files When creating a new project have the main game file extend this object and override the create method. Ensure you call the
 * overidden method and then add any additional code to the end of it, such as loading your first GameScreen. 
 * @author Alex Crowther */
public class GameCore extends Game {
	
	public AssetManager assets; // The asset manager used by the game to store all shared resources.
	
	@Override
	public void create() {
		// Creates a new AssetManager and configures it.
		assets = new AssetManager();
		assets.setLoader(BitmapFont.class, ".ttf", new FreeTypeFontLoader(new InternalFileHandleResolver()));
	}
	
	@Override
	public void dispose() {
		assets.dispose(); // When the game closes all shared assets are disposed of.
	}

}
