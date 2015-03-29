package ColorPaletteEditor.main;

import generic.Application;
import generic.EditorProgramMain;
import ColorPaletteEditor.UI.ColorModifier;
import ColorPaletteEditor.UI.ColorPaletteMenu;
import ColorPaletteEditor.data.ColorPalette;
import ColorPaletteEditor.file.ColorPaletteFiler;
import UI.UILayer;
import UI.UILayerManager;
import UI.input.MouseUserDevice;

public abstract class ColorPaletteEditorApplication {
	
	public ColorPalette colorPalette;
	public ColorModifier colorChooser;
	public ColorPaletteMenu paletteMenu;
	public ColorPaletteFiler colorFiler;
	public MouseUserDevice mouseUserDevice;
	public UILayerManager layerManager;
	
	public ColorPaletteEditorApplication() {
		colorPalette = new ColorPalette();
		colorFiler = new ColorPaletteFiler();
		colorFiler.setPalette(colorPalette);

		colorChooser = newColorModifier();
		paletteMenu = newColorPaletteMenu();
		paletteMenu.setColorModifier(colorChooser);
		paletteMenu.setPalette(colorPalette);
		colorFiler.addDataModificationListener(paletteMenu.getDataModificationListener());
		
		mouseUserDevice = newMouseUserDevice();
		layerManager = new UILayerManager();
	}
	
	abstract MouseUserDevice  newMouseUserDevice();
	abstract ColorModifier    newColorModifier();
	abstract ColorPaletteMenu newColorPaletteMenu();
	abstract UILayer          newColorChooserLayer();
	abstract UILayer          newColorDeleteButtonLayer();
	abstract UILayer          newFileMenu();
	abstract UILayer          newUserDeviceDisplayLayer();
	
	public void start() {
		layerManager.addLayers(newColorChooserLayer(),
							   paletteMenu,
							   newColorDeleteButtonLayer(),
							   newFileMenu(),
							   newUserDeviceDisplayLayer());
		
		Application editorProgram = new Application();
		editorProgram.setMain(EditorProgramMain.create(layerManager, mouseUserDevice));
		editorProgram.start();
	}
	
}