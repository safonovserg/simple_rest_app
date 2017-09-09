package com.app.config;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;


/**
 * Created by Sergey on 06.09.2017.
 */
public class TomcatLauncher {

    private volatile boolean terminated = false;
    private volatile boolean started = false;
    private Tomcat tomcat;

    public static void main(String[] args) throws Exception {
        new TomcatLauncher().start();
    }

    public synchronized void start() throws Exception {
        String contextPath = "";
        String appBase = ".";
        // call jar with -Dport=X
        String port = System.getProperty("port");
        if (port == null || port.isEmpty()) {
            port = "8080";
        }

        tomcat = new Tomcat();
        tomcat.setPort(Integer.valueOf(port));
        tomcat.getHost().setAppBase(appBase);
        Context context = tomcat.addWebapp(contextPath, appBase);

        final ResourceConfig application = new ResourceConfig()
                .packages("com.app.web");

        // also possible to use new ResourceConfig(MoneyController.class)
        Tomcat.addServlet(context, "jersey-container-servlet",
                          new ServletContainer(application));

        context.addServletMapping("/*", "jersey-container-servlet");


        //DI mapper
        context.addApplicationListener("com.app.config.DependencyMapper");

        //register guice
        final FilterDef guiceFilter = new FilterDef();
        guiceFilter.setFilterName("guice");
        guiceFilter.setFilterClass("com.google.inject.servlet.GuiceFilter");
        context.addFilterDef(guiceFilter);

        FilterMap filter1mapping = new FilterMap();
        filter1mapping.setFilterName("guice");
        filter1mapping.addURLPattern("/*");
        context.addFilterMap(filter1mapping);

        tomcat.start();
        //tomcat.getServer().await();
        started = true;
        await();
    }

    public void await() {
        while (!terminated) {
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void stop() {
        terminated = true;
        try {
            tomcat.stop();
            tomcat.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isStarted() {
        return started;
    }
}
