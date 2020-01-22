package team.chisel.client.util;

import com.tterrag.registrate.Registrate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import team.chisel.Chisel;

@RequiredArgsConstructor
public enum ChiselLangKeys {
    
    TT_BLOCK_NAME("tooltip", "blockname", "%1$s (%2$s)"),

    TT_CHISEL_GUI("tooltip", "gui", "%sRight-click%s to open GUI"),
    TT_CHISEL_LC1("tooltip", "lc1", "%sLeft-click%s to chisel blocks in the world"),
    TT_CHISEL_LC2("tooltip", "lc2", "%sTarget a block%s by leaving it in the inventory"),
    TT_CHISEL_MODES("tooltip", "modes", "Has multiple chiseling modes."),
    TT_CHISEL_SELECTED_MODE("tooltip", "modes.selected", "Selected mode: %s"),
    ;
    
    private final String type, key, value;
    
    @Getter
    private TranslationTextComponent component;
    
    public TranslationTextComponent format(Object... args) {
        return new TranslationTextComponent(getComponent().getKey(), args);
    }
    
    public static void init(Registrate registrate) {
        for (ChiselLangKeys lang : values()) {
            lang.component = registrate.addLang(lang.type, new ResourceLocation(Chisel.MOD_ID, lang.key), lang.value);
        }
    }
}
