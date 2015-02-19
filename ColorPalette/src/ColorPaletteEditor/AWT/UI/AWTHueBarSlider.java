package ColorPaletteEditor.AWT.UI;

import AWT.UI.AWTBarSlider;


public class AWTHueBarSlider extends AWTBarSlider {

	@Override
	protected void pressAction() {
		
	}

	@Override
	protected void releaseAction() {
		
	}

	public void setHue(int i) { 
		setFillPercent(i / 255f); 
	}
	public int  getHue() { 
		return (int)(255 * getFillPercent()); 
	}
	
}
