all:
	mvn clean package
	mkdir -p ./server/plugins
	cp ./target/QuestSys-0.33b.jar ./server/plugins/QuestSys-0.33b.jar
	cd ./server && java -jar ./purpur.jar --nogui 

run:
	mkdir -p ./server/plugins
	cp ./target/QuestSys-0.33b.jar ./server/plugins/QuestSys-0.33b.jar
	cd ./server && java -jar ./purpur.jar --nogui 

build:
	mvn clean package
	
install:
	mkdir -p ./server 
	wget -O ./server/purpur.jar https://api.purpurmc.org/v2/purpur/1.21.11/2563/download
	echo "eula=true" > ./server/eula.txt
	echo -e \
"accepts-transfers=false\n"\
"allow-flight=false\n"\
"broadcast-console-to-ops=true\n"\
"broadcast-rcon-to-ops=true\n"\
"bug-report-link=\n"\
"debug=false\n"\
"difficulty=peaceful\n"\
"enable-code-of-conduct=false\n"\
"enable-jmx-monitoring=false\n"\
"enable-query=false\n"\
"enable-rcon=false\n"\
"enable-status=true\n"\
"enforce-secure-profile=true\n"\
"enforce-whitelist=false\n"\
"entity-broadcast-range-percentage=100\n"\
"force-gamemode=false\n"\
"function-permission-level=2\n"\
"gamemode=creative\n"\
"generate-structures=true\n"\
"generator-settings={}\n"\
"hardcore=false\n"\
"hide-online-players=false\n"\
"initial-disabled-packs=\n"\
"initial-enabled-packs=vanilla\n"\
"level-name=world\n"\
"level-seed=\n"\
"level-type=flat\n"\
"log-ips=true\n"\
"management-server-allowed-origins=\n"\
"management-server-enabled=false\n"\
"management-server-host=localhost\n"\
"management-server-port=0\n"\
"management-server-secret=9BxfxhLM1PzInuHHbSzxi8YDT3kseOal9YrNNfDj\n"\
"management-server-tls-enabled=true\n"\
"management-server-tls-keystore=\n"\
"management-server-tls-keystore-password=\n"\
"max-chained-neighbor-updates=1000000\n"\
"max-players=20\n"\
"max-tick-time=60000\n"\
"max-world-size=29999984\n"\
"motd=A Minecraft Server\n"\
"network-compression-threshold=256\n"\
"online-mode=false\n"\
"op-permission-level=4\n"\
"pause-when-empty-seconds=-1\n"\
"player-idle-timeout=0\n"\
"prevent-proxy-connections=false\n"\
"query.port=25565\n"\
"rate-limit=0\n"\
"rcon.password=\n"\
"rcon.port=25575\n"\
"region-file-compression=deflate\n"\
"require-resource-pack=false\n"\
"resource-pack=\n"\
"resource-pack-id=\n"\
"resource-pack-prompt=\n"\
"resource-pack-sha1=\n"\
"server-ip=\n"\
"server-name=Unknown Server\n"\
"server-port=25565\n"\
"simulation-distance=10\n"\
"spawn-protection=16\n"\
"status-heartbeat-interval=0\n"\
"sync-chunk-writes=true\n"\
"text-filtering-config=\n"\
"text-filtering-version=0\n"\
"use-native-transport=true\n"\
"view-distance=10\n"\
"white-list=false" > ./server/server.properties