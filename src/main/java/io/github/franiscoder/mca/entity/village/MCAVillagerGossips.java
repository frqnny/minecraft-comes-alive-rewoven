package io.github.franiscoder.mca.entity.village;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.util.Util;
import net.minecraft.util.dynamic.DynamicSerializableUuid;
import net.minecraft.village.VillageGossipType;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MCAVillagerGossips {
    private final Map<UUID, MCAVillagerGossips.Reputation> entityReputation = Maps.newHashMap();

    private static int max(int left, int right) {
        return Math.max(left, right);
    }

    private static int mergeReputation(VillageGossipType type, int left, int right) {
        int i = left + right;
        return i > type.maxValue ? Math.max(type.maxValue, left) : i;
    }

    public void decay() {
        Iterator<Reputation> iterator = this.entityReputation.values().iterator();

        while (iterator.hasNext()) {
            Reputation reputation = iterator.next();
            reputation.decay();
            if (reputation.isObsolete()) {
                iterator.remove();
            }
        }

    }

    private Stream<GossipEntry> entries() {
        return this.entityReputation.entrySet().stream().flatMap((entry) -> (entry.getValue()).entriesFor(entry.getKey()));
    }

    private Collection<GossipEntry> pickGossips(Random random, int count) {
        List<GossipEntry> list = this.entries().collect(Collectors.toList());
        if (list.isEmpty()) {
            return Collections.emptyList();
        } else {
            int[] is = new int[list.size()];
            int i = 0;

            int listSize = list.size();
            for (int j = 0; j < listSize; ++j) {
                GossipEntry gossipEntry = list.get(j);
                i += Math.abs(gossipEntry.getValue());
                is[j] = i - 1;
            }

            Set<GossipEntry> set = Sets.newIdentityHashSet();

            for (int k = 0; k < count; ++k) {
                int l = random.nextInt(i);
                int m = Arrays.binarySearch(is, l);
                set.add(list.get(m < 0 ? -m - 1 : m));
            }

            return set;
        }
    }

    private Reputation getReputationFor(UUID target) {
        return this.entityReputation.computeIfAbsent(target, (uUID) -> new Reputation());
    }

    public void shareGossipFrom(MCAVillagerGossips from, Random random, int count) {
        Collection<GossipEntry> collection = from.pickGossips(random, count);
        collection.forEach((gossipEntry) -> {
            int i = gossipEntry.value - gossipEntry.type.shareDecrement;
            if (i >= 2) {
                this.getReputationFor(gossipEntry.target).associatedGossip.mergeInt(gossipEntry.type, i, MCAVillagerGossips::max);
            }

        });
    }

    public int getReputationFor(UUID target, Predicate<VillageGossipType> gossipTypeFilter) {
        Reputation reputation = this.entityReputation.get(target);
        return reputation != null ? reputation.getValueFor(gossipTypeFilter) : 0;
    }

    public void startGossip(UUID target, VillageGossipType type, int value) {
        Reputation reputation = this.getReputationFor(target);
        reputation.associatedGossip.mergeInt(type, value, (integer, integer2) -> MCAVillagerGossips.mergeReputation(type, integer, integer2));
        reputation.clamp(type);
        if (reputation.isObsolete()) {
            this.entityReputation.remove(target);
        }

    }

    public <T> Dynamic<T> serialize(DynamicOps<T> dynamicOps) {
        return new Dynamic<>(dynamicOps, dynamicOps.createList(this.entries().map((gossipEntry) -> gossipEntry.serialize(dynamicOps)).map(Dynamic::getValue)));
    }

    public void deserialize(Dynamic<?> dynamic) {
        dynamic.asStream().map(GossipEntry::deserialize).flatMap((dataResult) -> Util.stream(dataResult.result())).forEach((gossipEntry) -> this.getReputationFor(gossipEntry.target).associatedGossip.put(gossipEntry.type, gossipEntry.value));
    }

    static class Reputation {
        final Object2IntMap<VillageGossipType> associatedGossip;

        Reputation() {
            this.associatedGossip = new Object2IntOpenHashMap<>();
        }

        public int getValueFor(Predicate<VillageGossipType> gossipTypeFilter) {
            return this.associatedGossip.object2IntEntrySet()
                    .stream()
                    .filter(
                            (entry) -> gossipTypeFilter.test(entry.getKey()))
                    .mapToInt((entry) -> entry.getIntValue() * entry.getKey().multiplier)
                    .sum();
        }

        public Stream<GossipEntry> entriesFor(UUID target) {
            return this.associatedGossip.object2IntEntrySet().stream().map((entry) -> new GossipEntry(target, entry.getKey(), entry.getIntValue()));
        }

        public void decay() {
            ObjectIterator<Object2IntMap.Entry<VillageGossipType>> objectIterator = this.associatedGossip.object2IntEntrySet().iterator();

            while (objectIterator.hasNext()) {
                Object2IntMap.Entry<VillageGossipType> entry = objectIterator.next();
                int i = entry.getIntValue() - (entry.getKey()).decay;
                if (i < 2) {
                    objectIterator.remove();
                } else {
                    entry.setValue(i);
                }
            }

        }

        public boolean isObsolete() {
            return this.associatedGossip.isEmpty();
        }

        public void clamp(VillageGossipType gossipType) {
            int i = this.associatedGossip.getInt(gossipType);
            if (i > gossipType.maxValue) {
                this.associatedGossip.put(gossipType, gossipType.maxValue);
            }

            if (i < 2) {
                this.remove(gossipType);
            }

        }

        public void remove(VillageGossipType gossipType) {
            this.associatedGossip.removeInt(gossipType);
        }
    }

    static class GossipEntry {
        public final UUID target;
        public final VillageGossipType type;
        public final int value;

        public GossipEntry(UUID uUID, VillageGossipType villageGossipType, int i) {
            this.target = uUID;
            this.type = villageGossipType;
            this.value = i;
        }

        public static DataResult<MCAVillagerGossips.GossipEntry> deserialize(Dynamic<?> dynamic) {
            return DataResult.unbox(DataResult.instance().group(dynamic.get("Target").read(DynamicSerializableUuid.field_25122), dynamic.get("Type").asString().map(VillageGossipType::byKey), dynamic.get("Value").asNumber().map(Number::intValue)).apply(DataResult.instance(), MCAVillagerGossips.GossipEntry::new));
        }

        public int getValue() {
            return this.value * this.type.multiplier;
        }

        public String toString() {
            return "GossipEntry{target=" + this.target + ", type=" + this.type + ", value=" + this.value + '}';
        }

        public <T> Dynamic<T> serialize(DynamicOps<T> dynamicOps) {
            return new Dynamic<>(dynamicOps, dynamicOps.createMap(ImmutableMap.of(dynamicOps.createString("Target"), DynamicSerializableUuid.field_25122.encodeStart(dynamicOps, this.target).result().orElseThrow(RuntimeException::new), dynamicOps.createString("Type"), dynamicOps.createString(this.type.key), dynamicOps.createString("Value"), dynamicOps.createInt(this.value))));
        }
    }
}

