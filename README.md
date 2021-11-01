# ChimpModerator

Simple Discord moderation bot based on command execution.

## Table of contents
* [Technologies](#technologies)
* [Features](#features)
* [Screenshots](#screenshots)
* [Usage Examples](#usage-examples)
  * [Seeing help](#seeing-help)
  * [User format](#user-format)
  * [Kicking and banning users](#kicking-and-banning-users)
  * [Purge](#purge)
  * [Censoring](#censoring)
  * [Config](#config)
  * [AutoModerator](#automoderator)

## Technologies

Project is created with:

  * Java
  * Swing
  * Gradle
  * JDA (Java Discord API)
  * IntelliJ IDEA
  * My 🧠 and 🖐
  
## Features
  * A set of easy to use commands
  * Basic functionality like: `ban`, `kick`
  * Mass delete messages with a `purge` command which has a set of useful filters for selecting only ones that should be deleted
  * Ability to censor expressions - bot will search for them and warn / kick / ban users when they use them.
  * Adjust bot's behaviour with a `config` command
  * Keep track of what's going on with a log window
  * Multi-server - bot can work in multiple server simultaneously
  * Save / load your configuration
  * Custom prefix for all commands
  * Built-in console - ability to run commands / send messages directly from bot's main window
  * Advanced `help` command containing all informations needed

## Screenshots
![Main view](https://i.imgur.com/pwcezgC.png)

![Server view](https://i.imgur.com/Xj4JWc9.png)

![Help example](https://i.imgur.com/KKJv48J.png)

![Warning](https://i.imgur.com/acYw5BX.png)

## Usage examples
Note that default prefix for commands is `!`

### Seeing help

`!help` - displays list of available commands

`!help [command]` - displays help for specified command

`!help [command] detailed` - displays all available help for this command

### User format

Various commands need specified user while executing command (like `ban`).
Those commands can accept user in those formats:
- discord mentions
- <@USERID> or <@!USERID>
- @USERNAME#1234 (NOTE: This method will return user only when it's already in cache)


### Kicking and banning users

`ban` and `kick` commands have the same syntax so those examples will work for both. 

`!ban @User reason Rules violation` - bans mentioned user with specified reason

`!kick @USERNAME#1234` - kicks user without specified reason

### Purge

Delete messages in large numbers (up to 100 / usage).

NOTE: Expressions containing more than one word can be grouped as follows `"expression with 4 words"`

List of all available filters:
  * `contains [expression]`
  * `equals [expression]`
  * `startswith [expression]`
  * `endswith [expression]`
  * `from [user]`
  * `has [file / embed / mentions / invites / reactions]`

`!purge amount 55 from @User has mentions contains "lol"` - from last 55 messages, delete those sent by mentioned user, containing at least one mention and word "lol"

`!purge amount 10 equals "smh"` - from the last 10 messages, delete those which content is equal to "smh"

### Censoring

Managing censored expressions. 
`!censored` - displays list of all censored expressions

`!censored add [expression] [expression] ...` - add new expression(s)

`!censored remove [expression]` - remove expression(s)

### Config

`!config automoderator` - check if [AutoModerator](#automoderator) is enabled

`!config automoderator [true / false]` - enable / disable looking for censored expressions and executing punishments

`!config [warn / kick]` - display warn / kick amount

`!config [warn / kick] [int]` - set warn / kick amount

`!config prefix` - display current prefix

`!config prefix [new prefix]` - set new prefix

`!config [save / load]` - save / load configuration

### AutoModerator

Keeps track of all server violations. Decides when the violation occured and manages what punishment should be used. 
