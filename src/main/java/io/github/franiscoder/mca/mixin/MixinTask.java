package io.github.franiscoder.mca.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Task.class)
public interface MixinTask {
	@Invoker
	<E extends LivingEntity> boolean invokeShouldKeepRunning(ServerWorld world, E entity, long time);
}
