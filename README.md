server core: [purpur](https://purpurmc.org/download/purpur)

TODO:
- [ ] необязательные поля

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