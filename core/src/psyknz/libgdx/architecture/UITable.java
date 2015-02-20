package psyknz.libgdx.architecture;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class UITable implements UIFeature {
	
	public float borderSize;		// The size of the border around the outside of the table.
	public float paddingSize;		// The amount of space there should be between each cell and the border.
	public boolean proportional;	// If sizes are proportional they should be between 0 and 1, otherwise in pixels.
	
	private Rectangle bounds;				// Rectangle representing the size and position of the table.
	private Array<float[]> cells;			// Array containing the measurements for each cell as proportion of the table size.
	private Array<Rectangle> cellBounds;	// Array containing bounding boxes for each cell in the table.
	private Array<UIFeature> features;		// Array containing references to all UIFeatures edited by the table.
	
	/**
	 * Creates a blank UITable with no padding or border. Perfect for sub-tables.
	 */
	public UITable() {
		this(0, 0, false);
	}
	
	/**
	 * Creates a new blank UITable with the given padding and border dimensions. 
	 * @param borderSize Size of the border.
	 * @param paddingSize Size of padding between cells.
	 * @param proportional Whether or not the table should use proportional sizes for the border and padding.
	 */
	public UITable(float borderSize, float paddingSize, boolean proportional) {
		this.borderSize = borderSize;		// Sets sizing information for the table.
		this.paddingSize = paddingSize;
		this.proportional = proportional;
		
		bounds = new Rectangle();			// Initialises all variables used by the table.
		cells = new Array<float[]>();
		cellBounds = new Array<Rectangle>();
		features = new Array<UIFeature>();
	}
	
	/**
	 * Adds a new row to the table of the given height, with cells of the given widths.
	 * @param height Height of the cells in the row as a proportion of available space on the table.
	 * @param widths Width of the cells in the row as a proportion of available space on the row.
	 */
	public void addRow(float height, float... widths) {
		float[] c = new float[widths.length + 1];	// Creates a new array to hold cell information.
		c[0] = height;								// The first entry in the array stores the row height.
		for(int i = 1; i < c.length; i++) {			// For every row width specified,
			c[i] = widths[i - 1];					// the width is saved
			cellBounds.add(new Rectangle());		// and a new blank rectangle is created.
		}
		cells.add(c);								// The new row is added to the array of rows.
	}
	
	/**
	 * Adds a new row to the table of the given height and with the number of cells specified. Cells will be of equal width.
	 * @param height Height of the cells in the row as a proportion of available space on the table.
	 * @param numCells Number of cells which should be present on the row.
	 */
	public void addRow(float height, int numCells) {
		float[] widths = new float[numCells];								// Initialises a blank array with entries = cells.
		for(int i = 0; i < widths.length; i++) widths[i] = 1.0f / numCells;	// Sets the size of each cell to an equal amount.
		addRow(height, widths);												// Adds a new row of the given height and widths.
	}
	
	/**
	 * Adds a UIFeature to the table to manage the sizing of.
	 * @param f The UIFeature to be added to the UITable.
	 */
	public void addFeature(UIFeature f) {
		features.add(f);
	}

	@Override
	/** 
	 * Sets the bounding box for the UI Table. All cells in the table are resized and child UI Features have their bounding boxes
	 * updated appropriately.	 * 
	 * @param x coordinate for the bottom left corner of the table.
	 * @param y coordinate for the bottom left corner of the table.
	 * @param width of the table in in-game units.
	 * @param height of the table in in-game units.
	 */
	public void setBounds(float x, float y, float width, float height) {
		bounds.set(x, y, width, height);	// Sets the size of the table's bounding box.
		
		float border;												// Variable to store the size of the border in in-game units.
		if(proportional && borderSize > 0) {						// If the border is a percentage of the total size of the table
			if(width < height) border = bounds.width * borderSize;	// the shortest edge is used as the basis for calculating the size
			else border = bounds.height * borderSize;				// of the border in units.
		}
		else border = borderSize;									// If not proportional then the exact value is used.
		
		Rectangle borderedBounds = new Rectangle(x + border, y + border,	// A new Rectangle is defined inside of the table
				bounds.width - border * 2, bounds.height - border * 2);		// bounds which accounts for the border.
		
		float padding;													// Variable to store the width of cell padding in in-game units.
		if(proportional && paddingSize > 0) {							// If the padding is a percentage of the total size of the table
			if(width < height) padding = bounds.width * paddingSize;	// the shortest edge is used as the basis for calculating the
			else padding = bounds.height * paddingSize;					// size of the padding in units.
		}
		else padding = paddingSize;										// Otherwise padding size is set directly.
		
		cellBounds.clear();	// Clears the contents of the array holding bounding rectangles for all UI features before refilling it.
		
		float maxCellHeight = borderedBounds.height - padding * (cells.size + 1);	// Maximum height available for cells after padding
		
		Rectangle cellRect = new Rectangle();	// Instantiates a blank rectangle to manipulate.
		cellRect.y = borderedBounds.y + borderedBounds.height;
		for(float[] r : cells) {												// For every row of cells in the table
			cellRect.height = maxCellHeight * r[0];								// the rows height is determined,
			cellRect.x = borderedBounds.x + padding;							// the blank rect is set against the left margin,
			cellRect.y -= cellRect.height + padding;							// and the rects y is set.
			float maxCellWidth = borderedBounds.width - padding * (r.length);	// The amount of space available for cells is found.
			
			for(int i = 1; i < r.length; i++) {				// For every cell present in the current row
				cellRect.width = maxCellWidth * r[i];		// the width of the cell is determined,
				Rectangle rect = new Rectangle(cellRect);	// and a new rectangle is constructed using the blank rect.
				cellBounds.add(rect);						// The new rect is added to the array of cell bounding boxes.
				cellRect.x += cellRect.width + padding;		// The blank rect is moved along to the position of the next cell.
			}
		}
		
		for(int i = 0; i < features.size; i++) {					// For every UI feature managed by this table
			if(i < cellBounds.size) cellRect = cellBounds.get(i);	// its newly created bounding box is accessed.
			else break;												// If there are no bounding boxes left features cease to be updated.
			
			if(features.get(i).getClass() == UITable.class) {	// If the feature being evaluated is a table itself
				UITable t = (UITable) features.get(i);			// it is cast into a variable to be manipulated.
				t.borderSize = 0;								// Sub-table borders are removed.
				t.paddingSize = padding;						// Sub-tables use the same padding values as their parent,
				t.proportional = false;							// and proportional padding is disabled.
				cellRect.width += padding * 2;					// Padding around the new cell is also removed,
				cellRect.height += padding * 2;					// Since it will be added when the sub-table sets its size.
				cellRect.x -= padding;							// Cell is shifted appropriately on the x axis
				cellRect.y -= padding;							// and the y axis.
			}
			
			features.get(i).setBounds(cellRect.x, cellRect.y, cellRect.width, cellRect.height); // Sets size of corresponding UI feature.
		}
	}

	@Override
	/**
	 * Sets the position of the table, and moves all UIFeatures managed by the table appropriately.
	 * @param x position of the tables bottom left corner on the x-axis.
	 * @param y position of the tables bottom left corner on the y-axis.
	 */
	public void setPosition(float x, float y) {
		translate(x - bounds.x, y - bounds.y);	// Translates the table by the difference in its posiition and the new position.
	}
	
	/**
	 * Shifts the table, and all of its managed UIFeatures, by the specified distance.
	 * @param x amount the table should be moved along the x-axis.
	 * @param y amount the table should be moved along the y-axis.
	 */
	public void translate(float x, float y) {
		for(UIFeature f : features) f.setPosition(f.getBounds().x + x, f.getBounds().x + y);	// All UIFeatures are moved.
		bounds.setPosition(bounds.x + x, bounds.y + y);											// Then the tables position is updated.
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}
	
	/**
	 * The number of cells currently present in the table which can hold UIFeatures.
	 * @return Number of cells currently present in the table. Useful for quickly filling with features.
	 */
	public int getNumCells() {
		int numCells = 0;
		for(float[] row : cells) numCells += row.length - 1;
		return numCells;
	}
}
