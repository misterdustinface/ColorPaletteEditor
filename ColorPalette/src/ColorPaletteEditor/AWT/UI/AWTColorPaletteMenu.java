package ColorPaletteEditor.AWT.UI;

import generic.ListenerPattern.Listener;
import generic.structures.ColorData;
import generic.structures.Grid;

import java.awt.Color;
import java.awt.Graphics2D;

import AWT.UI.AWTMenuButton;
import AWT.UI.AWTUILayer;
import AWT.rendering.AWTMenuDrawer;
import ColorPaletteEditor.UI.ColorPaletteMenu;
import UI.widgets.MenuButton;

public class AWTColorPaletteMenu extends ColorPaletteMenu implements AWTUILayer {
	
	protected AWTMenuDrawer menuDrawer;
	
	public AWTColorPaletteMenu(Grid DISPLAYGRID) {
		super(DISPLAYGRID);
		menuDrawer = AWTMenuDrawer.getMenuDrawer();
	}
	
	public void render(Graphics2D g) {
		menuDrawer.setGraphics(g);
		menuDrawer.drawUIMenu(this);

		if (canFitNewEmptyEntry()) {
			menuDrawer.drawPlusOnButton(getEmptyEntry());
		}
	}
	
	protected MenuButton newColorPaletteButton(ColorData colorData) {
		AWTColorPaletteButton colorPaletteButton = new AWTColorPaletteButton(colorData);
		colorPaletteButton.fill();
		return colorPaletteButton;
	}
	
	class AWTColorPaletteButton extends AWTMenuButton {

		private final Color PRESSED_COLOR   = new Color( pressedColor.getRed(), pressedColor.getGreen(), pressedColor.getBlue(), 64);
		private final Color HIGHLIGHT_COLOR = new Color( highlightColor.getRed(), highlightColor.getGreen(), highlightColor.getBlue(), 64);
		private ColorData localColordata;
		
		public AWTColorPaletteButton(ColorData INITIAL_COLOR) {
			localColordata = INITIAL_COLOR;
			pressedColor   = PRESSED_COLOR;
			highlightColor = HIGHLIGHT_COLOR;
			becomeMyDesignatedColor();
			addShouldUpdateButtonColorListener(this, new Listener() {
				protected void whenNotified() {
					updateButtonColor();
				}
			});
		}
		
		private void updateButtonColor() {
			localColordata = getColorChooserColor();
			becomeMyDesignatedColor();
		}
		
		private synchronized void becomeMyDesignatedColor() {
			setColor(pressedColor,
					 new Color(localColordata.r, localColordata.g, localColordata.b, localColordata.a),
					 highlightColor);
		}
		
	}
	
}