package ColorPaletteEditor.UI;

import generic.ColorData;
import generic.VoidFunctionPointer;
import generic.ListenerPattern.Listener;
import generic.ListenerPattern.Descriptive.DataModificationListener;
import generic.ListenerPattern.Descriptive.SelectiveNotifier;

import java.util.ArrayList;

import UI.DynamicGridMenu;
import UI.Grid;
import UI.MenuButton;

public abstract class ColorPaletteMenu extends DynamicGridMenu {

	private ArrayList<ColorData> paletteColors;
	private ColorChooserMenu colorChooser;
	private int selectedButtonIndex;
	private SelectiveNotifier buttonColorUpdateNotifier;
	
	public ColorPaletteMenu(ColorChooserMenu COLOR_CHOOSER, Grid DISPLAYGRID) {
		super(DISPLAYGRID);
		colorChooser = COLOR_CHOOSER;
		colorChooser.addDataModificationListener(isColorChooserSliderChangedListener);
		buttonColorUpdateNotifier = new SelectiveNotifier();
		selectedButtonIndex = 0;
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
					paletteColors.remove(selectedButtonIndex);
				}
			}
		};
	}
	
	private DataModificationListener isColorChooserSliderChangedListener = new DataModificationListener() {
		@Override
		protected void whenMyDataIsModifiedExternally() {
			//buttonColorUpdateNotifier.notifyListeners();
			buttonColorUpdateNotifier.notifyListener(selectedButtonIndex);
		}
	};
	
	protected void addShouldUpdateButtonColorListener(Listener listener) {
		buttonColorUpdateNotifier.addListener(listener);
	}
	
	protected ColorData getColorChooserColor() {
		return colorChooser.getColor();
	}
	
	@Override
	final protected MenuButton newButton(int buttonIndex) {
		ColorData colorData = paletteColors.get(buttonIndex);
		MenuButton colorPaletteButton = newColorPaletteButton(colorData);
		colorPaletteButton.setButtonPressedFunction(colorPaletteButtonPressedFunction(colorData, buttonIndex));
		return colorPaletteButton;
	}
	
	@Override
	final protected MenuButton newEmptyButton() {
		ColorData colorData = new ColorData(200,200,200,255);
		MenuButton colorPaletteButton = newColorPaletteButton(colorData);
		colorPaletteButton.setButtonPressedFunction(newEmptyButtonPressedFunction(colorPaletteButton));
		return colorPaletteButton;
	}
	
	abstract protected MenuButton newColorPaletteButton(ColorData colorData);
	
	private VoidFunctionPointer colorPaletteButtonPressedFunction(final ColorData colorData, final int buttonIndex) {
		return new VoidFunctionPointer() {
			private final ColorData COLOR_DATA = colorData;
			private final int BUTTON_INDEX = buttonIndex;
			@Override
			public void call() {
				colorChooser.setColor(COLOR_DATA);
				selectedButtonIndex = BUTTON_INDEX;
			}
		};
	}
	
	private VoidFunctionPointer newEmptyButtonPressedFunction(final MenuButton thisButton) {
		return new VoidFunctionPointer() {
			@Override
			public void call() {
				ColorData currentColorChooserColor = getColorChooserColor();
				paletteColors.add(currentColorChooserColor);
				int thisButtonIndex = paletteColors.size() - 1;
				buttonColorUpdateNotifier.notifyListener(thisButtonIndex);
				thisButton.setButtonPressedFunction(colorPaletteButtonPressedFunction(currentColorChooserColor, thisButtonIndex));
			}
		};
	}

}
