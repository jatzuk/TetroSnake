package util

import tetrosnake.Canvas.Companion.WIDTH
import tetrosnake.Canvas.Companion.HEIGHT
import tetrosnake.Canvas.Companion.POINT_SIZE_BLOCK

/*
 * Created with passion and love
 *    for project TetroSnake
 *        by Jatzuk on 16.01.2019
 *                                            *_____*
 *                                           *_*****_*
 *                                          *_(O)_(O)_*
 *                                         **____V____**
 *                                         **_________**
 *                                         **_________**
 *                                          *_________*
 *                                           ***___***
 */

interface GameObject

enum class Direction {
    UP, RIGHT, DOWN, LEFT;
}

class BoundsNotFoundException(string: String) : Exception(string)

fun randomX(min: Int = 0, max: Int = WIDTH / POINT_SIZE_BLOCK) = (Math.random() * (max - min)).toInt() + min

fun randomY(min: Int = 0, max: Int = HEIGHT / POINT_SIZE_BLOCK) = (Math.random() * (max - min)).toInt() + min

fun randomDirection() = Direction.values()[(Math.random() * Direction.values().size).toInt()]
