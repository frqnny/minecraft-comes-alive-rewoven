package io.github.franiscoder.mca.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.franiscoder.mca.components.data.PlayerData;
import io.github.franiscoder.mca.init.MCAComponents;
import io.github.franiscoder.mca.util.enums.SpouseType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.UserCache;
import net.minecraft.util.Util;

import java.util.Collection;
import java.util.UUID;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


/*
 * MCACommand is a giant command for server-sided interactions
 *
 * to improve readability, we use some import statics.
 */
@SuppressWarnings({"nullable", "SameReturnValue"})
public class MCACommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal("mca").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(0))
				.then(literal("marry")
						.then(argument("targets", EntityArgumentType.players())
								.executes((commandContext) -> askToMarry(commandContext.getSource(), EntityArgumentType.getPlayers(commandContext, "targets"))))));
		
		dispatcher.register(literal("mca").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(0))
				.then(literal("requests")
						.executes((commandContext) -> getAndPrintRequests(commandContext.getSource()))));
		
		dispatcher.register(literal("mca").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(0))
				.then(literal("accept")
						.then(argument("targets", EntityArgumentType.players())
								.executes((commandContext) -> acceptMarriageRequest(commandContext.getSource(), EntityArgumentType.getPlayers(commandContext, "targets"))))));
		dispatcher.register(literal("mca").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(0))
				.then(literal("deny")
						.then(argument("targets", EntityArgumentType.player())
								.executes((commandContext) -> denyMarriageRequest(commandContext.getSource(), EntityArgumentType.getPlayers(commandContext, "targets"))))));
	}
	
	private static int askToMarry(ServerCommandSource source, Collection<ServerPlayerEntity> targets) throws CommandSyntaxException {
		ServerPlayerEntity sourcePlayer = source.getPlayer();
		UUID sourceUUID = sourcePlayer == null ? Util.NIL_UUID : sourcePlayer.getUuid();
		ServerPlayerEntity targetPlayer = getPlayer(targets);
		UUID targetUUID = targetPlayer == null ? Util.NIL_UUID : targetPlayer.getUuid();
		
		boolean alreadyAsked = MCAComponents.PLAYER_DATA_COMPONENT.get(sourcePlayer).get().outgoingRequests.contains(targetUUID);
		if (!alreadyAsked) {
			MCAComponents.PLAYER_DATA_COMPONENT.get(sourcePlayer).get().outgoingRequests.add(targetUUID);
			source.sendFeedback(new TranslatableText("commands.mca.ask.source", targetPlayer.getDisplayName()).formatted(Formatting.GOLD), false);
			MCAComponents.PLAYER_DATA_COMPONENT.get(targetPlayer).get().marriageRequests.add(sourceUUID);
			targetPlayer.sendSystemMessage(new TranslatableText("commands.mca.ask.target", sourcePlayer.getDisplayName()).formatted(Formatting.GOLD), sourceUUID);
		} else {
			source.sendFeedback(new TranslatableText("commands.mca.ask.fail"), false);
		}
		return targets.size();
	}
	
	private static int getAndPrintRequests(ServerCommandSource source) throws CommandSyntaxException {
		ServerPlayerEntity sourcePlayer = source.getPlayer();
		
		PlayerData data = MCAComponents.PLAYER_DATA_COMPONENT.get(sourcePlayer).get();
		UserCache cache = source.getWorld().getServer().getUserCache();
		
		source.sendFeedback(new TranslatableText("commands.mca.print.outgoingrequests").formatted(Formatting.GOLD, Formatting.BOLD), false);
		for (UUID uuid : data.outgoingRequests) {
			source.sendFeedback(new LiteralText(" > " + cache.getByUuid(uuid).getName()).formatted(Formatting.GOLD), false);
		}
		source.sendFeedback(new TranslatableText("commands.mca.print.marriagerequests").formatted(Formatting.GOLD, Formatting.BOLD), false);
		for (UUID uuid : data.marriageRequests) {
			source.sendFeedback(new LiteralText(" > " + cache.getByUuid(uuid).getName()).formatted(Formatting.GOLD), false);
		}
		return 1;
	}
	
	private static int acceptMarriageRequest(ServerCommandSource source, Collection<ServerPlayerEntity> targets) throws CommandSyntaxException {
		ServerPlayerEntity sourcePlayer = source.getPlayer();
		ServerPlayerEntity targetPlayer = getPlayer(targets);
		
		PlayerData sourceData = MCAComponents.PLAYER_DATA_COMPONENT.get(sourcePlayer).get();
		PlayerData targetData = MCAComponents.PLAYER_DATA_COMPONENT.get(targetPlayer).get();
		
		if (sourceData.isMarried || targetData.isMarried) {
			source.sendFeedback(new TranslatableText("commands.mca.accept.source"), false);
			return targets.size();
		}
		
		sourceData.isMarried = true;
		sourceData.spouseType = SpouseType.PLAYER;
		sourceData.spousePlayer = targetPlayer.getUuid();
		
		targetData.isMarried = true;
		targetData.spouseType = SpouseType.PLAYER;
		targetData.spousePlayer = sourcePlayer.getUuid();
		return targets.size();
	}
	
	private static int denyMarriageRequest(ServerCommandSource source, Collection<ServerPlayerEntity> targets) {
		return targets.size();
	}
	
	
	private static ServerPlayerEntity getPlayer(Collection<ServerPlayerEntity> targets) {
		return (ServerPlayerEntity) targets.toArray()[0];
	}
	
	
}
