package io.github.franiscoder.mca.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import io.github.franiscoder.mca.entity.MCAVillagerEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.dynamic.GlobalPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.poi.PointOfInterestStorage;

import java.util.Optional;

public class WalkTowardJobSiteTask extends Task<MCAVillagerEntity> {
	final float speed;
	
	public WalkTowardJobSiteTask(float speed) {
		super(ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE, MemoryModuleState.VALUE_PRESENT), 1200);
		this.speed = speed;
	}
	
	protected boolean shouldRun(ServerWorld serverWorld, MCAVillagerEntity villagerEntity) {
		return villagerEntity.getBrain().getFirstPossibleNonCoreActivity().map((activity) -> activity == Activity.IDLE || activity == Activity.WORK || activity == Activity.PLAY).orElse(true);
	}
	
	protected boolean shouldKeepRunning(ServerWorld serverWorld, MCAVillagerEntity villagerEntity, long l) {
		return villagerEntity.getBrain().hasMemoryModule(MemoryModuleType.POTENTIAL_JOB_SITE);
	}
	
	protected void keepRunning(ServerWorld serverWorld, MCAVillagerEntity villagerEntity, long l) {
		LookTargetUtil.walkTowards(villagerEntity, villagerEntity.getBrain().getOptionalMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get().getPos(), this.speed, 1);
	}
	
	protected void finishRunning(ServerWorld serverWorld, MCAVillagerEntity villagerEntity, long l) {
		Optional<GlobalPos> optional = villagerEntity.getBrain().getOptionalMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
		optional.ifPresent((globalPos) -> {
			BlockPos blockPos = globalPos.getPos();
			ServerWorld serverWorld2 = serverWorld.getServer().getWorld(globalPos.getDimension());
			if (serverWorld2 != null) {
				PointOfInterestStorage pointOfInterestStorage = serverWorld2.getPointOfInterestStorage();
				if (pointOfInterestStorage.test(blockPos, (pointOfInterestType) -> true)) {
					pointOfInterestStorage.releaseTicket(blockPos);
				}
				
				DebugInfoSender.sendPointOfInterest(serverWorld, blockPos);
			}
		});
		villagerEntity.getBrain().forget(MemoryModuleType.POTENTIAL_JOB_SITE);
	}
}
