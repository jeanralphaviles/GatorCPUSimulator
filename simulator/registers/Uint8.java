package simulator.registers;

public class Uint8 implements Register, Cloneable {
	private byte value;
	
	public Uint8() {
		setValue(0);
	}
	
	public Uint8(int value) {
		setValue(value);
	}

	@Override
	public void setValue(int value) {
		this.value = (byte) value;
	}

	@Override
	public int getValue() {
		return ((int)value) & 0xFF;
	}
	
	@Override
	public Uint8 clone() {
		return new Uint8(getValue());
	}

}
