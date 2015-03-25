package ColorPaletteEditor.AWT.UI;

import java.awt.Color;
import java.awt.Graphics2D;

import shapes.Circle;
import shapes.Rectangle;
import AWT.UI.AWTBarSlider;
import AWT.UI.AWTUILayer;
import AWT.graphicdata.EditorAWTGraphicData;
import AWT.rendering.AWTMenuDrawer;
import AWT.rendering.AWTShapeDrawer;
import ColorPaletteEditor.UI.BarSliderColorModifier;
import UI.widgets.BarSlider;

public class AWTBarSliderColorModifier extends BarSliderColorModifier implements AWTUILayer {
	
	final private static EditorAWTGraphicData graphicData = EditorAWTGraphicData.getGraphicData();
	final private AWTMenuDrawer menuDrawer;
	final private AWTShapeDrawer shapeDrawer;
	private Color awtColor;
	private Rectangle resultColorDisplay;
	private Circle referenceWhiteCircle;
	
	public AWTBarSliderColorModifier(Rectangle DISPLAYBOX) {
		super(DISPLAYBOX);
		
		menuDrawer = AWTMenuDrawer.getMenuDrawer();
		shapeDrawer = AWTShapeDrawer.getShapeDrawer();
		
		awtColor = new Color(color.r, color.g, color.b, color.a);
		
		((AWTBarSlider)hueSliders[0]).setColor(Color.RED.darker(), Color.RED, Color.RED.darker());
		((AWTBarSlider)hueSliders[1]).setColor(Color.GREEN.darker(), Color.GREEN, Color.GREEN.darker());
		((AWTBarSlider)hueSliders[2]).setColor(Color.BLUE.darker(), Color.BLUE, Color.BLUE.darker());
		((AWTBarSlider)hueSliders[3]).setColor(graphicData.getColorOf("buttonPressed"), graphicData.getColorOf("button"), graphicData.getColorOf("buttonHighlight"));
	
		int xOff = elementOffset * 5 + barSliderWidth * 4;
		int yOff = elementOffset;
		float xPos = displayBox.x + xOff;
		float yPos = displayBox.y + yOff;
		resultColorDisplay = new Rectangle(xPos, yPos, displayBox.width - xOff - elementOffset, displayBox.height - yOff - elementOffset);
		float smallerRadius = (resultColorDisplay.width/4 < resultColorDisplay.height/4) ? resultColorDisplay.width/4 : resultColorDisplay.height/4;
		referenceWhiteCircle = new Circle(resultColorDisplay.getCenterX(), resultColorDisplay.getCenterY(), smallerRadius);
	}

	protected AWTBarSlider newBarSliderSubclass() {
		AWTBarSlider slider = new AWTBarSlider();
		slider.setBaseColor(graphicData.getColorOf("lightclear"));
		return slider;
	}

	public void render(Graphics2D g) {
		menuDrawer.setGraphics(g);
		menuDrawer.drawMenuBox((int)displayBox.x, (int)displayBox.y, (int)displayBox.width, (int)displayBox.height);
		for (BarSlider slider : hueSliders)
			menuDrawer.drawSlider((AWTBarSlider)slider);

		renderChooserColor(g);
	}

	private void renderChooserColor(Graphics2D g) {
		shapeDrawer.setColor(Color.WHITE);
		shapeDrawer.drawFilledCircle(referenceWhiteCircle);
		shapeDrawer.setColor(awtColor);
		shapeDrawer.drawFilledRectangle(resultColorDisplay);
	}

	protected void onColorChangedEvent() {
		awtColor = new Color(color.r, color.g, color.b, color.a);
	}
	
}