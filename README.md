# HClientv1
An old client that I made for use on kaboom.pw. It was originally for personal use but I decided to release it anyway. Some of the features are broken either because I didn't finish porting them back when I was porting from mcp to fabric or because they no longer work in the most recent version.

## Commands
You can just write commands in chat directly and they'll be caught. There is no command prefix.
### fc
Enables fast click, in which right clicking repeats very fast.
### rc
Returns clicking to normal speed
### throw \<amount>
broken

makes each right click throw multiple items at once
### immortal
No longer works in current version of Kaboom.

When you die, death is ignored and you continue moving around in a half-alive state. You can interact with the world and chat, but people can't kill you, even with /kill.
### drop
Toggles drop mode.

In drop mode, it spams look packets that make you look in random directions and constantly throws copies of whatever item you're holding using creative inventory actions. It's meant to spew items all over the place, but it doesn't work very well in current versions of Kaboom due to item drop limits.
### dropexplode \<number>
Similar to drop, except it happens all at once. The number specified is the number of items thrown out in random directions. Doesn't work very well in current versions of Kaboom due to item drop limits.
### scaffold
Toggles scaffolding, which should be self explanatory.
### push
Toggles whether you are able to be pushed by other mobs. The toggling is a bit janky because I was lazy with porting.
### rtp
Makes you rapidly move up to sky level and then move randomly very far away, so long as your path is not obstructed by other blocks.
### recordchat
Toggles whether regular chat is recorded in the command log.
### clearlogs
Clears the command log
### listuuid/uuidlist
Lists the indexes of the players in tablist
### setuuid [index]
Sets $uuid to the uuid corresponding to the index specified. The indexes of different players can be listed with the listuuid command. The indexes are also shown in the chatter window at the top.
### saveuuid
Detects which players have not yet changed their username with /username (based on whether their name in tablist matches their uuid) and saves the username-uuid pair. Beside a person's name in the tablist panel of the Chatter window, it will display a name in parentheses if their uuid is saved. The name in parentheses is their login name.
### updateuuid
Logs in a bot called uuid_updater. This bot grabs the tablist entries of players in vanish and ads them to your tablist.
### give
An old command that I made to give me a funny sword. It doesn't work anymore since I didn't bother to port it.
### give \<filename>
Filename corresponds to a file in the ".minecraft/chathacks nbt files" folder. It will give you the item corresponding to the nbt file.
### getnbt
Puts the nbt of whatever item you're holding in .minecraft/NBT_Log.txt
### fromnbt
Grabs the nbt from .minecraft/NBT_Give.txt and puts in your hand.
### savenbt \<filename>
Saves the nbt of the item you're currently holding to ".minecraft/chathacks nbt files". You don't need to add an extension because the client will automatically append .txt to the end of your filename.
### stp \<uuid>
If you're in spectator mode, you'll teleport to the entity with the specified uuid.
### cb \<command>
Places a command block that runs the specified command.
### crypt \<command>
Adds the hash to an HBot command. The key is intentionally removed from the code, so it will not work as is.
### cval \<command>
Appends the correct 2fa token for CoreBot commands. I forgot to remove my token from here so I'll just ask for it to be changed if we start working on CoreBot again.
### rainbowchat \<text>
Your chatted text will be rainbow.
### bot [flags] \<login message>
Logs in a bot with a random name. It says the specified login message as soon as it registers that it has successfully joined the server. The login message can be a command.

The bot recognizes the following flags:
* -w sets the bot's movement pattern to "walk"
* -o sets the bot's movement pattern to "orbit"
* -f sets te bot's movement pattern to "flank"
* -r sets the bot's movement pattern to "random"
* -c enables copy mode on the bot
* -d enables drop mode on the bot
* -a enables attack mode on the bot
* -i appends the bot's id to the end of their name (zero-padded to 4 digits)

