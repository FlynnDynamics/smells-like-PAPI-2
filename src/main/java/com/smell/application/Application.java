package com.smell.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * @author FlynnDynamics
 * @version ${version}
 * @since 24/04/24
 */
@SpringBootApplication
@Theme(value = "mytodo")
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        openBrowser();
    }

    private static void openBrowser() {
        String os = System.getProperty("os.name").toLowerCase();
        Runtime rt = Runtime.getRuntime();
        String url = "http://localhost:8080";

        try {
            if (os.contains("win")) {
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (os.contains("mac")) {
                rt.exec("open " + url);
            } else if (os.contains("nix") || os.contains("nux")) {
                String[] browsers = {"chrome", "firefox", "mozilla", "konqueror", "epiphany", "opera", "midori", "vivaldi"};
                String browser = null;
                for (String b : browsers) {
                    if (browser == null && rt.exec(new String[]{"which", b}).getInputStream().read() != -1) {
                        rt.exec(new String[]{b, url});
                        browser = b;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
