package team.chisel.client.gui;

import com.google.common.collect.ImmutableSet;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;
import team.chisel.Chisel;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Set;

@Getter
public enum PreviewType {
    PANEL(16, generateBetween()),

    @SuppressWarnings("null")
    HOLLOW(16, ArrayUtils.removeElement(generateBetween(), new BlockPos(1, 2, 0))),

    PLUS(20,
            new BlockPos(1, 1, 0),
            new BlockPos(1, 2, 0),
            new BlockPos(2, 2, 0),
            new BlockPos(0, 2, 0),
            new BlockPos(1, 3, 0)
    ),

    SINGLE(50, new BlockPos(1, 2, 0)),

    ;

    private final float scale;
    private final Set<BlockPos> positions;
    private final TranslatableComponent localizedName;

    PreviewType(float scale, @Nonnull BlockPos... positions) {
        this.scale = scale;
        this.positions = ImmutableSet.copyOf(positions);
        this.localizedName = Chisel.registrateBase().addLang("hitech", new ResourceLocation(Chisel.MOD_ID, "preview." + name().toLowerCase(Locale.ROOT)), RegistrateLangProvider.toEnglishName(name()));
    }

    private static @Nonnull BlockPos[] generateBetween() {
        BlockPos[] ret = new BlockPos[(2 + 1) * (3 - 1 + 1)];
        int i = 0;
        for (int x = 0; x <= 2; x++) {
            for (int y = 1; y <= 3; y++) {
                for (int z = 0; z <= 0; z++) {
                    ret[i++] = new BlockPos(x, y, z);
                }
            }
        }
        return ret;
    }
}