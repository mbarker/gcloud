package com.ni.gcloud.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @author mbarker
 */
@Path("/secure")
public class SecureService {

	@GET
	@Path("ping")
	public String ping() {
		return "SecureService pong ... " + System.currentTimeMillis();
	}
}
