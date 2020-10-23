package com.baro.bot.discord.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "command")
public class CommandEntity {

    @Id
    @Column(unique = true, nullable = false)
    private Long serverId;

    @Column(unique = true, nullable = false)
    private String name;

    private boolean enabled;

    public CommandEntity(Long serverId, String name, boolean enabled) {
        this.serverId = serverId;
        this.name = name;
        this.enabled = enabled;
    }
}
