package ColorPaletteEditor.main;

import generic.ColorData;

import java.util.ArrayList;

import AWT.UI.AWTColorChooserMenu;
import AWT.UI.AWTColorPaletteMenu;
import AWT.UI.AWTDefaultMouseUserDevice;
import AWT.UI.AWTEditorPanel;
import AWT.UI.AWTFileMenu;
import AWT.UI.AWTMenuButton;
import AWT.UI.AWTMouseUserDevice;
import AWT.UI.AWTProgramWindow;
import AWT.UI.AWTSimpleUserDeviceDisplayLayer;
import ColorPaletteEditor.file.ColorPaletteFiler;
import data.shapes.Grid;
import data.shapes.Point;
import data.shapes.Rectangle;

public class AWTColorPaletteEditorProgram {
	
	static int X_OFFSET     = 16;
	static int MENU_WIDTH   = 180;
	static int Y_OFFSET     = 16;
	static int MENU_HEIGHT  = 100;
	
	static int COLOR_CHOOSER_DISPLAYBOX_YOFFSET = Y_OFFSET + 100;
	
	static Rectangle COLOR_CHOOSER_DISPLAYBOX = new Rectangle(new Point(X_OFFSET, COLOR_CHOOSER_DISPLAYBOX_YOFFSET), MENU_WIDTH, MENU_HEIGHT);
	
	static int BUTTON_OFFSET = 16;
	static int BUTTON_SIZE   = 32;
	static int BUTTON_ROWS   = 4;
	static int BUTTON_COLS   = 3;

	static Point COLOR_PALETTE_POSITION = new Point(2*X_OFFSET + MENU_WIDTH, Y_OFFSET);
	static Grid COLOR_PALETTE_DISPLAYGRID = new Grid(BUTTON_ROWS, BUTTON_COLS);
	
	static int DELETE_BUTTON_X_POS = 107;
	static int DELETE_BUTTON_Y_POS = 80 - Y_OFFSET;
	
	public static void main(String[] args) {
		AWTProgramWindow window = new AWTProgramWindow("Color Palette");
		window.setSize(400, 300);
		
		ArrayList<ColorData> colorPalette 	= new ArrayList<ColorData>();
		ColorPaletteFiler 	 colorFiler 	= new ColorPaletteFiler();
		colorFiler.setPalette(colorPalette);

		final AWTColorChooserMenu colorChooser = new AWTColorChooserMenu(COLOR_CHOOSER_DISPLAYBOX);
		
		final AWTColorPaletteMenu paletteMenu = new AWTColorPaletteMenu(colorChooser, COLOR_PALETTE_DISPLAYGRID);
		paletteMenu.setPostition(COLOR_PALETTE_POSITION);
		paletteMenu.setButtonOffset(BUTTON_OFFSET);
		paletteMenu.setButtonSize(BUTTON_SIZE);
		paletteMenu.setPalette(colorPalette);
		colorFiler.addDataModificationListener(paletteMenu.getDataModificationListener());
		
		AWTMenuButton COLOR_DELETE_BUTTON = new AWTMenuButton();
		COLOR_DELETE_BUTTON.textLabel.setText("DELETE");
		COLOR_DELETE_BUTTON.textLabel.center();
		COLOR_DELETE_BUTTON.makeSuggestedBoxRelativeToPoint(DELETE_BUTTON_X_POS, DELETE_BUTTON_Y_POS);
		COLOR_DELETE_BUTTON.setButtonPressedFunction(paletteMenu.getColorDeleteFunction());
		
		//colorChooser.addButton(COLOR_DELETE_BUTTON);		
		
		AWTMouseUserDevice 	userDevice 	= new AWTDefaultMouseUserDevice();
		AWTEditorPanel 		editorPanel = new AWTEditorPanel(userDevice);
		AWTFileMenu 		fileMenu 	= new AWTFileMenu(colorFiler);
				
		editorPanel.addLayer(colorChooser);
		editorPanel.addLayer(paletteMenu);
		editorPanel.addLayer(fileMenu);
		editorPanel.addLayer(new AWTSimpleUserDeviceDisplayLayer(userDevice));
		window.add(editorPanel);
		window.revalidate();
	}
}