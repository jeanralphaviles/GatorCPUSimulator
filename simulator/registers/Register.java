package simulator.registers;

public interface Register {
	public void setValue(int value);
	public int getValue();
	
	public default int complement() {
		return ~getValue();
	}
	
	public default int bitAnd(Register reg) {
		return bitAnd(reg.getValue());
	}
	
	public default int bitAnd(int value) {
		return getValue() & value;
	}
	
	public default int bitOr(Register reg) {
		return bitOr(reg.getValue());
	}
	
	public default int bitOr(int value) {
		return getValue() | value;
	}
	
	public default int sum(Register reg) {
		return sum(reg.getValue());
	}
	
	public default int sum(int value) {
		return getValue() + value;
	}
	
	public default int leftShift(int bits) {
		return getValue() << bits;
	}
	
	public default int rightShift(int bits) {
		return getValue() >> bits;
	}

	public default void setValue(Register reg) {
		setValue(reg.getValue());
	}

	public default void add(Register reg) {
		add(reg.getValue());
	}
	
	public default void add(int value) {
		setValue(getValue() + value);
	}
	
	public default void clear() {
		setValue(0);
	}
	
	public default boolean equals(Register reg) {
		return equals(reg.getValue());
	}
	
	public default boolean equals(int value) {
		return getValue() == value;
	}
}
