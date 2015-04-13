package com.cricketcraft.chisel.client.command;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.common.CarvableBlocks;
import com.cricketcraft.chisel.common.block.BlockCarvable;
import com.cricketcraft.chisel.common.block.subblocks.ISubBlock;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IRegistry;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Command for testing purposes
 *
 * @author minecreatr
 */
public class CommandTest implements ICommand{

    public String getCommandName(){
        return "ctest";
    }

    public String getCommandUsage(ICommandSender s){
        return "Chisel test command";
    }

    public List getCommandAliases(){
        return Lists.newArrayList();
    }

    public void processCommand(ICommandSender var1, String[] var2) throws CommandException{
        try {
            Field f = GuiIngame.class.getDeclaredField("itemRenderer");
            f.setAccessible(true);
            RenderItem renderer =  (RenderItem)f.get(Minecraft.getMinecraft().ingameGUI);
            Field f2 = RenderItem.class.getDeclaredField("itemModelMesher");
            f2.setAccessible(true);
            ItemModelMesher mesher = (ItemModelMesher)f2.get(renderer);
            Field f3 = ItemModelMesher.class.getDeclaredField("simpleShapesCache");
            f3.setAccessible(true);
            Map shapes = (Map)f3.get(mesher);
            for (Map.Entry entry : (Set<Map.Entry>)shapes.entrySet()){
                var1.addChatMessage(new ChatComponentText(entry.getKey().toString()+"="+entry.getValue().toString()));
            }
        } catch (Exception e){
            var1.addChatMessage(new ChatComponentText("error"));
            e.printStackTrace();
        }
    }

    public boolean canCommandSenderUseCommand(ICommandSender var1){
        return true;
    }

    public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3){
        return Lists.newArrayList();
    }

    public boolean isUsernameIndex(String[] var1, int var2){
        return false;
    }

    public int compareTo(Object o){
        return 0;
    }
}
