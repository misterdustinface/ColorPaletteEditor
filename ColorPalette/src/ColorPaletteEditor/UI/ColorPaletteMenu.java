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
	private MenuButton selectedButton;
	private SelectiveNotifier<MenuButton> buttonColorUpdateNotifier;
	
	public ColorPaletteMenu(ColorChooserMenu COLOR_CHOOSER, Grid DISPLAYGRID) {
		super(DISPLAYGRID);
		colorChooser = COLOR_CHOOSER;
		colorChooser.addDataModificationListener(isColorChooserSliderChangedListener);
		buttonColorUpdateNotifier = new SelectiveNotifier<MenuButton>();
		paletteColors  = null;
		selectedButton = null;
	}

	public void setPalette(ArrayList<ColorData> palette) {
		paletteColors = palette;
		clearButtons();
		addNewButtons(paletteColors.size());
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
					int paletteColorIndexToRemove = buttons.indexOf(selectedButton);
					removeButton(selectedButton);
					paletteColors.remove(paletteColorIndexToRemove);
				}
			}
		};
	}
	
	protected void addShouldUpdateButtonColorListener(MenuButton thisButton, Listener listener) {
		buttonColorUpdateNotifier.addListener(thisButton, listener);
	}
	
	protected ColorData getColorChooserColor() {
		return colorChooser.getColor();
	}
	
	@Override
	final protected MenuButton newButton(int buttonIndex) {
		ColorData colorData = paletteColors.get(buttonIndex);
		MenuButton colorPaletteButton = newColorPaletteButton(colorData);
		colorPaletteButton.setButtonPressedFunction(colorPaletteButtonPressedFunction(colorPaletteButton));
		return colorPaletteButton;
	}
	
	@Override
	final protected MenuButton newEmptyButton() {
		ColorData colorData = new ColorData(0.75f,0.75f,0.75f,1.0f);
		MenuButton colorPaletteButton = newColorPaletteButton(colorData);
		colorPaletteButton.setButtonPressedFunction(newEmptyButtonPressedFunction(colorPaletteButton));
		return colorPaletteButton;
	}
	
	abstract protected MenuButton newColorPaletteButton(ColorData colorData);
	
	private VoidFunctionPointer colorPaletteButtonPressedFunction(final MenuButton thisButton) {
		return new VoidFunctionPointer() {
			private final MenuButton THIS_BUTTON = thisButton;
			@Override
			public void call() {
				selectedButton = null;
				ColorData current = paletteColors.get(buttons.indexOf(THIS_BUTTON));
				colorChooser.setColor(current);
				selectedButton = THIS_BUTTON;
			}
		};
	}
	
	private VoidFunctionPointer newEmptyButtonPressedFunction(final MenuButton thisButton) {
		return new VoidFunctionPointer() {
			private final MenuButton THIS_BUTTON = thisButton;
			@Override
			public void call() {
				selectedButton = null;
				ColorData currentColorChooserColor = getColorChooserColor();
				paletteColors.add(currentColorChooserColor);
				buttonColorUpdateNotifier.notifyListener(thisButton);
				thisButton.setButtonPressedFunction(colorPaletteButtonPressedFunction(thisButton));
				selectedButton = THIS_BUTTON;
			}
		};
	}
	
	private DataModificationListener isColorChooserSliderChangedListener = new DataModificationListener() {
		@Override
		protected void whenMyDataIsModifiedExternally() {
			if (selectedButton != null) {
				buttonColorUpdateNotifier.notifyListener(selectedButton);
			}
		}
	};

}
