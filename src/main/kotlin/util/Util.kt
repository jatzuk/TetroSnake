package util

import tetrosnake.Canvas
import tetrosnake.Canvas.Companion.WIDTH
import tetrosnake.Canvas.Companion.HEIGHT
import tetrosnake.Canvas.Companion.POINT_SIZE_BLOCK
import java.awt.Point
import java.util.ArrayList

/**
 ** Created with passion and love
 **    for project TetroSnake
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
    interface GameObject {
        val body: ArrayList<Point>
    }


//    fun getRoundX10(): Int {
//        var x = (Math.random() * WIDTH / POINT_SIZE_BLOCK - POINT_SIZE_BLOCK).toInt() - POINT_SIZE_BLOCK
//        if (x % 5 != 0) x = Math.round((x + 6) / Canvas.POINT_SIZE_BLOCK.toDouble()).toInt() * POINT_SIZE_BLOCK + POINT_SIZE_BLOCK
//        return x
//    }

    fun randomX(posix: Int = 0) = (Math.random() * (WIDTH / POINT_SIZE_BLOCK - posix)).toInt()

//    fun getRoundY10(): Int {
//        var y = (Math.random() * HEIGHT / POINT_SIZE_BLOCK - POINT_SIZE_BLOCK).toInt() - POINT_SIZE_BLOCK
//        if (y % 5 != 0) y = Math.round((y + 6) / POINT_SIZE_BLOCK.toDouble()).toInt() * POINT_SIZE_BLOCK + POINT_SIZE_BLOCK
//        return y
//    }

    fun randomY(posix: Int = 0) = (Math.random() * (HEIGHT / POINT_SIZE_BLOCK - posix)).toInt()

    fun randomDirection() = Direction.values()[(Math.random() * Direction.values().size).toInt()]

    enum class Direction {
        UP, RIGHT, DOWN, LEFT;
    }
}