package ColorPaletteEditor.AWT.UI;

import generic.ColorData;
import generic.VoidFunctionPointer;
import generic.ListenerPattern.Descriptive.DataModificationListener;

import java.awt.Color;
import java.util.ArrayList;

import AWT.UI.AWTDynamicGridMenu;
import AWT.UI.AWTMenuButton;
import UI.Grid;
import UI.MouseUserDevice;

public class AWTColorPaletteMenu extends AWTDynamicGridMenu {

	private ArrayList<ColorData> paletteColors;
	private AWTColorChooserMenu  colorChooser;
	private boolean shouldRemoveColor;
	private int toRemove;
	
	public AWTColorPaletteMenu(AWTColorChooserMenu COLOR_CHOOSER, Grid DISPLAYGRID) {
		super(DISPLAYGRID);
		colorChooser = COLOR_CHOOSER;
		setPalette(new ArrayList<ColorData>());
	}
	
	public void setPalette(ArrayList<ColorData> colorData) {
		paletteColors = colorData;
		refreshButtons();
	}
	
	private void removeColorAtIndex(int i) {
		if (paletteColors.size() > 0) {
			paletteColors.remove(i);
			refreshButtons();
		}
	}
	
	public DataModificationListener getDataModificationListener() {
		return new DataModificationListener() {
			@Override
			protected void whenMyDataIsModifiedExternally() {
				refreshButtons(paletteColors.size());
			}
		};
	}
	
	@Override
	protected AWTMenuButton newButton(int index) {
		AWTMenuButton colorPaletteButton = new ColorPaletteButton(paletteColors.get(index));
		colorPaletteButton.fill();
		return colorPaletteButton;
	}
	
	@Override
	protected AWTMenuButton newEmptyButton() {
		final ColorData colorData = new ColorData(200,200,200,255);
		AWTMenuButton colorPaletteButton = new ColorPaletteButton(colorData);
		colorPaletteButton.setButtonPressedFunction(new VoidFunctionPointer() {
			@Override
			public void call() {
				paletteColors.add(colorData);
			}
		});
		colorPaletteButton.fill();
		return colorPaletteButton;
	}	
	
	public VoidFunctionPointer getColorDeleteFunction() {
		return new VoidFunctionPointer() {
			@Override
			public void call() {
				requestColorDeletion(colorChooser.getColorData());
			}
		};
	}
	
	private void requestColorDeletion(ColorData COLOR_DATA) { 
		setColorToRemove(COLOR_DATA); 
	}
	
	private void setColorToRemove(ColorData cd) { 
		toRemove = paletteColors.indexOf(cd); 
		shouldRemoveColor = toRemove != -1;
	}
	
	private void removalComplete() { 
		shouldRemoveColor = false;
	}
	
	private void removeColorIfRequested() {
		if (shouldRemoveColor) {
			removeColorAtIndex(toRemove);
			removalComplete();
		}
	}
	
	public void update(MouseUserDevice mouse) {
		super.update(mouse);
		removeColorIfRequested();
	}
	
	class ColorPaletteButton extends AWTMenuButton {

		private ColorData colordata;
		
		private final Color PRESSED_COLOR   = new Color(	pressedColor.getRed(),
															pressedColor.getGreen(),
															pressedColor.getBlue(),
															64);
		private final Color HIGHLIGHT_COLOR = new Color(	highlightColor.getRed(),
															highlightColor.getGreen(),
															highlightColor.getBlue(),
															64);
//		private boolean isDeleteButtonPressed = false;
		
		public ColorPaletteButton(ColorData COLOR_DATA) {
			colordata = COLOR_DATA;
			pressedColor = PRESSED_COLOR;
			highlightColor = HIGHLIGHT_COLOR;
			updateColor();
			
			colorChooser.addDataModificationListener(new DataModificationListener() {
				@Override
				protected void whenMyDataIsModifiedExternally() {
					updateColor();
				}
			});
		}
		
		private void updateColor() {
			setColor(pressedColor,
					 new Color(colordata.r, colordata.g, colordata.b, colordata.a),
					 highlightColor);
		}
		
		@Override
		protected void pressAction() { 
//			if(isDeleteButtonPressed) { 
//				requestColorDeletion(colordata);
//				isDeleteButtonPressed = false;
//			} else {
				colorChooser.setColorData(colordata); 
//			}
		}
		
		@Override
		protected void releaseAction() {
			
		}
		
		public void update(MouseUserDevice mouse) {
//			isDeleteButtonPressed = mouse.isTerciaryButton() && mouse.isClicked();
			super.update(mouse);
		}
		
	}
}