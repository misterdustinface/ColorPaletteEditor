package ColorPaletteEditor.file;

import file.LuaScriptFiler;
import generic.ColorData;

import java.util.ArrayList;

public class ColorPaletteFiler extends LuaScriptFiler {

	private ArrayList<ColorData> loadedPalette;
	private ArrayList<ColorData> palette;

	public ColorPaletteFiler() {}
	
	public ColorPaletteFiler(ArrayList<ColorData> PALETTE) {
		setPalette(PALETTE);
	}
	
	public void setPalette(ArrayList<ColorData> PALETTE) { palette = PALETTE; }
	
	@Override
	protected String dataToLuaScript() {
		String script = new String();
		for(ColorData color : palette){
			script += createEntry(	String.valueOf(color.r), String.valueOf(color.g),
									String.valueOf(color.b), String.valueOf(color.a));
		}
		return scriptHeading("Color Palette") + script + scriptCloser("End of Palette");
	}
	
	@Override
	protected void preparseOperation() {
		loadedPalette = new ArrayList<ColorData>();
	}
	
	@Override
	protected void parseLine(String line) {
		ColorData color = parseColorDataFromLine(line);
		loadedPalette.add(color);
	}

	private ColorData parseColorDataFromLine(String line) {
		String[] entries = super.parseEntries(line);
		int[] hues = new int[] {Integer.parseInt(entries[0]),Integer.parseInt(entries[1]),
								Integer.parseInt(entries[2]),Integer.parseInt(entries[3])};
		return new ColorData(hues);
	}

	@Override
	protected void postparseOperation() {
		palette.addAll(loadedPalette);
	}
}
