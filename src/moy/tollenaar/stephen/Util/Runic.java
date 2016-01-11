package moy.tollenaar.stephen.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.meta.BookMeta;

public class Runic {
	private final static Map<String, String> RUNIC_ALPHABET = fillMap();

	public String translateToRunic(String normal) {
		String returning = "";
		if (normal.length() != 1) {
			for (int i = 0; i < normal.length(); i++) {
				if (RUNIC_ALPHABET.get(Character.toString(normal.charAt(i))
						.toUpperCase()) != null) {
					returning += RUNIC_ALPHABET.get(Character.toString(
							normal.charAt(i)).toUpperCase());
				} else if (Character.toString(normal.charAt(i)).equals(" ")) {
					returning += " ";
				} else if(Character.toString(normal.charAt(i)).equals(".")){
					returning += ".";
				}
			}
		} else {
			if (RUNIC_ALPHABET.get(normal.toUpperCase()) != null) {
				returning += RUNIC_ALPHABET.get(normal.toUpperCase());
			} else if (normal.equals(" ")) {
				returning += " ";
			} else if(normal.equals(".")){
				returning += ".";
			}
		}

		return returning;
	}

	public List<String> translateToRunic(List<String> normal){
		List<String> temp = new ArrayList<String>();
		for(String in : normal){
			temp.add(translateToRunic(in));
		}
		return temp;
	}
	public List<String> translateToNormal(List<String> runic){
		List<String> temp = new ArrayList<String>();
		for(String in : runic){
			temp.add(translateToNormale(in));
		}
		return temp;
	}
	
	public String translateToNormale(String runic) {
		String returning = "";
		for (int i = 0; i < runic.length(); i++) {
			String c = Character.toString(runic.charAt(i));
			boolean next = false;
			for (String in : RUNIC_ALPHABET.keySet()) {
				if (RUNIC_ALPHABET.get(in).equals(c)) {
					returning += in;
					next = true;
					break;
				}
			}
			if (!next) {
				returning += " ";
			}
		}
		return returning.toLowerCase();
	}
	
	public BookMeta translateBook(BookMeta in){
		BookMeta meta = in.clone();
		meta.setAuthor(translateToNormale(in.getAuthor()));
		meta.setTitle(translateToNormale(in.getTitle()));
		for(int i  =0; i < in.getPages().size(); i++){
			meta.setPage(i+1, translateToNormale(in.getPage(i+1)));
		}
		return meta;
	}
	
	public boolean isRunic(String in){
		if(in.length() != 1){
			String begin = Character.toString(in.charAt(0));
			for(String alpha : RUNIC_ALPHABET.keySet()){
				if(RUNIC_ALPHABET.get(alpha).equals(begin.toUpperCase())){
					return true;
				}
			}
		}else{
			for(String alpha : RUNIC_ALPHABET.keySet()){
				if(RUNIC_ALPHABET.get(alpha).equals(in.toUpperCase())){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isNormal(String in){
		if(in.length() != 1){
			String begin = Character.toString(in.charAt(0));
			if(RUNIC_ALPHABET.get(begin.toUpperCase()) != null){
				return true;
			}
		}else{
			if(RUNIC_ALPHABET.get(in.toUpperCase()) != null){
				return true;
			}
		}
		return false;
	}

	
	
	
	public int calcLvl(String title, int playerlvl){
		int lvl = 0;
		if(isRunic(title)){
			String translated = translateToNormale(title);
			String[] split = translated.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
			for(String in : split){
				try{
					int volume = Integer.parseInt(in);
					switch(volume){
					case 1:
						return (int) Math.ceil(17*Math.exp(1/49*-1.22378*(playerlvl-1)));
					case 2:
						return (int) Math.ceil(30*Math.exp(1/49*-1.09861*(playerlvl-1)));
					case 3:
						return (int) Math.ceil(60*Math.exp(1/49*-1.38629*(playerlvl-1)));
					}
					
					break;
				}catch(NumberFormatException ex){
					continue;
				}
			}
		}
		
		
		
		return lvl;
	}
	
	
	
	//all private stuff
	private static Map<String, String> fillMap() {
		Map<String, String> filler = new HashMap<String, String>();
		for (int i = 1; i < 37; i++) {
			filler.put(getLetter(i), getRunic(i));
		}
		return filler;
	}

	private static String getRunic(int place) {
		switch (place) {
		case 1:
			return "\u16AB";
		case 2:
			return "\u16D2";
		case 3:
			return "\u16B2";
		case 4:
			return "\u16E5";
		case 5:
			return "\u16D6";
		case 6:
			return "\u16A0";
		case 7:
			return "\u16B7";
		case 8:
			return "\u16BA";
		case 9:
			return "\u16C1";
		case 10:
			return "\u16C3";
		case 11:
			return "\u16B3";
		case 12:
			return "\u16DA";
		case 13:
			return "\u16D7";
		case 14:
			return "\u16AC";
		case 15:
			return "\u16DF";
		case 16:
			return "\u16C8";
		case 17:
			return "\u16B8";
		case 18:
			return "\u16B1";
		case 19:
			return "\u16CD";
		case 20:
			return "\u16CF";
		case 21:
			return "\u16A2";
		case 22:
			return "\u16B9";
		case 23:
			return "\u16E9";
		case 24:
			return "\u16E0";
		case 25:
			return "\u16E3";
		case 26:
			return "\u16D8";
		case 27:
			return "\u16C6";
		case 28:
			return "\u16BF";
		case 29:
			return "\u16AD";
		case 30:
			return "\u16B0";
		case 31:
			return "\u16AE";
		case 32:
			return "\u16AF";
		case 33:
			return "\u16BC";
		case 34:
			return "\u16D9";
		case 35:
			return "\u16BD";
		case 36:
			return "\u16DB";
		default:
			return "\u16E4";

		}
	}

	private static String getLetter(int place) {
		switch (place) {
		case 1:
			return "A";
		case 2:
			return "B";
		case 3:
			return "C";
		case 4:
			return "D";
		case 5:
			return "E";
		case 6:
			return "F";
		case 7:
			return "G";
		case 8:
			return "H";
		case 9:
			return "I";
		case 10:
			return "J";
		case 11:
			return "K";
		case 12:
			return "L";
		case 13:
			return "M";
		case 14:
			return "N";
		case 15:
			return "O";
		case 16:
			return "P";
		case 17:
			return "Q";
		case 18:
			return "R";
		case 19:
			return "S";
		case 20:
			return "T";
		case 21:
			return "U";
		case 22:
			return "V";
		case 23:
			return "W";
		case 24:
			return "X";
		case 25:
			return "Y";
		case 26:
			return "Z";
		case 27:
			return "0";
		case 28:
			return "1";
		case 29:
			return "2";
		case 30:
			return "3";
		case 31:
			return "4";
		case 32:
			return "5";
		case 33:
			return "6";
		case 34:
			return "7";
		case 35:
			return "8";
		case 36:
			return "9";
		default:
			return ".";

		}

	}

}
