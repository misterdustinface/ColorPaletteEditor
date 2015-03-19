package ColorPaletteEditor.AWT.UI;

import generic.ColorData;
import generic.ListenerPattern.Listener;

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
	
	protected MenuButton newColorPaletteButton(ColorData colorData) {
		ColorPaletteButton colorPaletteButton = new ColorPaletteButton(colorData);
		colorPaletteButton.fill();
		return colorPaletteButton;
	}
	
	class ColorPaletteButton extends AWTMenuButton {

		private ColorData localColordata;
		
		private final Color PRESSED_COLOR   = new Color(	pressedColor.getRed(),
															pressedColor.getGreen(),
															pressedColor.getBlue(),
															64);
		private final Color HIGHLIGHT_COLOR = new Color(	highlightColor.getRed(),
															highlightColor.getGreen(),
															highlightColor.getBlue(),
															64);
		
		public ColorPaletteButton(ColorData INITIAL_COLOR) {
			localColordata = INITIAL_COLOR;
			pressedColor   = PRESSED_COLOR;
			highlightColor = HIGHLIGHT_COLOR;
			updateButtonColor();
			addShouldUpdateButtonColorListener(new Listener() {
				@Override
				protected void whenNotified() {
					updateButtonColor();
				}
			});
		}
		
		private void updateButtonColor() {
			localColordata = getColorChooserColor();
			setColor(pressedColor,
					 new Color(localColordata.r, localColordata.g, localColordata.b, localColordata.a),
					 highlightColor);
		}
		
	}
	
}