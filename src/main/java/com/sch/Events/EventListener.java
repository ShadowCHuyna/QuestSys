package com.sch.Events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.sch.QuestSys;
import com.sch.Executors.Executor;
import com.sch.Executors.ExecutorManager;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventListener implements Listener {
	private static EventListener instance;

	public static EventListener PickMe() {
		if (EventListener.instance == null)
			EventListener.instance = new EventListener();
		return EventListener.instance;
	}

	private EventListener() {
		Set<Class<? extends Event>> eventClasses = Stream.of(
                PlayerJoinEvent.class,
                PlayerQuitEvent.class,
                PlayerRespawnEvent.class,
                PlayerPostRespawnEvent.class,
                PlayerDeathEvent.class,
                PlayerInteractEvent.class,
                BlockBreakEvent.class,
                BlockPlaceEvent.class,
                PlayerMoveEvent.class,
				PlayerJumpEvent.class,
                EntityDamageByEntityEvent.class,
                PlayerSwapHandItemsEvent.class,
                PlayerToggleSneakEvent.class,
                PlayerInteractAtEntityEvent.class,
                PlayerItemConsumeEvent.class,
				CraftItemEvent.class
        ).collect(Collectors.toSet());

		for (Class<? extends Event> eventClass : eventClasses) {
			Bukkit.getPluginManager().registerEvent(
				eventClass,
				this,
				EventPriority.NORMAL,
				(l, e) -> this.onAnyEvent(e),
				QuestSys.PickMe()
			);
		}
	}

	private final Map<Executor, Map<EventTypes, CopyOnWriteArrayList<Consumer<Event>>>> subscriptionTable = new HashMap<>();

	private ExecutorManager executorManager = ExecutorManager.PickMe();

	private void CallEvent(EventTypes eventType, Event event, Executor executor) {
		Map<EventTypes, CopyOnWriteArrayList<Consumer<Event>>> executorMap =
				subscriptionTable.get(executor);
		if (executorMap == null)
			return;

		CopyOnWriteArrayList<Consumer<Event>> handlers =
				executorMap.get(eventType);
		if (handlers == null)
			return;

		for (Consumer<Event> handler : handlers) {
			handler.accept(event);
		}
	}

	public Subscribe Subscribe(EventTypes eventType, ArrayList<Executor> executors, Consumer<Event> handler) {
		return new Subscribe(eventType, executors, handler);
	}

	public void AddHandler(EventTypes eventType, Executor executor, Consumer<Event> handler) {
		Map<EventTypes, CopyOnWriteArrayList<Consumer<Event>>> byEvent =
				subscriptionTable.computeIfAbsent(executor, k -> new HashMap<>());

		CopyOnWriteArrayList<Consumer<Event>> handlers =
				byEvent.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>());

		handlers.add(handler);
	}


	public void RemoveHandler(EventTypes eventType, Executor executor, Consumer<Event> handler) {

		Map<EventTypes, CopyOnWriteArrayList<Consumer<Event>>> byEvent =
				subscriptionTable.get(executor);
		if (byEvent == null)
			return;

		CopyOnWriteArrayList<Consumer<Event>> handlers =
				byEvent.get(eventType);
		if (handlers == null)
			return;

		handlers.remove(handler);

		if (handlers.isEmpty())
			byEvent.remove(eventType);

		if (byEvent.isEmpty())
			subscriptionTable.remove(executor);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Executor e = executorManager.Get(event.getPlayer().getName());
		e.SetPlayer(event.getPlayer());
	}

	@EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
		Executor e = executorManager.Get(event.getPlayer().getName());
		e.SetPlayer(null);
    }

	public void onAnyEvent(Event event){
		Player player = null;

		if (event instanceof PlayerEvent pe) {
			player = pe.getPlayer();
		} else if (event instanceof EntityDamageByEntityEvent ede && (ede.getDamager() instanceof Player || ede.getEntity() instanceof Player)) {
			if(ede.getDamager() instanceof Player) player = (Player)ede.getDamager();
			else if(ede.getEntity() instanceof Player)player = (Player)ede.getEntity();
		} else if(event instanceof CraftItemEvent ce){
			player = (Player) ce.getWhoClicked();
		}

		if (player != null) {
			EventTypes type = EventTypes.valueOf(event.getEventName());
			if (type != null)
				CallEvent(type, event, executorManager.Get(player.getName()));
		}
	}
}
