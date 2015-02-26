## Reproducing Authentication Problem

From the root directory (this one) run the following:

```
mvn clean install
cd ear
mvn clean appengine:devserver
```

This should startup all 3 modules.

You should be able to see the modules deployed here:
http://localhost:8080/_ah/admin/modules

There should be 3 running:
 - default (requires any auth)
 - secure (requires admin)
 - unsecure (no auth required)

Default should always be on 8080, the other 2 ports are randomly chosen.

Open a new Incognito Window, and navigate to the following URL:
http://localhost:61515/

And see the page without being logged in.
If you try to access the secured page:
http://localhost:61514/

You will be asked to login. If you don't check admin when you login you should see an unauthorized page.


#### Testing Default Service access to Secured/Unsecured
Test the ping urls:
 - http://localhost:8080/default/ping
 - http://localhost:61514/secure/ping
 - http://localhost:61515/unsecure/ping

Test the unsecure ping url through default service:
 - http://localhost:8080/default/ping/unsecure
 - http://localhost:8080/default/ping/unsecure/urlfetch

Test the secured ping url through default service:
 - http://localhost:8080/default/ping/secure
 - http://localhost:8080/default/ping/secure/urlfetch

When you look at the server logs, you should see something like:
```
[INFO] Feb 26, 2015 3:38:45 PM org.glassfish.jersey.filter.LoggingFilter log
[INFO] INFO: 6 * Server has received a request on thread 834954761@qtp-323705467-0
[INFO] 6 > GET http://localhost:8080/default/ping/secure
[INFO] 6 > Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
[INFO] 6 > Accept-Language: en-US,en;q=0.8
[INFO] 6 > Cache-Control: max-age=0
[INFO] 6 > Cookie: dev_appserver_login=test@example.com:true:18580476422013912411
[INFO] 6 > Host: localhost:8080
[INFO] 6 > User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36
[INFO]
[INFO] Feb 26, 2015 3:38:45 PM com.google.apphosting.utils.jetty.AppEngineAuthentication$AppEngineAuthenticator authenticate
[INFO] INFO: Got /secure/ping but no one was logged in, redirecting.
[INFO] Feb 26, 2015 3:38:45 PM org.glassfish.jersey.filter.LoggingFilter log
[INFO] INFO: 6 * Server responded with a response on thread 834954761@qtp-323705467-0
[INFO] 6 < 200
[INFO] 6 < Content-Type: text/html
[INFO]

...

[INFO] Feb 26, 2015 3:39:13 PM org.glassfish.jersey.filter.LoggingFilter log
[INFO] INFO: 7 * Server has received a request on thread 834954761@qtp-323705467-0
[INFO] 7 > GET http://localhost:8080/default/ping/secure/urlfetch
[INFO] 7 > Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
[INFO] 7 > Accept-Language: en-US,en;q=0.8
[INFO] 7 > Cookie: dev_appserver_login=test@example.com:true:18580476422013912411
[INFO] 7 > Host: localhost:8080
[INFO] 7 > User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36
[INFO]
[INFO] Feb 26, 2015 3:39:13 PM com.google.apphosting.utils.jetty.AppEngineAuthentication$AppEngineAuthenticator authenticate
[INFO] INFO: Got /secure/ping but no one was logged in, redirecting.
[INFO] Feb 26, 2015 3:39:13 PM org.glassfish.jersey.filter.LoggingFilter log
[INFO] INFO: 7 * Server responded with a response on thread 834954761@qtp-323705467-0
[INFO] 7 < 200
[INFO] 7 < Content-Type: text/html
[INFO]

```

As you can see, there is no ```X-Appengine-Inbound-Appid``` header, as we would expect to see from the documentation at: https://cloud.google.com/appengine/docs/java/urlfetch/#Java_Making_requests