package com.app.config;

import com.app.dao.AccountDAO;
import com.app.dao.AccountDAOImpl;
import com.app.services.AccountService;
import com.app.services.AccountServiceImpl;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.squarespace.jersey2.guice.JerseyGuiceServletContextListener;

import java.util.Collections;
import java.util.List;

/**
 * Register dependencies, registered in TomcatLauncher
 * Created by Sergey on 07.09.2017.
 */
public class DependencyMapper extends JerseyGuiceServletContextListener {

    @Override
    protected List<? extends Module> modules() {
        return Collections.singletonList(new ServletModule() {
            @Override
            protected void configureServlets() {
                bind(AccountDAO.class).to(AccountDAOImpl.class).in(Singleton.class);
                bind(AccountService.class).to(AccountServiceImpl.class).in(Singleton.class);
            }
        });
    }
}
