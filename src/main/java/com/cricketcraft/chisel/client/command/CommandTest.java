package com.cricketcraft.chisel.client.command;

import com.cricketcraft.chisel.common.CarvableBlocks;
import com.google.common.collect.Lists;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.HoverEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.List;

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
