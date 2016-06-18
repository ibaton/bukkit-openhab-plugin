# bukkit-openhab-plugin

Plugin spigot integrating minecraft with openhab
https://github.com/ibaton/Openhab-Minecraft-Binding

## Youtube Video
https://www.youtube.com/watch?v=TdvkTorzkXU&feature=youtu.be

## Setup
- Download plugin from [here](https://github.com/ibaton/bukkit-openhab-plugin/releases/download/1.5/OHMinecraft.jar). 
- Add jar to Server->plugins.

## Things
Server:
- players : Number of connected players.
- maxPlayers : Maximum players allowed on server.
- bukkitVersion : The version of bukkit running on server.
- version : The minecraft version server is running.
- name : The name of server.
- online : If server is online or not.

Player:
- name : The name of player.
- display name : The name that player is displayed as.
- level : The level of player.
- total experience : The total experiance of player.
- experience : The experiance a player has on current level.
- health : The current health of player.
- walk_speed : The current walk speed of player.
- location x : The x location of player.
- location y : The y location of player.
- location z : The z location of player.
- online : If player is connected to server.

Sign:
- name : Text on sign (Used to identify sign).
- active : If the sign has active redstone on the blick under it. 
