package team.chisel.api.rendering;

import net.minecraft.util.IIcon;
import team.chisel.ctmlib.ISubmap;

public class ClientUtils {

	public static int renderCTMId;

	public static ISubmap wrap(final IIcon[][] map) {
		return new ISubmap() {

			@Override
			public int getWidth() {
				return map.length;
			}

			@Override
			public int getHeight() {
				return map[0].length;
			}

			@Override
			public IIcon getSubIcon(int x, int y) {
				return map[x][y];
			}
		};
	}
}
