# QuestSys

Квестовая система для Minecraft серверов на Paper API.

## <a name="требования"></a>Требования

- **Minecraft:** 1.21.11
- **Сервер:** Purpur (Paper API)
- **Java:** 21

## <a name="возможности"></a>Возможности

- Создание квестов с различными условиями выполнения
- Поддержка одиночных и групповых квестов
- Временные квесты с ограничением по времени
- Сохранение прогресса выполнения квестов
- Гибкая система наград (команды при выполнении/провале)
- Случайный выбор квеста из группы

## Оглавление

- [Требования](#требования)
- [Возможности](#возможности)
- [Установка](#установка)
- [Команды](#команды)
- [Структура конфигурации](#структура-конфигурации)
  - [Иерархия](#иерархия)
  - [Поля квеста](#поля-квеста)
  - [Плейсхолдеры в командах](#плейсхолдеры-в-командах)
  - [Условия выполнения](#условия-выполнения)
    - [walk (ходьба)](#walk-ходьба)
    - [jump (прыжки)](#jump-прыжки)
    - [kill (убийство)](#kill-убийство)
    - [block (взаимодействие с блоками)](#block-взаимодействие-с-блоками)
    - [damage (урон)](#damage-урон)
    - [craft (крафт)](#craft-крафт)
  - [Пример полной конфигурации](#пример-полной-конфигурации)
- [Сохранение данных](#сохранение-данных)
- [Разработка](#разработка)

## <a name="установка"></a>Установка

1. Скачать последнюю версию плагина
2. Положить файл `QuestSys.jar` в папку `plugins`
3. Перезапустить сервер
4. Настроить квесты в `plugins/QuestSys/config.yml`

## <a name="команды"></a>Команды

| Команда | Описание |
|---------|----------|
| `/questsys add <path> <scoreboard> <players...>` | Создать новый квест |
| `/questsys <uuid> remove <players...>` | Удалить игроков из квеста |
| `/questsys <uuid> append <players...>` | Добавить игроков в квест |
| `/questsys <uuid> delete` | Удалить квест |
| `/questsys get <player>` | Показать квесты игрока |
| `/questsys reload` | Перезагрузить конфигурацию |

### Примеры использования

```bash
# Создать квест для игрока
/questsys add player.daily.easy quest1 Player1 Player2

# Удалить игрока из квеста
/questsys b949a252-34ef-4a41-bd3a-2510b71a66b3 remove Player1

# Добавить игрока в квест
/questsys b949a252-34ef-4a41-bd3a-2510b71a66b3 append Player3

# Удалить квест
/questsys b949a252-34ef-4a41-bd3a-2510b71a66b3 delete

# Посмотреть квесты игрока
/questsys get Player1

# Перезагрузить конфиг после изменений
/questsys reload
```

## <a name="структура-конфигурации"></a>Структура конфигурации

### <a name="иерархия"></a>Иерархия

Квесты организованы в произвольную иерархию. Вы сами определяете структуру:

```
all:
    <любая_группа>:
        <подгруппа>:
            ...
                <id_квеста>:
                    name: ...
                    conditions: ...
```

**Примеры групп:**
- `player.daily.easy` - ежедневные лёгкие квесты для игроков
- `player.weekly.hard` - еженедельные сложные квесты
- `clan.daily` - ежедневные квесты для кланов
- `event.special` - ивентовые квесты

### <a name="поля-квеста"></a>Поля квеста

| Поле | Тип | Обязательно | Описание |
|------|-----|-------------|----------|
| `name` | String | Нет | Название квеста |
| `description` | String | Нет | Описание квеста |
| `exp` | Integer | Да | Количество опыта (очков) |
| `live_time` | Integer | Да | Время жизни квеста в секундах |
| `on_complete` | List<String> | Нет | Команды при выполнении (для каждого игрока) |
| `on_fail` | List<String> | Нет | Команды при провале (для каждого игрока) |
| `on_complete_once` | List<String> | Нет | Команды при выполнении (один раз) |
| `on_fail_once` | List<String> | Нет | Команды при провале (один раз) |
| `conditions` | List | Да | Список условий выполнения |

### <a name="плейсхолдеры-в-командах"></a>Плейсхолдеры в командах

- `{player}` - имя игрока-исполнителя
- `{UUID}` - UUID квеста

### <a name="условия-выполнения"></a>Условия выполнения

Поддерживаются следующие типы условий:

#### <a name="walk-ходьба"></a>walk (ходьба)

Условие: пройти определённое расстояние.

```yaml
- walk:
    # Блок под ногами (опционально). Если указано - засчитывается только на этом блоке
    block: DIRT
    
    # Точное количество (альтернатива range)
    target_count: 100
    
    # Или диапазон: случайное значение между min и max
    range:
        - 50
        - 200
```

#### <a name="jump-прыжки"></a>jump (прыжки)

Условие: совершить определённое количество прыжков.

```yaml
- jump:
    # Блок под ногами (опционально)
    block: GRASS
    
    target_count: 50
    # или range: [30, 100]
```

#### <a name="kill-убийство"></a>kill (убийство)

Условие: убить определённое количество мобов/игроков.

```yaml
- kill:
    # Тип моба (опционально). Если не указан - любой моб
    entity: ZOMBIE
    
    target_count: 10
    # или range: [5, 20]
```

**Доступные типы мобов:** ZOMBIE, SKELETON, CREEPER, SPIDER, ENDERMAN, и др. (любой EntityType из Minecraft)

#### <a name="block-взаимодействие-с-блоками"></a>block (взаимодействие с блоками)

Условие: добыть, установить или кликнуть по блоку.

```yaml
- block:
    # Тип блока (опционально). Если не указан - любой блок
    block: DIAMOND_ORE
    
    # Тип взаимодействия:
    # - placement - установка блока
    # - destroy - добыча блока
    # - click - любой клик
    # - l_click - левый клик
    # - r_click - правый клик
    action: destroy
    
    target_count: 20
    # или range: [10, 50]
```

#### <a name="damage-урон"></a>damage (урон)

Условие: нанести или получить урон.

```yaml
- damage:
    # Направление:
    # - p2e - игрок наносит урон сущности
    # - e2p - сущность наносит урон игроку
    direction: p2e
    
    # Тип сущности (опционально)
    entity: ZOMBIE
    
    target_count: 100
    # или range: [50, 200]
```

#### <a name="craft-крафт"></a>craft (крафт)

Условие: создать определённое количество предметов.

```yaml
- craft:
    # Предмет (опционально). Если не указан - любой предмет
    item: DIAMOND_SWORD
    
    target_count: 5
    # или range: [1, 10]
```

### <a name="пример-полной-конфигурации"></a>Пример полной конфигурации

```yaml
all:
    player:
        daily:
            easy:
                collect_dirt:
                    name: "Сбор земли"
                    description: "Собери 10 блоков земли"
                    exp: 50
                    live_time: 3600
                    on_complete:
                        - give {player} diamond 1
                        - say {player} выполнил квест!
                    on_fail:
                        - say {player} не успел выполнить квест :(
                    conditions:
                        - block:
                            action: destroy
                            block: DIRT
                            range: 
                                - 10
                                - 10
                kill_zombies:
                    name: "Охотник на зомби"
                    description: "Убей 5 зомби"
                    exp: 100
                    live_time: 1800
                    on_complete:
                        - give {player} iron_sword 1
                    conditions:
                        - kill:
                            entity: ZOMBIE
                            target_count: 5
            hard:
                epic_quest:
                    name: "Эпический квест"
                    description: "Пройди 100 блоков и прыгни 50 раз"
                    exp: 500
                    live_time: 7200
                    on_complete:
                        - give {player} netherite_sword 1
                        - give {player} diamond 10
                    on_complete_once:
                        - broadcast Игрок {player} выполнил эпический квест!
                    conditions:
                        - walk:
                            target_count: 100
                        - jump:
                            target_count: 50
        weekly:
            weekly_boss:
                name: "Босс недели"
                description: "Убей 100 мобов любого типа"
                exp: 1000
                live_time: 604800
                on_complete:
                    - give {player} dragon_egg 1
                conditions:
                    - kill:
                        target_count: 100
    clan:
        daily:
            clan_build:
                name: "Строительство клана"
                description: "Установите 100 блоков камня"
                exp: 200
                live_time: 3600
                on_complete:
                    - execute as @a[team=clan] run give @s diamond 5
                conditions:
                    - block:
                        action: placement
                        block: STONE
                        target_count: 100
```

## <a name="сохранение-данных"></a>Сохранение данных

Прогресс квестов автоматически сохраняется в файл `plugins/QuestSys/quests.yml`.

### Структура сохранённых данных

```yaml
b949a252-34ef-4a41-bd3a-2510b71a66b3:
    id: collect_dirt
    start_time: 1704067200
    is_end: false
    executors:
        - Player1
        - Player2
    conditions:
        - block:
            action: destroy
            block: DIRT
            target_count: 10
            count: 5
```

| Поле | Описание |
|------|----------|
| `id` | ID квеста из конфигурации |
| `start_time` | Время старта квеста (Unix timestamp) |
| `is_end` | Завершён ли квест |
| `executors` | Список исполнителей квеста |
| `conditions` | Прогресс по каждому условию |

### Восстановление прогресса

При запуске сервера плагин автоматически загружает все сохранённые квесты и восстанавливает прогресс игроков.

## <a name="разработка"></a>Разработка
- go_too
- swim

### Сборка

```bash
mvn clean package
```


### Технические детали

- **API:** Paper 1.21.11
- **Сохранение:** YAML файл (автосохранение каждые 20 секунд)
