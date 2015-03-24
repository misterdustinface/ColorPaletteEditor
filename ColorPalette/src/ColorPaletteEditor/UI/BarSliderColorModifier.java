package ColorPaletteEditor.UI;

import generic.ListenerPattern.Listener;
import generic.structures.ColorData;
import shapes.Rectangle;
import UI.UILayer;
import UI.input.MouseUserDevice;
import UI.widgets.BarSlider;

public abstract class BarSliderColorModifier extends ColorModifier implements UILayer {
	
	final protected BarSlider[] hueSliders;
	final protected Rectangle displayBox;
	
	protected static int barSliderWidth = 21;
	protected static int elementOffset= 4;
	
	public BarSliderColorModifier(Rectangle DISPLAYBOX) {
		super();
		displayBox = DISPLAYBOX;
		hueSliders = new BarSlider[4];
		constructHueSliders();		
		setSlidersToMatchColorData();
	}
	
	@Override
	public void update(MouseUserDevice mouse) {
		for (BarSlider slider : hueSliders) {
			slider.update(mouse);
		}
	}
	
	public void setColor(final ColorData source) {
		super.setColor(source);
		onColorChangedEvent();
		setSlidersToMatchColorData();
	}
	
	abstract protected void onColorChangedEvent();
	
	abstract protected BarSlider newBarSliderSubclass();
	
	private void setSlidersToMatchColorData() {
		hueSliders[0].setFillPercent(color.r);
		hueSliders[1].setFillPercent(color.g);
		hueSliders[2].setFillPercent(color.b);
		hueSliders[3].setFillPercent(color.a);
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
		@Override
		protected void whenNotified() {
			setColorDataToMatchSliders();
			notifyColorModified();
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