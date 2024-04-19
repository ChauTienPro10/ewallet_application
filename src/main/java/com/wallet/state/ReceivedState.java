package com.wallet.state;

public class ReceivedState implements PackageState {

    @Override
    public void next(Package pkg) {
    	return;
    }

    @Override
    public void prev(Package pkg) {
        pkg.setState(new DeliveredState());
    }

	@Override
	public String printStatus() {
		return("Package was received");
		
	}
}
