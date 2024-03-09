# Odablock Plugin [![Plugin Installs](https://img.shields.io/endpoint?url=https://i.pluginhub.info/shields/installs/plugin/c-engineer-completed)](https://runelite.net/plugin-hub/m0bile%20btw) [![Plugin Rank](https://img.shields.io/endpoint?url=https://i.pluginhub.info/shields/rank/plugin/c-engineer-completed)](https://runelite.net/plugin-hub)

##### A plugin for [RuneLite](https://runelite.net/)

Odablock announces when you complete an achievement!

Huge thanks to [Odablock](https://kick.com/odablock) for providing custom recorded audio for this plugin!

Some `actions` might have multiple sounds, whenever there are multiple sounds, the sound being played will be chosen at random.
___
## General Troubleshooting
BEFORE TRYING ANYTHING ELSE, ENABLE THIS IN THE **RUNESCAPE** SETTINGS

![image](https://user-images.githubusercontent.com/62370532/208992085-e2c07494-d8bb-489e-b7f3-ed538175acbc.png)

Whenever this doesn't fix your issue, please feel free to look in the [Issues](https://github.com/DapperMickie/odablock-sounds/issues) section of this github page to see if anyone else had this issue. If there is not an issue open for your specific problem, please open a new issue so we can track all the bugs/problems that you might find using this plugin. We will try to resolve all problems within a reasonable time.
___

## Customising your sounds

### Warning

Because we have a system in place that automatically updates the sounds for this plugin, it is highly recommended to have a backup folder of your custom sounds. The system __will always__ override all the sounds whenever a sound update comes out. This means that after each sound update, you will have to replace all your custom sounds again.

### 1. Locate your `.runelite` folder

On windows this is likely to be here: `C:\Users\<your username>\.runelite`

If you aren't sure, it's the same place that stores your `settings.properties`

Within this `.runelite` folder, there should be a `odablock-sounds` folder, which is where the sound files are downloaded to

### 2. Prepare your sound files

Make sure your files are all `.wav` format (just changing the extension won't work, actually convert them)

Make sure the file name __exactly__ matches the name of the existing file (in `odablock-sounds` folder) you want to replace

### 3. Understand how the files are handled

If you replace an existing file in `odablock-sounds` using exactly the same file name, your sound will be loaded instead

If you place a new file with an unexpected file name in `odablock-sounds`, it will be deleted

If you place a new folder inside `odablock-sounds` that is unexpected, this should be left as is, so can be used to store multiple sounds that you may want to swap in at a future date

If you want to revert to a default sound file, simply delete the relevant file in `odablock-sounds` and the default file will be re-downloaded when the plugin next starts

### 4. If it fails to play your sound

Remove your sound and make sure it plays the default sound for that event - if not, there is something misconfigured in your plugin _or in-game_ settings. For example, the collection log event can only be captured if your _in-game_ notifications for collection log slots are turned on

Check that your file is actually a valid `.wav` and not just a renamed `.mp3` or similar

Check that the file is still there in the `odablock-sounds` folder, if you accidentally used an incorrect file name, it won't have been loaded, and will have been deleted
___

## Other information

### Currently implemented sounds include

- Armadyl Crossbow special attack ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/AcbSpec.java) & sounds [1](https://github.com/DapperMickie/odablock-sounds/blob/sounds/AcbSpec_r1.wav), [2](https://github.com/DapperMickie/odablock-sounds/blob/sounds/AcbSpec_r2.wav) and [3](https://github.com/DapperMickie/odablock-sounds/blob/sounds/AcbSpec_r3.wav))
- Accepting a trade ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/AcceptTrade.java) & [sound](https://github.com/DapperMickie/odablock-sounds/blob/sounds/AcceptTrade.wav))
- Achievement diaries (per tier, not per task. [code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/AchievementDiaries.java) & sounds [1](https://github.com/DapperMickie/odablock-sounds/blob/sounds/AchievementDiary_r1.wav) and [2](https://github.com/DapperMickie/odablock-sounds/blob/sounds/AchievementDiary_r2.wav))
- Armadyl Godsword special attack ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/AgsSpec.java) & sounds [1](https://github.com/DapperMickie/odablock-sounds/blob/sounds/AgsSpec_r1.wav), [2](https://github.com/DapperMickie/odablock-sounds/blob/sounds/AgsSpec_r2.wav) and [3](https://github.com/DapperMickie/odablock-sounds/blob/sounds/AgsSpec_r3.wav))
- Collection log slot (requires game chat notification messages. [code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/CollectionLog.java) & [sound](https://github.com/DapperMickie/odablock-sounds/blob/sounds/ColLogSlotCompleted_r1.wav))
- Combat achievements (per task, not per tier. [code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/CombatAchievements.java) & sounds [1](https://github.com/DapperMickie/odablock-sounds/blob/sounds/CombatTaskCompleted_r1.wav), [2](https://github.com/DapperMickie/odablock-sounds/blob/sounds/CombatTaskCompleted_r2.wav), [3](https://github.com/DapperMickie/odablock-sounds/blob/sounds/CombatTaskCompleted_r3.wav) and [4](https://github.com/DapperMickie/odablock-sounds/blob/sounds/CombatTaskCompleted_r4.wav))
- Chambers of Xerics sounds ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/CoxSounds.java) & sounds [white light](https://github.com/DapperMickie/odablock-sounds/blob/sounds/WhiteLightAfterRaid.wav) and purple light [1](https://github.com/DapperMickie/odablock-sounds/blob/sounds/GettingPurple_r1.wav), [2](https://github.com/DapperMickie/odablock-sounds/blob/sounds/GettingPurple_r2.wav))
- Dragon Dagger special attack ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/DdsSpec.java) & sounds [1](https://github.com/DapperMickie/odablock-sounds/blob/sounds/DdsSpec_r1.wav), [2](https://github.com/DapperMickie/odablock-sounds/blob/sounds/DdsSpec_r2.wav) and [3](https://github.com/DapperMickie/odablock-sounds/blob/sounds/DdsSpec_r3.wav))
- Death (plays whenever you die. [code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/Death.java) & [sound](https://github.com/DapperMickie/odablock-sounds/blob/sounds/DyingHCIMCompleted_r1.wav))
- Declining a trade ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/DeclineTrade.java) & [sound](https://github.com/DapperMickie/odablock-sounds/blob/sounds/DeclineTrade.wav))
- Dharoks Axe ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/DhAxe.java) & sounds [Block 1](https://github.com/DapperMickie/odablock-sounds/blob/sounds/DhAxe_block_r1.wav), [Block 2](https://github.com/DapperMickie/odablock-sounds/blob/sounds/DhAxe_block_r2.wav), [Block 3](https://github.com/DapperMickie/odablock-sounds/blob/sounds/DhAxe_block_r3.wav), [Chop 1](https://github.com/DapperMickie/odablock-sounds/blob/sounds/DhAxe_chop_r1.wav), [Chop 2](https://github.com/DapperMickie/odablock-sounds/blob/sounds/DhAxe_chop_r2.wav), [Hack 1](https://github.com/DapperMickie/odablock-sounds/blob/sounds/DhAxe_hack_r1.wav), [Hack 2](https://github.com/DapperMickie/odablock-sounds/blob/sounds/DhAxe_hack_r2.wav), [Smash 1](https://github.com/DapperMickie/odablock-sounds/blob/sounds/DhAxe_smash_r1.wav), [Smash 2](https://github.com/DapperMickie/odablock-sounds/blob/sounds/DhAxe_smash_r2.wav) and [Smash 3](https://github.com/DapperMickie/odablock-sounds/blob/sounds/DhAxe_smash_r3.wav))
- Dismiss random events ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/DismissRandomEvent.java) & [sound](https://github.com/DapperMickie/odablock-sounds/blob/sounds/DismissingRandomEvent.wav))
- Entering bank pin ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/EnteringBankPin.java) & [sound](https://github.com/DapperMickie/odablock-sounds/blob/sounds/TypingInBankpin.wav))
- Giving a bone to the varrock dog ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/GiveBone.java) & [sound](https://github.com/DapperMickie/odablock-sounds/blob/sounds/GiveBone.wav))
- Hairdresser ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/HairDresser.java) & sounds [1](https://github.com/DapperMickie/odablock-sounds/blob/sounds/Hairdresser_r1.wav), [2](https://github.com/DapperMickie/odablock-sounds/blob/sounds/Hairdresser_r2.wav) and [3](https://github.com/DapperMickie/odablock-sounds/blob/sounds/Hairdresser_r3.wav))
- Killing player ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/KillingPlayer.java) & sounds [1](https://github.com/DapperMickie/odablock-sounds/blob/sounds/KillingSomeone_r1.wav), [2](https://github.com/DapperMickie/odablock-sounds/blob/sounds/KillingSomeone_r2.wav), [3](https://github.com/DapperMickie/odablock-sounds/blob/sounds/KillingSomeone_r3.wav) and [4](https://github.com/DapperMickie/odablock-sounds/blob/sounds/KillingSomeone_r4.wav))
- Killing rat ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/KillingRat.java) & sounds [1](https://github.com/DapperMickie/odablock-sounds/blob/sounds/KillingRatOrScurrius_r1.wav) and [2](https://github.com/DapperMickie/odablock-sounds/blob/sounds/KillingRatOrScurrius_r2.wav))
- Level up ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/LevelUp.java) & [sound](https://github.com/DapperMickie/odablock-sounds/blob/sounds/LevelUpCompleted_r1.wav))
- Pet (whenever you receive a pet. [code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/Pet.java) & [sound](https://github.com/DapperMickie/odablock-sounds/blob/sounds/NewPet.wav))
- Pet dog ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/PetDog.java) & [sound](https://github.com/DapperMickie/odablock-sounds/blob/sounds/PettingDog.wav))
- PK chest (whenever you open the loot chest after a PK. [code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/PkChest.java) & [sound](https://github.com/DapperMickie/odablock-sounds/blob/sounds/ClickingPkLootChest.wav))
- Prayer down (whenever you run out of prayer points, except for when triggered by redemption proc. [code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/PrayerDown.java) & [sound](https://github.com/DapperMickie/odablock-sounds/blob/sounds/SmitedNoPrayer.wav))
- Quest completed ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/QuestCompleted.java) & [sound](https://github.com/DapperMickie/odablock-sounds/blob/sounds/QuestCompleted_r1.wav))
- Redemption proc ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/RedemptionProc.java) & sounds [1](https://github.com/DapperMickie/odablock-sounds/blob/sounds/RedemptionProc_r1.wav), [2](https://github.com/DapperMickie/odablock-sounds/blob/sounds/RedemptionProc_r2.wav), [3](https://github.com/DapperMickie/odablock-sounds/blob/sounds/RedemptionProc_r3.wav) and [4](https://github.com/DapperMickie/odablock-sounds/blob/sounds/RedemptionProc_r4.wav))
- Report player ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/ReportPlayer.java) & sounds [1](https://github.com/DapperMickie/odablock-sounds/blob/sounds/ReportPlayer_r1.wav), [2](https://github.com/DapperMickie/odablock-sounds/blob/sounds/ReportPlayer_r2.wav) and [3](https://github.com/DapperMickie/odablock-sounds/blob/sounds/ReportPlayer_r3.wav))
- Ruby bolt proc ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/RubyBoltProc.java) & [sound](https://github.com/DapperMickie/odablock-sounds/blob/sounds/RubyProc.wav))
- Snowballed (only works for a specific [list of users](https://github.com/DapperMickie/odablock-sounds/blob/snowball/users.txt). [code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/SnowBalled.java) & sounds [1](https://github.com/DapperMickie/odablock-sounds/blob/sounds/Snowball_r1.wav) and [2](https://github.com/DapperMickie/odablock-sounds/blob/sounds/Snowball_r1.wav))
- Tombs of Amascut ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/ToaChestLight.java) & sounds [white light](https://github.com/DapperMickie/odablock-sounds/blob/sounds/WhiteLightAfterRaid.wav) and purple light [1](https://github.com/DapperMickie/odablock-sounds/blob/sounds/GettingPurple_r1.wav), [2](https://github.com/DapperMickie/odablock-sounds/blob/sounds/GettingPurple_r2.wav))
- Tombs of Amascut open purple chest ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/ToaChestOpens.java) & [sound](https://github.com/DapperMickie/odablock-sounds/blob/sounds/ToaChestOpens.wav))
- Theatre of Blood ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/TobChestLight.java) & sounds [white light](https://github.com/DapperMickie/odablock-sounds/blob/sounds/WhiteLightAfterRaid.wav) and purple light [1](https://github.com/DapperMickie/odablock-sounds/blob/sounds/GettingPurple_r1.wav), [2](https://github.com/DapperMickie/odablock-sounds/blob/sounds/GettingPurple_r2.wav))
- Turning on run ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/TurnOnRun.java) & [sound](https://github.com/DapperMickie/odablock-sounds/blob/sounds/TurningOnRun.wav))
- Vengeance ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/Vengeance.java) & [sound](https://github.com/DapperMickie/odablock-sounds/blob/sounds/Vengeance.wav))
- Zebak roar ([code](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/sounds/ZebakRoar.java) & [sound](https://github.com/DapperMickie/odablock-sounds/blob/sounds/ZebakRoar.wav))

and 'public' chat messages for some of the above (that only you can see)

### Systems

We have implemented a few systems to support all of these features. 

#### Sound system

First and foremost we have implemented a sound system that consists of a [sound engine](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/SoundEngine.java) and a [sound file manager](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/SoundFileManager.java) to play all the sounds.

Sounds are downloaded to the local file system instead of being 'baked in' to the plugin build, allowing for further
expansion in the future while also 'supporting' user-swapped sounds for pre-existing events/actions (please refer to the warning section of `Customising your sounds`).

#### Snowball system

The snowball system consists of a [snowball user manager](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/SnowballUserManager.java) this manager downloads/updates the list of users that are allowed to snowball people (and have the sound play). We have chosen to not make this list editable on your end.

#### Player kill system

Because the OSRS team adds new player kill lines from time to time, we've chosen to add a system to update the possible player kill lines without having to push a new plugin. This system uses the [Player kill line manager](https://github.com/DapperMickie/odablock-sounds/blob/master/src/main/java/com/github/dappermickie/odablock/PlayerKillLineManager.java). This manager downloads/updates a [list of possible kill lines](https://github.com/DapperMickie/odablock-sounds/blob/playerkillpatterns/pklines.txt). This system is then used in the `Killing player` sound to determine whether or not you killed someone.


### Planned / Work In Progress expansions

- none

### Potential future expansions

- none at this moment

### Known Issues

- PulseAudio on linux can just refuse to accept the audio formats used despite claiming to accept them.
- Chambers of Xerics sounds don't play correctly (now plays whenever you enter the Olm room instead of waiting until you finished the raid).
- Dismiss random events plays whenever you 'dismiss' the rune pouch.
