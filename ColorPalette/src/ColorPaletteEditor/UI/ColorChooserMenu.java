package ColorPaletteEditor.UI;

import generic.ColorData;
import generic.ListenerPattern.Listener;
import generic.ListenerPattern.Descriptive.DataModificationNotifier;

import java.util.ArrayList;

import shapes.Rectangle;
import UI.BarSlider;
import UI.MenuButton;
import UI.MouseUserDevice;
import UI.UILayer;

public abstract class ColorChooserMenu extends DataModificationNotifier implements UILayer {
	
	final protected BarSlider[] hueSliders;
	final protected ArrayList<MenuButton> buttons;
	final protected Rectangle displayBox;
	
	final private Listener hueSliderChangeListener = new Listener() {
		@Override
		protected void whenNotified() {
			setColorDataToMatchSliders();
			notifyDataModified();
		}
	};
	
	protected static int elementSize = 64;
	protected static int elementShortsize = 21;
	protected static int elementOffset= 4;
	protected ColorData color;
	
	public ColorChooserMenu(Rectangle DISPLAYBOX) {
		displayBox = DISPLAYBOX;
		buttons = new ArrayList<MenuButton>();
		color = new ColorData(128,128,128,255);
		hueSliders = new BarSlider[4];
		
		for (int i = 0; i < 4; ++i) {
			hueSliders[i] = newBarSliderSubclass();
			hueSliders[i].addChangeListener(hueSliderChangeListener);
		}
		
		for (int i = 0; i < 4; ++i) {
			hueSliders[i].setBase(new Rectangle(displayBox.x + (1+i)*elementOffset + (i)*elementShortsize, displayBox.y +elementOffset , elementShortsize, displayBox.height));
		}
		
		setColorDataToMatchSliders();
	}
	
	abstract protected BarSlider newBarSliderSubclass();
	abstract protected void colorDataUpdated();
	
	public void setColorData(ColorData COLOR_DATA) {
		color = COLOR_DATA;
		setSlidersToMatchColorData();
	}
	
	public ColorData getColorData() { 
		return color; 
	}
	
	private void setSlidersToMatchColorData() {
		hueSliders[0].setFillPercent(color.r);
		hueSliders[1].setFillPercent(color.g);
		hueSliders[2].setFillPercent(color.b);
		hueSliders[3].setFillPercent(color.a);
	}
	
	private void setColorDataToMatchSliders() {
		color.r = hueSliders[0].getFillPercent();
		color.g = hueSliders[1].getFillPercent();
		color.b = hueSliders[2].getFillPercent();
		color.a = hueSliders[3].getFillPercent();
		colorDataUpdated();
	}
	
	@Override
	public void update(MouseUserDevice mouse) {
		for (MenuButton button : buttons)
			button.update(mouse);
		for (BarSlider slider : hueSliders)
			slider.update(mouse);
	}
	
	public void addButton(MenuButton button) {
		buttons.add(button);
	}

}
