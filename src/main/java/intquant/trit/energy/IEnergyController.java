package intquant.trit.energy;

public interface IEnergyController {
	
	default public int getDebugId() {
		return 0;
	}
	
	default public  long getProvideableLight() {
		return Math.max(0, manageLight(0));
	}
	default public  long getProvideableForce() {
		return Math.max(0, manageForce(0));
	}
	default public  long getProvideableSpatial() {
		return Math.max(0, manageSpatial(0));
	}
	
	default public  long getAcceptableLight() {
		return Math.max(0, getMaxLightStorage() - manageLight(0));
	}
	default public  long getAcceptableForce() {
		return Math.max(0, getMaxForceStorage() - manageForce(0));
	}
	default public  long getAcceptableSpatial() {
		return Math.max(0, getMaxSpatialStorage() - manageSpatial(0));
	}
	
	default public boolean isValid() {return true;};
	//void setLinker(BlockPos pos);
	//BlockPos getLinker();
	default public String getDebugData() {
		return "<-NO VALUE->";
	}
	
	//Should add value to energy and return result
	default public  long manageLight(long value)   {return 0;};
	default public  long manageForce(long value)   {return 0;};
	default public  long manageSpatial(long value) {return 0;};
	
	default public  long getMaxLightStorage()   {return 0;};
	default public  long getMaxForceStorage()   {return 0;};
	default public  long getMaxSpatialStorage() {return 0;};
	
    default public long getForcedAcceptableLight()    {return 0;}
    default public long getForcedProvideableLight()   {return 0;}

    default public long getForcedAcceptableForce()    {return 0;}
    default public long getForcedProvideableForce()   {return 0;}

    default public long getForcedAcceptableSpatial()  {return 0;}
    default public long getForcedProvideableSpatial() {return 0;}
	
	default public  long provideLight(long value) {
		long targetValue = Math.min(getProvideableLight(), value);
		manageLight(-targetValue);
		return targetValue;
	}
	default public  long provideForce(long value) {
		long targetValue = Math.min(getProvideableForce(), value);
		manageForce(-targetValue);
		return targetValue;
	}
	default public  long provideSpatial(long value) {
		long targetValue = Math.min(getProvideableSpatial(), value);
		manageSpatial(-targetValue);
		return targetValue;
	}
	
	
	default public  long acceptLight(long value) {
		long targetValue = Math.min(getAcceptableLight(), value);
		manageLight(targetValue);
		return targetValue;
	}
	default public  long acceptForce(long value) {
		long targetValue = Math.min(getAcceptableForce(), value);
		manageForce(targetValue);
		return targetValue;
	}
	default public  long acceptSpatial(long value) {
		long targetValue = Math.min(getAcceptableSpatial(), value);
		manageSpatial(targetValue);
		return targetValue;
	}

	default public long getAcceptableEnergy(int type) {
		if (type == 0) {
			return getAcceptableLight();
		}
		if (type == 1) {
			return getAcceptableForce();
		}
		if (type == 2) {
			return getAcceptableSpatial();
		}
		return 0;
	}

	default public long getProvideableEnergy(int type) {
		if (type == 0) {
			return getProvideableLight();
		}
		if (type == 1) {
			return getProvideableForce();
		}
		if (type == 2) {
			return getProvideableSpatial();
		}
		return 0;
	}

	default public long getStorableEnergy(int type) {
		if (type == 0) {
			return getMaxLightStorage();
		}
		if (type == 1) {
			return getMaxForceStorage();
		}
		if (type == 2) {
			return getMaxSpatialStorage();
		}
		return 0;
	}

	default public long manageEnergy(int type, long amount) {
		if (type == 0) {
			return manageLight(amount);
		}
		if (type == 1) {
			return manageForce(amount);
		}
		if (type == 2) {
			return manageSpatial(amount);
		}
		return 0;
	}
}
