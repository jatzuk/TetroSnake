package util

import tetrosnake.Canvas

/**
 ** Created with passion and love
 **    for project Snake
 **        by Jatzuk on 16.01.2019
 **                                            *_____*
 **                                           *_*****_*
 **                                          *_(O)_(O)_*
 **                                         **____V____**
 **                                         **_________**
 **                                         **_________**
 **                                          *_________*
 **                                           ***___***
 */

object Util {
    fun getRoundX10(): Int {
        var x = (Math.random() * Canvas.WIDTH).toInt() + 1 - Canvas.POINT_SIZE_BLOCK
        if (x % 5 != 0) x = Math.round((x + 6) / Canvas.POINT_SIZE_BLOCK.toDouble()).toInt() * Canvas.POINT_SIZE_BLOCK + Canvas.POINT_SIZE_BLOCK
        return x
    }

    fun getRoundY10(): Int {
        var y = (Math.random() * Canvas.HEIGHT).toInt() + 1 - Canvas.POINT_SIZE_BLOCK
        if (y % 5 != 0) y = Math.round((y + 6) / Canvas.POINT_SIZE_BLOCK.toDouble()).toInt() * Canvas.POINT_SIZE_BLOCK + Canvas.POINT_SIZE_BLOCK
        return y
    }

    fun randomDirection() = Direction.values()[(Math.random() * Direction.values().size).toInt()]

    enum class Direction {
        UP, RIGHT, DOWN, LEFT;
    }
}