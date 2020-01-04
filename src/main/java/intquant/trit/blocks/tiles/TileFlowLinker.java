package intquant.trit.blocks.tiles;

import javax.annotation.Nullable;

import intquant.trit.Trit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileFlowLinker extends TileEnergyController implements ITickable {
	private int tickCounter = 0;
	private int exchX = 0;
	private int exchY = 0;
	private int exchZ = 0;
	public long rates[][] = new long[3][3];
	protected long MaxRate = 1000;

	public TileFlowLinker() {
		super();
		setMaxLightStorage(10000);
		setMaxForceStorage(10000);
		setMaxSpatialStorage(10000);
		setDoAccept(true);
		setDoProvide(true);
	}

	public int[] getLinkedDistances() {
		int exchV[] = { exchX, exchY, exchZ };
		return exchV;
	}

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

	public void update() {
		if (world == null || world.isRemote)
			return;
		if (pos == null || world == null)
			return;

		tickCounter++;
		if (tickCounter > 20 * 5) {
			tickCounter = Trit.rand.nextInt(10);
			exchX = traceRay(new BlockPos(1, 0, 0));
			exchY = traceRay(new BlockPos(0, 1, 0));
			exchZ = traceRay(new BlockPos(0, 0, 1));
		}
		if (world.getWorldTime()%5 != 0) return;

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
		IBlockState state = world.getBlockState(getPos());
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
				if (tile.isDoAccept()) {
					long v = Math.min(tile.getAcceptableEnergy(energyId), getProvideableEnergy(energyId));
					tile.manageEnergy(energyId, v);
					manageEnergy(energyId, -v);
					rates[sideId][energyId] = v;
				}
				if (tile.isDoProvide()) {
					long v = Math.min(getAcceptableEnergy(energyId), tile.getProvideableEnergy(energyId));
					manageEnergy(energyId, v);
					tile.manageEnergy(energyId, -v);
					rates[sideId][energyId] = v;

				}
			}
		}
	}

	public String getDebugData() {
		return String.format("%d %d %d | %d %d %d", exchX, exchY, exchZ, rates[0][0], rates[1][0], rates[2][0]);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		return compound;
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = new NBTTagCompound();
		for (int i = 0; i < 9; i++) {
			nbt.setLong(Integer.toString(i), rates[i / 3][i % 3]);
		}
		nbt.setIntArray("dists", getLinkedDistances());
		return nbt;

	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 1, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		// CommonProxy.logger.info("data packet recieved");
		NBTTagCompound nbt = packet.getNbtCompound();
		for (int i = 0; i < 9; i++) {
			// nbt.setLong(Integer.toString(i), rates[i/3][i%3]);
			rates[i / 3][i % 3] = nbt.getLong(Integer.toString(i));
		}
		int[] dists = nbt.getIntArray("dists");
		exchX = dists[0];
		exchY = dists[1];
		exchZ = dists[2];
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(pos.getX()-15, pos.getY()-15, pos.getZ()-15, pos.getX()+15, pos.getY()+15, pos.getZ()+15);
	}
}
