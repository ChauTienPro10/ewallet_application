package com.wallet.state;

public class Package {
	private String name;
	private PackageState state = new OrderedState();

    
	
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PackageState getState() {
		return state;
	}

	public void setState(PackageState state) {
		this.state = state;
	}

	public void previousState() {
        state.prev(this);
    }

    public void nextState() {
        state.next(this);
    }

    public String getStatus() {
        return state.printStatus();
    }
}
