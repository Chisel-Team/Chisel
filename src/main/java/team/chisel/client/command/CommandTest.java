package team.chisel.client.command;

import com.google.common.collect.Lists;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.List;

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
    public List getCommandAliases() {
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
    public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
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
