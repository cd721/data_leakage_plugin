package com.github.cd721.data_leakage_plugin.listeners;

import com.intellij.openapi.application.ApplicationActivationListener;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.wm.IdeFrame;
import org.jetbrains.annotations.NotNull;

//https://plugins.jetbrains.com/docs/intellij/ide-infrastructure.html#logging

public class MyApplicationActivationListener implements ApplicationActivationListener {
    private static final Logger logger = Logger.getInstance(MyApplicationActivationListener.class);

    @Override
    public void applicationActivated(@NotNull IdeFrame ideFrame) {
        logger.info("The application has been activated.");
    }

    @Override
    public void applicationDeactivated(@NotNull IdeFrame ideFrame) {
        logger.info("The application has been deactivated.");
    }

}
