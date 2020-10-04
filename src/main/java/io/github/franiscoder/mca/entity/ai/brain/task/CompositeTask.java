package io.github.franiscoder.mca.entity.ai.brain.task;

import com.mojang.datafixers.util.Pair;
import io.github.franiscoder.mca.mixin.MixinTask;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.WeightedList;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CompositeTask<E extends LivingEntity> extends Task<E> {
    private final Set<MemoryModuleType<?>> memoriesToForgetWhenStopped;
    private final CompositeTask.Order order;
    private final RunMode runMode;
    private final WeightedList<Task<? super E>> tasks = new WeightedList<>();

    public CompositeTask(Map<MemoryModuleType<?>, MemoryModuleState> requiredMemoryState, Set<MemoryModuleType<?>> memoriesToForgetWhenStopped, CompositeTask.Order order, RunMode runMode, List<Pair<Task<? super E>, Integer>> tasks) {
        super(requiredMemoryState);
        this.memoriesToForgetWhenStopped = memoriesToForgetWhenStopped;
        this.order = order;
        this.runMode = runMode;
        tasks.forEach((pair) -> this.tasks.add(pair.getFirst(), pair.getSecond()));
    }

    protected boolean shouldKeepRunning(ServerWorld world, E entity, long time) {
        return this.tasks.stream()
                .filter(
                        (task) -> task.getStatus() == Status.RUNNING)
                .anyMatch(
                        (task) -> ((MixinTask) task).invokeShouldKeepRunning(world, entity, time)
                );
    }

    protected boolean isTimeLimitExceeded(long time) {
        return false;
    }

    protected void run(ServerWorld world, E entity, long time) {
        this.order.apply(this.tasks);
        this.runMode.run(this.tasks, world, entity, time);
    }

    protected void keepRunning(ServerWorld world, E entity, long time) {
        this.tasks.stream().filter((task) -> task.getStatus() == Status.RUNNING).forEach((task) -> task.tick(world, entity, time));
    }

    protected void finishRunning(ServerWorld world, E entity, long time) {
        this.tasks.stream().filter((task) -> task.getStatus() == Status.RUNNING).forEach((task) -> task.stop(world, entity, time));
        Brain<?> var10001 = entity.getBrain();
        this.memoriesToForgetWhenStopped.forEach(var10001::forget);
    }

    public String toString() {
        Set<? extends Task<? super E>> set = this.tasks.stream().filter((task) -> task.getStatus() == Status.RUNNING).collect(Collectors.toSet());
        return "(" + this.getClass().getSimpleName() + "): " + set;
    }

    enum RunMode {
        RUN_ONE {
            public <E extends LivingEntity> void run(WeightedList<Task<? super E>> tasks, ServerWorld world, E entity, long time) {
                tasks.stream().filter((task) -> task.getStatus() == Status.STOPPED).filter((task) -> task.tryStarting(world, entity, time)).findFirst();
            }
        },
        TRY_ALL {
            public <E extends LivingEntity> void run(WeightedList<Task<? super E>> tasks, ServerWorld world, E entity, long time) {
                tasks.stream().filter((task) -> task.getStatus() == Status.STOPPED).forEach((task) -> task.tryStarting(world, entity, time));
            }
        };

        RunMode() {
        }

        public abstract <E extends LivingEntity> void run(WeightedList<Task<? super E>> tasks, ServerWorld world, E entity, long time);
    }

    enum Order {
        ORDERED((weightedList) -> {
        }),
        SHUFFLED(WeightedList::shuffle);

        private final Consumer<WeightedList<?>> listModifier;

        Order(Consumer<WeightedList<?>> listModifier) {
            this.listModifier = listModifier;
        }

        public void apply(WeightedList<?> list) {
            this.listModifier.accept(list);
        }
    }
}
