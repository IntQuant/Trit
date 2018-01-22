package intquant.trit.misc;

import java.util.function.Predicate;

import intquant.trit.energy.IEnergyController;

public class ValidicyPredicate implements Predicate<IEnergyController> {

	public ValidicyPredicate() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean test(IEnergyController arg0) {
		if (arg0 == null | arg0.equals(null)) {
			return true;
		}
		return !arg0.isValid();
	}

}
