package io.github.franiscoder.mca.init;

import io.github.franiscoder.mca.command.MCACommand;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class MCACommands {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> MCACommand.register(dispatcher));
    }
}
