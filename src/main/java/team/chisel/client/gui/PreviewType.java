package team.chisel.client.gui;

import java.util.Locale;
import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.ImmutableSet;
import com.tterrag.registrate.providers.RegistrateLangProvider;

import lombok.Getter;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import team.chisel.Chisel;

@Getter 
public enum PreviewType {
    PANEL(16, generateBetween(0, 1, 0, 2, 3, 0)),
    
    @SuppressWarnings("null")
    HOLLOW(16, ArrayUtils.removeElement(generateBetween(0, 1, 0, 2, 3, 0), new BlockPos(1, 2, 0))),
    
    PLUS(20,
        new BlockPos(1, 1, 0),
        new BlockPos(1, 2, 0),
        new BlockPos(2, 2, 0),
        new BlockPos(0, 2, 0),
        new BlockPos(1, 3, 0)
    ),

    SINGLE(50, new BlockPos(1, 2, 0)),
    
    ;
    
    private static @Nonnull BlockPos[] generateBetween(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        BlockPos[] ret = new BlockPos[(maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1)];
        int i = 0;
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    ret[i++] = new BlockPos(x, y, z);
                }
            }
        }
        return ret;
    }
    
    private final float scale;
    private final Set<BlockPos> positions;
    private final TranslationTextComponent localizedName;
    
    private PreviewType(float scale, @Nonnull BlockPos... positions) {
        this.scale = scale;
        this.positions = ImmutableSet.copyOf(positions);
        this.localizedName = Chisel.registrate().addLang("hitech", new ResourceLocation(Chisel.MOD_ID, "preview." + name().toLowerCase(Locale.ROOT)), RegistrateLangProvider.toEnglishName(name()));
    }
}