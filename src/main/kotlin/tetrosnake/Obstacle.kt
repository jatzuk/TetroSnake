package tetrosnake

import tetrosnake.Canvas.Companion.EMPTY_TAG
import tetrosnake.Canvas.Companion.HEIGHT
import tetrosnake.Canvas.Companion.POINT_SIZE_BLOCK
import tetrosnake.Canvas.Companion.WIDTH
import tetrosnake.Canvas.Companion.board

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

    private fun horizontalSearch(x: Int, y: Int, recLvl: Int = 0, bound: Int = -1/*range: IntRange = -1..-1*/): Pair<Int, Int>? {
        var pair: Pair<Int, Int>? = null
        if ( /*x - 1 in range*/  x < 1 || x > WIDTH / POINT_SIZE_BLOCK - 1) {
            println("\t\tcan not add for line $y, returning null")
            return pair
        }

        val xl = x - 1
        val xr = x + 1
        println("trying to add object, x: $x, y: $y, reclvl: $recLvl")

        if (board[y][xl] == EMPTY_TAG && board[y][x] == EMPTY_TAG && board[y][xr] == EMPTY_TAG) {
            println("\t\tmatched at x: $x, y: $y | $xl..$xr, reclvl: $recLvl")
            pair = Pair(xl, y)
        } else {
            if (pair == null && x / 2 != bound && x / 2 > 0/* && x / 2 !in range*/) {
                println("\tleft->")
                pair = horizontalSearch(x / 2, y, recLvl + 1, x /*xl..xr*/)
            }
            if (pair == null && x * 2 != bound && x * 2 <= WIDTH / POINT_SIZE_BLOCK - 1) {
                println("\tright->")
                pair = horizontalSearch(x * 2, y, recLvl + 1, x /*xl..xr*/)
            }
        }
        return pair
    }

    private fun changeYAxis(x: Int, y: Int, recLvl: Int = 0): Pair<Int, Int>? {
        var pair: Pair<Int, Int>? = null

        if (y < 0 || y > HEIGHT / POINT_SIZE_BLOCK - 1) return pair

        var cy = y / 2
        while (cy > 1 && pair == null) {
            cy /= 2
            pair = horizontalSearch(x, cy, recLvl + 1)
        }
//        cy = y
//        while (cy > HEIGHT - 1 && pair == null) {
//            cy *= 2
//            pair = horizontalSearch(x, cy, recLvl + 1)
//        }

        return pair
    }
}