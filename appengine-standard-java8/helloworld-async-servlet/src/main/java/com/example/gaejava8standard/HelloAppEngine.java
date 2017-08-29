/**
 * Copyright 2017 Google Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.gaejava8standard;

// [START example]

import com.google.appengine.api.ThreadManager;
import com.google.appengine.api.utils.SystemProperty;

import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// With @WebServlet annotation the webapp/WEB-INF/web.xml is no longer required.
@WebServlet(asyncSupported = true, name = "HelloAppEngine", value = "/hello")
public class HelloAppEngine extends HttpServlet {

    //    private static ExecutorService executor = Executors.newFixedThreadPool(5);
    private static ExecutorService executor = Executors.newFixedThreadPool(5, ThreadManager.backgroundThreadFactory());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        AsyncContext asyncContext = request.startAsync();
        CompletableFuture.supplyAsync(() -> {
            Properties properties = System.getProperties();
            asyncContext.getResponse().setContentType("text/plain");
            try {
                asyncContext.getResponse().getWriter().println("Hello App Engine - Standard using "
                        + SystemProperty.version.get() + " Java " + properties.get("java.specification.version"));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                asyncContext.complete();
                return null;
            }
        }, executor);
    }

    public static String getInfo() {
        return "Version: " + System.getProperty("java.version")
                + " OS: " + System.getProperty("os.name")
                + " User: " + System.getProperty("user.name");
    }

}
// [END example]
