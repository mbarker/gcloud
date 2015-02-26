package com.ni.gcloud.service;

import com.google.appengine.api.modules.ModulesService;
import com.google.appengine.api.modules.ModulesServiceFactory;
import com.google.appengine.api.urlfetch.*;

import org.apache.commons.io.IOUtils;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author mbarker
 */
@Path("/default")
public class DefaultService {

	private static final ModulesService MODULES = ModulesServiceFactory.getModulesService();
	private static final URLFetchService URL_FETCH = URLFetchServiceFactory.getURLFetchService();

	@GET
	@Path("ping")
	public String ping() {
		return "DefaultService pong ... " + System.currentTimeMillis();
	}


	// Following instructions from here: https://cloud.google.com/appengine/docs/java/urlfetch/#Java_Making_requests

	@GET
	@Path("ping/unsecure")
	public String pingUnsecure() throws Exception {
		URL url = makeURL("unsecure", "/unsecure/ping");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setInstanceFollowRedirects(false);
		return IOUtils.toString(conn.getInputStream());
	}

	@GET
	@Path("ping/unsecure/urlfetch")
	public String pingUnsecureUrlfetch() throws Exception {
		URL url = makeURL("unsecure", "/unsecure/ping");
		HTTPResponse resp = URL_FETCH.fetch(new HTTPRequest(url, HTTPMethod.GET, FetchOptions.Builder.doNotFollowRedirects()));
		return new String(resp.getContent());
	}

	@GET
	@Path("ping/secure")
	public String pingSecure() throws Exception {
		URL url = makeURL("secure", "/secure/ping");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setInstanceFollowRedirects(false);
		return IOUtils.toString(conn.getInputStream());
	}

	@GET
	@Path("ping/secure/urlfetch")
	public String pingSecureUrlFetch() throws Exception {
		URL url = makeURL("secure", "/secure/ping");
		HTTPResponse resp = URL_FETCH.fetch(new HTTPRequest(url, HTTPMethod.GET, FetchOptions.Builder.doNotFollowRedirects()));
		return new String(resp.getContent());
	}


	private URL makeURL(String module, String path) throws MalformedURLException {
		return new URL("http://" + MODULES.getVersionHostname(module, MODULES.getCurrentVersion()) + path);
	}
}
