package com.github.hhhzzzsss.chathacks;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class ChathacksUtil {
	public static String asFormattedString(Text text) {
		StringBuilder sb = new StringBuilder();
		
		String prevStyleString = "";
		
		String textString = text.asString();
		if (!textString.isEmpty()) {
			String styleString = styleAsString(text.getStyle());
			sb.append(styleString);
			prevStyleString = styleString;
		}
		sb.append(textString);
		
		for (Text sibling : text.getSiblings()) {
			String siblingString = sibling.asString();
			if (!siblingString.isEmpty()) {
				String styleString = styleAsString(sibling.getStyle());
				if (!styleString.equals(prevStyleString)) {
					if (!prevStyleString.isEmpty()) {
						sb.append(Formatting.RESET);
					}
				}
				
				sb.append(styleString);
				prevStyleString = styleString;
			}
			
			sb.append(siblingString);
		}
		
		if (!prevStyleString.isEmpty()) {
			sb.append(Formatting.RESET);
		}
		
		return sb.toString();
	}
	
	public static String styleAsString(Style style) {
		if (style.isEmpty()) {
			return "";
		}
		else {
			StringBuilder sb = new StringBuilder();
			if (style.getColor() != null) {
				Formatting formatting = Formatting.byName(style.getColor().getName());
				if (formatting != null) {
					sb.append(formatting);
				}
			}
			if (style.isBold()) {
				sb.append(Formatting.BOLD);
			}
			if (style.isItalic()) {
				sb.append(Formatting.ITALIC);
			}
			if (style.isUnderlined()) {
				sb.append(Formatting.UNDERLINE);
			}
			if (style.isObfuscated()) {
				sb.append(Formatting.OBFUSCATED);
			}
			if (style.isStrikethrough()) {
				sb.append(Formatting.STRIKETHROUGH);
			}
			return sb.toString();
		}
	}
}
