package com.pqqqqq.directessentials;

import com.google.inject.Inject;
import com.pqqqqq.directessentials.commands.CommandDeleteWarp;
import com.pqqqqq.directessentials.commands.CommandSetWarp;
import com.pqqqqq.directessentials.commands.CommandWarp;
import com.pqqqqq.directessentials.commands.essentials.CommandSave;
import com.pqqqqq.directessentials.config.DataConfig;
import com.pqqqqq.directessentials.events.CoreEvents;
import com.pqqqqq.directessentials.wrappers.game.EssentialsGame;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.InitializationEvent;
import org.spongepowered.api.event.state.ServerStartedEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.service.event.EventManager;
import org.spongepowered.api.util.command.dispatcher.SimpleDispatcher;

import java.io.File;

/**
 * Created by Kevin on 2015-05-11.
 */
@Plugin(id = DirectEssentials.ID, name = DirectEssentials.NAME, version = DirectEssentials.VERSION)
public class DirectEssentials {
    public static final String ID = "directessentials";
    public static final String NAME = "DirectEssentials";
    public static final String VERSION = "0.1 BETA";

    private Game game;

    private EssentialsGame essentialsGame;
    private DataConfig dcfg;

    public static DirectEssentials plugin;

    @Inject
    private Logger logger;

    @Inject
    public DirectEssentials(Logger logger) {
        this.logger = logger;
    }

    @Subscribe
    public void init(InitializationEvent event) {
        plugin = this;
        game = event.getGame();

        // Register events
        EventManager eventManager = game.getEventManager();
        eventManager.register(this, new CoreEvents(this));

        // Register commands
        CommandService commandService = game.getCommandDispatcher();
        commandService.register(this, new CommandWarp(this), "warp");
        commandService.register(this, new CommandSetWarp(this), "setwarp", "swarp");
        commandService.register(this, new CommandDeleteWarp(this), "deletewarp", "dwarp", "removewarp");

        // Essentials main plugin commands
        SimpleDispatcher essentialsCommand = new SimpleDispatcher();
        essentialsCommand.register(new CommandSave(this), "save");

        commandService.register(this, essentialsCommand, "essentials", "ess", "de", "directessentials", "dessentials", "dess");

        // Instantiate managers
        essentialsGame = new EssentialsGame(game);
    }

    @Subscribe
    public void started(ServerStartedEvent event) {
        // Config
        dcfg = new DataConfig(this, new File("config/DirectEssentials/data.json"));
        dcfg.init();
        dcfg.load();
    }

    @Subscribe
    public void stopping(ServerStoppingEvent event) {
        dcfg.save();
    }

    public Logger getLogger() {
        return logger;
    }

    public Game getGame() {
        return game;
    }

    public DataConfig getDataConfig() {
        return dcfg;
    }

    public EssentialsGame getEssentialsGame() {
        return essentialsGame;
    }
}