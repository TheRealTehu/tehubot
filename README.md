<p align="left">
    <img src="https://raw.githubusercontent.com/PKief/vscode-material-icon-theme/ec559a9f6bfd399b82bb44393651661b08aaf7ba/icons/folder-markdown-open.svg" align="left" width="30%">
</p>
<p align="left"><h1 align="left">TEHU-BOT</h1></p>
<p align="left">
	<em><code>❯ Discord bot created by <a href="https://github.com/TheRealTehu">Levente Kovács</a></code></em>
</p>
<p align="left">Built with the tools and technologies:</p>
	<a href="https://skillicons.dev">
		<img src="https://skillicons.dev/icons?i=discord,java,spring,postgres,idea,maven,docker,md">
	</a>
<br>

##  Table of Contents

- [ Overview](#overview)
- [ Features](#features)
- [ Getting Started](#getting-started)
  - [ Using the bot on your server](#using-the-bot-on-your-server)
  - [ Running the bot for yourself](#running-the-bot-for-yourself)
    - [ Prerequisites](#prerequisites)
    - [ Running the bot](#running-the-bot)
- [ Project Roadmap](#project-roadmap)
- [ Contributing](#contributing)
- [ License](#license)
- [ Acknowledgments](#acknowledgments)

---

##  Overview

<code>❯ Tehubot is a general purpose Discord bot with a wide variety of commands and features 
developed using <a href="https://github.com/discord-jda/JDA">JDA (Java Discord API)</a>.
</code>  

<code>❯ The project places great emphasis on the OOP and SOLID principles in its structure   
which isn't present in tutorials and most public projects of its kind.</code>  

<code>❯ The bot can execute commands using Discord's built-in slash commands and can react to specific events  
such as new user joining the server. It can also store data on a persistent database and can give interesting  
server based statistics.</code>

---

##  Features

The bot differentiates between two kinds of actions: Events and Commands.  
 - Events happen automatically, triggered by a specific happening on the Discord server where the bot is active.  
 - Commands can be used by users with the help of Discord's built-in slash command system.

<h3>Events:</h3>
 - <b>ServerJoinEvent:</b> When the bot is added to a new server, an admin can choose a text channel for the bot.  
The bot will then execute its initial setup and will send a welcome message.
 - <b>ServerNewMemberEvent:</b> If a new user joins a server where the bot is active, it will greet the new member  
with a welcome gif.
 - Poll related events will be detailed at Poll command.

<h3>Commands:</h3>
 - <code>/setup</code>: Manually trigger the bots initial setup logic. It will set the servers default text channel as  
the bots channel and save all members into the database. Requires `MANAGE_SERVER` permission.
 - <code>/sendgif \<gifprompt\> \<gifchannel\></code>: Sends a gif to text channel. Gif will be loaded from <a href= "https://tenor.com/">Tenor</a>.  
By default, the bot will send the gif to the channel where the command was given. Member requires `MESSAGE_SEND`  
permission in the channel for the gif to be sent.  
    - <b>gifprompt</b>: Mandatory. Give search term for the gif. Prompt can only contain alphanumerical characters.
    - <b>gifchannel</b>: Optional. Choose text channel to send the gif to.
 - <code>/coinflip</code>: Flips a coin and displays which side it landed on. Result will be printed to the channel where  
the command was given. Member needs `MESSAGE_SEND` permission in the channel for the command to execute.
 - <code>/diceroll \<sides\></code>: Rolls a dice and displays the result. By default, a 6 sided die will be used.  
Result will be printed to the channel where the command was given. Member needs `MESSAGE_SEND` permission in  
the channel for the command to execute.
   - <b>sides</b>: Optional. Choose the number of sides the dice should have between 3 and 100.
 - <code>/getwiki \<wikititle\></code>: Gets an article from Wikipedia. Result will be printed to the  
channel where the command was given. Member needs `MESSAGE_SEND` permission in the channel for the command to execute.
    - <b>wikititle</b>: Mandatory. The title of the wiki article the member wants to see.
 - <code>/create poll \<description\> \<answer1\> \<answer2..20\> \<timelimit\> \<numberofvotes\> \<minrole\> \<anonymousvote\></code>:  
Creates a poll on which members can vote by sending emojis. Emojis will be chosen from the servers custom emojis  
and will be filled with basic emojis if needed. Members can vote by reacting with the emoji corresponding with the option  
they choose. Members can revert their vote by removing the emoji. In case of an anonymous vote, votes can be reverted by  
reacting with the same emoji again. Only members with `MESSAGE_SEND_POLLS` permission can create polls.
    - <b>description</b>: Mandatory. The question or topic the members should vote on.
    - <b>answer1</b>: Mandatory. The first choosable option.
    - <b>answer2..20</b>: Optional. There can be up to 20 options to vote for. 
    - <b>timelimit</b>: Optional. Set an end time for the poll, when it will automatically close and declare the result.  
   Time limit must be given in UTC time in the following format: `yyyy-MM-dd HH:mm`. If no time limit was given,  
   poll will end when manually closed with `/closepoll` command. If time limit is set, the bot will automatically check  
   if a poll's time limit is expired and will close the given poll and print its result to the text channel set for the bot.
    - <b>numberofvotes</b>: Optional. Set the number of options a member can vote on in a given poll. By default,  
   each member can only vote once.
    - <b>minrole</b>: Optional. Set the minimum required role for a member to vote. By default, the `@everyone` role  
   will be set.
    - <b>anonymousvote</b>: Optional. Set the voting to be anonymous. If set, the bot will remove emojis from the poll  
   automatically.
 - <code>/closepoll \<pollid\></code>: Closes a poll with given id and prints result. Only members with `MANAGE_EVENTS`  
permission can close polls. Polls with time limit can also be closed manually.
    - <b>pollid</b>: Mandatory. The id for the poll the member wishes to close.
 - <code>/guildstatistics</code>: Gets information about bot usage on the server. Currently, counts the number of calls  
for each command. Result will be printed to the channel where the command was given. Member needs `MESSAGE_SEND`  
permission in the channel for the command to execute.

---

#  Getting Started

## Using the bot on your server

Add the bot to your Discord server using this [link](https://discord.com/oauth2/authorize?client_id=933482786340347934&permissions=49348905139009&scope=bot+applications.commands).  
The bot might ask for more intents than current functionality strictly necessitates. This is for future development   
and feature ideas.  
Currently the bot is only running on a personal computer for testing purposes so it is mostly offline. If you'd like to  
see it in action, please reach out to me.

## Running the bot for yourself

###  Prerequisites

Before getting started with Tehubot you'll need:

- **For running the bot:** Docker
- **For running the code:** Java, PostgreSQL

###  Running the bot

To successfully run Tehubot on your computer you have to do the following steps:

1. Clone the Tehubot repository:
```sh
❯ git clone https://github.com/TheRealTehu/tehubot
```

2. Navigate to the project directory:
```sh
❯ cd tehubot
```

3. Add environmental variables:
- Get necessary keys:
  - [Discord bot token](https://discord.com/developers/)
  - [Tenor api key](https://tenor.com/gifapi/documentation#quickstart)

- On Windows:
```sh
❯ $Env:BOT_TOKEN = "RECEIVE A DISCORD BOT TOKEN FROM: https://discord.com/developers/"
❯ $Env:TENOR_API_KEY = "RECEIVE A TENOR API KEY FROM: https://tenor.com/gifapi/documentation#quickstart"
```

- On Linux or macOs:
```sh
❯ export BOT_TOKEN = "RECEIVE A DISCORD BOT TOKEN FROM: https://discord.com/developers/"
❯ export TENOR_API_KEY = "RECEIVE A TENOR API KEY FROM: https://tenor.com/gifapi/documentation#quickstart"
```

4. (Optional) Change environment variables in docker-compose:
- Change `POSTGRES_USER` and `POSTGRES_PASSWORD` for a more secure database
- Change `HIBERNATE_DDL` to `update` if you want the database to persist between launches

5. Run the bot:  
```sh
❯ docker compose up
```

---
##  Project Roadmap

- [X] **`Task 1`**: <strike>Release 1.0.0 version of the bot.</strike>
- [ ] **`Task 2`**: Implement emotional support command.
- [ ] **`Task 3`**: Implement message hall of fame event.
- [ ] **`Task 4`**: Implement cat horoscope command.
- [ ] **`Task 5`**: Implement tell a joke command.
- [ ] **`Task 6`**: Implement get a meme command.
- [ ] **`Task 7`**: Implement music player command.
- [ ] **`Task 8`**: Implement video player command.
- [ ] **`Task 9`**: Implement get Github repository information command.
- [ ] **`Task 10`**: Implement compliment command.
- [ ] **`Task 11`**: Implement Steam game picker command.
- [ ] **`Task 12`**: Refactor GuildStatistics command to display more detailed information.
- [ ] **`Task 13`**: Add AI functionality to actions.

---

##  Contributing

- **💬 [Join the Discussions](https://github.com/TheRealTehu/tehubot/discussions)**: Share your insights, provide feedback, or ask questions.
- **🐛 [Report Issues](https://github.com/TheRealTehu/tehubot/issues)**: Submit bugs found or log feature requests for the `Tehubot` project.

---

##  License

This project is protected under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) License. For more details, refer to the `LICENSE` file.

---

##  Acknowledgments

 - Project was started based on [TechnoVision](https://www.youtube.com/@TechnoVisionTV) JDA tutorial videos.
 - Special thank you for the following people, who helped with ideas, solutions and inputs for the project:
   - [Artúr Kovács](https://github.com/ArturKovacsCC)
   - [Dániel Hatás](https://github.com/samson84)
   - [Péter Ittzés](https://github.com/ittzes-cc)

---