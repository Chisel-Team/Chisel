package team.chisel.client.util;

import com.tterrag.registrate.Registrate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.util.text.TranslationTextComponent;

@RequiredArgsConstructor
public enum ChiselLangKeys {
    
    BLOCK_NAME("tooltip.blockname", "%1$s (%2$s)"),

    CHISEL_TOOLTIP_GUI("tooltip.chisel.gui", "%sRight-click%s to open GUI"),
    CHISEL_TOOLTIP_LC1("tooltip.chisel.lc1", "%sLeft-click%s to chisel blocks in the world"),
    CHISEL_TOOLTIP_LC2("tooltip.chisel.lc2", "%sTarget a block%s by leaving it in the inventory"),
    CHISEL_TOOLTIP_MODES("tooltip.chisel.modes", "Has multiple chiseling modes."),
    CHISEL_TOOLTIP_SELECTED_MODE("tooltip.chisel.modes.selected", "Selected mode: %s"),
    ;
    
    private final String key, value;
    
    @Getter
    private TranslationTextComponent component;
    
    public TranslationTextComponent format(Object... args) {
        return new TranslationTextComponent(getComponent().getKey(), args);
    }
    
    public static void init(Registrate registrate) {
        for (ChiselLangKeys lang : values()) {
            lang.component = registrate.addLang(lang.key, lang.value);
        }
    }
}
