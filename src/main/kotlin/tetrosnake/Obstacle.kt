package tetrosnake

import tetrosnake.Canvas.Companion.EMPTY_TAG
import tetrosnake.Canvas.Companion.HEIGHT
import tetrosnake.Canvas.Companion.OBSTACLE_TAG
import tetrosnake.Canvas.Companion.POINT_SIZE_BLOCK
import tetrosnake.Canvas.Companion.WALL_SIZE
import tetrosnake.Canvas.Companion.WIDTH
import tetrosnake.Canvas.Companion.board
import util.BoundsNotFoundException
import util.randomX
import util.randomY
import java.util.*
import kotlin.math.PI

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
    init {
        if (snake == null) {
            try {
                val direction = Random().nextBoolean()
                if (direction) {
                    val (x, y) = arrangeObstacle(randomX(max = WIDTH / POINT_SIZE_BLOCK - 1), randomY(max = HEIGHT / POINT_SIZE_BLOCK - 1), direction) ?: throw BoundsNotFoundException("can not arrange new game object")
                    for (i in 0 until size) board[y][x + i] = OBSTACLE_TAG
                } else {
                    val (x, y) = arrangeObstacle(randomX(0, WIDTH / POINT_SIZE_BLOCK - 1), randomY(0, HEIGHT / POINT_SIZE_BLOCK - 1), direction) ?: throw BoundsNotFoundException("can not arrange new game object")
                    for (i in 0 until size) board[y + i][x] = OBSTACLE_TAG
                }
            } catch (e: BoundsNotFoundException) {
                e.printStackTrace()
            }
        } else for (i in 0 until snake.body.size) board[snake.body[i].y][snake.body[i].x] = OBSTACLE_TAG
    }

    fun arrangeObstacle(x: Int, y: Int, direction: Boolean): Pair<Int, Int>? {
        var pair: Pair<Int, Int>?
        if (direction) {
            pair = horizontalSearch(x, y)
            if (pair == null) pair = changeYAxis(x, y)
        } else {
            pair = verticalSearch(x, y)
            if (pair == null) pair = changeXAxis(x, y)
        }
        return pair
    }

    private fun horizontalSearch(x: Int, y: Int, recLvl: Int = 0, bound: Int = -1): Pair<Int, Int>? {
        if (x < 1 || x > WIDTH / POINT_SIZE_BLOCK - 1) return null
        var pair: Pair<Int, Int>? = null
        val xl = x - 1
        val xr = x + 1
        println("trying to add object, x: $x, y: $y, reclvl: $recLvl")

        if (board[y][xl] == EMPTY_TAG && board[y][x] == EMPTY_TAG && board[y][xr] == EMPTY_TAG) pair = Pair(xl, y)
        else {
            if (pair == null && x - WALL_SIZE != bound && x - WALL_SIZE > 0) pair = horizontalSearch(x - WALL_SIZE, y, recLvl + 1, x)
            if (pair == null && x + WALL_SIZE != bound && x + WALL_SIZE <= WIDTH / POINT_SIZE_BLOCK - 1) pair = horizontalSearch(x + WALL_SIZE, y, recLvl + 1, x)
        }
        return pair
    }

    private fun changeYAxis(x: Int, y: Int, recLvl: Int = 0): Pair<Int, Int>? {
        if (y < 0 || y > HEIGHT / POINT_SIZE_BLOCK - 1) return null
        var pair: Pair<Int, Int>? = null
        var cy = y
        while (cy - WALL_SIZE > 0 && pair == null) {
            cy -= WALL_SIZE
            pair = horizontalSearch(x, cy, recLvl + 1)
        }
        cy = y
        while (cy + WALL_SIZE < HEIGHT / POINT_SIZE_BLOCK && pair == null) {
            cy += WALL_SIZE
            pair = horizontalSearch(x, cy, recLvl + 1)
        }
        return pair
    }

    private fun verticalSearch(x: Int, y: Int, bound: Int = -1): Pair<Int, Int>? {
        if (y < 1 || y > HEIGHT / POINT_SIZE_BLOCK - 1) return null
        var pair: Pair<Int, Int>? = null
        val yt = y - 1
        val yb = y + 1

        if (board[yt][x] == EMPTY_TAG && board[y][x] == EMPTY_TAG && board[yb][x] == EMPTY_TAG) pair = Pair(x, yt)
        else {
            if (pair == null && y - WALL_SIZE != bound && y - WALL_SIZE > 0) pair = verticalSearch(x, y - WALL_SIZE, y)
            if (pair == null && y + WALL_SIZE != bound && y + WALL_SIZE < HEIGHT / POINT_SIZE_BLOCK - 1) pair = verticalSearch(x, y + WALL_SIZE, y)
        }
        return pair
    }

    private fun changeXAxis(x: Int, y: Int): Pair<Int, Int>? {
        if (x < 0 || x > WIDTH / POINT_SIZE_BLOCK - 1) return null
        var pair: Pair<Int, Int>? = null
        var cx = x
        while (cx - WALL_SIZE > 0 && pair == null) {
            cx -= WALL_SIZE
            pair = verticalSearch(cx, y)
        }
        cx = x
        while (cx + WALL_SIZE < WIDTH / POINT_SIZE_BLOCK && pair == null) {
            cx += WALL_SIZE
            pair = verticalSearch(cx, y)
        }
        return pair
    }
}