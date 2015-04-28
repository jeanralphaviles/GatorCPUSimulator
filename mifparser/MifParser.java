package mifparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import simulator.registers.Uint8;

public class MifParser {
	public static Pattern depthRegex = Pattern.compile("DEPTH",
			Pattern.CASE_INSENSITIVE);
	public static Pattern addrRegex = Pattern.compile("ADDRESS_RADIX",
			Pattern.CASE_INSENSITIVE);
	public static Pattern dataRegex = Pattern.compile("DATA_RADIX",
			Pattern.CASE_INSENSITIVE);
	public static Pattern beginRegex = Pattern.compile("BEGIN",
			Pattern.CASE_INSENSITIVE);
	public static Pattern endRegex = Pattern.compile("END",
			Pattern.CASE_INSENSITIVE);
	public static Pattern addrRangeRegex = Pattern
			.compile("\\[[0-9A-Fa-f]+\\.\\.[0-9A-Fa-f]+\\]");

	public static Uint8[] parseMif(File file) throws Exception {
		int depth = 4096, addrRadix = 16, dataRadix = 16;
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		// Get MetaData about the MIF File before "BEGIN" is encountered
		// Note that everything has to be on separate lines. Also this
		// parser doesn't care if someone defines a value twice. It'll
		// just overwrite the previously stored value. Since we are only
		// dealing with the GCPU, we only care about the radixes and the
		// depth of the ram.
		while ((line = br.readLine()) != null) {
			line = trimComments(line);
			line = line.trim();
			if (depthRegex.matcher(line).find()) {
				String number = substring(line, '=', ';');
				depth = Integer.parseInt(number);
				if (depth > 4096) {
					br.close();
					throw new Exception("MIF depth too large, DEPTH must be less or equal to 4096");
				} else if (depth <= 0) {
					br.close();
					throw new Exception("MIF depth is <= 0");
				}
			} else if (addrRegex.matcher(line).find()) {
				String radix = substring(line, '=', ';').toUpperCase();
				switch (radix) {
				case "BIN":
					addrRadix = 2;
					break;
				case "DEC":
					addrRadix = 10;
					break;
				case "HEX":
					addrRadix = 16;
					break;
				default:
					throw new Exception("Unknown address radix");
				}
			} else if (dataRegex.matcher(line).find()) {
				String radix = substring(line, '=', ';').toUpperCase();
				switch (radix) {
				case "BIN":
					dataRadix = 2;
					break;
				case "DEC":
					dataRadix = 10;
					break;
				case "HEX":
					addrRadix = 16;
					break;
				default:
					throw new Exception("Unknown data radix");
				}
			}
			if (beginRegex.matcher(line).find()) {
				break;
			}
		}
		// Create memory of appropriate size and initialize
		Uint8[] memory = new Uint8[depth];
		for(int i = 0; i < depth; ++i) {
			memory[i] = new Uint8();
		}
		// Fill out memory
		while ((line = br.readLine()) != null) {
			line = trimComments(line);
			line = line.trim();
			if (endRegex.matcher(line).find()) {
				break;
			} else if (!line.isEmpty()) {
				// Fill the specified address or address range
				ArrayList<Integer> addresses = getAddressRange(line, addrRadix);
				int value = Integer.parseInt(substring(line, ':', ';'),
						dataRadix);
				for (int address : addresses) {
					if (address < 0 || address >= depth) {
						br.close();
						throw new Exception("Invalid address encountered");
					}
					memory[address] = new Uint8(value);
				}
			}
		}
		br.close();
		return memory;
	}

	private static String substring(String string, char lowerBound,
			char upperBound) {
		if (string.indexOf(lowerBound) >= string.indexOf(upperBound)) {
			return "";
		} else {
			return string.substring(string.indexOf(lowerBound) + 1,
					string.indexOf(upperBound)).trim();
		}
	}

	private static String trimComments(String line) {
		StringBuilder sb = new StringBuilder();
		boolean inComment = false;
		for (int i = 0; i < line.length(); ++i) {
			if (i < line.length() - 1 && line.substring(i, i + 2).equals("--")) {
				return sb.toString();
			}
			char c = line.charAt(i);
			if (c == '%') {
				inComment = !inComment;
			} else if (!inComment) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	private static ArrayList<Integer> getAddressRange(String line, int addrRadix) {
		ArrayList<Integer> range = new ArrayList<Integer>();
		if (addrRangeRegex.matcher(line).find()) {
			// Check if any [X..Y] sections exists
			int lower = Integer.parseInt(substring(line, '[', '.'), addrRadix);
			int upper = Integer.parseInt(
					line.substring(line.indexOf("..") + 2, line.indexOf(']')),
					addrRadix);
			for (int i = lower; i <= upper; ++i) {
				range.add(i);
			}
		} else if (!line.isEmpty()) {
			// Single address
			range.add(Integer.parseInt(line.substring(0, line.indexOf(":"))
					.trim(), addrRadix));
		}
		return range;
	}

}
