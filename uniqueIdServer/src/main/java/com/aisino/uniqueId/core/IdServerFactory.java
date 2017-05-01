package com.aisino.uniqueId.core;

import java.util.Arrays;

import com.aisino.uniqueId.util.SystemConfig;

public class IdServerFactory {
	private IdServerFactory(){};
	
	public static IdServer getInstance(){
		return IDServerHolder.instance;
	}

	private static class IDServerHolder{
		private static final IdServer instance = new IdServer(SystemConfig.datacenterId,SystemConfig.workerId);
	}
	public static void main(String[] args){
		int [] a = new int[10];
		Arrays.sort(a);
	}
	
}
