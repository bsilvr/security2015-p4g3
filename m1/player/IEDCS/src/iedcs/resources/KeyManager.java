package iedcs.resources;

import iedcs.resources.Location.SNMac;
import iedcs.resources.Location.SNUnix;
import iedcs.resources.Location.SNWindows;

public class KeyManager {

	private static String deviceKey = "";
	private static String playerKey = "kR5OjIhyGFLT7hSk";
	private static String IV = "oObVMqPyzcRzWvyB";
	private static byte[] random = new byte[16];
	private static byte[] key = new byte[16];
	private static byte[] fileKey = new byte[16];

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
	}

	public static void setFileKey(byte[] fileKeyStr){
		fileKey = fileKeyStr;
	}
	public static byte[] getFileKey(){
		return fileKey;
	}

	public static void setKey(byte[] keyStr){
		key = keyStr;
	}
	public static byte[] getKey(){
		return key;
	}
	public static void setRandom(byte[] randomStr){
		random = randomStr;
	}
	public static byte[] getRandom(){
		return random;
	}
	public static String getDeviceKey(){
		return deviceKey+"00000000";
	}
	public static String getPlayerKey(){
		return playerKey;
	}
	public static String getIV(){
		return IV;
	}

}
