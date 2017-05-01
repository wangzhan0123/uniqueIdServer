package com.aisino.uniqueId.util;

import java.io.IOException;
import java.util.Properties;


public class SystemConfig {
	
	public static long twepoch = 1479437792365L;
	public static long busiIdBits = 4L;
	public static long datacenterIdBits = 2L;
	public static long workerIdBits = 4L;
	public static long sequenceBits =12L;
	public static long datacenterId = 0L;
	public static long workerId = 0L;
	
	
	static{
		Properties pro = new Properties();
		try {
			pro.load(SystemConfig.class.getResourceAsStream("/config.properties"));
			twepoch = Long.parseLong(pro.getProperty("twepoch"));
			datacenterIdBits = Long.parseLong(pro.getProperty("datacenterIdBits"));
			workerIdBits = Long.parseLong(pro.getProperty("workerIdBits"));
			sequenceBits = Long.parseLong(pro.getProperty("sequenceBits"));
			busiIdBits = Long.parseLong(pro.getProperty("busiIdBits"));
			datacenterId = Long.parseLong(pro.getProperty("datacenterId"));
			workerId = Long.parseLong(pro.getProperty("workerId"));
			if(0L>datacenterId || 3L<datacenterId){
				throw new IllegalArgumentException(String.format(
						"datacenterId Id can't be greater than %d or less than 0",
						3));
			}
			if(0L>workerId || 15L < workerId){
				throw new IllegalArgumentException(String.format(
						"workerId Id can't be greater than %d or less than 0",
						15));
			}
		} catch (IOException e) {
			System.out.println("------------"+e+"-------");
		} finally{
			pro.clear();
		}
		
	}

	public static void main(String[] args) {
	}

}
