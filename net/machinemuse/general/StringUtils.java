package net.machinemuse.general;

import java.text.DecimalFormat;

public abstract class StringUtils {
	public static final char[] smallSuffixes = { 'm', 'u', 'n', 'p', 'f', 'a',
			'z', 'y' };
	public static final char[] bigSuffixes = { 'k', 'M', 'G', 'T', 'P', 'E',
			'Z', 'Y' };

	public static String formatNumberShort(double number) {
		DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
		format.setMinimumIntegerDigits(1);
		format.setMaximumFractionDigits(2);
		format.applyPattern("##0.##E0");

		String[] exploded = format.format(number).split("E");
		String retval = exploded[0];
		if (retval.length() > 3) {
			if (retval.charAt(3) == '.') {
				retval = retval.substring(0, 3);
			} else {
				retval = retval.substring(0, 4);
			}
		}
		int exponent = Integer.parseInt(exploded[1]);
		if (exponent > 0) {
			retval += bigSuffixes[exponent / 3 - 1];
		} else if (exponent < 0) {
			retval += smallSuffixes[exponent / -3 - 1];
		}
		return retval;
	}

	public static String prependFormatTag(String str, char format) {
		return "\u00a7" + format + str;
	}

	public static String appendResetTag(String str) {
		return str + "\u00a7r";
	}

	public static String wrapFormatTags(String str, char format) {
		return appendResetTag(prependFormatTag(str, format));
	}

	public static String wrapFormatTags(String str, FormatCodes code) {
		return wrapFormatTags(str, code.character);
	}

	public static String wrapMultipleFormatTags(String str, Object... tags) {
		for (Object tag : tags) {
			if (tag instanceof Character) {
				str = prependFormatTag(str, (Character) tag);
			} else if (tag instanceof FormatCodes) {
				str = prependFormatTag(str, ((FormatCodes) tag).character);
			}
		}
		return appendResetTag(str);
	}

	public static enum FormatCodes {
		Black('0'),
		DarkBlue('1'),
		DarkGreen('2'),
		DarkAqua('3'),
		DarkRed('4'),
		Purple('5'),
		Gold('6'),
		Grey('7'),
		DarkGrey('8'),
		Indigo('9'),
		BrightGreen('a'),
		Aqua('b'),
		Red('c'),
		Pink('d'),
		Yellow('e'),
		White('f'),
		RandomChar('k'),
		Bold('l'),
		Strike('m'),
		Underlined('n'),
		Italic('o'),
		Reset('r');

		public char character;

		private FormatCodes(char character) {
			this.character = character;
		}
	}
}
