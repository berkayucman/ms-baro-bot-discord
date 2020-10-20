package com.baro.bot.discord.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BotConfig {

    private final String token;
    private final String activity;
    private final String prefix;
    private final List<String> botOwnerIds;
    private final boolean production;

    public BotConfig(
            @Value("${token}") String token,
            @Value("${activity}") String activity,
            @Value("${prefix}") String prefix,
            @Value("${owner.ids}") List<String> botOwnerIds,
            @Value("${production}") boolean production) {
        this.token = token;
        this.activity = activity;
        this.prefix = prefix;
        this.botOwnerIds = botOwnerIds;
        this.production = production;
    }

    public String getToken() {
        return token;
    }

    public String getActivity() {
        return activity;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isProduction() {
        return production;
    }

    public List<String> getBotOwnerIds() {
        return botOwnerIds;
    }
}
