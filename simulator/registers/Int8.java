package simulator.registers;

public class Int8 implements Register {
	private byte value;

	public Int8() {
		setValue(0);
	}

	public Int8(int value) {
		setValue(value);
	}

	@Override
	public void setValue(int value) {
		this.value = (byte) value;
	}

	@Override
	public int getValue() {
		return value & 0xFF;
	}

}
