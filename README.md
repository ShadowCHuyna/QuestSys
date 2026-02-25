server core: [purpur](https://purpurmc.org/download/purpur)

TODO:
- [ ] db
- [ ] SCOREBOARD

сохранять данные так:
```yml
uuid:
  id: example_id
  executors:
	  nick: uuid
	  nick: uuid
	  nick: uuid
	  nick: uuid
  conditions:
	  -walk:
		  # block: DIRT
		  target_count: 10
		  count: 5
```

cmds:
- [X] /QuestSys add clan.daily.easy {SCOREBOARD} {PLAYER_0} {PLAYER_1} {PLAYER_n}
- [ ] /QuestSys {QWEST_UUID} remove {PLAYER} {PLAYER_n}
- [ ] /QuestSys {QWEST_UUID} append {PLAYER} {PLAYER_n}
- [X] /QuestSys get {PLAYER}

conditions:
- [X] walk
- [ ] jump
- [ ] kill
- [ ] block
- [ ] damage
- [ ] craft

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
                    on_complite: # выполнится для всех исполнителей
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