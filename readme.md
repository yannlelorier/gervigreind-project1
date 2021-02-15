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

We began by implementing progressively the DFS all the way to AB pruning.