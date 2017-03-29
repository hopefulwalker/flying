/*
  File: UUIDGenerator.java
  Originally written by Walker.

  Rivision History:
  Date         Who     Version  What
  2015.1.8     walker  0.1.0    retrive this from flygroup.
                                The perfermance is poor compare to java.util.UUID.
 */
package com.flying.util.uk;

import java.net.*;
import java.security.*;

import org.apache.commons.logging.*;

public final class UUIDGenerator {
	private static Log logger = LogFactory.getLog(UUIDGenerator.class);
	// length of ID
	private static int ID_LENGTH = 32;
	// single static instance
	private static UUIDGenerator me = new UUIDGenerator();
	// secure random to provide nonrepeating seed
	private SecureRandom seeder;
	// cached value for mid part of string
	private String midValue;

	// Constructor. Create ip + system hashcode.
	private UUIDGenerator() {
		try {
			// get the internet address
			InetAddress inet = InetAddress.getLocalHost();
			byte[] bytes = inet.getAddress();
			String hexInetAddress = hexFormat(getInt(bytes), 8);
			// get the hashcode for this object
			String thisHashCode = hexFormat(System.identityHashCode(this), 8);
			// set up mid value string
			this.midValue = hexInetAddress + thisHashCode;
			// load up the randomizer first
			seeder = new SecureRandom();
		} catch (Exception e) {
			logger.error(seeder, e);
		}
	}

	/**
	 * Get instance of UUID generator. Singleton class implemention
	 *
	 * @return Instance of this class
	 */
	public static UUIDGenerator getInstance() {
		return me;
	}

	/**
	 * @return the universally uniqueue ID
	 */
	public String getUUID() {
		long timeNow = System.currentTimeMillis();
		// get int value as unsigned
		int timeLow = (int) timeNow & 0xFFFFFFFF;
		// get next random value
		int node = seeder.nextInt();
		return (hexFormat(timeLow, 8) + midValue + hexFormat(node, 8)).toUpperCase();
	}

	/**
	 * @return the length of UUID.
	 */
	public static int getUUIDLength() {
		return ID_LENGTH;
	}

	private int getInt(byte ipByte[]) {
		int i = 0;
		int j = 24;
		for (int k = 0; j >= 0; k++) {
			int l = ipByte[k] & 0xff;
			i += l << j;
			j -= 8;
		}
		return i;
	}

	private String hexFormat(int value, int fixedLength) {
		String s = Integer.toHexString(value);
		return padHex(s, fixedLength) + s;
	}

	private String padHex(String value, int fixedLength) {
		StringBuffer stringbuffer = new StringBuffer();
		if (value.length() < fixedLength) {
			for (int i = 0; i < fixedLength - value.length(); i++) {
				stringbuffer.append("0");
			}
		}
		return stringbuffer.toString();
	}
}