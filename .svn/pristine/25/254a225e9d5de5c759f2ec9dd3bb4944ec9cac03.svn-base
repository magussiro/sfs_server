package com.sfs.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataType;
import com.smartfoxserver.v2.entities.data.SFSObject;


public class Util {
	
    private static final Logger logger = LoggerFactory.getLogger("Extensions");
    
    private static HQRandom random = new HQRandom();
	private static SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void logTest(final String text) {
//    	logger.debug(text);
    }
    
    public static void logInfo(final String text) {
    	logger.info(text);
    }

    public static void logDebug(final String text) {
    	logger.debug(text);
    }

    public static void logWarn(final String text) {
    	logger.warn(text);
    }

    public static void logError(final String text) {
    	logger.error(text);
    }
 
    public static void logException(final Exception e) {
    	logger.error(e.getMessage());
		StackTraceElement[] elements = e.getStackTrace();
		for (int i=0;i<elements.length;++i) {
			logger.error(elements[i].toString());
		}
    }
    
    public static SFSObject copySFSObject(ISFSObject sfsObj)
    {
    	final byte[] bytes = sfsObj.toBinary(); 
    	final SFSObject obj = SFSObject.newFromBinaryData(bytes);
    	return obj;
    }

    public static int getRandom(final int range) {
        final int roll = random.nextInt(range);
    	return roll;
    }

    public static long getRandom() {
        final long roll = random.nextLong();
    	return roll;
    }

    public static float clamp(final float minValue, final float maxValue, final float value) {
    	if (value > maxValue)
    		return maxValue;
    	
    	if (value < minValue)
    		return minValue;
    	
    	return value;
    }
    
    public static int clamp(final int minValue, final int maxValue, final int value) {
    	if (value > maxValue)
    		return maxValue;
    	
    	if (value < minValue)
    		return minValue;
    	
    	return value;
    }
    
    public static void assertTest(final String msg, final boolean condition) {
    	if (condition == false) {
    		throw new AssertionError(msg);
    	}
    }
    
    public static String printDate(final Date date) {
    	return sdFormat.format(date);
    }

	public static long getObjLongValue(final ISFSObject obj, final String key) {
		return (obj.get(key).getTypeId() == SFSDataType.INT) ? obj.getInt(key) : obj.getLong(key);
	}
    
	public static void main(String[] args) {
	}
}
