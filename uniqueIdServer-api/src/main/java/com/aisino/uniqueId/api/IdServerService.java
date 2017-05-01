package com.aisino.uniqueId.api;

public interface IdServerService {
	Idres uniqueIdbyGet(long busi);
	
	Idres uniqueIdbyPost();
	Idres uniqueIdbyPost(Idreq idreq);
	

}
