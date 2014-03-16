package info.jbcs.minecraft.chisel.item;

import info.jbcs.minecraft.chisel.CarvableHelper;
import info.jbcs.minecraft.chisel.Carving;
import info.jbcs.minecraft.chisel.CarvingVariation;
import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.GeneralChiselClient;
import info.jbcs.minecraft.chisel.Packets;
import info.jbcs.minecraft.utilities.General;
import info.jbcs.minecraft.utilities.packets.PacketData;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import pl.asie.lib.network.Packet;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public class ItemChisel extends ItemTool {
	Random random=new Random();
	public Carving carving;

	private static final HashSet<String> toolSet = new HashSet<String>();
	
	public ItemChisel(Carving c) {
		super(1,ToolMaterial.IRON,CarvableHelper.getChiselBlockSet());

		maxStackSize = 1;
		setMaxDamage(500);
		efficiencyOnProperMaterial=100f;
		
		toolSet.add("chisel");
		carving=c;
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack) {
		return toolSet;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		entityplayer.openGui(Chisel.instance, 0, world, 0, 0, 0);
		
		return itemstack;
	}
/*
    @Override
	public void registerIcons(IconRegister register)
    {
    }*/

    HashMap<String,Long> chiselUseTime=new HashMap<String,Long>();
    HashMap<String,String> chiselUseLocation=new HashMap<String,String>();
	
    @Override
	public boolean onBlockStartBreak(ItemStack stack, final int x, final int y, final int z, EntityPlayer player){
		World world=player.worldObj;
		Block block = world.getBlock(x, y, z);
		int blockMeta=world.getBlockMetadata(x,y,z);
		
		if(! ForgeHooks.isToolEffective(stack, block, blockMeta))
			return false;
		
		ItemStack chiselTarget=null;
		
		if(stack.stackTagCompound!=null){
			chiselTarget=ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("chiselTarget"));
		}

		boolean chiselHasBlockInside=true;
		
		if(chiselTarget==null){
			Long useTime=chiselUseTime.get(player.getCommandSenderName());
			String loc=chiselUseLocation.get(player.getCommandSenderName());
			
			if(useTime!=null && chiselUseLocation!=null && loc.equals(x+"|"+y+"|"+z)){
				long cooldown=20;
				long time=world.getWorldInfo().getWorldTotalTime();
				
				if(time>useTime-cooldown && time<useTime+cooldown)
					return true;					
				
			}
			
			CarvingVariation[] variations=carving.getVariations(block, blockMeta);
			if(variations==null || variations.length<2) return true;
			
			int index=random.nextInt(variations.length-1);
			if(variations[index].equals(block) && variations[index].meta==blockMeta){
				index++;
				if(index>=variations.length) index=0;
			}
			CarvingVariation var=variations[index];
			chiselTarget=new ItemStack(var.block,1,var.damage);
			
			chiselHasBlockInside=false;
		}
		
		Item target = chiselTarget.getItem();
		
		int targetMeta=chiselTarget.getItemDamage();

		boolean match=carving.isVariationOfSameClass(Block.getBlockFromItem(target),targetMeta,block,blockMeta);
		Item result = target;
		
		
		/* special case: stone can be carved to cobble and bricks */
		if(!match && block.equals(Blocks.stone) && Block.getBlockFromItem(target).equals(Chisel.blockCobblestone))
			match=true;
		if(!match && block.equals(Blocks.stone) && Block.getBlockFromItem(target).equals(Chisel.stoneBrick))
			match=true;
		
		if(!match) return true;
		if(result.equals(block) && targetMeta == blockMeta) return true;
		
		if(! world.isRemote || chiselHasBlockInside) 
			world.setBlock(x, y, z, Block.getBlockFromItem(result), chiselTarget.getItemDamage(), 2);
		
		switch(FMLCommonHandler.instance().getEffectiveSide()){
		case SERVER:
			chiselUseTime.put(player.getCommandSenderName(), world.getWorldInfo().getWorldTotalTime());
			chiselUseLocation.put(player.getCommandSenderName(),x+"|"+y+"|"+z);
			
			try {
				Packet packet = Chisel.packet.create(Packets.CHISELED).writeInt(x).writeInt(y).writeInt(z);
				Chisel.packet.sendToAllAround(packet, new TargetPoint(player.dimension, x, y, z, 30.0f));
			} catch(Exception e) { e.printStackTrace(); }
			break;
			
		case CLIENT:
			if(chiselHasBlockInside){
				String sound=carving.getVariationSound(result, chiselTarget.getItemDamage());
				GeneralChiselClient.spawnChiselEffect(x, y, z, sound);
			}
			break;
			
		default:
			break;
		} 
		
		stack.damageItem(1, player);
	    if(stack.stackSize==0){
	    	player.inventory.mainInventory[player.inventory.currentItem]=
	    		chiselHasBlockInside?
	    		chiselTarget:
	    		null;
	    }
	    
		return true;
    }
	
	
}
