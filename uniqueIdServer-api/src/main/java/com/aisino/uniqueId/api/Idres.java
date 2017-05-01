package com.aisino.uniqueId.api;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Idres implements Serializable{
	//id生成器生成的id
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Idres [id=" + id + "]";
	}

	public Idres(long id) {
		super();
		this.id = id;
	}

	public Idres(){
	}
	
	
}
