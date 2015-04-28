package simulator;

import simulator.registers.Flag;
import simulator.registers.Int8;
import simulator.registers.Uint16;
import simulator.registers.Uint8;

public class Cpu {
	private Memory memory;
	private int state = 0x0; // Current state

	// Registers
	private Int8 a = new Int8(); // A register
	private Int8 b = new Int8(); // B register
	private Uint16 x = new Uint16(); // X register
	private Uint8 xDisp = new Uint8(); // X displacement register
	private Uint16 y = new Uint16(); // Y register
	private Uint8 yDisp = new Uint8(); // Y displacement register
	private Uint8 ir = new Uint8(); // Instruction register
	private Uint16 pc = new Uint16(); // Program counter
	private Uint16 mar = new Uint16(); // Memory address register

	// Multiplexer selects
	private Uint8 msa = new Uint8(); // Multiplexer select A
	private Uint8 msb = new Uint8(); // Multiplexer select B
	private Uint8 msc = new Uint8(); // Multiplexer select C

	// Control flags
	private Flag irld = new Flag(); // Instruction register load
	private Flag rw = new Flag(); // Read/Write flag

	// Addressing
	private Uint8 regInc = new Uint8(); // Register increment
	private Uint8 addrSel = new Uint8(); // Address select

	// Register loading
	private Uint8 pcld = new Uint8(); // Program counter load
	private Uint8 marld = new Uint8(); // Memory address register load
	private Uint8 xld = new Uint8(); // X register load
	private Uint8 yld = new Uint8(); // Y register load
	private Flag xDispLd = new Flag(); // X displacement register load
	private Flag yDispLd = new Flag(); // Y displacement register load
	
	public Cpu() {
		this.memory = new Memory();
	}
	
	public Cpu(Uint8[] rom, Uint8[] ram) {
		this.memory = new Memory(rom, ram);
	}

