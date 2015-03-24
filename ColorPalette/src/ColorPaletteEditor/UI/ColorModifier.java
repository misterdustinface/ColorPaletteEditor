package ColorPaletteEditor.UI;

import generic.ListenerPattern.Descriptive.DataModificationNotifier;
import generic.structures.ColorData;

public abstract class ColorModifier extends DataModificationNotifier {
	
	final protected ColorData color;
	
	public ColorModifier() {
		color = new ColorData(0.75f,0.75f,0.75f,1.0f);
	}
	
	final public ColorData getColor() {
		return color.clone();
	}
	
	public void setColor(final ColorData source) {
		color.r = source.r;
		color.g = source.g;
		color.b = source.b;
		color.a = source.a;
		notifyColorModified();
	}
	
	final protected void notifyColorModified() {
		notifyDataModified();
	}

}