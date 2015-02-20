package psyknz.libgdx.architecture;

import aurelienribon.tweenengine.TweenAccessor;

public class UIFeatureTween implements TweenAccessor<UIFeature> {
	
	public static final int X = 0;
	public static final int Y = 1;
	public static final int POS = 2;
	public static final int WIDTH = 3;
	public static final int HEIGHT = 4;
	public static final int SIZE = 5;
	public static final int BOUNDS = 6;

	@Override
	public int getValues(UIFeature ui, int tweenType, float[] val) {
		switch(tweenType) {
			case X: val[0] = ui.getBounds().x; return 1;
			case Y: val[0] = ui.getBounds().y; return 1;
			case POS: val[0] = ui.getBounds().x; val[1] = ui.getBounds().y; return 2;
			case WIDTH: val[0] = ui.getBounds().width; return 1;
			case HEIGHT: val[0] = ui.getBounds().height; return 1;
			case SIZE: val[0] = ui.getBounds().width; val[1] = ui.getBounds().height; return 2;
			case BOUNDS: val[0] = ui.getBounds().x; val[1] = ui.getBounds().y;
					val[2] = ui.getBounds().width; val[3] = ui.getBounds().height; return 4;
			default: assert false; return -1;
		}
	}

	@Override
	public void setValues(UIFeature ui, int tweenType, float[] val) {
		switch(tweenType) {
			case X: ui.setPosition(val[0], ui.getBounds().y); break;
			case Y: ui.setPosition(ui.getBounds().x, val[0]); break;
			case POS: ui.setPosition(val[0], val[1]); break;
			case WIDTH: ui.setBounds(ui.getBounds().x, ui.getBounds().y, val[0], ui.getBounds().height); break;
			case HEIGHT: ui.setBounds(ui.getBounds().x, ui.getBounds().y, ui.getBounds().width, val[0]); break;
			case SIZE: ui.setBounds(ui.getBounds().x, ui.getBounds().y, val[0], val[1]); break;
			case BOUNDS: ui.setBounds(val[0], val[1], val[2], val[3]); break;
			default: assert false; break;
		}
	}

}
