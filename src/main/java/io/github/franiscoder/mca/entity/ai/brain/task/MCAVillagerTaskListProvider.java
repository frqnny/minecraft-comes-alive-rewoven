package io.github.franiscoder.mca.entity.ai.brain.task;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import io.github.franiscoder.mca.entity.MCAVillagerEntity;
import io.github.franiscoder.mca.init.MCAEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.Optional;

public class MCAVillagerTaskListProvider {
	public static ImmutableList<Pair<Integer, ? extends Task<? super MCAVillagerEntity>>> createCoreTasks(VillagerProfession profession, float f) {
		return ImmutableList.of(
				new Pair<>(0, new StayAboveWaterTask(0.8F)),
				new Pair<>(0, new OpenDoorsTask()),
				new Pair<>(0, new LookAroundTask(45, 90)),
				new Pair<>(0, new PanicTask()),
				new Pair<>(0, new WakeUpTask()),
				new Pair<>(0, new HideWhenBellRingsTask()),
				new Pair<>(0, new StartRaidTask()),
				new Pair<>(0, new ForgetCompletedPointOfInterestTask(profession.getWorkStation(), MemoryModuleType.JOB_SITE)),
				new Pair<>(0, new ForgetCompletedPointOfInterestTask(profession.getWorkStation(), MemoryModuleType.POTENTIAL_JOB_SITE)),
				new Pair<>(1, new WanderAroundTask()),
				new Pair<>(2, new WorkStationCompetitionTask(profession)),
				new Pair<>(3, new FollowCustomerTask(f)),
				new Pair<>(5, new WalkToNearestVisibleWantedItemTask<>(f, false, 4)),
				new Pair<>(6, new FindPointOfInterestTask(profession.getWorkStation(), MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, true, Optional.empty())),
				new Pair<>(7, new WalkTowardJobSiteTask(f)),
				new Pair<>(8, new TakeJobSiteTask(f)),
				new Pair<>(10, new FindPointOfInterestTask(PointOfInterestType.HOME, MemoryModuleType.HOME, false, Optional.of((byte) 14))),
				new Pair<>(10, new FindPointOfInterestTask(PointOfInterestType.MEETING, MemoryModuleType.MEETING_POINT, true, Optional.of((byte) 14))),
				new Pair<>(10, new GoToWorkTask()),
				new Pair<>(10, new LoseJobOnSiteLossTask())
		);
	}
	
	public static ImmutableList<Pair<Integer, ? extends Task<? super MCAVillagerEntity>>> createWorkTasks(VillagerProfession profession, float f) {
		VillagerWorkTask villagerWorkTask2;
		boolean isFarmer = profession == VillagerProfession.FARMER;
		if (isFarmer) {
			villagerWorkTask2 = new FarmerWorkTask();
		} else {
			villagerWorkTask2 = new VillagerWorkTask();
		}
		
		return ImmutableList.of(
				createBusyFollowTask(),
				new Pair<>(5, new RandomTask(ImmutableList.of(
						new Pair<>(villagerWorkTask2, 7),
						new Pair<>(new GoToIfNearbyTask(MemoryModuleType.JOB_SITE, 0.4F, 4), 2),
						new Pair<>(new GoToNearbyPositionTask(MemoryModuleType.JOB_SITE, 0.4F, 1, 10), 5),
						new Pair<>(new GoToSecondaryPositionTask(MemoryModuleType.SECONDARY_JOB_SITE, f, 1, 6, MemoryModuleType.JOB_SITE), 5),
						new Pair<>(new FarmerVillagerTask(), isFarmer ? 2 : 5),
						new Pair<>(new BoneMealTask(), isFarmer ? 4 : 7)))),
				new Pair<>(10, new HoldTradeOffersTask(400, 1600)),
				new Pair<>(10, new FindInteractionTargetTask(EntityType.PLAYER, 4)),
				new Pair<>(2, new VillagerWalkTowardsTask(MemoryModuleType.JOB_SITE, f, 9, 100, 1200)),
				new Pair<>(3, new GiveGiftsToHeroTask(100)),
				new Pair<>(99, new ScheduleActivityTask())
		);
	}
	
