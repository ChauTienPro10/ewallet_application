package com.wallet.state;

public interface PackageState {
	void next(Package pkg);
    void prev(Package pkg);
    String printStatus();
}
