package magico13.mods.NetherBits.handlers;

import magico13.mods.NetherBits.machine.ContainerCobbleGen;
import magico13.mods.NetherBits.machine.GuiCobbleGen;
import magico13.mods.NetherBits.machine.TileEntityCobbleGen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class NetherBitsGuiHandler implements IGuiHandler{

    //returns an instance of the Container you made earlier
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world,
                    int x, int y, int z) {
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
            if(tileEntity instanceof TileEntityCobbleGen){
                    return new ContainerCobbleGen(player.inventory, (TileEntityCobbleGen) tileEntity);
            }
            return null;
    }

    //returns an instance of the Gui you made earlier
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world,
                    int x, int y, int z) {
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
            if(tileEntity instanceof TileEntityCobbleGen){
                    return new GuiCobbleGen(player.inventory, (TileEntityCobbleGen) tileEntity);
            }
            return null;
    }

}
