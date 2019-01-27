package tetrosnake

import tetrosnake.Canvas.Companion.EMPTY_TAG
import tetrosnake.Canvas.Companion.HEIGHT
import tetrosnake.Canvas.Companion.OBSTACLE_TAG
import tetrosnake.Canvas.Companion.POINT_SIZE_BLOCK
import tetrosnake.Canvas.Companion.SNAKE_BODY_TAG
import tetrosnake.Canvas.Companion.SNAKE_HEAD_TAG
import tetrosnake.Canvas.Companion.WIDTH
import tetrosnake.Canvas.Companion.board
import tetrosnake.Canvas.Companion.snake
import util.Direction
import util.GameObject
import java.awt.Point
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

class Snake : GameObject {
    val body = ArrayList<Point>()
    var isFalling = false
    var isAlive = true
    //        TODO("self killing in right case")
    var direction = /*randomDirection()*/ Direction.LEFT

    init {
        for (i in 0 until 3) body.add(Point(6 + i, 8))
        for ((sp, i) in (0 until body.size).withIndex()) {
            val char = if (i == 0) SNAKE_HEAD_TAG else SNAKE_BODY_TAG
            board[body[sp].y][body[sp].x] = char
        }
    }

    fun move() {
        if (!isFalling) {
            board[body[body.size - 1].y][body[body.size - 1].x] = EMPTY_TAG
            for (i in body.size - 1 downTo 1) {
                body[i].x = body[i - 1].x
                body[i].y = body[i - 1].y
                board[body[i].y][body[i].x] = SNAKE_BODY_TAG
            }

            when (direction) {
                Direction.UP -> if (body[0].y > 0) body[0].y-- else body[0].y = HEIGHT / POINT_SIZE_BLOCK - 1
                Direction.RIGHT -> if (body[0].x < WIDTH / POINT_SIZE_BLOCK - 1) body[0].x++ else body[0].x = 0
                Direction.DOWN -> if (body[0].y < HEIGHT / POINT_SIZE_BLOCK - 1) body[0].y++ else body[0].y = 0
                Direction.LEFT -> if (body[0].x > 0) body[0].x-- else body[0].x = WIDTH / POINT_SIZE_BLOCK - 1
            }
            board[body[0].y][body[0].x] = SNAKE_HEAD_TAG

        } else {
            val max = body.maxBy { it.y }!!
            if (max.y < HEIGHT / POINT_SIZE_BLOCK - 1 && !checkCollisionAtObstaclesOnFall()) {
                for (i in 0 until body.size) board[body[i].y++][body[i].x] = EMPTY_TAG
                for (i in 0 until body.size) board[body[i].y][body[i].x] = SNAKE_BODY_TAG
            } else transformToObstacle()
        }
    }

    fun checkCollisionWith(gameObject: GameObject): Boolean {
        when (gameObject) {
            is Snake -> for (i in 1 until body.size) if (checkCollisionAtPoint(gameObject.body[i].x, gameObject.body[i].y)) return true
            is Food -> if (checkCollisionAtPoint(gameObject.x, gameObject.y)) return true
        }
        return false
    }

    private fun transformToObstacle() {
        Obstacle(snake = snake)
        isFalling = false
        snake = Snake()
    }

    private fun checkCollisionAtPoint(x: Int, y: Int) = body[0].x == x && body[0].y == y

    private fun checkCollisionAtObstaclesOnFall(): Boolean {
        var minX = body[0].x
        var maxX = body[body.size - 1].x
        var minY = body[0].y
        var maxY = body[body.size - 1].y

        for (point in body) {
            if (point.x < minX) minX = point.x
            if (point.x > maxX) maxX = point.x
            if (point.y < minY) minY = point.y
            if (point.y > maxY) maxY = point.y
        }

        for (y in maxY downTo minY) {
            for (x in minX..maxX) {
                if (board[y][x] == SNAKE_BODY_TAG && board[y + 1][x] == OBSTACLE_TAG) return true
            }
        }
        return false
    }
}
