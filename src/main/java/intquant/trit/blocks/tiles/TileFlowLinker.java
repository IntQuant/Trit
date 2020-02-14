package intquant.trit.blocks.tiles;

import javax.annotation.Nullable;

import intquant.trit.Trit;
import intquant.trit.blocks.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileFlowLinker extends TileEnergyController implements ITickableTileEntity {
	private int tickCounter = 0;
	private int exchX = 0;
	private int exchY = 0;
	private int exchZ = 0;
	public long rates[][] = new long[3][3];
	protected long MaxRate = 1000;
	int dir = 0;

	//protected FEConversionCapability fe_handler;
	public TileFlowLinker(int dir) {
		super(ModBlocks.TILE_FLOWLINKER);
		setMaxLightStorage(10000);
		setMaxForceStorage(10000);
		setMaxSpatialStorage(10000);
		setDoAccept(true);
		setDoProvide(true);
		this.dir = dir;
	}

	public TileFlowLinker() {
		super(ModBlocks.TILE_FLOWLINKER);
		setMaxLightStorage(10000);
		setMaxForceStorage(10000);
		setMaxSpatialStorage(10000);
		setDoAccept(true);
		setDoProvide(true);		
	}

	public TileFlowLinker(TileEntityType<?> t) {
		super(t);
		//fe_handler = new FEConversionCapability(this);
	}

	public int[] getLinkedDistances() {
		int exchV[] = { exchX, exchY, exchZ };
		return exchV;
	}
	/*
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return (T)fe_handler;
		}
		return super.getCapability(capability, facing);
	}
	*/
	@Nullable
	protected TileEnergyController getEnergyBlockAt(BlockPos bpos) {
		TileEntity tile = world.getTileEntity(bpos);
		if (tile != null && tile instanceof TileEnergyController) {
			return (TileEnergyController) tile;
		}
		return null;
	}

	private int traceRay(BlockPos advance) {
		int tokens = 15;
		int res = 0;
		BlockPos cpos = pos;
		while (tokens-- > 0) {
			res++;
			cpos = cpos.add(advance);
			if (!(world.isBlockLoaded(cpos) && world.isAirBlock(cpos))) {
				break;
			}
		}
		if (world.isBlockLoaded(cpos) && (getEnergyBlockAt(cpos) != null)) {
			return res;
		} else {
			return 0;
		}
	}

	public void tick() {
		if (world == null || world.isRemote)
			return;
		if (pos == null || world == null)
			return;

		/*
		BlockPos placed_on = pos.offset(EnumFacing.getFront(dir).getOpposite());
		TileEntity pon = world.getTileEntity(placed_on);
		if (pon != null) {
			IEnergyStorage cap = pon.getCapability(CapabilityEnergy.ENERGY, EnumFacing.getFront(dir));
			if (cap != null) {
				if (cap.canReceive()) {
					int v = cap.receiveEnergy((int)manageLight(0), false);
					manageLight(-v);
				} else 
				if (cap.canExtract()) {
					int v = cap.extractEnergy((int)getAcceptableLight(), false);
					manageLight(v);
				}
			}
		}*/


		tickCounter++;
		if (tickCounter > 20 * 5) {
			tickCounter = Trit.rand.nextInt(10);
			exchX = traceRay(new BlockPos(1, 0, 0));
			exchY = traceRay(new BlockPos(0, 1, 0));
			exchZ = traceRay(new BlockPos(0, 0, 1));
		}
		//if (world.getWorldTime()%5 != 0) return;

		if (exchX > 0) {
			exchangeEnergy(pos.add(exchX, 0, 0), 0);
		}
		if (exchY > 0) {
			exchangeEnergy(pos.add(0, exchY, 0), 1);
		}
		if (exchZ > 0) {
			exchangeEnergy(pos.add(0, 0, exchZ), 2);
		}
		markDirty();
		BlockState state = world.getBlockState(getPos());
		world.notifyBlockUpdate(getPos(), state, state, 3);
	}

	private void exchangeEnergy(BlockPos bpos, int sideId) {
		TileEnergyController tile = getEnergyBlockAt(bpos);
		if (tile == null)
			return;

		for (int energyId = 0; energyId < 3; energyId++) {
			boolean isSelfUni = isDoAccept() && isDoProvide();
			boolean isTileUni = tile.isDoAccept() && tile.isDoProvide();
			if (isSelfUni && isTileUni) {
				if (getAcceptableEnergy(energyId) > tile.getAcceptableEnergy(energyId)) {
					long tr = (getAcceptableEnergy(energyId) - tile.getAcceptableEnergy(energyId)) / 2;
					//long hv = getStorableEnergy(energyId) / 2;
					//if (tile.manageEnergy(energyId, 0) < hv) continue;
					//long tr = Math.min(hv - manageEnergy(energyId, 0), tile.manageEnergy(energyId, 0) - hv);
					manageEnergy(energyId, tr);
					tile.manageEnergy(energyId, -tr);
					rates[sideId][energyId] = tr;
				} else {
					long tr = (tile.getAcceptableEnergy(energyId) - getAcceptableEnergy(energyId)) / 2;
					//long hv = tile.getStorableEnergy(energyId) / 2;
					//if (manageEnergy(energyId, 0) < hv) continue;
					//long tr = Math.min(hv - tile.manageEnergy(energyId, 0), manageEnergy(energyId, 0) - hv);
					tile.manageEnergy(energyId, tr);
					manageEnergy(energyId, -tr);
					rates[sideId][energyId] = tr;
				}
			} else {
				long v1 = 0;
				long v2 = 0;
				if (tile.isDoAccept()) {
					long v = Math.min(tile.getAcceptableEnergy(energyId), getProvideableEnergy(energyId));
					tile.manageEnergy(energyId, v);
					manageEnergy(energyId, -v);
					v1 = v;
				}
				if (tile.isDoProvide()) {
					long v = Math.min(getAcceptableEnergy(energyId), tile.getProvideableEnergy(energyId));
					manageEnergy(energyId, v);
					tile.manageEnergy(energyId, -v);
					v2 = v;
				}
				rates[sideId][energyId] = Math.max(v1, v2);
			}
		}
	}

	public String getDebugData() {
		return String.format("%d %d %d | %d %d %d", exchX, exchY, exchZ, rates[0][0], rates[1][0], rates[2][0]);
	}

	@Override
	public void read(CompoundNBT compound) {
		if (compound.contains("dir", 99)) {
			dir = compound.getInt("dir");
		}
		super.read(compound);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound.putInt("dir", dir);
		return super.write(compound);
	}

	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT nbt = new CompoundNBT();
		for (int i = 0; i < 9; i++) {
			nbt.putLong(Integer.toString(i), rates[i / 3][i % 3]);
		}
		nbt.putIntArray("dists", getLinkedDistances());
		return nbt;

	}
	/*
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 1, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		// CommonProxy.logger.info("data packet recieved");
		CompoundNBT nbt = packet.getNbtCompound();
		for (int i = 0; i < 9; i++) {
			// nbt.setLong(Integer.toString(i), rates[i/3][i%3]);
			rates[i / 3][i % 3] = nbt.getLong(Integer.toString(i));
		}
		int[] dists = nbt.getIntArray("dists");
		exchX = dists[0];
		exchY = dists[1];
		exchZ = dists[2];
	}*/

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(pos.getX()-15, pos.getY()-15, pos.getZ()-15, pos.getX()+15, pos.getY()+15, pos.getZ()+15);
	}
}