	public static ImmutableList<Pair<Integer, ? extends Task<? super MCAVillagerEntity>>> createPlayTasks(float f) {
		return ImmutableList.of(
				new Pair<>(0, new WanderAroundTask(80, 120)),
				createFreeFollowTask(),
				new Pair<>(5, new PlayWithVillagerBabiesTask()),
				new Pair<>(5, new RandomTask<>(ImmutableMap.of(
						MemoryModuleType.VISIBLE_VILLAGER_BABIES,
						MemoryModuleState.VALUE_ABSENT),
						ImmutableList.of(
								new Pair<>(FindEntityTask.create(MCAEntities.MCA_VILLAGER, 8, MemoryModuleType.INTERACTION_TARGET, f, 2), 2),
								new Pair<>(FindEntityTask.create(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, f, 2), 1),
								new Pair<>(new FindWalkTargetTask(f), 1), Pair.of(new GoTowardsLookTarget(f, 2), 1),
								new Pair<>(new JumpInBedTask(f), 2), Pair.of(new WaitTask(20, 40), 2)))),
				new Pair<>(99, new ScheduleActivityTask())
		);
	}
	
	
	public static ImmutableList<Pair<Integer, ? extends Task<? super MCAVillagerEntity>>> createRestTasks(VillagerProfession profession, float f) {
		return ImmutableList.of(
				new Pair<>(2, new VillagerWalkTowardsTask(MemoryModuleType.HOME, f, 1, 150, 1200)),
				new Pair<>(3, new ForgetCompletedPointOfInterestTask(PointOfInterestType.HOME, MemoryModuleType.HOME)),
				new Pair<>(3, new SleepTask()),
				new Pair<>(5, new RandomTask<>(ImmutableMap.of(
						MemoryModuleType.HOME,
						MemoryModuleState.VALUE_ABSENT),
						ImmutableList.of(
								new Pair<>(new WalkHomeTask(f), 1),
								new Pair<>(new WanderIndoorsTask(f), 4),
								new Pair<>(new GoToPointOfInterestTask(f, 4), 2),
								new Pair<>(new WaitTask(20, 40), 2)))),
				createBusyFollowTask(),
				new Pair<>(99, new ScheduleActivityTask())
		);
	}
	
	public static ImmutableList<Pair<Integer, ? extends Task<? super MCAVillagerEntity>>> createMeetTasks(VillagerProfession profession, float f) {
		return ImmutableList.of(
				new Pair<>(2, new RandomTask(ImmutableList.of(
						new Pair<>(new GoToIfNearbyTask(MemoryModuleType.MEETING_POINT, 0.4F, 40), 2),
						new Pair<>(new MeetVillagerTask(), 2)))), Pair.of(10, new HoldTradeOffersTask(400, 1600)),
				new Pair<>(10, new FindInteractionTargetTask(EntityType.PLAYER, 4)),
				new Pair<>(2, new VillagerWalkTowardsTask(MemoryModuleType.MEETING_POINT, f, 6, 100, 200)),
				new Pair<>(3, new GiveGiftsToHeroTask(100)),
				new Pair<>(3, new ForgetCompletedPointOfInterestTask(PointOfInterestType.MEETING, MemoryModuleType.MEETING_POINT)),
				new Pair<>(3, new CompositeTask<>(ImmutableMap.of(), ImmutableSet.of(MemoryModuleType.INTERACTION_TARGET), CompositeTask.Order.ORDERED, CompositeTask.RunMode.RUN_ONE, ImmutableList.of(
						Pair.of(new GatherItemsVillagerTask(), 1)))),
				createFreeFollowTask(),
				new Pair<>(99, new ScheduleActivityTask())
		);
	}
	
	public static ImmutableList<Pair<Integer, ? extends Task<? super MCAVillagerEntity>>> createIdleTasks(VillagerProfession profession, float f) {
		return ImmutableList.of(
				Pair.of(2, new RandomTask<>(ImmutableList.of(
						new Pair<>(FindEntityTask.create(EntityType.VILLAGER, 8, MemoryModuleType.INTERACTION_TARGET, f, 2), 2),
						new Pair<>(new FindEntityTask<>(EntityType.VILLAGER, 8, PassiveEntity::isReadyToBreed, PassiveEntity::isReadyToBreed, MemoryModuleType.BREED_TARGET, f, 2), 1),
						new Pair<>(FindEntityTask.create(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, f, 2), 1),
						new Pair<>(new FindWalkTargetTask(f), 1),
						new Pair<>(new GoTowardsLookTarget(f, 2), 1),
						new Pair<>(new JumpInBedTask(f), 1),
						new Pair<>(new WaitTask(30, 60), 1)))),
				Pair.of(3, new GiveGiftsToHeroTask(100)),
				Pair.of(3, new FindInteractionTargetTask(EntityType.PLAYER, 4)),
				Pair.of(3, new HoldTradeOffersTask(400, 1600)),
				Pair.of(3, new CompositeTask<>(ImmutableMap.of(), ImmutableSet.of(MemoryModuleType.INTERACTION_TARGET),
						CompositeTask.Order.ORDERED,
						CompositeTask.RunMode.RUN_ONE,
						ImmutableList.of(
								new Pair<>(new GatherItemsVillagerTask(), 1)))),
				Pair.of(3, new CompositeTask<>(ImmutableMap.of(), ImmutableSet.of(MemoryModuleType.BREED_TARGET),
						CompositeTask.Order.ORDERED,
						CompositeTask.RunMode.RUN_ONE,
						ImmutableList.of(
								new Pair<>(new VillagerBreedTask(), 1)))),
				createFreeFollowTask(),
				new Pair<>(99, new ScheduleActivityTask())
		);
	}
	
