# Artificial Intelligence - Project 1

## Model of the environment

### State of the environment

- Width
- Height
- For each cell in the environment - what is on it (black - 2, white - 1 or nothing - 0)

### Successor state for an action

After an action occurs, the state generated only changes the cells in the environment that were affected, meaning where the piece comes from and where is is going, and if another piece was captured.

### Legal actions

The legal actions for any state can be separated into the following cases:

#### Case 1: There is nothing in front of the pawn and no enemies at the front diagonals

> The Player can move only one cell forward

#### Case 2: There is a piece in front of the player

> The player cannot move

#### Case 3: There are enemies on the front diagonals and there is nothing in front of the pawn

> The player can choose between going forwards, or a front diagonal move

#### Case 4: There is something in front of the player and enemies on either front diagonal

> The player can only attack either front diagonal

### Report

We ran the experiments principally on the small gdl files (5x5 and 3x5) with no times constraints at first. we focused primarily on getting the logic of the main algorithm correct before jumping into technicalities of the game (time constraints, legalities of the moves) while developing those on the side.

#### Results

**Note**: Our implementation of project offered unsuccesful results. Next is a detailed overview of them:

##### Outputs

```sh
-Error- Environment : doMove() -> c.myMap[1][1] != 1
-Error- Environment : doMove() -> c.myMap[1][3] != 2
-Error- Environment : doMove() -> c.myMap[1][1] != 1
-Error- Environment : undoMove() -> c.myMap[0][2] != 1
-Error- Environment : doMove() -> c.myMap[1][1] != 1
-Error- Environment : undoMove() -> c.myMap[2][2] != 1
-Error- Environment : doMove() -> c.myMap[1][1] != 1
-Error- Environment : undoMove() -> c.myMap[1][2] != 1
-Error- Environment : undoMove() -> c.myMap[1][2] != 2
-Error- Environment : doMove() -> c.myMap[1][4] != 2
.
.
.
```

After the logging of a lot of errors, the following output can be seen:

```sh
Timeout
This is returned move:  Move from (0, 0) to (0, 0)
```

Which corresponds to the "dummy move" we create at the beginning of the `bestMove()` function. The move is then submitted like that to the game, which results in the failure of our agent to win.

The results for the tests we ran were in a lot of cases, confusing. We analyzed closely the different parts of the program, specifically:

- The main AB pruning algorithm: we checked the logic time and time again, and found no evident flaws in the implementation
- Getting the legal moves: The legal moves calculation proved to be getting the correct moves, so it was not accounting for other non-allowed moves, meaning that the problem was not actually there.

**Possible Problems**: the way that we clone the `State` object (in order to avoid overwriting states), may be one of the reasons the program did not work correctly. However, we did not seem to find the problem behind the error.

Another possible mistake might have been the handling of the turns, which means that we gave turns to black when it was actually white's and viceversa, however after intense debugging, we were not able to find mistakes there.

#### Interpretation of Results (Outputs)

We came to the conclusion that we were stuck with a Java-specific semantic error. Something that had to do with the cloning of the Java objects that was not handling the values and getting incorrect updates on the environment.
