package ColorPaletteEditor.AWT.UI;

import AWT.UI.AWTBarSlider;


public class AWTHueBarSlider extends AWTBarSlider {

	@Override
	protected void pressAction() {
		
	}

	@Override
	protected void releaseAction() {
		
	}

	public void setHue(float val) { 
		setFillPercent(val); 
	}
	public float getHue() { 
		return getFillPercent(); 
	}
	
}