	public static ImmutableList<Pair<Integer, ? extends Task<? super MCAVillagerEntity>>> createPanicTasks(VillagerProfession profession, float f) {
		float g = f * 1.5F;
		return ImmutableList.of(
				new Pair<>(0, new StopPanickingTask()),
				new Pair<>(1, GoToRememberedPositionTask.toEntity(MemoryModuleType.NEAREST_HOSTILE, g, 6, false)),
				new Pair<>(1, GoToRememberedPositionTask.toEntity(MemoryModuleType.HURT_BY_ENTITY, g, 6, false)),
				new Pair<>(3, new FindWalkTargetTask(g, 2, 2)),
				createBusyFollowTask()
		);
	}
	
	public static ImmutableList<Pair<Integer, ? extends Task<? super MCAVillagerEntity>>> createPreRaidTasks(VillagerProfession villagerProfession, float f) {
		return ImmutableList.of(
				new Pair<>(0, new RingBellTask()),
				new Pair<>(0, new RandomTask<>(ImmutableList.of(
						new Pair<>(new VillagerWalkTowardsTask(MemoryModuleType.MEETING_POINT, f * 1.5F, 2, 150, 200), 6),
						new Pair<>(new FindWalkTargetTask(f * 1.5F), 2)))),
				createBusyFollowTask(),
				new Pair<>(99, new EndRaidTask())
		);
	}
	
	public static ImmutableList<Pair<Integer, ? extends Task<? super MCAVillagerEntity>>> createRaidTasks(VillagerProfession villagerProfession, float f) {
		return ImmutableList.of(
				new Pair<>(0, new RandomTask<>(ImmutableList.of(
						new Pair<>(new SeekSkyAfterRaidWinTask(f), 5),
						new Pair<>(new RunAroundAfterRaidTask(f * 1.1F), 2)))),
				new Pair<>(0, new CelebrateRaidWinTask(600, 600)),
				new Pair<>(2, new HideInHomeDuringRaidTask(24, f * 1.4F)),
				createBusyFollowTask(),
				new Pair<>(99, new EndRaidTask()));
	}
	
	public static ImmutableList<Pair<Integer, ? extends Task<? super MCAVillagerEntity>>> createHideTasks(VillagerProfession villagerProfession, float f) {
		return ImmutableList.of(
				new Pair<>(0, new ForgetBellRingTask(15, 3)),
				new Pair<>(1, new HideInHomeTask(32, f * 1.25F, 2)),
				createBusyFollowTask());
	}
	
	private static Pair<Integer, Task<LivingEntity>> createFreeFollowTask() {
		return new Pair<>(5, new RandomTask<>(ImmutableList.of(
				new Pair<>(new FollowMobTask(EntityType.CAT, 8.0F), 8),
				new Pair<>(new FollowMobTask(EntityType.VILLAGER, 8.0F), 2),
				new Pair<>(new FollowMobTask(EntityType.PLAYER, 8.0F), 2),
				new Pair<>(new FollowMobTask(SpawnGroup.CREATURE, 8.0F), 1),
				new Pair<>(new FollowMobTask(SpawnGroup.WATER_CREATURE, 8.0F), 1),
				new Pair<>(new FollowMobTask(SpawnGroup.WATER_AMBIENT, 8.0F), 1),
				new Pair<>(new FollowMobTask(SpawnGroup.MONSTER, 8.0F), 1),
				new Pair<>(new WaitTask(30, 60), 2)))
		);
	}
	
	private static Pair<Integer, Task<LivingEntity>> createBusyFollowTask() {
		return new Pair<>(5, new RandomTask<>(ImmutableList.of(new Pair<>(new FollowMobTask(EntityType.VILLAGER, 8.0F), 2), new Pair<>(new FollowMobTask(EntityType.PLAYER, 8.0F), 2), new Pair<>(new WaitTask(30, 60), 8))));
	}
}
