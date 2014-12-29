# cardgame

Card game platform for playing any card game remotely. Does not enforce any rules (yet), so you can only play with trusted players. You shuld *never* gamble on this platform.

## Usage

    $ java -jar cardgame.jar [username]

## Commands

### Local commands

Upon startup the engine starts to listen a specific default port.

    > create-game [name] [number-of-players] [decktype]

    gamename@localhost> deal [number-of-cards]
Dealing is allowed only if all players have joined. Without `number-of-cards`, all of the cards are dealt. (How to specify cards dealt on table?)


### Remote commands

    > connect [host]
    @host> list-games
    @host> join-game [name]


## License

Card game source code is licensed with the MIT License, see LICENSE.txt
