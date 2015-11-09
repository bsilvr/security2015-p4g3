package iedcs.resources;

import iedcs.resources.Location.SNMac;
import iedcs.resources.Location.SNUnix;
import iedcs.resources.Location.SNWindows;

public class KeyManager {

	private static String deviceKey = "";
	private static String playerKey = "kR5OjIhyGFLT7hSk";
	private static String IV = "oObVMqPyzcRzWvyB";

	public void createDeviveKey(){

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

	public static String getDeviceKey(){
		return deviceKey;
	}
	public static String getPlayerKey(){
		return playerKey;
	}
	public static String getIV(){
		return IV;
	}

}
