package ColorPaletteEditor.UI;

import generic.ListenerPattern.Listener;
import generic.ListenerPattern.Descriptive.DataModificationListener;
import generic.ListenerPattern.Descriptive.SelectiveNotifier;
import generic.fp.VoidFunctionPointer;
import generic.structures.ColorData;
import generic.structures.Grid;

import java.util.ArrayList;

import UI.widgets.DynamicGridMenu;
import UI.widgets.MenuButton;

public abstract class ColorPaletteMenu extends DynamicGridMenu {

	private ArrayList<ColorData> colorPalette;
	private ColorModifier externalColorModifier;
	private volatile boolean ignoreExternalColorModifierChangeBecauseThisMenuCreatedTheChange;
	private SelectiveNotifier<MenuButton> shouldUpdateButtonColorNotifier;
	private MenuButton selectedButton;
	
	public ColorPaletteMenu(Grid DISPLAYGRID) {
		super(DISPLAYGRID);
		shouldUpdateButtonColorNotifier = new SelectiveNotifier<MenuButton>();
		ignoreExternalColorModifierChangeBecauseThisMenuCreatedTheChange = false;
		externalColorModifier = null;
		colorPalette = null;
		selectedButton = null;
	}
	
	public void setColorModifier(final ColorModifier COLOR_MODIFIER) {
		externalColorModifier = COLOR_MODIFIER;
		externalColorModifier.addDataModificationListener(onExternalColorModifierChange);
	}

	public void setPalette(final ArrayList<ColorData> palette) {
		colorPalette = palette;
		clearButtons();
		addNewButtons(colorPalette.size());
	}
	
	public DataModificationListener getDataModificationListener() {
		return new DataModificationListener() {
			@Override
			protected void whenMyDataIsModifiedExternally() {
				refreshButtons(colorPalette.size());
			}
		};
	}
	
	public VoidFunctionPointer getColorDeleteFunction() {
		return new VoidFunctionPointer() {
			@Override
			public void call() {
				synchronized (this) {
					if (selectedButton != null) {
						removePaletteColor(selectedButton);
						removeButton(selectedButton);
						selectedButton = null;
					}
				}
			}
		};
	}
	
	protected void addShouldUpdateButtonColorListener(MenuButton thisButton, Listener listener) {
		shouldUpdateButtonColorNotifier.addListener(thisButton, listener);
	}
	
	protected ColorData getColorChooserColor() {
		return externalColorModifier.getColor();
	}
	
	@Override
	final protected MenuButton newButton(int buttonIndex) {
		ColorData colorData = colorPalette.get(buttonIndex);
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
	
	private void removePaletteColor(MenuButton paletteButton) {
		colorPalette.remove(buttons.indexOf(paletteButton));
	}
	
	private ColorData getPaletteColor(MenuButton paletteButton) {
		return colorPalette.get(buttons.indexOf(paletteButton));
	}
	
	private VoidFunctionPointer colorPaletteButtonPressedFunction(final MenuButton thisButton) {
		return new VoidFunctionPointer() {
			private final MenuButton THIS_BUTTON = thisButton;
			@Override
			public void call() {
				selectedButton = THIS_BUTTON;
				ignoreExternalColorModifierChangeBecauseThisMenuCreatedTheChange = true;
				externalColorModifier.setColor(getPaletteColor(selectedButton));
				ignoreExternalColorModifierChangeBecauseThisMenuCreatedTheChange = false;
			}
		};
	}

	private VoidFunctionPointer newEmptyButtonPressedFunction(final MenuButton thisButton) {
		return new VoidFunctionPointer() {
			private final MenuButton THIS_BUTTON = thisButton;
			@Override
			public void call() {
				selectedButton = THIS_BUTTON;
				colorPalette.add(getColorChooserColor());
				shouldUpdateButtonColorNotifier.notifyListener(thisButton);
				thisButton.setButtonPressedFunction(colorPaletteButtonPressedFunction(thisButton));
			}
		};
	}
	
	final private DataModificationListener onExternalColorModifierChange = new DataModificationListener() {
		@Override
		protected void whenMyDataIsModifiedExternally() {
			if (!ignoreExternalColorModifierChangeBecauseThisMenuCreatedTheChange && selectedButton != null) {
				shouldUpdateButtonColorNotifier.notifyListener(selectedButton);
			}
		}
	};

}
