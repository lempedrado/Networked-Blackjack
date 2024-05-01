# Usage
There are two main methods that need to be ran for the Server and Client side. The proper commands for each class is as follows:
### BlackJackMultiServer Thread
`java BlackJackMultiServer <port mumber>`
### Player
`java Player <host name> <player name> <port number>`


# Playing
The Player will receive various prompts from the Server to guide them on the expected input
If the passed input does not meet the expected value, they will be prompted for input again.
The server side handles the game conditions and will return the outcome of the game when an end condition is met.
At the end of the game, the Player will be prompted to play a new game.