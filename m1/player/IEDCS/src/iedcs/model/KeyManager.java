package iedcs.model;

import iedcs.resources.Location.SNMac;
import iedcs.resources.Location.SNUnix;
import iedcs.resources.Location.SNWindows;

public class KeyManager {

	private static String deviceKey = "";
	private static String playerKey = "kR5OjIhyGFLT7hSk";
	private static String playerVersion = "1.0.0";
	private static byte[] iv = new byte[16];
	private static byte[] random = new byte[16];
	private static byte[] key2 = new byte[16];
	private static byte[] key1 = new byte[16];
	private static byte[] fileKey = new byte[16];
	private static String book = null;

	public static void createDeviveKey(){

    	if (System.getProperties().getProperty("os.name").contains("Mac")) {
    		deviceKey = SNMac.getSerialNumber();
    		deviceKey = deviceKey.substring(Math.max(deviceKey.length() - 8, 0));
	    }
    	else if (System.getProperties().getProperty("os.name").contains("Windows")) {
    		deviceKey = SNWindows.getSerialNumber();
    		deviceKey = deviceKey.substring(Math.max(deviceKey.length() - 8, 0));
	    }
    	else if (System.getProperties().getProperty("os.name").contains("Linux")) {
    		deviceKey = SNUnix.getSerialNumber();
    		deviceKey = deviceKey.substring(Math.max(deviceKey.length() - 8, 0));
	    }
		else{
			System.exit(0);
		}
    	deviceKey += deviceKey;
    	System.out.print(deviceKey);

	}

	public static void setBook(String bookStr){
		book = bookStr;
	}
	public static String getBook(){
		return book;
	}

	public static void setFileKey(byte[] fileKeyStr){
		fileKey = fileKeyStr;
	}
	public static byte[] getFileKey(){
		return fileKey;
	}
	public static void setKey1(byte[] keyStr){
		key1 = keyStr;
	}
	public static byte[] getKey1(){
		return key1;
	}
	public static void setKey2(byte[] keyStr){
		key2 = keyStr;
	}
	public static byte[] getKey2(){
		return key2;
	}
	public static void setRandom(byte[] randomStr){
		random = randomStr;
	}
	public static byte[] getRandom(){
		return random;
	}
	public static String getDeviceKey(){
		return deviceKey;
	}
	public static String getPlayerKey(){
		return playerKey;
	}
	public static String getPlayerVersion(){
		return playerVersion;
	}
	public static void setIv(byte[] randomStr){
		iv = randomStr;
	}
	public static byte[] getIv(){
		return iv;
	}

}
