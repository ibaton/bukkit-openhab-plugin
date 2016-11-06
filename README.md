# bukkit-openhab-plugin

Plugin spigot integrating minecraft with openhab
https://github.com/ibaton/Openhab-Minecraft-Binding

## Youtube Video
https://www.youtube.com/watch?v=TdvkTorzkXU&feature=youtu.be

## Setup
- Download plugin from [here](https://github.com/ibaton/bukkit-openhab-plugin/releases/download/1.5/OHMinecraft.jar). 
- Add jar to Server->plugins.

Set listening port in Server->plugins->OHMinecraft->config.yml

## Things
Server:
- players : Number of connected players.
- maxPlayers : Maximum players allowed on server.
- bukkitVersion : The version of bukkit running on server.
- version : The minecraft version server is running.
- name : The name of server.

Player:
- name : The name of player.
- level : The level of player.
- total experience : The total experiance of player.
- experience : Percentage of experience bar filled for next level.
- health : The current health of player.
- walk_speed : The current walk speed of player.
- location : The location of player.

Sign:
- name : Text on sign (Used to identify sign).
- active : If the sign has active redstone on the blick under it. 
