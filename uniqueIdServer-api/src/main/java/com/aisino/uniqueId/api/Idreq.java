package com.aisino.uniqueId.api;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Idreq implements Serializable{
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
	}
	
	//获取id时请提供业务编码，如采集为1，签章为2，开票为3，未知填0，最大为15
	private long busi;

	public long getBusi() {
		return busi;
	}

	public void setBusi(long busi) {
		this.busi = busi;
	}


	public Idreq(long busi) {
		super();
		this.busi = busi;
	}

	@Override
	public String toString() {
		return "Idreq [busi=" + busi + "]";
	}

	public Idreq(){
		
	}
	
	
}
