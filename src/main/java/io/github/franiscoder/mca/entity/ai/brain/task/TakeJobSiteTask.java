package io.github.franiscoder.mca.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import io.github.franiscoder.mca.entity.MCAVillagerEntity;
import io.github.franiscoder.mca.util.TaskHelper;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.dynamic.GlobalPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.Optional;

public class TakeJobSiteTask extends Task<MCAVillagerEntity> {
	private final float speed;
	
	public TakeJobSiteTask(float speed) {
		super(ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.JOB_SITE, MemoryModuleState.VALUE_ABSENT, MemoryModuleType.MOBS, MemoryModuleState.VALUE_PRESENT));
		this.speed = speed;
	}
	
	private static boolean canUseJobSite(PointOfInterestType poiType, MCAVillagerEntity villager, BlockPos pos) {
		boolean bl = villager.getBrain().getOptionalMemory(MemoryModuleType.POTENTIAL_JOB_SITE).isPresent();
		if (bl) {
			return false;
		} else {
			Optional<GlobalPos> optional = villager.getBrain().getOptionalMemory(MemoryModuleType.JOB_SITE);
			VillagerProfession villagerProfession = villager.getVillagerData().getProfession();
			if (villager.getVillagerData().getProfession() != VillagerProfession.NONE && villagerProfession.getWorkStation().getCompletionCondition().test(poiType)) {
				return optional.map(globalPos -> globalPos.getPos().equals(pos)).orElseGet(() -> TakeJobSiteTask.canReachJobSite(villager, pos, poiType));
			} else {
				return false;
			}
		}
	}
	
	private static boolean canReachJobSite(MCAVillagerEntity villager, BlockPos pos, PointOfInterestType poiType) {
		Path path = villager.getNavigation().findPathTo(pos, poiType.getSearchDistance());
		return path != null && path.reachesTarget();
	}
	
	private static void forgetJobSiteAndWalkTarget(MCAVillagerEntity villager) {
		villager.getBrain().forget(MemoryModuleType.WALK_TARGET);
		villager.getBrain().forget(MemoryModuleType.LOOK_TARGET);
		villager.getBrain().forget(MemoryModuleType.POTENTIAL_JOB_SITE);
	}
	
	protected boolean shouldRun(ServerWorld serverWorld, MCAVillagerEntity villagerEntity) {
		if (villagerEntity.isBaby()) {
			return false;
		} else {
			return villagerEntity.getVillagerData().getProfession() == VillagerProfession.NONE;
		}
	}
	
	protected void run(ServerWorld serverWorld, MCAVillagerEntity villagerEntity, long l) {
		BlockPos blockPos = villagerEntity.getBrain().getOptionalMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get().getPos();
		Optional<PointOfInterestType> optional = serverWorld.getPointOfInterestStorage().getType(blockPos);
		optional.flatMap(pointOfInterestType -> TaskHelper.streamSeenVillagers(villagerEntity, (villagerEntityx) -> TakeJobSiteTask.canUseJobSite(pointOfInterestType, villagerEntityx, blockPos)).findFirst()).ifPresent((villagerEntity2) -> this.claimSite(serverWorld, villagerEntity, villagerEntity2, blockPos, villagerEntity2.getBrain().getOptionalMemory(MemoryModuleType.JOB_SITE).isPresent()));
	}
	
	private void claimSite(ServerWorld world, MCAVillagerEntity previousOwner, MCAVillagerEntity newOwner, BlockPos pos, boolean jobSitePresent) {
		TakeJobSiteTask.forgetJobSiteAndWalkTarget(previousOwner);
		if (!jobSitePresent) {
			LookTargetUtil.walkTowards(newOwner, pos, this.speed, 1);
			newOwner.getBrain().remember(MemoryModuleType.POTENTIAL_JOB_SITE, GlobalPos.create(world.getRegistryKey(), pos));
			DebugInfoSender.sendPointOfInterest(world, pos);
		}
		
	}
}