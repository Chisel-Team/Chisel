package team.chisel.client.util;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.RegistrateLangProvider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import team.chisel.Chisel;

@RequiredArgsConstructor
public enum ChiselLangKeys {
    
    TT_BLOCK_NAME("tooltip", "blockname", "%1$s (%2$s)"),

    TT_CHISEL_GUI("tooltip", "gui", "%sRight-click%s to open GUI"),
    TT_CHISEL_LC1("tooltip", "lc1", "%sLeft-click%s to chisel blocks in the world"),
    TT_CHISEL_LC2("tooltip", "lc2", "%sTarget a block%s by leaving it in the inventory"),
    TT_CHISEL_MODES("tooltip", "modes", "Has multiple chiseling modes."),
    TT_CHISEL_SELECTED_MODE("tooltip", "modes.selected", "Selected mode: %s"),
    
    PREVIEW("hitech", "preview"),
    BUTTON_CHISEL("button", "chisel"),
    BUTTON_CHISEL_ALL("button", "chisel_all"),
    
    JEI_TITLE("jei", "title", "Chiseling"),
    JEI_DESC_CHISEL_GENERIC("jei", "desc.chisel.generic", "A chisel can be used to create different variations of many different blocks. It can either be used with the GUI, by right clicking, or in the world, by left clicking. If a target is selected in the GUI, when the chisel is used in the world, it will only chisel to that type."),
    JEI_DESC_CHISEL_IRON("jei", "desc.chisel.iron", "The iron chisel is the most basic chisel, it has a low durability, and can only chisel one block in the world at a time."),
    JEI_DESC_CHISEL_DIAMOND("jei", "desc.chisel.diamond", "The diamond chisel is the mid-tier chisel, it has a high durability, and the ability to chisel up to 3x3 (9) blocks at once!"),
    JEI_DESC_CHISEL_HITECH("jei", "desc.chisel.hitech", "The iChisel is the newest revolution in chiseling technology! No more moving stacks around, just click on the source (in your inventory) and then the target (in the selection slots), then the button on the left to chisel away. Holding SHIFT allows you to chisel every stack of the same type at once. The handy preview window at the top left shows what the target block will look like in the world. Finally, the iChisel allows you to chisel OVER 9000 blocks in the world! Using the Contiguous and Contiguous 2D modes you can chisel an area of similar blocks at the same time."),
    ;
    
    private final String type, key, value;
    
    private ChiselLangKeys(String type, String key) {
        this(type, key, RegistrateLangProvider.toEnglishName(key));
    }
    
    @Getter
    private TranslatableComponent component;
    
    public TranslatableComponent format(Object... args) {
        return new TranslatableComponent(getComponent().getKey(), args);
    }
    
    public static void init(Registrate registrate) {
        for (ChiselLangKeys lang : values()) {
            lang.component = registrate.addLang(lang.type, new ResourceLocation(Chisel.MOD_ID, lang.key), lang.value);
        }
    }
}
