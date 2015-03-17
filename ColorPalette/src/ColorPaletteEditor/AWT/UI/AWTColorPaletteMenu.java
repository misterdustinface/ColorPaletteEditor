package ColorPaletteEditor.AWT.UI;

import generic.ColorData;
import generic.VoidFunctionPointer;
import generic.ListenerPattern.Descriptive.DataModificationListener;

import java.awt.Color;
import java.awt.Graphics2D;

import AWT.UI.AWTMenuButton;
import AWT.UI.AWTUILayer;
import AWT.rendering.AWTMenuDrawer;
import ColorPaletteEditor.UI.ColorPaletteMenu;
import UI.Grid;
import UI.MenuButton;

public class AWTColorPaletteMenu extends ColorPaletteMenu implements AWTUILayer {
	
	protected AWTMenuDrawer menuDrawer;
	
	public AWTColorPaletteMenu(AWTColorChooserMenu COLOR_CHOOSER, Grid DISPLAYGRID) {
		super(COLOR_CHOOSER, DISPLAYGRID);
		menuDrawer = AWTMenuDrawer.getMenuDrawer();
	}
	
	@Override
	public void render(Graphics2D g) {
		menuDrawer.setGraphics(g);
		menuDrawer.drawUIMenu(this);

		if (canFitNewEmptyEntry()) {
			menuDrawer.drawPlusOnButton(getEmptyEntry());
		}
	}
	
	@Override
	protected MenuButton newButton(int index) {
		AWTMenuButton colorPaletteButton = new ColorPaletteButton(paletteColors.get(index));
		colorPaletteButton.fill();
		return colorPaletteButton;
	}
	
	@Override
	protected MenuButton newEmptyButton() {
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
	
	class ColorPaletteButton extends AWTMenuButton {

		private final ColorData localColordata;
		
		private final Color PRESSED_COLOR   = new Color(	pressedColor.getRed(),
															pressedColor.getGreen(),
															pressedColor.getBlue(),
															64);
		private final Color HIGHLIGHT_COLOR = new Color(	highlightColor.getRed(),
															highlightColor.getGreen(),
															highlightColor.getBlue(),
															64);
		
		public ColorPaletteButton(ColorData COLOR_DATA) {
			localColordata = COLOR_DATA;
			pressedColor = PRESSED_COLOR;
			highlightColor = HIGHLIGHT_COLOR;
			updateButtonColor();
			
			colorChooser.addDataModificationListener(new DataModificationListener() {
				@Override
				protected void whenMyDataIsModifiedExternally() {
					updateButtonColor();
				}
			});
		}
		
		private void updateButtonColor() {
			setColor(pressedColor,
					 new Color(localColordata.r, localColordata.g, localColordata.b, localColordata.a),
					 highlightColor);
		}
		
		@Override
		protected void pressAction() { 
			colorChooser.setColorData(localColordata); 
		}	
	}
	
}