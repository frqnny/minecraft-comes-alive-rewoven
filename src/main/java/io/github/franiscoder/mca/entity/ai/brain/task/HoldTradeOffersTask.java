package io.github.franiscoder.mca.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.github.franiscoder.mca.entity.MCAVillagerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.TradeOffer;

import java.util.List;

public class HoldTradeOffersTask extends Task<MCAVillagerEntity> {
    private final List<ItemStack> offers = Lists.newArrayList();
    private ItemStack customerHeldStack;
    private int offerShownTicks;
    private int offerIndex;
    private int ticksLeft;

    public HoldTradeOffersTask(int minRunTime, int maxRunTime) {
        super(ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET, MemoryModuleState.VALUE_PRESENT), minRunTime, maxRunTime);
    }

    public boolean shouldRun(ServerWorld serverWorld, MCAVillagerEntity villagerEntity) {
        Brain<?> brain = villagerEntity.getBrain();
        if (!brain.getOptionalMemory(MemoryModuleType.INTERACTION_TARGET).isPresent()) {
            return false;
        } else {
            LivingEntity livingEntity = brain.getOptionalMemory(MemoryModuleType.INTERACTION_TARGET).get();
            return livingEntity.getType() == EntityType.PLAYER && villagerEntity.isAlive() && livingEntity.isAlive() && !villagerEntity.isBaby() && villagerEntity.squaredDistanceTo(livingEntity) <= 17.0D;
        }
    }

    public boolean shouldKeepRunning(ServerWorld serverWorld, MCAVillagerEntity villagerEntity, long l) {
        return this.shouldRun(serverWorld, villagerEntity) && this.ticksLeft > 0 && villagerEntity.getBrain().getOptionalMemory(MemoryModuleType.INTERACTION_TARGET).isPresent();
    }

    public void run(ServerWorld serverWorld, MCAVillagerEntity villagerEntity, long l) {
        super.run(serverWorld, villagerEntity, l);
        this.findPotentialCustomer(villagerEntity);
        this.offerShownTicks = 0;
        this.offerIndex = 0;
        this.ticksLeft = 40;
    }

    public void keepRunning(ServerWorld serverWorld, MCAVillagerEntity villagerEntity, long l) {
        LivingEntity livingEntity = this.findPotentialCustomer(villagerEntity);
        this.setupOffers(livingEntity, villagerEntity);
        if (!this.offers.isEmpty()) {
            this.refreshShownOffer(villagerEntity);
        } else {
            villagerEntity.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            this.ticksLeft = Math.min(this.ticksLeft, 40);
        }

        --this.ticksLeft;
    }

    public void finishRunning(ServerWorld serverWorld, MCAVillagerEntity villagerEntity, long l) {
        super.finishRunning(serverWorld, villagerEntity, l);
        villagerEntity.getBrain().forget(MemoryModuleType.INTERACTION_TARGET);
        villagerEntity.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        this.customerHeldStack = null;
    }

    private void setupOffers(LivingEntity customer, MCAVillagerEntity villager) {
        boolean bl = false;
        ItemStack itemStack = customer.getMainHandStack();
        if (this.customerHeldStack == null || !ItemStack.areItemsEqualIgnoreDamage(this.customerHeldStack, itemStack)) {
            this.customerHeldStack = itemStack;
            bl = true;
            this.offers.clear();
        }

        if (bl && !this.customerHeldStack.isEmpty()) {
            this.loadPossibleOffers(villager);
            if (!this.offers.isEmpty()) {
                this.ticksLeft = 900;
                this.holdOffer(villager);
            }
        }

    }

    private void holdOffer(MCAVillagerEntity villager) {
        villager.equipStack(EquipmentSlot.MAINHAND, this.offers.get(0));
    }

    private void loadPossibleOffers(MCAVillagerEntity villager) {

        for (TradeOffer tradeOffer : villager.getOffers()) {
            if (!tradeOffer.isDisabled() && this.isPossible(tradeOffer)) {
                this.offers.add(tradeOffer.getMutableSellItem());
            }
        }

    }

    private boolean isPossible(TradeOffer offer) {
        return ItemStack.areItemsEqualIgnoreDamage(this.customerHeldStack, offer.getAdjustedFirstBuyItem()) || ItemStack.areItemsEqualIgnoreDamage(this.customerHeldStack, offer.getSecondBuyItem());
    }

    private LivingEntity findPotentialCustomer(MCAVillagerEntity villager) {
        Brain<?> brain = villager.getBrain();
        LivingEntity livingEntity = brain.getOptionalMemory(MemoryModuleType.INTERACTION_TARGET).get();
        brain.remember(MemoryModuleType.LOOK_TARGET, new EntityLookTarget(livingEntity, true));
        return livingEntity;
    }

    private void refreshShownOffer(MCAVillagerEntity villager) {
        if (this.offers.size() >= 2 && ++this.offerShownTicks >= 40) {
            ++this.offerIndex;
            this.offerShownTicks = 0;
            if (this.offerIndex > this.offers.size() - 1) {
                this.offerIndex = 0;
            }

            villager.equipStack(EquipmentSlot.MAINHAND, this.offers.get(this.offerIndex));
        }

    }
}