You cannot combine flags like `bot -oca hi`. The flags have to be like this: `bot -o -c -a hi`.
### botname \<name> [flags] \<login message>
Logs in a bot with the specified name. `&` symbols will be replaced with `§`.
### botsay \<text>
Makes all bots say something in chat. It can be a command.
### botsayid \<id> \<text>
Makes the bot with the specified id say something in chat. Bot ids are assigned in the order that they are created (and reset when you disconnect them "dball").
### botmove \<all/id> \<walk/orbit/flank/random/w/o/f/r>
Makes bots move with the specified movement pattern. You can specify either a specific bot id or "all".
### botcopy \<all/id> \<on/off>
Toggles copy mode for bots.

When copy mode is enabled, the bot will copy your look direction, left clicks, and right clicks.
### botdrop \<all/id> \<on/off>
Drop mode but for bots. You can specify the item held in their hand with the bothand command.
### botattack \<all/id> \<on/off>
Toggles attack mode for bots. Attack mode will cause the bots to attack all living entities in range. The bot ignores you, other bots, and invulnerable entities (such as players in creative mode).
### hitmode \<regular/multi/r/m>
Changes the bot's attack style. In regular mode, the bot will have a 12-tick cooldown between hits and only hit the closest entity. In multi mode, the bot will attack every single entity in range every single tick. The default mode is multi.
### bothand \<all/id>
Makes bots copy the current item in your hand into theirs.
### bottoss \<all/id> \<filename>
Makes bots throw out an item from ".minecraft/chathacks nbt files".
### botplace \<all/id> \<filename>
Makes the bots attempt to place the item specified below them. Only works if it is an item that can be placed.
### cmode \<0-2>
Changes the command log mode. In mode 0, commands do not appear in chat. In mode 1, commands of people with commandspy appear in chat. In mode 2, all commands appear in chat. Regardless of this setting, the commands will show up in the chatter window. In the current version of kaboom, there is no difference between modes 1 and 2 because commandspy no longer appears in two different colors.
### dball
Disconnects all bots. This will also reset the bot id counter.
### db \<id>
Disconnects a specific bot. This does not affect the id counter.
### pb
Stands for "pop bot". Removes the most recent bot and decrements the bot id counter.
### recoverbot/rb
Picks a bot that you tried to log in but failed and tries to log it in again. If some of your bots fail to log in, just say rb one by one until they're all logged in, but not too fast because the server limits logins to once every 5 seconds. In fact, if I remember correctly, you may experience some errors if you spam rb too fast.
### 

## Chatter
The chatter is a separate window that pops up with various displays and features.
### Username List
The top panel shows a list of usernames alongside their uuid. If you have any saved uuids, it will also show players' login names in parentheses. This list is based on tablist entries, so it will only display the players that exist in tablist. In order to see vanished players, try using `updateuuid`.
### $plist
The grey box on the left is a list of entries that you can assign to $plist. When you say something containing $plist, it expands it by looping through plist and, for each entry, sending a command that replaces $plist with that entry. Due to the chat ratelimit that was implemented, this doesn't really work anymore.
### Command log
The right pane is a log of commandspy commands.
### Center Pane
The central pane is where you can type messages you want to be sent to chat. If you type multiple messages, they all get sent (although current versions of kaboom have a ratelimit so it won't necessarily work that well).

If a line is preceded by `loop`, the message  will be looped. You can also preceed a line by sloop and floop which correspond to slow loop and fast loop respectively. This is compatible with client commands, so for example, if you do `loop botsayid 0 hi there` the bot with id 0 will say "hi there" on a loop. This is a convenient way to do basic "scripting" of the bot.

You can also do `setusername <username>` in this pane to change your username for the next login.

The messages and directives in this pane only get processed when you hit the reload button.
### Reload button
Processes the stuff you write in the center pane.

## Chat Replacements
Some terms (generally starting with $) will get replaced by the client.
### $uuid
Replaced with the uuid you set with `setuuid`
### $plist
Recursively replaced with the contents of the plist pane in the Chatter window. Multiple list entries does not work well in the current version of Kaboom due to the chat ratelimit.
### $ulist
Recursively replaced with the uuids of players in tablist. Does not work well in the current version of Kaboom due to the chat ratelimit.
### $tlist
Recursively replaced with the names of players in tablist. Does not work well in the current version of Kaboom due to the chat ratelimit.
### $coords
Replaced with your current block coordinates (integer coordinates).
### $mapcoords
Replaced with the corner of the nearest map grid tile.
