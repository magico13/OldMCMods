package mods.magico13.ExtraIndustrial.generator;



import ic2.api.item.Items;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialFunctions;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialMod;
import mods.magico13.ExtraIndustrial.core.PacketUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerFusionReactorCore extends Container {
	protected TileEntityFusionReactorCore tileEntity;
	protected int lastEnergy=0;
	protected int lastHydrogen=0;
	protected EntityPlayer player;
	
	public ContainerFusionReactorCore(InventoryPlayer inventory, World world, int x,
			int y, int z)
	{
		tileEntity = (TileEntityFusionReactorCore) world.getBlockTileEntity(x, y, z);
		addSlotToContainer(new Slot(tileEntity, 0, 12, 34));

		player = inventory.player;
		bindPlayerInventory(inventory);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileEntity.isUseableByPlayer(player);
	}

	@Override
	public ItemStack slotClick(int slotNum, int par2, int par3, EntityPlayer player)
	{
		if (slotNum == 0)
		{
			ItemStack playerStack = player.inventory.getItemStack();
			if (playerStack != null)
				if (!ExtraIndustrialFunctions.identicalItems(playerStack, Items.getItem("electrolyzedWaterCell")) 
						&& !ExtraIndustrialFunctions.identicalItems(playerStack, new ItemStack(ExtraIndustrialMod.blockTesting, 1, 0))
						&& !ExtraIndustrialFunctions.identicalItems(playerStack, new ItemStack(ExtraIndustrialMod.blockTesting, 1, 1)))
					return null;
		}
		return super.slotClick(slotNum, par2, par3, player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		ItemStack stack = null;
		Slot slotObject = (Slot) inventorySlots.get(slot);

		if (slotObject != null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();
			if (slot < 1) {
				if (!mergeItemStack(stackInSlot, 1,
						inventorySlots.size(), false)) {
					return null;
				}
			} else if (!ExtraIndustrialFunctions.identicalItems(stackInSlot, Items.getItem("electrolyzedWaterCell")) 
					|| !mergeItemStack(stackInSlot, 0, 1, false)) {
				return null;
			}

			if (stackInSlot.stackSize == 0) {
				slotObject.putStack(null);
			} else {
				slotObject.onSlotChanged();
			}
		}

		return stack;
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
						8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
		}
	}
	
	@Override
	public void addCraftingToCrafters(ICrafting par1ICrafting)
	{
		super.addCraftingToCrafters(par1ICrafting);
		PacketDispatcher.sendPacketToPlayer(PacketUtils.guiUpdatePacket(0, this.tileEntity.getCurrentEnergy()), (Player)this.player);
		//par1ICrafting.sendProgressBarUpdate(this, 0, this.tileEntity.getCurrentEnergy());
		par1ICrafting.sendProgressBarUpdate(this, 1, this.tileEntity.getCurrentHydrogen());
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		int currentEnergy = this.tileEntity.getCurrentEnergy();
		int currentHydrogen = this.tileEntity.getCurrentHydrogen();
		for (int var1 = 0; var1 < this.crafters.size(); ++var1)
		{
			ICrafting var2 = (ICrafting)this.crafters.get(var1);

			if (this.lastEnergy != currentEnergy)
			{
				//var2.sendProgressBarUpdate(this, 0, currentEnergy);
				//PacketUtils.guiUpdatePacket(0, currentEnergy);
				PacketDispatcher.sendPacketToPlayer(PacketUtils.guiUpdatePacket(0, currentEnergy), (Player)this.player);
			}
			
			if (this.lastHydrogen != currentHydrogen)
			{
				var2.sendProgressBarUpdate(this, 1, currentHydrogen);
			}
		}

		this.lastEnergy = currentEnergy;
		this.lastHydrogen = currentHydrogen;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2)
	{
		if (par1 == 0)
		{
			this.tileEntity.energy = par2;
		}

		if (par1 == 1)
		{
			this.tileEntity.hydrogenAmount = par2;	
		}
	}
}
