package de.hysky.ravengarder.events;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@Environment(EnvType.CLIENT)
public class RavengardEvents {
	public static Event<RavengardJoin> JOIN = EventFactory.createArrayBacked(RavengardJoin.class, callbacks -> () -> {
		for (RavengardJoin callback : callbacks) {
			callback.onRavengardJoin();
		}
	});

	public static Event<RavengardLeave> LEAVE = EventFactory.createArrayBacked(RavengardLeave.class, callbacks -> () -> {
		for (RavengardLeave callback : callbacks) {
			callback.onRavengardLeave();
		}
	});

	@Environment(EnvType.CLIENT)
	@FunctionalInterface
	public interface RavengardJoin {
		void onRavengardJoin();
	}

	@Environment(EnvType.CLIENT)
	@FunctionalInterface
	public interface RavengardLeave {
		void onRavengardLeave();
	}
}
