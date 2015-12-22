package team.chisel.client.command;

import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import com.google.common.collect.Lists;

/**
 * Command for testing purposes
 *
 * @author minecreatr
 */
public class CommandTest implements ICommand {

    @Override
    public String getCommandName() {
        return "ctest";
    }

    @Override
    public String getCommandUsage(ICommandSender s) {
        return "Chisel test command";
    }

    @Override
    public List<String> getCommandAliases() {
        return Lists.newArrayList();
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender var1) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
        return Lists.newArrayList();
    }

    @Override
    public boolean isUsernameIndex(String[] var1, int var2) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
