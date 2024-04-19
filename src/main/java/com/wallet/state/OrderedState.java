package com.wallet.state;

public class OrderedState  implements PackageState{
	@Override
    public void next(Package pkg) {
        pkg.setState(new DeliveredState());
    }

    @Override
    public void prev(Package pkg) {
    	return;
    }

    @Override
    public String printStatus() {
        return ("Package ordered");
    }
}
