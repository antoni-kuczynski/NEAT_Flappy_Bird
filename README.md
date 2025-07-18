# Flappy Bird with Machine learning
A Flappy Bird clone with machine learning implementation based on NEAT genetic algorithm.

![flappybird_training](https://github.com/user-attachments/assets/9a3be307-32a3-4849-8285-a34fd4f192ec)

# Features
### General
- Saving player score to a JSON file
- Customizable game parameters (e.g. pipes gap height, game's scale)
- Dynamic switching between machine learning modes and classic player controlled mode

### Machine learning
- Training mode
- Saving & loading pretrained neural networks to JSON files

# Implementation details
One population is made of 10 players, every player having a neural network which consists of 2 input, 6 hidden and 1 output neurons.<br>
#### Inputs:
- X distance to the closest pipe
- Y distance to the center of the pipe gap

#### Output:
- Whether the player should flap or not

#### Fitness function:
- Bird's total traveled X distance

