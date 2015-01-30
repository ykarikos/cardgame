# cardgame

Card game platform for playing any card game remotely. Does not enforce any rules (yet), so you can only play with trusted players. You should *never* gamble on this platform.

## Usage

    $ java -jar cardgame.jar [username]

## Commands

### Local commands

Upon startup the engine starts to listen a specific default port.

    > create-game [gamename] [number-of-players] [decktype]
    Server started.
    gamename> deal [number-of-cards]
    [username] dealt you 7♠, A♥
Dealing is allowed only if all players have joined.


### Remote commands

    > join [hostname]
    Joined.
    gamename> deal [number-of-cards]
    [username] dealt you J♣, 9♣

## License

Card game source code is licensed with the MIT License, see LICENSE.txt
