server core: [purpur](https://purpurmc.org/download/purpur)

TODO:
- [ ] необязательные поля
- [ ] баг при удалении квеста 
```
[02:27:54 ERROR]: Command exception: /questsys b949a252-34ef-4a41-bd3a-2510b71a66b3 delete
org.bukkit.command.CommandException: Unhandled exception executing command 'questsys' in plugin QuestSys v0.33b
        at org.bukkit.command.PluginCommand.execute(PluginCommand.java:47) ~[purpur-api-1.21.11-R0.1-SNAPSHOT.jar:?]
        at io.papermc.paper.command.brigadier.bukkit.BukkitCommandNode$BukkitBrigCommand.run(BukkitCommandNode.java:83) ~[purpur-1.21.11.jar:1.21.11-2563-e4663d8]
        at com.mojang.brigadier.context.ContextChain.runExecutable(ContextChain.java:73) ~[brigadier-1.3.10.jar:?]
        at net.minecraft.commands.execution.tasks.ExecuteCommand.execute(ExecuteCommand.java:30) ~[purpur-1.21.11.jar:1.21.11-2563-e4663d8]
        at net.minecraft.commands.execution.tasks.ExecuteCommand.execute(ExecuteCommand.java:13) ~[purpur-1.21.11.jar:1.21.11-2563-e4663d8]
        at net.minecraft.commands.execution.UnboundEntryAction.lambda$bind$0(UnboundEntryAction.java:8) ~[purpur-1.21.11.jar:1.21.11-2563-e4663d8]
        at net.minecraft.commands.execution.CommandQueueEntry.execute(CommandQueueEntry.java:5) ~[purpur-1.21.11.jar:1.21.11-2563-e4663d8]
        at net.minecraft.commands.execution.ExecutionContext.runCommandQueue(ExecutionContext.java:104) ~[purpur-1.21.11.jar:1.21.11-2563-e4663d8]
        at net.minecraft.commands.Commands.executeCommandInContext(Commands.java:477) ~[purpur-1.21.11.jar:1.21.11-2563-e4663d8]
        at net.minecraft.commands.Commands.performCommand(Commands.java:382) ~[purpur-1.21.11.jar:1.21.11-2563-e4663d8]
        at net.minecraft.commands.Commands.performCommand(Commands.java:370) ~[purpur-1.21.11.jar:1.21.11-2563-e4663d8]
        at net.minecraft.server.network.ServerGamePacketListenerImpl.performUnsignedChatCommand(ServerGamePacketListenerImpl.java:2456) ~[purpur-1.21.11.jar:1.21.11-2563-e4663d8]
        at net.minecraft.server.network.ServerGamePacketListenerImpl.lambda$handleChatCommand$15(ServerGamePacketListenerImpl.java:2429) ~[purpur-1.21.11.jar:1.21.11-2563-e4663d8]
        at net.minecraft.server.TickTask.run(TickTask.java:18) ~[purpur-1.21.11.jar:1.21.11-2563-e4663d8]
        at net.minecraft.util.thread.BlockableEventLoop.doRunTask(BlockableEventLoop.java:177) ~[purpur-1.21.11.jar:1.21.11-2563-e4663d8]
        at net.minecraft.util.thread.ReentrantBlockableEventLoop.doRunTask(ReentrantBlockableEventLoop.java:24) ~[purpur-1.21.11.jar:1.21.11-2563-e4663d8]
        at net.minecraft.server.MinecraftServer.doRunTask(MinecraftServer.java:1561) ~[purpur-1.21.11.jar:1.21.11-2563-e4663d8]
        at net.minecraft.server.MinecraftServer.doRunTask(MinecraftServer.java:188) ~[purpur-1.21.11.jar:1.21.11-2563-e4663d8]
        at net.minecraft.util.thread.BlockableEventLoop.pollTask(BlockableEventLoop.java:151) ~[purpur-1.21.11.jar:1.21.11-2563-e4663d8]
        at net.minecraft.server.MinecraftServer.pollTaskInternal(MinecraftServer.java:1541) ~[purpur-1.21.11.jar:1.21.11-2563-e4663d8]
        at net.minecraft.server.MinecraftServer.pollTask(MinecraftServer.java:1535) ~[purpur-1.21.11.jar:1.21.11-2563-e4663d8]
        at net.minecraft.server.MinecraftServer.recordTaskExecutionTimeWhileWaiting(MinecraftServer.java:1240) ~[purpur-1.21.11.jar:1.21.11-2563-e4663d8]
        at net.minecraft.server.MinecraftServer.runServer(MinecraftServer.java:1377) ~[purpur-1.21.11.jar:1.21.11-2563-e4663d8]
        at net.minecraft.server.MinecraftServer.lambda$spin$2(MinecraftServer.java:392) ~[purpur-1.21.11.jar:1.21.11-2563-e4663d8]
        at java.base/java.lang.Thread.run(Thread.java:1583) ~[?:?]
Caused by: java.lang.NullPointerException: Cannot invoke "com.sch.Quest.Quest.Delete()" because the return value of "com.sch.Quest.QuestManager.Get(java.util.UUID)" is null
        at QuestSys-0.33b.jar//com.sch.Quest.QuestManager.Remove(QuestManager.java:52) ~[?:?]
        at QuestSys-0.33b.jar//com.sch.Commands.QuestSysCmdRouter.Delete(QuestSysCmdRouter.java:75) ~[?:?]
        at QuestSys-0.33b.jar//com.sch.Commands.QuestSysCmdRouter.onCommand(QuestSysCmdRouter.java:44) ~[?:?]
        at org.bukkit.command.PluginCommand.execute(PluginCommand.java:45) ~[purpur-api-1.21.11-R0.1-SNAPSHOT.jar:?]
        ... 24 more
```

сохраняются данные так:
```yml
uuid:
    id: example_id
    start_time: 1984
    is_end: false
    executors:
        - nick
        - nick
        - nick
        - nick
    conditions:
          - walk:
                # block: DIRT
                target_count: 10
                count: 5
```

cmds:
- /questsys add clan.daily.easy {SCOREBOARD} {PLAYER_0} {PLAYER_1} {PLAYER_n}
- /questsys {QWEST_UUID} remove {PLAYER} {PLAYER_n}
- /questsys {QWEST_UUID} append {PLAYER} {PLAYER_n}
- /questsys {QWEST_UUID} delete
- /questsys get {PLAYER}

conditions:
- [X] walk
- [X] jump
- [X] kill
- [X] block
- [X] damage
- [X] craft

config example:
```yml
all:
    player:
        daily:
            easy:
                example_id:
                    name: example name
                    description: aboba
                    exp: 100
                    live_time: 3600
                    on_complete: # выполнится для всех исполнителей
                        - say {player} aboba
                    on_fail:
                        - say {player} ne aboba
                    conditions:
                        - walk:
                            # block: dirt
                            range: 
                                - 1
                                - 10
                        - jump:
                            # block: dirt 
                            range: 
                                - 10
                                - 100
                        - kill:
                            # entity: zombie
                            range: 
                                - 10
                                - 100
                        - block:
                            action: click # placement | destroy | l_click | r_click
                            block: ice
                            range: 
                                - 10
                                - 100
                        - damage:
                            direction: e2p # p2e
                            # entity: FROG
                            range: 
                                - 10
                                - 100
                        - craft:
                          # item: bed
                            range: 
                                - 10
                                - 100
        weekly:
        monthly:
    clan:
        daily:
        weekly:
        monthly:
```