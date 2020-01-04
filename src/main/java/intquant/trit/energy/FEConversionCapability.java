package intquant.trit.energy;

import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class FEConversionCapability implements IEnergyStorage {

    IEnergyController ctrl;

    public FEConversionCapability(IEnergyController ctrl) {
        this.ctrl = ctrl;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive()) return 0;
        long v = Math.min(ctrl.getAcceptableLight(), maxReceive);
        if (!simulate) {
            ctrl.manageLight(v);
        }
        return (int)v;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract()) return 0;
        long v = Math.min(ctrl.getProvideableLight(), maxExtract);
        if (!simulate) {
            ctrl.manageLight(-v);
        }
        return (int)v;
    }

    @Override
    public int getEnergyStored() {
        long v = ctrl.getMaxLightStorage();
        if (v > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else return (int)v;
    }

    @Override
    public int getMaxEnergyStored() {
        long v = ctrl.getMaxLightStorage();
        if (v > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else return (int)v;
    }

    @Override
    public boolean canExtract() {
        return ctrl.getAcceptableLight() > 0;
    }

    @Override
    public boolean canReceive() {
        return ctrl.getProvideableLight() > 0;
    }
    

}