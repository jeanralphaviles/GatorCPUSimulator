package simulator;

import simulator.registers.Register;
import simulator.registers.Uint8;

public class Memory {
	private final int size = 4096;
	private Uint8[] rom;
	private Uint8[] ram;
	
	public Memory() {
	}
	
	/**
	 * @param rom EPROM. Rounded to 4096 addresses
	 * @param ram SRAM. Rounded to 4096 addresses
	 */
	public Memory(Uint8[] rom, Uint8[] ram) {
		setRom(rom);
		setRam(ram);
	}
	
	public Uint8 at(int index) throws IndexOutOfBoundsException {
		if (index < 0x0 || index > 0x1FFF) {
			throw new IndexOutOfBoundsException("Illegal Memory Address");
		}
		if (index < 0x1000) {
			return rom[index];
		} else {
			return ram[index - 0x1000];
		}
	}
	
	public Uint8 at(Register reg) {
		return at(reg.getValue());
	}
	
	public void setIndex(Register index, Register value) throws Exception {
		setIndex(index.getValue(), value.getValue());
	}
	
	public void setIndex(int index, int value) throws Exception {
		if (index <= 0x1000) {
			throw new Exception("Cannot write to EPROM");
		}
		at(index).setValue(value);
	}
	
	public String printContents() {
		StringBuilder sb = new StringBuilder();
		sb.append("DEPTH = " + Integer.toString(getSize()) + "\n");
		sb.append("WIDTH = 8\n");
		sb.append("ADDRESS_RADIX = HEX\n");
		sb.append("DATA_RADIX = HEX\n\n");
		sb.append("CONTENT\n");
		sb.append("BEGIN\n");
		int streak = 0; // Keep track of how many values in a row we have, so that we can merge addresses
		int previous = at(0x1000).getValue();
		for (int i = 0x1000; i <= getSize() + 0x1000; ++i) {
			if (i < getSize() + 0x1000 && at(i).getValue() == previous) {
				// Continue streak
				++streak;
			} else {
				// Streak has ended
				if (streak > 5) {
					// Streak was long enough, print out a range
					sb.append('[');
					sb.append(Integer.toHexString(i - 0x1000 - streak));
					sb.append("..");
					sb.append(Integer.toHexString(i - 0x1000 - 1));
					sb.append(']');
					sb.append("\t:\t");
					sb.append(Integer.toHexString(previous));
					sb.append(";\n");
				} else {
					// Streak was not long enough, print what was to be a streak
					for (int j = i - streak; j < i; ++j) {
						sb.append(Integer.toHexString(j - 0x1000));
						sb.append("\t:\t");
						sb.append(Integer.toHexString(previous));
						sb.append(";\n");
					}
				}
				streak = 1;
				if (i < getSize() + 0x1000) {
					previous = at(i).getValue();
				}
			}
		}
		sb.append("\nEND;\n");
		return sb.toString().toUpperCase();
	}

	private int getSize() {
		return size;
	}
	
	/**
	 * @param rom EPROM to set. Rounded to 4096 addresses
	 */
	public void setRom(Uint8[] rom) {
		Uint8[] newRom = new Uint8[getSize()];
		for (int i = 0; i < getSize(); ++i) {
			if (i < rom.length) {
				newRom[i] = rom[i].clone();
			} else {
				newRom[i] = new Uint8();
			}
		}
		this.rom = newRom;
	}
	
	/**
	 * @param ram SRAM to set. Rounded to 4096 addresses
	 */
	public void setRam(Uint8[] ram) {
		Uint8[] newRam = new Uint8[getSize()];
		for (int i = 0; i < getSize(); ++i) {
			if (i < ram.length) {
				newRam[i] = ram[i].clone();
			} else {
				newRam[i] = new Uint8();
			}
		}
		this.ram = newRam;
	}
	
	public boolean ready() {
		return ram != null && rom != null;
	}

}
