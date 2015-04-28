package simulator.registers;

public class Flag implements Register {
	private boolean value;
	
	public Flag() {
		setValue(false);
	}
	
	public Flag(boolean flag) {
		setValue(flag);
	}
	
	public Flag(int value) {
		setValue(value);
	}
	
	public boolean isActive() {
		return value;
	}
	
	public void setValue(boolean value) {
		setValue(value ? 1 : 0);
	}

	@Override
	public void setValue(int value) {
		this.value = value > 0;
	}

	@Override
	public int getValue() {
		return value ? 1 : 0;
	}

}
