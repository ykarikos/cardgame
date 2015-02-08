# cardgame

Card game platform for playing any card game remotely. Does not enforce any rules (yet), so you can only play with trusted players. You should *never* gamble on this platform.

## Usage

    $ java -jar cardgame.jar [username]

## Commands

### Local commands

Upon startup the engine starts to listen a specific default port.

- `create-game [gamename] [number-of-players] [decktype]` Start a new game server
- `deal [number-of-cards]` Deal every player given number of cards. Dealing is allowed only if all players have joined.


### Remote commands

- `join [hostname]` Join a game at the given host
- `deal [number-of-cards]` Deal every player given number of cards. Dealing is allowed only if all players have joined.

## The game table

There's different places where cards can be during a game. These places can be referred in commands `discard` and `draw`.

- `hand` the player's hand cards, only visible to the player himself
- `mytable` the player's own table cards, with cards face-up, visible for all
- `drawpile` the draw pile or stock, with cards face-down, not visible
- `discardpile` the discard pile, with cards face-up, only the topmost is visible for all
- `throwaway` the cards that are discarded from the game for good, not visible

## Decks

- `french` 52 cards with four suits of spade (♠, S), diamonds (♦, D), clubs (♣, C) and hearts (♥, H)
    - The cards: `A♠` `2♠` `3♠` `4♠` `5♠` `6♠` `7♠` `8♠` `9♠` `T♠` `J♠` `Q♠` `K♠` `A♥` `2♥` `3♥` `4♥` `5♥` `6♥` `7♥` `8♥` `9♥` `T♥` `J♥` `Q♥` `K♥` `A♦` `2♦` `3♦` `4♦` `5♦` `6♦` `7♦` `8♦` `9♦` `T♦` `J♦` `Q♦` `K♦` `A♣` `2♣` `3♣` `4♣` `5♣` `6♣` `7♣` `8♣` `9♣` `T♣` `J♣` `Q♣` `K♣`
    - You may refer to the cards also with character instead of a symbol, so `K♥` is equal to `KH` or `kh`

## License

Card game source code is licensed with the MIT License, see LICENSE.txt
