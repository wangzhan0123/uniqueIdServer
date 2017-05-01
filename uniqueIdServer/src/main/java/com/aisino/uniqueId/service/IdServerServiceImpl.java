package com.aisino.uniqueId.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aisino.uniqueId.api.IdServerService;
import com.aisino.uniqueId.api.Idreq;
import com.aisino.uniqueId.api.Idres;
import com.aisino.uniqueId.core.IdServerFactory;

@Path("getId")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
public class IdServerServiceImpl implements IdServerService{
	private static Logger logger = LogManager.getLogger(IdServerServiceImpl.class);

	@GET
	@Path("{busi:\\d+}")
	@Override
	public Idres uniqueIdbyGet(@PathParam("busi") long busi) {
		if(0L>busi || 15L<busi){
			busi = 0L;
		}
		return uniqueIdbyPost(new Idreq(busi));
	}

	@POST
	@Path("voidpost")
	@Override
	public Idres uniqueIdbyPost() {
		return uniqueIdbyPost(new Idreq(0L));
	}

	@POST
	@Path("parapost")
	@Override
	public Idres uniqueIdbyPost(Idreq idreq) {
		long begin = System.currentTimeMillis();
		if(0L>idreq.getBusi() || 15L<idreq.getBusi()){
			idreq.setBusi(0L);
		}
		long id = IdServerFactory.getInstance().nextId(idreq.getBusi());
		logger.info("create id {} cost {} ms",id,(System.currentTimeMillis() - begin));
		return new Idres(id);
	}
}