	public void execute() throws Exception {
		zero();
		if (state == 0x0) {
			// State 0, Instruction Fetch
			msa.setValue(0x1);
			msb.setValue(0x2);
			irld.setValue(0x1);
			rw.setValue(0x1);
			state = 0x1;
		} else if (state == 0x1) {
			// State 1, Instruction Decode/Execution
			switch(ir.getValue()) {
			case 0x0:
				// TAB (Transfer A to B)
				msa.setValue(0x1);
				msb.setValue(0x1);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x0;
				break;
			case 0x1:
				// TBA (Transfer B to A)
				msa.setValue(0x2);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x0;
				break;
			case 0x2:
				// LDAA #data, state1
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x8);
				regInc.setValue(0x8);
				state = 0x2;
				break;
			case 0x3:
				// LDAB #data, state1
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x3;
				break;
			case 0x4:
				// LDAA addr, state1
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x4;
				break;
			case 0x5:
				// LDAB addr, state1
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x7;
				break;
			case 0x6:
				// STAA addr, state1
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0xA;
				break;
			case 0x7:
				// STAB addr, state1
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0xD;
				break;
			case 0x8:
				// LDX #data, state1
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x10;
				break;
			case 0x9:
				// LDY #data, state1
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x12;
				break;
			case 0xA:
				// LDX addr, state1
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x14;
				break;
			case 0xB:
				// LDY addr, state1
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x18;
				break;
			case 0xC:
				// LDAA dd, X state1
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x1C;
				break;
			case 0xD:
				// LDAA dd, Y state1
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x1E;
				break;
			case 0xE:
				// LDAB dd, X state1
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x20;
				break;
			case 0xF:
				// LDAB dd, Y state1
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x22;
				break;
			case 0x10:
				// STAA dd, X state1
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x24;
				break;
			case 0x11:
				// STAA dd, Y state1
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x26;
				break;
			case 0x12:
				// STAB dd, X state1
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x28;
				break;
			case 0x13:
				// STAB dd, Y state1
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x2A;
				break;
			case 0x14:
				// SUM_BA state1
				msa.setValue(0x3);
				msb.setValue(0x2);
				msc.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x0;
				break;
			case 0x15:
				// SUM_AB state1
				msa.setValue(0x1);
				msb.setValue(0x3);
				msc.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x0;
				break;
			case 0x16:
				// AND_BA state1
				msa.setValue(0x3);
				msb.setValue(0x2);
				msc.setValue(0x3);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x0;
				break;
			case 0x17:
				// AND_AB state1
				msa.setValue(0x1);
				msb.setValue(0x3);
				msc.setValue(0x3);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x0;
				break;
			case 0x18:
				// OR_BA state1
				msa.setValue(0x3);
				msb.setValue(0x2);
				msc.setValue(0x4);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x0;
				break;
			case 0x19:
				// OR_AB state1
				msa.setValue(0x1);
				msb.setValue(0x3);
				msc.setValue(0x4);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x0;
				break;
			case 0x1A:
				// COMA state1
				msa.setValue(0x3);
				msb.setValue(0x2);
				msc.setValue(0x5);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x0;
				break;
			case 0x1B:
				// COMB state1
				msa.setValue(0x1);
				msb.setValue(0x3);
				msc.setValue(0x6);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x0;
				break;
			case 0x1C:
				// SHFA_L state1
				msa.setValue(0x3);
				msb.setValue(0x2);
				msc.setValue(0x7);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x0;
				break;
			case 0x1D:
				// SHFA_R state1
				msa.setValue(0x3);
				msa.setValue(0x2);
				msc.setValue(0x8);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x0;
				break;
			case 0x1E:
				// SHFB_L state1
				msa.setValue(0x1);
				msb.setValue(0x3);
				msc.setValue(0x9);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x0;
				break;
			case 0x1F:
				// SHFB_R state1
				msa.setValue(0x1);
				msb.setValue(0x3);
				msc.setValue(0xA);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				state = 0x0;
				break;
			case 0x20:
				// BEQ addr state1
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				if (zeroFlag()) {
					state = 0x2C;
				} else {
					state = 0x2D;
				}
				break;
			case 0x21:
				// BNE addr state1
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				if (zeroFlag()) {
					state = 0x2F;
				} else {
					state = 0x2E;
				}
				break;
			case 0x22:
				// BN addr state1
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				if (negativeFlag()) {
					state = 0x30;
				} else {
					state = 0x31;
				}
				break;
			case 0x23:
				// BP addr state1
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x8);
				if (negativeFlag()) {
					state = 0x33;
				} else {
					state = 0x32;
				}
				break;
			case 0x30:
				// Increment X
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0xA);
				state = 0x0;
				break;
			case 0x31:
				// Increment Y
				msa.setValue(0x1);
				msb.setValue(0x2);
				rw.setValue(0x1);
				regInc.setValue(0x9);
				state = 0x0;
				break;
			default:
				break;
			}
		} else if (state == 0x2) {
			// LDAA #data, state2
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			state = 0x0;
		} else if (state == 0x3) {
			// LDAB #data, state3
			msa.setValue(0x1);
			msc.setValue(0x1);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			state = 0x0;
		} else if (state == 0x4) {
			// LDAA addr, state4
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			marld.setValue(0x2);
			state = 0x5;
		} else if (state == 0x5) {
			// LDAA addr, state5
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			marld.setValue(0x1);
			state = 0x6;
		} else if (state == 0x6) {
			// LDAA addr, state6
			msb.setValue(0x2);
			rw.setValue(0x1);
			addrSel.setValue(0x1);
			state = 0x0;
		} else if (state == 0x7) {
			// LDAB addr, state7
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			marld.setValue(0x2);
			state = 0x8;
		} else if (state == 0x8) {
			// LDAB addr, state8
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			marld.setValue(0x1);
			state = 0x9;
		} else if (state == 0x9) {
			// LDAB addr, state9
			msa.setValue(0x1);
			rw.setValue(0x1);
			addrSel.setValue(0x1);
			state = 0x0;
		} else if (state == 0xA) {
			// STAA addr, stateA
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			marld.setValue(0x2);
			state = 0xB;
		} else if (state == 0xB) {
			// STAA addr, stateB
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			marld.setValue(0x1);
			state = 0xC;
		} else if (state == 0xC) {
			// STAA addr, stateC
			msa.setValue(0x1);
			msb.setValue(0x2);
			addrSel.setValue(0x1);
			state = 0x0;
		} else if (state == 0xD) {
			// STAB addr, stateD
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			marld.setValue(0x2);
			state = 0xE;
		} else if (state == 0xE) {
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			marld.setValue(0x1);
			state = 0xF;
		} else if (state == 0xF) {
			// STAB addr, stateF
			msa.setValue(0x1);
			msb.setValue(0x2);
			msc.setValue(0x1);
			addrSel.setValue(0x1);
			state = 0x0;
		} else if (state == 0x10) {
			// LDX #data, state10
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			xld.setValue(0x2);
			state = 0x11;
		} else if (state == 0x11) {
			// LDX #data, state11
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			xld.setValue(0x1);
			state = 0x0;
		} else if (state == 0x12) {
			// LDY #data, state12
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			yld.setValue(0x2);
			state = 0x13;
		} else if (state == 0x13) {
			// LDY #data, state13
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			yld.setValue(0x1);
			state = 0x0;
		} else if (state == 0x14) {
			// LDX addr, state14
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			marld.setValue(0x2);
			state = 0x15;
		} else if (state == 0x15) {
			// LDX addr, state15
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			marld.setValue(0x1);
			state = 0x16;
		} else if (state == 0x16) {
			// LDX addr, state16
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x4);
			addrSel.setValue(0x1);
			xld.setValue(0x2);
			state = 0x17;
		} else if (state == 0x17) {
			// LDX addr, state17
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x4);
			addrSel.setValue(0x1);
			xld.setValue(0x1);
			state = 0x0;
		} else if (state == 0x18) {
			// LDY addr, state18
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			marld.setValue(0x2);
			state = 0x19;
		} else if (state == 0x19) {
			// LDY addr, state19
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			marld.setValue(0x1);
			state = 0x1A;
		} else if (state == 0x1A) {
			// LDY addr, state1A
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x4);
			addrSel.setValue(0x1);
			yld.setValue(0x2);
			state = 0x1B;
		} else if (state == 0x1B) {
			// LDY addr, state1B
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x4);
			addrSel.setValue(0x1);
			yld.setValue(0x1);
			state = 0x0;
		} else if (state == 0x1C) {
			// LDAA dd, X state1C
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			xDispLd.setValue(0x2);
			state = 0x1D;
		} else if (state == 0x1D) {
			// LDAA dd, X state1D
			msb.setValue(0x2);
			rw.setValue(0x1);
			addrSel.setValue(0x2);
			state = 0x0;
		} else if (state == 0x1E) {
			// LDAA dd, Y state1E
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			yDispLd.setValue(0x1);
			state = 0x1F;
		} else if (state == 0x1F) {
			// LDAA dd, Y state1F
			msb.setValue(0x2);
			rw.setValue(0x1);
			addrSel.setValue(0x3);
			state = 0x0;
		} else if (state == 0x20) {
			// LDAB dd, X state20
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			xDispLd.setValue(0x2);
			state = 0x21;
		} else if (state == 0x21) {
			// LDAB dd, X state21
			msa.setValue(0x1);
			rw.setValue(0x1);
			addrSel.setValue(0x2);
			state = 0x0;
		} else if (state == 0x22) {
			// LDAB dd, Y state22
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			yDispLd.setValue(0x1);
			state = 0x23;
		} else if (state == 0x23) {
			// LDAB dd, Y state23
			msa.setValue(0x1);
			rw.setValue(0x1);
			addrSel.setValue(0x3);
			state = 0x0;
		} else if (state == 0x24) {
			// STAA dd, X state24
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			xDispLd.setValue(0x2);
			state = 0x25;
		} else if (state == 0x25) {
			// STAA dd, X state25
			msa.setValue(0x1);
			msb.setValue(0x2);
			addrSel.setValue(0x2);
			state = 0x0;
		} else if (state == 0x26) {
			// STAA dd, Y state26
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			yDispLd.setValue(0x1);
			state = 0x27;
		} else if (state == 0x27) {
			// STAA dd, Y state27
			msa.setValue(0x1);
			msb.setValue(0x2);
			addrSel.setValue(0x3);
			state = 0x0;
		} else if (state == 0x28) {
			// STAB dd, X state28
			msa.setValue(0x1);
			msb.setValue(0x2);
			msc.setValue(0x1);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			xDispLd.setValue(0x2);
			state = 0x29;
		} else if (state == 0x29) {
			// STAB dd, X state29
			msa.setValue(0x1);
			msb.setValue(0x2);
			msc.setValue(0x1);
			addrSel.setValue(0x2);
			state = 0x0;
		} else if (state == 0x2A) {
			// STAB dd, Y state2A
			msa.setValue(0x1);
			msb.setValue(0x2);
			msc.setValue(0x1);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			yDispLd.setValue(0x1);
			state = 0x2B;
		} else if (state == 0x2B) {
			// STAB dd, Y state2B
			msa.setValue(0x1);
			msb.setValue(0x2);
			msc.setValue(0x1);
			addrSel.setValue(0x3);
			state = 0x0;
		} else if (state == 0x2C) {
			// BEQ addr state2C
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			pcld.setValue(0x2);
			state = 0x0;
		} else if (state == 0x2D) {
			// BEQ addr state2D
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			state = 0x0;
		} else if  (state == 0x2E) {
			// BNE addr state 2E
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			pcld.setValue(0x2);
			state = 0x0;
		} else if (state == 0x2F) {
			// BNE addr state2F
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			state = 0x0;
		} else if (state == 0x30) {
			// BN addr state30
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			pcld.setValue(0x2);
			state = 0x0;
		} else if (state == 0x31) {
			// BN addr state31
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			state = 0x0;
		} else if (state == 0x32) {
			// BP addr state32
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			pcld.setValue(0x2);
			state = 0x0;
		} else if (state == 0x33) {
			// BP addr state33
			msa.setValue(0x1);
			msb.setValue(0x2);
			rw.setValue(0x1);
			regInc.setValue(0x8);
			state = 0x0;
		}
		tick();
	}

	private void tick() throws Exception {
		Int8 output = new Int8();
		Uint16 selectedAddr = new Uint16();

		// Get the selected address
		switch (addrSel.getValue()) {
		case 0x0:
			selectedAddr.setValue(pc);
			break;
		case 0x1:
			selectedAddr.setValue(mar);
			break;
		case 0x2:
			selectedAddr.setValue(x.sum(xDisp));
			break;
		case 0x3:
			selectedAddr.setValue(y.sum(yDisp));
			break;
		}

		// Get the output of the ALU
		switch (msc.getValue()) {
		case 0x0: // REGA => output
			output.setValue(a);
			break;
		case 0x1: // REGB => output
			output.setValue(b);
			break;
		case 0x2: // Sum(A, B) => output
			output.setValue(a.sum(b));
			break;
		case 0x3: // And(A, B) => output
			output.setValue(a.bitAnd(b));
			break;
		case 0x4: // Or(A, B) => output
			output.setValue(a.bitOr(b));
			break;
		case 0x5: // COMA => output
			output.setValue(a.complement());
			break;
		case 0x6: // COMB => output
			output.setValue(b.complement());
			break;
		case 0x7: // SHFA_Left => output
			output.setValue(a.leftShift(1));
			break;
		case 0x8: // SHFA_Right => output
			output.setValue(a.rightShift(1));
			break;
		case 0x9: // SHFB_Left => output
			output.setValue(b.leftShift(1));
			break;
		case 0xA: // SHFB_Right => output
			output.setValue(b.rightShift(1));
			break;
		}

		// Output to ram
		if (rw.equals(0x0)) {
			memory.setIndex(selectedAddr, output);
		}

		// Set A,B registers from each other. Note that because of the way that
		// the
		// CPU is designed, the outputs of the registers can be swapped
		// atomically.
		// This means that I have to be careful and use temporary variables here
		// to
		// ensure that the registers are properly swapped.
		Int8 newA = a, newB = b;
		if (msa.equals(0x2)) {
			newA = b;
		}
		if (msb.equals(0x1)) {
			newB = a;
		}
		a = newA;
		b = newB;

		// Set A,B registers from the output of the ALU
		if (msa.equals(0x3)) {
			a = output;
		}
		if (msb.equals(0x3)) {
			b = output;
		}

		// Set A,B,IR registers from the input bus
		if (irld.isActive()) {
			ir.setValue(memory.at(selectedAddr));
		}
		if (msa.equals(0x0)) {
			a.setValue(memory.at(selectedAddr));
		}
		if (msb.equals(0x0)) {
			b.setValue(memory.at(selectedAddr));
		}

		// Set X and Y Displacement Registers from input bus
		if (xDispLd.isActive()) {
			xDisp.setValue(memory.at(selectedAddr));
		}
		if (yDispLd.isActive()) {
			yDisp.setValue(memory.at(selectedAddr));
		}

		// Set PC, MAR, X, and Y from the input bus
		if (pcld.bitAnd(0x2) != 0) {
			// Set lower 8 bits of PC to input
			pc.setValue(pc.bitAnd(0xFF00));
			pc.setValue(pc.bitOr(memory.at(selectedAddr)));
		}
		if (pcld.bitAnd(0x1) != 0) {
			// Set upper 8 bits of PC to input
			pc.setValue(pc.bitAnd(0x00FF));
			pc.setValue(pc.bitOr(memory.at(selectedAddr).leftShift(8)));
		}
		if (marld.bitAnd(0x2) != 0) {
			// Set lower 8 bits of MAR to input
			mar.setValue(mar.bitAnd(0xFF00));
			mar.setValue(mar.bitOr(memory.at(selectedAddr)));
		}
		if (marld.bitAnd(0x1) != 0) {
			// Set upper 8 bits of MAR to input
			mar.setValue(mar.bitAnd(0x00FF));
			mar.setValue(mar.bitOr(memory.at(selectedAddr).leftShift(8)));
		}
		if (xld.bitAnd(0x2) != 0) {
			// Set lower 8 bits of X to input
			x.setValue(x.bitAnd(0xFF00));
			x.setValue(x.bitOr(memory.at(selectedAddr)));
		}
		if (xld.bitAnd(0x1) != 0) {
			// Set upper 8 bits of X to input
			x.setValue(x.bitAnd(0x00FF));
			x.setValue(x.bitOr(memory.at(selectedAddr).leftShift(8)));
		}
		if (yld.bitAnd(0x2) != 0) {
			// Set lower 8 bits of Y to input
			y.setValue(y.bitAnd(0xFF00));
			y.setValue(y.bitOr(memory.at(selectedAddr)));
		}
		if (yld.bitAnd(0x1) != 0) {
			// Set upper 8 bits of Y to input
			y.setValue(y.bitAnd(0x00FF));
			y.setValue(y.bitOr(memory.at(selectedAddr).leftShift(8)));
		}

		// Increment PC, MAR, X, and Y
		if (regInc.bitAnd(0x8) != 0) {
			pc.add(1);
		}
		if (regInc.bitAnd(0x4) != 0) {
			mar.add(1);
		}
		if (regInc.bitAnd(0x2) != 0) {
			x.add(1);
		}
		if (regInc.bitAnd(0x1) != 0) {
			y.add(1);
		}

	}

	public void reset() {
		state = 0;
		a.clear();
		b.clear();
		x.clear();
		y.clear();
		ir.clear();
		pc.clear();
		mar.clear();
		xDisp.clear();
		yDisp.clear();
		zero();
	}

	private void zero() {
		msa.clear();
		msb.clear();
		msc.clear();
		irld.clear();
		rw.clear();
		regInc.clear();
		addrSel.clear();
		pcld.clear();
		marld.clear();
		xld.clear();
		yld.clear();
		xDispLd.clear();
		yDispLd.clear();
	}

	private boolean negativeFlag() {
		return a.bitAnd(0x80) != 0;
	}
	
	private boolean zeroFlag() {
		return a.getValue() == 0;
	}
	
	public boolean ready() {
		return memory.ready();
	}
	
	public void setRom(Uint8[] rom) {
		memory.setRom(rom);
	}
	
	public void setRam(Uint8[] ram) {
		memory.setRam(ram);
	}

	public void setA(int value) {
		a.setValue(value);
	}

	public int getA() {
		return a.getValue();
	}

	public void setB(int value) {
		b.setValue(value);
	}

	public int getB() {
		return b.getValue();
	}

	public void setX(int value) {
		x.setValue(value);
	}

	public int getX() {
		return x.getValue();
	}

	public void setY(int value) {
		y.setValue(value);
	}

	public int getY() {
		return y.getValue();
	}

	public void setProgramCounter(int value) {
		pc.setValue(value);
	}

	public int getProgramCounter() {
		return pc.getValue();
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public String getRamContents() {
		return this.memory.printContents();
	}

}
