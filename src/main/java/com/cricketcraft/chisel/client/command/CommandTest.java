package com.cricketcraft.chisel.client.command;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.common.CarvableBlocks;
import com.cricketcraft.chisel.common.block.BlockCarvable;
import com.cricketcraft.chisel.common.block.subblocks.ISubBlock;
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
        BlockCarvable block = CarvableBlocks.ANTIBLOCK.getBlock();
        for (ISubBlock s : block.allSubBlocks()){
            var1.addChatMessage(new ChatComponentText(s.getName()));
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
