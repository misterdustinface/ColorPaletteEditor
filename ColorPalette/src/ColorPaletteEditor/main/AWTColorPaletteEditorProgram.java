package ColorPaletteEditor.main;

import generic.structures.Grid;
import shapes.Point;
import shapes.Polygon;
import shapes.PolygonBuilder;
import shapes.Rectangle;
import AWT.UI.AWTEditorPanel;
import AWT.UI.AWTMenuButton;
import AWT.UI.AWTMenuButtonLayer;
import AWT.UI.AWTProgramWindow;
import AWT.UI.CommonMenus.AWTFileMenu;
import AWT.UI.Mouse.AWTMouseUserDevice;
import AWT.UI.Mouse.AWTMouseUserDeviceDisplayLayer;
import AWT.graphicdata.EditorAWTGraphicData;
import ColorPaletteEditor.AWT.UI.AWTBarSliderColorModifier;
import ColorPaletteEditor.AWT.UI.AWTColorPaletteMenu;
import ColorPaletteEditor.UI.ColorModifier;
import ColorPaletteEditor.UI.ColorPaletteMenu;
import UI.UILayer;
import UI.input.MouseUserDevice;

public class AWTColorPaletteEditorProgram extends ColorPaletteEditorApplication {
	
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
	
	private AWTMouseUserDevice mouseDev;
	private AWTBarSliderColorModifier colorMod;
	
	public AWTColorPaletteEditorProgram() {
		super();
		AWTProgramWindow window = new AWTProgramWindow("Color Palette");
		window.setSize(416, 284);
		AWTEditorPanel editorPanel = new AWTEditorPanel(mouseDev);
		editorPanel.setLayerManager(layerManager);
		window.add(editorPanel);
		window.revalidate();
		start();
	}
	
	public static void main(String[] args) {
		new AWTColorPaletteEditorProgram();
	}

	MouseUserDevice newMouseUserDevice() {
		mouseDev = new AWTMouseUserDevice();
		return mouseDev;
	}
	
	ColorModifier newColorModifier() {
		colorMod = new AWTBarSliderColorModifier(COLOR_CHOOSER_DISPLAYBOX);
		return colorMod;
	}

	ColorPaletteMenu newColorPaletteMenu() {
		AWTColorPaletteMenu paletteMenu = new AWTColorPaletteMenu(COLOR_PALETTE_DISPLAYGRID);
		paletteMenu.setPosition(COLOR_PALETTE_POSITION);
		paletteMenu.setButtonOffset(BUTTON_OFFSET);
		paletteMenu.setButtonSize(BUTTON_SIZE);
		return paletteMenu;
	}

	UILayer newColorChooserLayer() {
		return colorMod;
	}

	UILayer newColorDeleteButtonLayer() {
		AWTMenuButton COLOR_DELETE_BUTTON = new AWTMenuButton();
		COLOR_DELETE_BUTTON.setText("DELETE");
		
		EditorAWTGraphicData graphicData = EditorAWTGraphicData.getGraphicData();
		Polygon p = PolygonBuilder.makeBox(graphicData.getThicknessOf("buttonWidth"), graphicData.getThicknessOf("buttonHeight"));
		p.shift(DELETE_BUTTON_X_POS, DELETE_BUTTON_Y_POS);
		COLOR_DELETE_BUTTON.setPolygon(p);
		COLOR_DELETE_BUTTON.setButtonPressedFunction(paletteMenu.getColorDeleteFunction());
		
		AWTMenuButtonLayer DELETE_BUTTON_LAYER = new AWTMenuButtonLayer();
		DELETE_BUTTON_LAYER.setButton(COLOR_DELETE_BUTTON);
		
		return DELETE_BUTTON_LAYER;
	}

	UILayer newFileMenu() {
		AWTFileMenu fileMenu = new AWTFileMenu(colorFiler);
		fileMenu.setPosition(new Point(X_OFFSET, Y_OFFSET));
		return fileMenu;
	}

	UILayer newUserDeviceDisplayLayer() {
		return new AWTMouseUserDeviceDisplayLayer(mouseDev);
	}
}