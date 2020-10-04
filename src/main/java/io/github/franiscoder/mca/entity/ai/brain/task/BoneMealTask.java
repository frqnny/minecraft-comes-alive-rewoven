package io.github.franiscoder.mca.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import io.github.franiscoder.mca.entity.MCAVillagerEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.brain.BlockPosLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class BoneMealTask extends Task<MCAVillagerEntity> {
    private long startTime;
    private long lastEndEntityAge;
    private int duration;
    private Optional<BlockPos> pos = Optional.empty();

    public BoneMealTask() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT));
    }

    protected boolean shouldRun(ServerWorld serverWorld, MCAVillagerEntity villagerEntity) {
        if (villagerEntity.age % 10 == 0 && (this.lastEndEntityAge == 0L || this.lastEndEntityAge + 160L <= (long) villagerEntity.age)) {
            if (villagerEntity.getInventory().count(Items.BONE_MEAL) <= 0) {
                return false;
            } else {
                this.pos = this.findBoneMealPos(serverWorld, villagerEntity);
                return this.pos.isPresent();
            }
        } else {
            return false;
        }
    }

    protected boolean shouldKeepRunning(ServerWorld serverWorld, MCAVillagerEntity villagerEntity, long l) {
        return this.duration < 80 && this.pos.isPresent();
    }

    private Optional<BlockPos> findBoneMealPos(ServerWorld world, MCAVillagerEntity entity) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        Optional<BlockPos> optional = Optional.empty();
        int i = 0;

        for (int j = -1; j <= 1; ++j) {
            for (int k = -1; k <= 1; ++k) {
                for (int l = -1; l <= 1; ++l) {
                    mutable.set(entity.getBlockPos(), j, k, l);
                    if (this.canBoneMeal(mutable, world)) {
                        ++i;
                        if (world.random.nextInt(i) == 0) {
                            optional = Optional.of(mutable.toImmutable());
                        }
                    }
                }
            }
        }

        return optional;
    }

    private boolean canBoneMeal(BlockPos pos, ServerWorld world) {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        return block instanceof CropBlock && !((CropBlock) block).isMature(blockState);
    }

    protected void run(ServerWorld serverWorld, MCAVillagerEntity villagerEntity, long l) {
        this.addLookWalkTargets(villagerEntity);
        villagerEntity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BONE_MEAL));
        this.startTime = l;
        this.duration = 0;
    }

    private void addLookWalkTargets(MCAVillagerEntity villager) {
        this.pos.ifPresent((blockPos) -> {
            BlockPosLookTarget blockPosLookTarget = new BlockPosLookTarget(blockPos);
            villager.getBrain().remember(MemoryModuleType.LOOK_TARGET, blockPosLookTarget);
            villager.getBrain().remember(MemoryModuleType.WALK_TARGET, new WalkTarget(blockPosLookTarget, 0.5F, 1));
        });
    }

    protected void finishRunning(ServerWorld serverWorld, MCAVillagerEntity villagerEntity, long l) {
        villagerEntity.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        this.lastEndEntityAge = villagerEntity.age;
    }

    protected void keepRunning(ServerWorld serverWorld, MCAVillagerEntity villagerEntity, long l) {
        BlockPos blockPos = this.pos.get();
        if (l >= this.startTime && blockPos.isWithinDistance(villagerEntity.getPos(), 1.0D)) {
            ItemStack itemStack = ItemStack.EMPTY;
            SimpleInventory simpleInventory = villagerEntity.getInventory();
            int i = simpleInventory.size();

            for (int j = 0; j < i; ++j) {
                ItemStack itemStack2 = simpleInventory.getStack(j);
                if (itemStack2.getItem() == Items.BONE_MEAL) {
                    itemStack = itemStack2;
                    break;
                }
            }

            if (!itemStack.isEmpty() && BoneMealItem.useOnFertilizable(itemStack, serverWorld, blockPos)) {
                serverWorld.syncWorldEvent(2005, blockPos, 0);
                this.pos = this.findBoneMealPos(serverWorld, villagerEntity);
                this.addLookWalkTargets(villagerEntity);
                this.startTime = l + 40L;
            }

            ++this.duration;
        }
    }
}