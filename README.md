# Vampire-Infestation-Simulation
A simulation of a vampire infestation with humans, vampires, and priests.
- Humans adjacent to Vampires are infected
- Vampires adjacent to Priests are converted back into humans
- Each type of being moves according to set rules

Humans (White):
- If a vampire is seen within 10 squares of the direction it is facing, the human faces the opposite
direction and attempts to run two squares away
- Otherwise, moves in its current facing direction with a ten percent chance of turning a random direction

Vampire (Red):
- If a vampire sees a human within 10 squares of the direction it is facing, then it moves toward the human
- If priests splash holy water on the vampire, it is removed from the city
- Otherwise, moves in its current facing direction with a twenty percent chance of turning a random direction

Priest (Blue):
- If a priest sees a Vampire within 5 squares of the direction it is facing, then it moves towards it
- Otherwise, moves in its current facing direction with a fifteen percent chance of turning a random direction
- Twenty percent chance of splashing holy water on Vampires within 2 squares of the facing direction.

![Sample](/sample_infestation.png)
