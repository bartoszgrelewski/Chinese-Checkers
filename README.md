# Chinese-Checkers
Chinese Checkers Game.
This game works in client-server architecture. 
Player starts game, then waits for other players. Server provides verification of moves and rules and mediates in communication.

We used:
- JavaFX
- Client/Server communication by socket. Players have to connect with server.
- Server verifies player's moves.
- Patterns: Mediator and Observer.

How to run it? 
Import as a Java Maven project and run Server, then Client multiple times.
