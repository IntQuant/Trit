package intquant.trit.energy;

public interface EnergyController {
	
	default long getProvideableLight() {
		return Math.max(0, manageLight(0));
	}
	default long getProvideableForce() {
		return Math.max(0, manageForce(0));
	}
	default long getProvideableSpatial() {
		return Math.max(0, manageSpatial(0));
	}
	
	default long getAcceptableLight() {
		return Math.max(0, getLightStorage() - manageLight(0));
	}
	default long getAcceptableForce() {
		return Math.max(0, getForceStorage() - manageForce(0));
	}
	default long getAcceptableSpatial() {
		return Math.max(0, getSpatialStorage() - manageSpatial(0));
	}
	
	
	//Should add value to energy and return result
	default long manageLight(long value)   {return 0;};
	default long manageForce(long value)   {return 0;};
	default long manageSpatial(long value) {return 0;};
	
	default long getLightStorage()   {return 0;};
	default long getForceStorage()   {return 0;};
	default long getSpatialStorage() {return 0;};
	
	default long provideLight(long value) {
		long targetValue = Math.min(getProvideableLight(), value);
		manageLight(-targetValue);
		return targetValue;
	}
	default long provideForce(long value) {
		long targetValue = Math.min(getProvideableForce(), value);
		manageForce(-targetValue);
		return targetValue;
	}
	default long provideSpatial(long value) {
		long targetValue = Math.min(getProvideableSpatial(), value);
		manageSpatial(-targetValue);
		return targetValue;
	}
	
	
	default long acceptLight(long value) {
		long targetValue = Math.min(getAcceptableLight(), value);
		manageLight(-targetValue);
		return targetValue;
	}
	default long acceptForce(long value) {
		long targetValue = Math.min(getAcceptableForce(), value);
		manageForce(targetValue);
		return targetValue;
	}
	default long acceptSpatial(long value) {
		long targetValue = Math.min(getAcceptableSpatial(), value);
		manageSpatial(targetValue);
		return targetValue;
	}
}
