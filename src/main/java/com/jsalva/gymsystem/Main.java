package com.jsalva.gymsystem;

import com.jsalva.gymsystem.entity.*;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("GYM - CRM - SYSTEM -------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------");
        try {

            Tomcat tomcat = new Tomcat();
            tomcat.setPort(8080);

            tomcat.getConnector();

            String contextPath = "";
            String docBase = new File(".").getAbsolutePath();

            // This will automatically find and use your WebApplicationInitializer!
            tomcat.addWebapp(contextPath, docBase);

            tomcat.start();

            System.out.println("GYM - REST - API STARTED ON: http://localhost:8080" + contextPath);
            tomcat.getServer().await();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            System.out.println("Gym System Closed!");
        }
    }
}