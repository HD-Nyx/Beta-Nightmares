# Beta Nightmares 
<img src="src/main/resources/mod_icon.png" width="50" height="50" align="middle"> By HD_Nyx

A Minecraft Neoforge server side mod that reimplements the nightmare feature that was added from beta 1.3 and removed in release 1.0.

In Beta, sleeping in an exposed spot could get you punished, if a mob could pathfind to near your bed, you'd be woken up by it. The feature was later removed and replaced with the message "You may not sleep now; monsters are nearby". 

I personally think this removal was extremely dumb because the new sleep change forces players to mindlessly spam the bed to sleep when night is approaching instead of letting the player decide on quickly fortifying themselves before sleeping or continue their tasks with the risk of night time.

This mod reimplements that feature so players are now forced to actually settle down and have a somewhat structured base to progress night without being woken up.

Aside from adding that, I personally added a message to the player so they aren't confused upon the first time being woken up.

I also added configuration support for what mobs can wake you up, by default it is a zombie, skeleton and spider (just like beta) but you can completely change if that if you want.
Not only that but custom command execution for when a nightmare occurs, your welcome, modpack devs.

> [!NOTE]
> Changes to config wont apply if you are currently in the world, rejoin the world for it to apply

> [!WARNING]
> There is currently no erroring for command execution, please check your commands work before adding it to the config file.
> Also only mobs under the category 'MobCategory.MONSTER' will properly spawn in a nightmare for safety reasons

This mod uses the Apache 2.0 License
For modpack developers, only if you have a credits section then please credit my work. 
