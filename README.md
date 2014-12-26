# cardgame

FIXME: description

## Installation

Download from http://example.com/FIXME.

## Usage

FIXME: explanation

    $ java -jar cardgame-0.1.0-standalone.jar [args]

## Options

FIXME: listing of options this app accepts.


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

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
