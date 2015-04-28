package simulator;

import java.awt.ComponentOrientation;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import mifparser.MifParser;
import simulator.registers.Uint8;

@SuppressWarnings("serial")
public class Simulator extends JFrame {
	private Cpu cpu = new Cpu();
	private Uint8[] eprom;
	private Uint8[] sram;
	private JPanel contentPane;
	private JTextField regA;
	private JTextField txtRegb;
	private JTextField txtRegx;
	private JTextField txtRegy;
	private JTextField txtPc;
	private JTextField txtState;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Simulator frame = new Simulator();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Simulator() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setTitle("G-CPU Simulator");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(18, 29, 183, 193);
		contentPane.add(panel);
		panel.setLayout(new GridLayout(6, 2, 0, 10));
		panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		JLabel lblA = new JLabel("A:");
		panel.add(lblA);
		
		regA = new JTextField();
		regA.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (cpu.ready()) {
					return;
				}
				try {
					Simulator.this.cpu.setA(Integer.parseInt(Simulator.this.regA.getText()));
				} catch(NumberFormatException exception) {
				}
			}
		});
		regA.setText("-");
		panel.add(regA);
		regA.setColumns(10);
		
		JLabel lblB = new JLabel("B:");
		panel.add(lblB);
		
		txtRegb = new JTextField();
		txtRegb.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!cpu.ready()) {
					return;
				}
				try {
					Simulator.this.cpu.setB(Integer.parseInt(Simulator.this.txtRegb.getText()));
				} catch(NumberFormatException exception) {
				}
			}
		});
		txtRegb.setText("-");
		panel.add(txtRegb);
		txtRegb.setColumns(10);
		
		JLabel lblX = new JLabel("X:");
		panel.add(lblX);
		
		txtRegx = new JTextField();
		txtRegx.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!cpu.ready()) {
					return;
				}
				try {
					Simulator.this.cpu.setX(Integer.parseInt(Simulator.this.txtRegx.getText()));
				} catch(NumberFormatException exception) {
				}
			}
		});
		txtRegx.setText("-");
		panel.add(txtRegx);
		txtRegx.setColumns(10);
		
		JLabel lblY = new JLabel("Y:");
		panel.add(lblY);
		
		txtRegy = new JTextField();
		txtRegy.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!cpu.ready()) {
					return;
				}
				try {
					Simulator.this.cpu.setY(Integer.parseInt(Simulator.this.txtRegy.getText()));
				} catch(NumberFormatException exception) {
				}
			}
		});
		txtRegy.setText("-");
		panel.add(txtRegy);
		txtRegy.setColumns(10);
		
		JLabel lblPc = new JLabel("P.C.");
		panel.add(lblPc);
		
		txtPc = new JTextField();
		txtPc.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!cpu.ready()) {
					return;
				}
				try {
					Simulator.this.cpu.setProgramCounter(Integer.parseInt(Simulator.this.txtPc.getText()));
				} catch(NumberFormatException exception) {
				}
			}
		});
		txtPc.setText("-");
		panel.add(txtPc);
		txtPc.setColumns(10);
		
		JLabel lblState = new JLabel("State");
		panel.add(lblState);
		
		txtState = new JTextField();
		txtState.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!cpu.ready()) {
					return;
				}
				try {
					Simulator.this.cpu.setState(Integer.parseInt(Simulator.this.txtState.getText()));
				} catch(NumberFormatException exception) {
				}
			}
		});
		txtState.setText("-");
		panel.add(txtState);
		txtState.setColumns(10);
		
		JLabel lblRegisters = new JLabel("Registers");
		lblRegisters.setBounds(18, 6, 58, 16);
		contentPane.add(lblRegisters);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(202, 29, 242, 152);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JButton btnLoadMif = new JButton("Load EPROM");
		btnLoadMif.setBounds(0, 0, 121, 29);
		panel_1.add(btnLoadMif);
		
		JButton btnClockTick = new JButton("Clock Tick");
		btnClockTick.setBounds(0, 82, 242, 29);
		panel_1.add(btnClockTick);
		
		JButton btnReset = new JButton("Reset");
		btnReset.setBounds(0, 41, 121, 29);
		panel_1.add(btnReset);
		
		JButton btnExit = new JButton("Exit");
		btnExit.setBounds(121, 123, 121, 29);
		panel_1.add(btnExit);
		
		JButton btnShowRam = new JButton("Show SRAM");
		btnShowRam.setBounds(121, 41, 121, 29);
		panel_1.add(btnShowRam);
		
		JButton btnLoadSram = new JButton("Load SRAM");
		btnLoadSram.setBounds(121, 0, 121, 29);
		panel_1.add(btnLoadSram);
		
		JButton btnHelp = new JButton("Help");
		btnHelp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFrame frame = new JFrame("Help");
				JTextPane text = new JTextPane();
				text.setContentType("text/html");
				text.setText("<h1>Gator-CPU Simulator for Digital Logic</h1>\r\n<hr>\r\n<h2>Summary</h2>\r\nThis program simulates the Gator-CPU that is used in EEL 3701. With it you can simulate the execution of your programs, step by step, and see the results in a nicer format then you can in Quartus. It mimics the internal state-machine of the GCPU exactly to allow the user to see the values of the registers at every state.\r\n<h2>Usage</h2>\r\nTo use, first load a valid .mif file for the EPROM and another for the SRAM. Then, if there were no errors in parsing, you can start ticking the processor and watch the registers change. At any point, you can replace a .mif file, reset the memory to the starting point, or dump the contents of the SRAM into a .mif format to check the operation of your program. \r\n<h2>Valid .mif Files</h2>\r\nA valid mif file is any file that has a <i>depth</i> attribute of at most 4096 (0x1000), which is the maximum size for the GCPU, and has radixes of either (BIN, DEC, or HEX).  Note that setting a rom location twice in a .mif file will result in the last definition being put into the rom and setting a value for a rom location outside the range of the depth will cause an error in parsing. Also, <b>all definitions in the .mif file must be put on their own line</b>. This includes radix lines, address set lines, depth lines, etc...  and comments denoted with  a pair of %'s and line comments denoted with --'s are allowed as long as they don't span multiple lines of the .mif file.\r\n<h3>Sample .mif file</h3>\r\n<code>\r\n% File name = sram.mif\t\t\t\t\t%<br>\r\n% ************************************* %<br>\r\nDEPTH = 4096;\t% Address Bus Size %<br>\r\nWIDTH = 8;\t% Data Bus Size %<br>\r\n<br>\r\nADDRESS_RADIX \t= HEX;\t% Address Format %<br>\r\nDATA_RADIX \t\t= HEX;\t% Data Format %<br>\r\n<br>\r\nCONTENT<br>\r\nBEGIN<br>\r\n[0..7FF]\t:\tFF; \t% zero memory \t%<br>\r\n800\t\t\t:\t02;\t<br>\r\n801\t\t\t:\t37;\t<br>\r\n802\t\t\t:\t00;\t<br>\r\n803\t\t\t:\t9D;\t<br>\r\n804\t\t\t:\t9D;\t<br>\r\n805\t\t\t:\t02;\t<br>\r\n806\t\t\t:\t02;\t<br>\r\n807\t\t\t:\t02;\t<br>\r\n808\t\t\t:\t02;\t<br>\r\n809\t\t\t:\t02;\t<br>\r\n80A\t\t\t:\t02;\t<br>\r\n80B\t\t\t:\t00;\t<br>\r\n[80C..FFF]\t:\t00;<br>\r\n<br>\r\nEND;\r\n</code>\r\n<h2>Known Issues</h2>\r\n<ul>\r\n<li>Trying to execute an opcode that does not exist will cause the program to stop responding to clock ticks. Fix the issue, load a new EPROM, and reset to continue.</li>\r\n<li>Loading a file that is not a valid .mif file may work sometimes, the rom will be loaded and filled with all zeros in that case.</li>\r\n<li>Trying to access a memory location that is outside the range of 0x0000 - 0x1FFF will cause an error and trying to write to a memory location in EPROM 0x0000-0x0FFF won't work. These aren't issues, they're how the CPU is supposed to work, but they might throw someone off.</li>\r\n</ul>\r\n<hr>\r\n<br>\r\n<address>\r\nWritten by Jean-Ralph Aviles - 4/28/2015\r\n<br>\r\n<a href=\"https://github.com/jeanralphaviles\">github@jeanralphaviles</a>\r\n<br>\r\n<a href>jeanralph.aviles@gmail.com</a>\r\n</address>");
				JScrollPane scrollPane = new JScrollPane(text);

				text.setEditable(false);
				text.setCaretPosition(0);
				frame.setBounds(0, 0, 750, 400);
				frame.getContentPane().add(scrollPane);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
		btnHelp.setBounds(0, 123, 121, 29);
		panel_1.add(btnHelp);
		btnLoadSram.addMouseListener(new MouseAdapter() {
			final JFileChooser fc = new JFileChooser();
			@Override
			public void mouseClicked(MouseEvent e) {
				int retVal = fc.showOpenDialog(Simulator.this);
				if (retVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					try {
						sram = MifParser.parseMif(file);
						refreshMemory();
						refreshLabels();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(Simulator.this, "Error parsing MIF");
						e1.printStackTrace();
					}
				}
			}
		});
		btnShowRam.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (sram != null) {
					JFrame frame = new JFrame("Viewing Ram Contents");
					JEditorPane editor = new JEditorPane("text/plain", cpu.getRamContents());
					JScrollPane scrollPane = new JScrollPane(editor);

					editor.setEditable(false);
					editor.setCaretPosition(0);
					frame.setBounds(100, 100, 450, 300);
					frame.getContentPane().add(scrollPane);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(Simulator.this, "Ram not loaded");
				}
			}
		});
		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		btnReset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (cpu != null) {
					cpu.reset();
					refreshMemory();
					refreshLabels();
				}
			}
		});
		btnClockTick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnClockTick.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (cpu.ready()) {
					try {
						cpu.execute();
					} catch (Exception e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(Simulator.this, e1.getMessage());
					}
					refreshLabels();
				} else {
					JOptionPane.showMessageDialog(Simulator.this, "You must load a ram and rom first");
				}
			}
		});
		btnLoadMif.addMouseListener(new MouseAdapter() {
			final JFileChooser fc = new JFileChooser();
			@Override
			public void mouseClicked(MouseEvent e) {
				int retVal = fc.showOpenDialog(Simulator.this);
				if (retVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					try {
						eprom = MifParser.parseMif(file);
						refreshMemory();
						refreshLabels();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(Simulator.this, "Error parsing MIF");
						e1.printStackTrace();
					}
				}
			}
		});
		
		JLabel lblWrittenByJeanralph = new JLabel("Written by Jean-Ralph Aviles - '16");
		lblWrittenByJeanralph.setBounds(6, 256, 214, 16);
		contentPane.add(lblWrittenByJeanralph);
		
		refreshLabels();
	}
	
	private void refreshMemory() {
		if (cpu != null) {
			if (eprom != null) {
				cpu.setRom(eprom);
			}
			if (sram != null) {
				cpu.setRam(sram);
			}
		}
	}
	
	private void refreshLabels() {
		if (cpu != null) {
			try {
				regA.setText(Integer.toHexString(cpu.getA() & 0xFF).toUpperCase());
				txtRegb.setText(Integer.toHexString(cpu.getB() & 0xFF).toUpperCase());
				txtRegx.setText(Integer.toHexString((short)cpu.getX()).toUpperCase());
				txtRegy.setText(Integer.toHexString((short)cpu.getY()).toUpperCase());
				txtPc.setText(Integer.toHexString(cpu.getProgramCounter()).toUpperCase());
				txtState.setText(Integer.toHexString(cpu.getState()).toUpperCase());
			} catch (NullPointerException e) {
			}
		}
	}
}
