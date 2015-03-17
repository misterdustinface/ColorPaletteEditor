package ColorPaletteEditor.UI;

import generic.ColorData;
import generic.VoidFunctionPointer;
import generic.ListenerPattern.Descriptive.DataModificationListener;

import java.util.ArrayList;

import UI.DynamicGridMenu;
import UI.Grid;

public abstract class ColorPaletteMenu extends DynamicGridMenu {

	protected ArrayList<ColorData> paletteColors;
	protected ColorChooserMenu colorChooser;
	
	public ColorPaletteMenu(ColorChooserMenu COLOR_CHOOSER, Grid DISPLAYGRID) {
		super(DISPLAYGRID);
		colorChooser = COLOR_CHOOSER;
		
	}

	public void setPalette(ArrayList<ColorData> colorData) {
		paletteColors = colorData;
		refreshButtons();
	}
	
	public DataModificationListener getDataModificationListener() {
		return new DataModificationListener() {
			@Override
			protected void whenMyDataIsModifiedExternally() {
				refreshButtons(paletteColors.size());
			}
		};
	}
	
	public VoidFunctionPointer getColorDeleteFunction() {
		return new VoidFunctionPointer() {
			@Override
			public void call() {
				synchronized (this) {
					paletteColors.remove(colorChooser.getColorData());
				}
			}
		};
	}

}
