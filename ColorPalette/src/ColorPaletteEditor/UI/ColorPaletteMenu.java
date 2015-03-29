package ColorPaletteEditor.UI;

import generic.ListenerPattern.Listener;
import generic.ListenerPattern.Descriptive.DataModificationListener;
import generic.ListenerPattern.Descriptive.SelectiveNotifier;
import generic.fp.VoidFunctionPointer;
import generic.structures.ColorData;
import generic.structures.Grid;
import ColorPaletteEditor.data.ColorPalette;
import UI.widgets.DynamicGridMenu;
import UI.widgets.MenuButton;

public abstract class ColorPaletteMenu extends DynamicGridMenu {

	private ColorPalette colorPalette;
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

	public void setPalette(final ColorPalette palette) {
		colorPalette = palette;
		clearButtons();
		addNewButtons(colorPalette.getSize());
	}
	
	public DataModificationListener getDataModificationListener() {
		return new DataModificationListener() {
			protected void whenMyDataIsModifiedExternally() {
				refreshButtons(colorPalette.getSize());
			}
		};
	}
	
	public VoidFunctionPointer getColorDeleteFunction() {
		return new VoidFunctionPointer() {
			public synchronized void call() {
				if (selectedButton != null) {
					removePaletteColor(selectedButton);
					removeButton(selectedButton);
					selectedButton = null;
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
	
	final protected MenuButton newButton(int buttonIndex) {
		ColorData colorData = colorPalette.get(buttonIndex);
		MenuButton colorPaletteButton = newColorPaletteButton(colorData);
		colorPaletteButton.setButtonPressedFunction(colorPaletteButtonPressedFunction(colorPaletteButton));
		//colorPaletteButton.setText(""+buttonIndex);
		return colorPaletteButton;
	}
	
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
			private MenuButton THIS_BUTTON = thisButton;
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
			private MenuButton THIS_BUTTON = thisButton;
			public void call() {
				selectedButton = THIS_BUTTON;
				colorPalette.add(getColorChooserColor());
				shouldUpdateButtonColorNotifier.notifyListener(thisButton);
				thisButton.setButtonPressedFunction(colorPaletteButtonPressedFunction(thisButton));
				//thisButton.setText(""+(buttons.size()-1));
			}
		};
	}
	
	final private DataModificationListener onExternalColorModifierChange = new DataModificationListener() {
		protected void whenMyDataIsModifiedExternally() {
			if (!ignoreExternalColorModifierChangeBecauseThisMenuCreatedTheChange && selectedButton != null) {
				shouldUpdateButtonColorNotifier.notifyListener(selectedButton);
			}
		}
	};

}