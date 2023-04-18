package io.linuxeroagrio.todoapi.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigMapping(prefix="io.linuxeroagrio")
public interface TodoItemConfig {

    @WithName("greeting-message")
    String message();

    /*
    @WithDefault("true")
    boolean fullTodos();
    */
}