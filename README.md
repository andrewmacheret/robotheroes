# robotheroes

A java bot that plays the [Clicker Heroes](https://clickerheroes.com) end game for you.

![Bot screenshot](data/screenshot.png?raw=true "Bot screenshot")

##### This bot will not work correctly until you have amassed the following:
 1. Iris level ~150
  - Needs to be high enough so that you should be able to immediately purchase Frostleaf after clicking a candy
 2. Vaagur level 15 (max) - for proper cooldown management.
  - This requirement can be skirted by modifying [COOLDOWN_PERIOD](https://github.com/andrewmacheret/robotheroes/blob/master/src/com/macheret/robotheroes/RobotHero.java#L32) to Powersurge's cooldown + 1 second for safety (base 15 minutes, or 2.5 minutes after maxing Vaagur's level).
 3. Klepto level 30 (max) - this requirement is minor however and can be modified by changing [GOLDEN_CLICK_SECONDS](https://github.com/andrewmacheret/robotheroes/blob/master/src/com/macheret/robotheroes/RobotHero.java#L26) to Golden Click's duration
 4. All your gilds are on Frostleaf or later
 5. Turn off relic popups in the game config. It's slightly helpful to also turn off all popup text and turn on tiny monsters, but that is definitely not required.

##### Features:
 1. The bot will click the enemy a LOT (40 times per second) and collect gold faster than a human
 2. Every 5 seconds the bot will attempt to scroll down the hero screen, level the bottom-most hero 20 times and buy all available upgrades. Upon reaching a gilded hero, the bot will stop scrolling down.
 3. Cooldowns will be used in the following order (by default Clickstorm is disabled):
  1. 1, 2, 3, 4, 5, 7, 8, 6, 9 ([EDR combo](https://www.reddit.com/r/ClickerHeroes/comments/2j5v1k/about_edr_combo/))
  2. (wait 15 minutes)
  3. 8, 9, 1, 2, 3, 4, 5, 7
  4. (wait 2.5 minutes)
  5. 1, 2
  6. (wait 2.5 minutes)
  7. 1, 2
  8. (wait 2.5 minutes)
  9. 1, 2, 3, 4
  10. (wait 2.5 minutes)
  11. 1, 2
  12. (wait 2.5 minutes)
  13. 1, 2
  14. (wait 2.5 minutes, then start over again)
 4. Every 10 seconds the bot will check for candy
  - If before ascension time, click the candy immediately
  - If after ascension time [(60 minutes by default)](https://github.com/andrewmacheret/robotheroes/blob/master/src/com/macheret/robotheroes/RobotHero.java#L27), confirm that the candy is there 3 times to ensure no false positives, then begin the ascension process
 5. During ascension:
  1. Stop all normal enemy clicking, hero leveling, cooldown usage, and candy clicking
  2. Click relic screen and salvage the new relic if there is one
  3. Click ascend and confirm
  4. Change to progression mode (ascending always changes the game back to farm mode)
  5. Click candy, wait for gold to drop and then collect that gold
  6. Click hero screen, level all heroes up to and including Frostleaf to maximum level required for all their abilities
  7. Resume normal botting (resetting the cooldown state)

##### Things this bot does not do, and you need to handle manually:
 1. Spending your hero souls for you. I recommend an [ancients optimizer](http://s3-us-west-2.amazonaws.com/clickerheroes/ancientssoul.html) for that.
 2. Choosing which relic to salvage. The bot automatically salvages the newest relic.
 3. Command mercenaries on quests
 4. Organize your gilds for you. Be careful - if you've put all your gilds into Atlas and then you receive a random gild on Frostleaf, the bot will stop progressing past Frostleaf.
 5. Save your game

##### Prerequesites to run:
 1. [Java 7](http://www.oracle.com/technetwork/java/javase/downloads/index.html) or greater
 2. A browser that can run Clicker Heroes (a shockwave flash game)
 3. [Download the jar file](https://github.com/andrewmacheret/robotheroes/blob/master/target/robotheroes-1.0.jar?raw=true) or use [git](https://git-scm.com/) the clone the source with `git clone git@github.com:andrewmacheret/robotheroes.git`

##### To run:
 1. Level all heroes before Frostleaf to high enough level to upgrade all abilties, and level Frostleaf at least once, and turn on progression mode
 2. Scroll down to the very bottom of the hero list
 3. If necessary, use `mvn package` if you made any changes to the source files (requires [mvn](https://maven.apache.org/))
 4. Start the bot: `java -jar target/robotheroes-1.0.jar`
 5. Follow instructions:
  - Turn off capslock if it is on
  - Focus on the window Clicker Heroes is running in and move the mouse to the top left corner of the game, then press capslock
  - Press capslocks 2 more times to confirm you are ready
 6. Watch the magic
  - Don't move the window while the bot is running
  - In order to pause the bot, turn off capslock. To resume, turn on capslock.
