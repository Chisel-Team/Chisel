package com.cricketcraft.chisel.client.command;

import com.cricketcraft.chisel.Chisel;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
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
        Map ro;
        try {
            Field f = Minecraft.class.getDeclaredField("modelManager");
            f.setAccessible(true);
            ModelManager m=(ModelManager)f.get(Minecraft.getMinecraft());
            Field f2 = ModelManager.class.getDeclaredField("modelRegistry");
            f2.setAccessible(true);
            IRegistry r = (IRegistry)f2.get(m);
            Field f3 = r.getClass().getDeclaredField("registryObjects");
            f3.setAccessible(true);
            ro = (Map)f3.get(r);
        } catch (Exception exception){
            throw new RuntimeException(exception);
        }
        for (Map.Entry e : (Set<Map.Entry>)ro.entrySet()){
            Chisel.logger.info(e.getKey().toString()+" "+e.getValue().toString());
        }
        var1.addChatMessage(new ChatComponentText("Did stuff"));
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
