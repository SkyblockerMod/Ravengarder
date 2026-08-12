package de.hysky.ravengarder.utils;

import de.hysky.ravengarder.events.RavengardEvents;
import net.azureaaron.hmapi.events.HypixelPacketEvents;
import net.azureaaron.hmapi.network.packet.s2c.HypixelS2CPacket;
import net.azureaaron.hmapi.network.packet.v1.s2c.LocationUpdateS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

import java.util.Optional;

public class RavengardInfo {
    private static boolean isOnRavengard = false;
    private static RavengardLocation location = RavengardLocation.UNKNOWN;

    public static boolean isOnRavengard() {
        return isOnRavengard;
    }

    public static RavengardLocation getLocation() {
        return location;
    }

    public static void init() {
        ClientPlayConnectionEvents.DISCONNECT.register((_, _) -> onDisconnect());
        HypixelPacketEvents.HELLO.register(RavengardInfo::onPacket);
        HypixelPacketEvents.LOCATION_UPDATE.register(RavengardInfo::onPacket);
        HypixelPacketEvents.PLAYER_INFO.register(RavengardInfo::onPacket);
    }

    private static void onPacket(HypixelS2CPacket packet) {
        switch (packet) {
            case LocationUpdateS2CPacket(_, _, _, Optional<String> mode, Optional<String> map) -> {
                if (mode.isPresent() && mode.get().startsWith("RAVENGARD")) {
                    onJoin(mode.get(), map.orElse(""));
                } else {
                    onDisconnect();
                }
            }
            default -> {}
        }
    }

    private static void onJoin(String mode, String map) {
        boolean wasOnRavengard = isOnRavengard;

        isOnRavengard = true;
        location = RavengardLocation.from(mode);

        if (!wasOnRavengard) {
            RavengardEvents.JOIN.invoker().onRavengardJoin();
        }
    }

    private static void onDisconnect() {
        boolean wasOnRavengard = isOnRavengard;

        isOnRavengard = false;
        location = RavengardLocation.UNKNOWN;

        if (wasOnRavengard) {
            RavengardEvents.LEAVE.invoker().onRavengardLeave();
        }
    }
}
