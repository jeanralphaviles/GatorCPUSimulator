# Gator-CPU Simulator for Digital Logic

* * *

## Summary

This program simulates the Gator-CPU that is used in EEL 3701\. With it you can simulate the execution of your programs, step by step, and see the results in a nicer format then you can in Quartus. It mimics the internal state-machine of the GCPU exactly to allow the user to see the values of the registers at every state.
## Usage

To use, first load a valid .mif file for the EPROM and another for the SRAM. Then, if there were no errors in parsing, you can start ticking the processor and watch the registers change. At any point, you can replace a .mif file, reset the memory to the starting point, or dump the contents of the SRAM into a .mif format to check the operation of your program.
## Valid .mif Files

A valid mif file is any file that has a _depth_ attribute of at most 4096 (0x1000), which is the maximum size for the GCPU, and has radixes of either (BIN, DEC, or HEX). Note that setting a rom location twice in a .mif file will result in the last definition being put into the rom and setting a value for a rom location outside the range of the depth will cause an error in parsing. Also, **all definitions in the .mif file must be put on their own line**. This includes radix lines, address set lines, depth lines, etc... and comments denoted with a pair of %'s and line comments denoted with --'s are allowed as long as they don't span multiple lines of the .mif file.
### Sample .mif file

`% File name = sram.mif %  
% ************************************* %  
DEPTH = 4096; % Address Bus Size %  
WIDTH = 8; % Data Bus Size %  

ADDRESS_RADIX = HEX; % Address Format %  
DATA_RADIX = HEX; % Data Format %  

CONTENT  
BEGIN  
[0..7FF] : FF; % zero memory %  
800 : 02;   
801 : 37;   
802 : 00;   
803 : 9D;   
804 : 9D;   
805 : 02;   
806 : 02;   
807 : 02;   
808 : 02;   
809 : 02;   
80A : 02;   
80B : 00;   
[80C..FFF] : 00;  

END;`
## Known Issues

*   Trying to execute an opcode that does not exist will cause the program to stop responding to clock ticks. Fix the issue, load a new EPROM, and reset to continue.
*   Loading a file that is not a valid .mif file may work sometimes, the rom will be loaded and filled with all zeros in that case.
*   Trying to access a memory location that is outside the range of 0x0000 - 0x1FFF will cause an error and trying to write to a memory location in EPROM 0x0000-0x0FFF won't work. These aren't issues, they're how the CPU is supposed to work, but they might throw someone off.
