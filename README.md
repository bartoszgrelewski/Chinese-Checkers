# Chinese-Checkers
Chinese Checkers Game.  
This game works in client-server architecture.  
Player starts game, then waits for other players.   
Server provides verification of moves and rules and mediates in communication.  
It was group project. We wrote this in a pair. We used GIT, GITHUB for managing versions.  
To check if our project is working we wrote JUnit tests.

We used:
- JavaFX
- Client/Server communication by socket. Players have to connect with server.
- Server verifies player's moves.
- Patterns: Mediator and Observer.

How to run it? 
Import as a Java Maven project and run Server, then Client multiple times.
