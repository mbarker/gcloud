package com.ni.gcloud.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @author mbarker
 */
@Path("/unsecure")
public class UnsecureService {

	@GET
	@Path("ping")
	public String ping() {
		return "UnsecureService pong ... " + System.currentTimeMillis();
	}
}
