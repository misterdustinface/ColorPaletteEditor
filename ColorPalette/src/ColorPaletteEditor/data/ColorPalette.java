package ColorPaletteEditor.data;

import java.util.Iterator;

import generic.datastructures.Table;
import generic.structures.ColorData;

public class ColorPalette implements Iterable<ColorData> {
	
	private Table<ColorData> palette;
	private int size;
	
	public ColorPalette() {
		palette = new Table<ColorData>();
		size = 0;
	}
	
	public void clear() {
		palette.clear();
		size = 0;
	}
	
	public void add(ColorData color) {
		palette.insert(indexToString(size), color);
		size++;
	}
	
	public void remove(int index) {
		palette.remove(indexToString(index));
		size--;
	}
	
	public ColorData get(int index) {
		return palette.get(indexToString(index));
	}
	
	public void merge(ColorPalette other) {
		for (String indexString : other.palette.getNames()) {
			add(other.get(Integer.valueOf(indexString)));
		}
	}
	
	public int getSize() {
		return size;
	}
	
	private String indexToString(int i) {
		return Integer.toString(i);
	}

	public Iterator<ColorData> iterator() {
		return new Iterator<ColorData>(){
			
			private int index = 0;
			
			public boolean hasNext() {
				return index < size;
			}

			public ColorData next() {
				ColorData thisColor = get(index);
				index++;
				return thisColor;
			}

			public void remove() {
				
			}
			
		};
	}

}
