package ColorPaletteEditor.AWT.UI;

import generic.ColorData;
import generic.ListenerPattern.Listener;
import generic.ListenerPattern.Descriptive.DataModificationNotifier;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import shapes.Rectangle;
import AWT.UI.AWTBarSlider;
import AWT.UI.AWTMenuButton;
import AWT.UI.AWTUILayer;
import AWT.graphicdata.AWTGraphicData;
import AWT.rendering.AWTMenuDrawer;
import UI.MouseUserDevice;

public class AWTColorChooserMenu extends DataModificationNotifier implements AWTUILayer {
	
	final private static int R = 0;
	final private static int G = 1;
	final private static int B = 2;
	final private static int A = 3;
	final private AWTHueBarSlider[] hueSliders = { new AWTHueBarSlider(), new AWTHueBarSlider(), new AWTHueBarSlider(), new AWTHueBarSlider() };
	final private ArrayList<AWTMenuButton> buttons;
	final private AWTMenuDrawer menuDrawer;
	final private AWTGraphicData graphicData;
	final private Listener hueSliderChangeListener = new Listener() {
		@Override
		protected void whenNotified() {
			formColorWithCombinedHues();
			notifyDataModified();
		}
	};
	
	private Rectangle displayBox;
	private static int elementSize      = 64;
	private static int elementShortsize = 21;
	private static int elementOffset    = 4;
	
	private Color     awtColor;
	private ColorData color;
	
	public AWTColorChooserMenu(Rectangle DISPLAYBOX) {
		displayBox = DISPLAYBOX;
				
		graphicData = AWTGraphicData.getGraphicData();
		
		color    = new ColorData(128,128,128,255);
		awtColor = new Color(color.r, color.g, color.b, color.a);
		
		hueSliders[R].setBase(new Rectangle(displayBox.x + elementOffset, displayBox.y +elementOffset , elementShortsize, displayBox.height));
		hueSliders[R].setBaseColor(graphicData.lightclear);
		hueSliders[R].setColor(Color.RED.darker(), Color.RED, Color.RED.darker());
		hueSliders[R].addChangeListener(hueSliderChangeListener);
		
		hueSliders[G].setBase(new Rectangle(displayBox.x + 2*elementOffset + elementShortsize, displayBox.y +elementOffset , elementShortsize, displayBox.height));
		hueSliders[G].setBaseColor(graphicData.lightclear);
		hueSliders[G].setColor(Color.GREEN.darker(), Color.GREEN, Color.GREEN.darker());
		hueSliders[G].addChangeListener(hueSliderChangeListener);
		
		hueSliders[B].setBase(new Rectangle(displayBox.x + 3*elementOffset + 2*elementShortsize, displayBox.y +elementOffset, elementShortsize, displayBox.height));
		hueSliders[B].setBaseColor(graphicData.lightclear);
		hueSliders[B].setColor(Color.BLUE.darker(), Color.BLUE, Color.BLUE.darker());
		hueSliders[B].addChangeListener(hueSliderChangeListener);
		
		hueSliders[A].setBase(new Rectangle(displayBox.x + 4*elementOffset + 3*elementShortsize, displayBox.y +elementOffset, elementShortsize, displayBox.height));
		hueSliders[A].setBaseColor(graphicData.lightclear);
		hueSliders[A].setColor(graphicData.buttonPressedColor, graphicData.buttonColor, graphicData.buttonHighlightColor);
		hueSliders[A].addChangeListener(hueSliderChangeListener);
		
		setSlidersToMatchColorData();
		
		menuDrawer = AWTMenuDrawer.getMenuDrawer();
		buttons = new ArrayList<AWTMenuButton>();
	}
	
	public ColorData getColorData() { 
		return color; 
	}
	
	public void setColorData(ColorData COLOR_DATA) {
		color    = COLOR_DATA;
		awtColor = new Color(color.r, color.g, color.b, color.a);
		setSlidersToMatchColorData();
	}	
	
	private void setSlidersToMatchColorData() {
		hueSliders[R].setHue(color.r);
		hueSliders[G].setHue(color.g);
		hueSliders[B].setHue(color.b);
		hueSliders[A].setHue(color.a);
	}
	
	private void formColorWithCombinedHues() {
		setColorDataToMatchSliders();
		awtColor = new Color(color.r, color.g, color.b, color.a);
	}
	
	private void setColorDataToMatchSliders() {
		color.r = hueSliders[R].getHue();
		color.g = hueSliders[G].getHue();
		color.b = hueSliders[B].getHue();
		color.a = hueSliders[A].getHue();
	}
	
	@Override
	public void render(Graphics2D g) {
		menuDrawer.setGraphics(g);
		//menuDrawer.drawMenu(position, width, height);
		for (AWTMenuButton button : buttons)
			menuDrawer.drawButton(button);
		for (AWTBarSlider slider : hueSliders)
			menuDrawer.drawSlider(slider);

		g.setColor(Color.WHITE);
		g.fillOval((int)(displayBox.x + displayBox.width - elementSize - elementOffset), (int)(displayBox.y + elementSize/4 + elementOffset), elementSize, elementSize);
		g.setColor(awtColor);
		g.fillRect((int)(displayBox.x + displayBox.width - elementSize - elementOffset), (int)(displayBox.y + elementOffset), elementSize, (int)displayBox.height);
	}

	@Override
	public void update(MouseUserDevice mouse) {
		for (AWTMenuButton button : buttons)
			button.update(mouse);
		for (AWTBarSlider slider : hueSliders)
			slider.update(mouse);
	}
	
	public void addButton(AWTMenuButton button) {
		buttons.add(button);
	}
}
