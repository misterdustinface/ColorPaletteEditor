package ColorPaletteEditor.AWT.UI;

import java.awt.Color;
import java.awt.Graphics2D;

import shapes.Rectangle;
import AWT.UI.AWTBarSlider;
import AWT.UI.AWTMenuButton;
import AWT.UI.AWTUILayer;
import AWT.graphicdata.EditorAWTGraphicData;
import AWT.rendering.AWTMenuDrawer;
import ColorPaletteEditor.UI.ColorChooserMenu;
import UI.BarSlider;
import UI.MenuButton;

public class AWTColorChooserMenu extends ColorChooserMenu implements AWTUILayer {
	
	final private static EditorAWTGraphicData graphicData = EditorAWTGraphicData.getGraphicData();
	final private AWTMenuDrawer menuDrawer;
	private Color awtColor;
	
	public AWTColorChooserMenu(Rectangle DISPLAYBOX) {
		super(DISPLAYBOX);
		
		menuDrawer = AWTMenuDrawer.getMenuDrawer();
		
		((AWTBarSlider)hueSliders[0]).setColor(Color.RED.darker(), Color.RED, Color.RED.darker());
		((AWTBarSlider)hueSliders[1]).setColor(Color.GREEN.darker(), Color.GREEN, Color.GREEN.darker());
		((AWTBarSlider)hueSliders[2]).setColor(Color.BLUE.darker(), Color.BLUE, Color.BLUE.darker());
		((AWTBarSlider)hueSliders[3]).setColor(graphicData.getColorOf("buttonPressed"), graphicData.getColorOf("button"), graphicData.getColorOf("buttonHighlight"));
	}
	
	@Override
	protected AWTBarSlider newBarSliderSubclass() {
		AWTBarSlider slider = new AWTBarSlider();
		slider.setBaseColor(graphicData.getColorOf("lightclear"));
		return slider;
	}
	
	@Override
	public void render(Graphics2D g) {
		menuDrawer.setGraphics(g);
		//menuDrawer.drawMenu(position, width, height);
		for (MenuButton button : buttons)
			menuDrawer.drawButton((AWTMenuButton)button);
		for (BarSlider slider : hueSliders)
			menuDrawer.drawSlider((AWTBarSlider)slider);

		renderChooserColor(g);
	}

	private void renderChooserColor(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillOval((int)(displayBox.x + displayBox.width - elementSize - elementOffset), (int)(displayBox.y + elementSize/4 + elementOffset), elementSize, elementSize);
		g.setColor(awtColor);
		g.fillRect((int)(displayBox.x + displayBox.width - elementSize - elementOffset), (int)(displayBox.y + elementOffset), elementSize, (int)displayBox.height);
	}

	@Override
	protected void colorDataUpdated() {
		awtColor = new Color(color.r, color.g, color.b, color.a);
	}
	
}
