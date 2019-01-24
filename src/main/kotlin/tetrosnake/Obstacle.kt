package tetrosnake

import tetrosnake.Canvas.Companion.OBSTACLE_TAG
import tetrosnake.Canvas.Companion.board
import util.Util.randomX
import util.Util.randomY
import java.util.*

/**
 ** Created with passion and love
 **    for project Snake
 **        by Jatzuk on 21.01.2019
 **                                            *_____*
 **                                           *_*****_*
 **                                          *_(O)_(O)_*
 **                                         **____V____**
 **                                         **_________**
 **                                         **_________**
 **                                          *_________*
 **                                           ***___***
 */

class Obstacle(size: Int = Canvas.WALL_SIZE, snake: Snake? = null) {
//    init {
//        if (snake == null) {
//            val directionFlag = Random().nextBoolean()
//            val (x, y) = arrangeObstacle(randomX(2), randomY(2), true)!!
//            if (true) {
//                for (i in 0 until size) board[y][x + i] = OBSTACLE_TAG
//            } else for (i in 0 until size) board[y - i][x] = OBSTACLE_TAG
//
//        } else {
//            for (i in 0 until snake.body.size) board[snake.body[i].y][snake.body[i].x] = OBSTACLE_TAG
//
////            {
////                body.add(Point(snake.body[i]))
//
//        }
//    }

    fun arrangeObstacle(x: Int, y: Int, direction: Boolean): Pair<Int, Int>? {
        var pair: Pair<Int, Int>? = null

        if (direction) {
            pair = horizontalSearch(x, y)
            if (pair == null) {
                println("did not found any solutions on $y changing axis...")
                pair = changeYAxis(x, y)
            }
        } else {

        }

        return pair
    }

    private fun horizontalSearch(x: Int, y: Int, recLvl: Int = 0, range: IntRange = -1..-1): Pair<Int, Int>? {
        var pair: Pair<Int, Int>? = null
        if (x - 1 in range || x < 1 || x > Canvas.WIDTH - 1) {
            println("\t\tcan not add for line $y, returning null")
            return pair
        }

        val xl = x - 1
        val xr = x + 1
        println("trying to add object, x: $x, y: $y, reclvl: $recLvl")

        if (board[y][xl] == Canvas.EMPTY_TAG && board[y][x] == Canvas.EMPTY_TAG && board[y][xr] == Canvas.EMPTY_TAG) {
            println("\t\tmatched at x: $x, y: $y | $xl..$xr, reclvl: $recLvl")
            pair = Pair(xl, y)
        } else {
            if (pair == null && x / 2 >= 1/* && x / 2 !in range*/) {
                println("\tleft-> ${xl - 1} >=")
                pair = horizontalSearch(x / 2, y, recLvl + 1, xl..xr)
            }
            if (pair == null && x * 2 <= Canvas.WIDTH - 1) {
                println("\tright-> ${xr + 1} <= ${Canvas.WIDTH - 1}")
                pair = horizontalSearch(x * 2, y, recLvl + 1, xl..xr)
            }
        }
        return pair
    }

    private fun changeYAxis(x: Int, y: Int, recLvl: Int = 0): Pair<Int, Int>? {
        var pair: Pair<Int, Int>? = null

        if (y < 0 || y > Canvas.HEIGHT - 1) return pair

        while (y > -1 && pair == null) pair = horizontalSearch(x, y / 2, recLvl + 1)
        while (y > Canvas.HEIGHT - 1 && pair == null) pair = horizontalSearch(x, y * 2, recLvl + 1)

        return pair
    }
}