package ColorPaletteEditor.UI;

import listenerpattern.Listener;
import shapes.Rectangle;
import structures.ColorData;
import UI.UILayer;
import UI.input.MouseUserDevice;
import UI.widgets.BarSlider;

public abstract class BarSliderColorModifier extends ColorModifier implements UILayer {
	
	final protected BarSlider[] hueSliders;
	final protected Rectangle displayBox;
	
	protected static int barSliderWidth = 21;
	protected static int elementOffset= 4;
	
	private volatile boolean ignoreHueSliderNotifications = false;
	
	public BarSliderColorModifier(Rectangle DISPLAYBOX) {
		super();
		displayBox = DISPLAYBOX;
		hueSliders = new BarSlider[4];
		constructHueSliders();		
		setSlidersToMatchColorData();
	}
	
	public void update(MouseUserDevice mouse) {
		for (BarSlider slider : hueSliders) {
			slider.update(mouse);
		}
	}
	
	public void setColor(final ColorData source) {
		super.setColor(source);
		setSlidersToMatchColorData();
		onColorChangedEvent();
	}
	
	abstract protected void onColorChangedEvent();
	
	abstract protected BarSlider newBarSliderSubclass();
	
	private void setSlidersToMatchColorData() {
		ignoreHueSliderNotifications = true;
		hueSliders[0].setFillPercent(color.r);
		hueSliders[1].setFillPercent(color.g);
		hueSliders[2].setFillPercent(color.b);
		hueSliders[3].setFillPercent(color.a);
		ignoreHueSliderNotifications = false;
	}
	
	private void constructHueSliders() {
		for (int i = 0; i < 4; ++i) {
			hueSliders[i] = newBarSliderSubclass();
			hueSliders[i].addChangeListener(newHueSliderChangeListener());
		}
		for (int i = 0; i < 4; ++i) {
			hueSliders[i].setBase(new Rectangle(displayBox.x + (1+i)*elementOffset + (i)*barSliderWidth, displayBox.y +elementOffset , barSliderWidth, displayBox.height - 2*elementOffset));
		}
	}
	
	private Listener newHueSliderChangeListener() {
		return hueSliderChangedListener;
	}
	
	final private Listener hueSliderChangedListener = new Listener() {
		protected void whenNotified() {
			if (!ignoreHueSliderNotifications) {
				setColorDataToMatchSliders();
				notifyColorModified();
			}
		}
	};
	
	private void setColorDataToMatchSliders() {
		color.r = hueSliders[0].getFillPercent();
		color.g = hueSliders[1].getFillPercent();
		color.b = hueSliders[2].getFillPercent();
		color.a = hueSliders[3].getFillPercent();
		onColorChangedEvent();
	}

}