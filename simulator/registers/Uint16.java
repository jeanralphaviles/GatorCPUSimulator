package simulator.registers;

public class Uint16 implements Register {
	private short value;
	
	public Uint16() {
		setValue(0);
	}
	
	public Uint16(int value) {
		setValue(value);
	}

	@Override
	public void setValue(int value) {
		this.value = (short) value;
	}

	@Override
	public int getValue() {
		return ((int)value) & 0xFFFF;
	}

}
