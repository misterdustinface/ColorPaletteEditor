package ColorPaletteEditor.main;

import file.GenericExtended.ColorPaletteFiler;
import generic.Application;
import generic.EditorProgramMain;
import generic.structures.ColorData;
import generic.structures.Grid;

import java.util.ArrayList;

import shapes.Point;
import shapes.Polygon;
import shapes.PolygonBuilder;
import shapes.Rectangle;
import UI.UILayerManager;
import AWT.UI.AWTEditorPanel;
import AWT.UI.AWTMenuButton;
import AWT.UI.AWTMenuButtonLayer;
import AWT.UI.AWTProgramWindow;
import AWT.UI.CommonMenus.AWTFileMenu;
import AWT.UI.Mouse.AWTDefaultMouseUserDevice;
import AWT.UI.Mouse.AWTMouseUserDevice;
import AWT.UI.Mouse.AWTSimpleUserDeviceDisplayLayer;
import AWT.graphicdata.EditorAWTGraphicData;
import ColorPaletteEditor.AWT.UI.AWTBarSliderColorModifier;
import ColorPaletteEditor.AWT.UI.AWTColorPaletteMenu;

public class AWTColorPaletteEditorProgram {
	
	static int X_OFFSET     = 16;
	static int Y_OFFSET     = 16;
	
	static int BUTTON_OFFSET = 16;
	static int BUTTON_SIZE   = 32;
	static int BUTTON_ROWS   = 4;
	static int BUTTON_COLS   = 3;
	
	static int DELETE_BUTTON_X_POS = 120;
	static int DELETE_BUTTON_Y_POS = Y_OFFSET;
	
	static int COLOR_CHOOSER_WIDTH  = 204;
	static int COLOR_CHOOSER_HEIGHT = 150;
	
	static int COLOR_CHOOSER_DISPLAYBOX_YOFFSET = Y_OFFSET + BUTTON_SIZE + Y_OFFSET;
	
	static Rectangle COLOR_CHOOSER_DISPLAYBOX = new Rectangle(new Point(X_OFFSET, COLOR_CHOOSER_DISPLAYBOX_YOFFSET), COLOR_CHOOSER_WIDTH, COLOR_CHOOSER_HEIGHT);

	static Point COLOR_PALETTE_POSITION = new Point(2*X_OFFSET + COLOR_CHOOSER_WIDTH, Y_OFFSET);
	static Grid COLOR_PALETTE_DISPLAYGRID = new Grid(BUTTON_ROWS, BUTTON_COLS);
	
	public static void main(String[] args) {
		AWTProgramWindow window = new AWTProgramWindow("Color Palette");
		window.setSize(416, 284);
		
		ArrayList<ColorData> colorPalette 	= new ArrayList<ColorData>();
		ColorPaletteFiler 	 colorFiler 	= new ColorPaletteFiler();
		colorFiler.setPalette(colorPalette);

		final AWTBarSliderColorModifier colorChooser = new AWTBarSliderColorModifier(COLOR_CHOOSER_DISPLAYBOX);
		
		final AWTColorPaletteMenu paletteMenu = new AWTColorPaletteMenu(COLOR_PALETTE_DISPLAYGRID);
		paletteMenu.setPosition(COLOR_PALETTE_POSITION);
		paletteMenu.setButtonOffset(BUTTON_OFFSET);
		paletteMenu.setButtonSize(BUTTON_SIZE);
		paletteMenu.setColorModifier(colorChooser);
		paletteMenu.setPalette(colorPalette);
		colorFiler.addDataModificationListener(paletteMenu.getDataModificationListener());
		
		AWTMenuButton COLOR_DELETE_BUTTON = new AWTMenuButton();
		COLOR_DELETE_BUTTON.setText("DELETE");
		
		EditorAWTGraphicData graphicData = EditorAWTGraphicData.getGraphicData();
		Polygon p = PolygonBuilder.makeBox(graphicData.getThicknessOf("buttonWidth"), graphicData.getThicknessOf("buttonHeight"));
		p.shift(DELETE_BUTTON_X_POS, DELETE_BUTTON_Y_POS);
		COLOR_DELETE_BUTTON.setPolygon(p);
		COLOR_DELETE_BUTTON.setButtonPressedFunction(paletteMenu.getColorDeleteFunction());
		
		AWTMenuButtonLayer DELETE_BUTTON_LAYER = new AWTMenuButtonLayer();
		DELETE_BUTTON_LAYER.setButton(COLOR_DELETE_BUTTON);
		
		AWTMouseUserDevice 	userDevice 	= new AWTDefaultMouseUserDevice();
		AWTEditorPanel 		editorPanel = new AWTEditorPanel(userDevice);
				
		AWTFileMenu fileMenu = new AWTFileMenu(colorFiler);
		fileMenu.setPosition(new Point(X_OFFSET, Y_OFFSET));
		
		UILayerManager layerManager = new UILayerManager();
		layerManager.addLayers(colorChooser,
							   paletteMenu,
							   DELETE_BUTTON_LAYER,
							   fileMenu,
							   new AWTSimpleUserDeviceDisplayLayer(userDevice));
		
		editorPanel.setLayerManager(layerManager);
		
		window.add(editorPanel);
		window.revalidate();
		
		Application editorProgram = new Application();
		editorProgram.setMain(EditorProgramMain.create(layerManager, userDevice));
		editorProgram.start();
	}
}