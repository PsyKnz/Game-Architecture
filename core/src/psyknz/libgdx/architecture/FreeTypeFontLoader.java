package psyknz.libgdx.architecture;

import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Array;

/** {@link AssetLoader} for {@link BitmapFont} instances. Loads the font description file (.fnt) asynchronously, loads the
 * {@link Texture} containing the glyphs as a dependency. The {@link FreeTypeFontParameter} allows you to set things like texture
 * filters or whether to flip the glyphs on the y-axis..
 * @author mzechner */
public class FreeTypeFontLoader extends AsynchronousAssetLoader<BitmapFont, FreeTypeFontLoader.FreeTypeFontParameter> {
	
	FreeTypeFontGenerator.FreeTypeFontParameter p; // Native parameter file used to generate FreeType fonts.
	
	public FreeTypeFontLoader(FileHandleResolver resolver) {
		super(resolver);
		p = new FreeTypeFontGenerator.FreeTypeFontParameter();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, FreeTypeFontParameter parameter) {
		return null;
	}

	@Override
	public void loadAsync (AssetManager manager, String fileName, FileHandle file, FreeTypeFontParameter parameter) {
		// Sets the values for the parameter file. If no loader parameter file is passed then the previous parameters are used.
		if(parameter != null) {
			p.flip = parameter.flip;
			p.characters = parameter.characters;
			p.genMipMaps = parameter.genMipMaps;
			p.magFilter = parameter.magFilter;
			p.minFilter = parameter.minFilter;
			p.packer = parameter.packer;
			p.size = parameter.size;
		}
	}

	@Override
	public BitmapFont loadSync (AssetManager manager, String fileName, FileHandle file, FreeTypeFontParameter parameter) {
		FreeTypeFontGenerator g = new FreeTypeFontGenerator(file);
		BitmapFont font = g.generateFont(p); // Generates a new BitmapFont file.
		g.dispose(); // Disposes of the now used FreeTypeFontGenerator once finished.
		return font;
	}

	/** Parameter to be passed to {@link AssetManager#load(String, Class, AssetLoaderParameters)} if additional configuration is
	 * necessary for the {@link BitmapFont} derived from the {@link FreeTypeFontGenerator}.
	 * @author Alex Crowther */
	static public class FreeTypeFontParameter extends AssetLoaderParameters<BitmapFont> {
		/** The size in pixels */
		public int size = 16;
		/** The characters the font should contain */
		public String characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		/** The optional PixmapPacker to use */
		public PixmapPacker packer = null;
		/** Whether to flip the font horizontally */
		public boolean flip = false;
		/** Whether or not to generate mip maps for the resulting texture */
		public boolean genMipMaps = false;
		/** Minification filter */
		public TextureFilter minFilter = TextureFilter.Nearest;
		/** Magnification filter */
		public TextureFilter magFilter = TextureFilter.Nearest;
	}
}