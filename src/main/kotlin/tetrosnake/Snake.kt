package tetrosnake

import tetrosnake.Canvas.Companion.EMPTY_TAG
import tetrosnake.Canvas.Companion.HEIGHT
import tetrosnake.Canvas.Companion.POINT_SIZE_BLOCK
import tetrosnake.Canvas.Companion.SNAKE_BODY_TAG
import tetrosnake.Canvas.Companion.SNAKE_HEAD_TAG
import tetrosnake.Canvas.Companion.WIDTH
import tetrosnake.Canvas.Companion.board
import tetrosnake.Canvas.Companion.obstacles
import tetrosnake.Canvas.Companion.snake
import util.Util
import java.awt.Point
import java.util.ArrayList

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

class Snake : Util.GameObject {
    override val body = ArrayList<Point>()
    var isFalling = false
    var isAlive = true
    //        TODO("self killing in right case")
    var direction = /*randomDirection()*/ Util.Direction.LEFT

    init {
        with(body) {
            //            TODO("random")
            add(Point(40, 25))
            add(Point(41, 25))
            add(Point(42, 25))
        }
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
                Util.Direction.UP -> if (body[0].y > 0) body[0].y-- else body[0].y = HEIGHT / POINT_SIZE_BLOCK - 1
                Util.Direction.RIGHT -> if (body[0].x < WIDTH / POINT_SIZE_BLOCK - 1) body[0].x++ else body[0].x = 0
                Util.Direction.DOWN -> if (body[0].y < HEIGHT / POINT_SIZE_BLOCK - 1) body[0].y++ else body[0].y = 0
                Util.Direction.LEFT -> if (body[0].x > 0) body[0].x-- else body[0].x = WIDTH / POINT_SIZE_BLOCK - 1
            }
            board[body[0].y][body[0].x] = SNAKE_HEAD_TAG

        } else {
            val max = body.maxBy { it.y }!!
            if (max.y < HEIGHT / POINT_SIZE_BLOCK - 1 && !checkCollisionAtObstaclesWhileFalling()) {
                for (i in 0 until body.size) board[body[i].y++][body[i].x] = EMPTY_TAG
                for (i in 0 until body.size) board[body[i].y][body[i].x] = SNAKE_BODY_TAG
            } else transformToObstacle()

        }
    }

    fun checkCollisionWith(gameObject: Util.GameObject): Boolean {
        when (gameObject) {
            is Snake -> for (i in 1 until body.size) if (checkCollisionAtPoint(gameObject.body[i])) return true
            is Food -> if (checkCollisionAtPoint(gameObject.body[0])) return true
            is Obstacle -> for (i in 0 until gameObject.body.size) if (checkCollisionAtPoint(gameObject.body[i])) return true
        }
        return false
    }

    fun transformToObstacle() {
        obstacles.add(Obstacle(snake = snake))
        isFalling = false
        snake = Snake()
    }

    private fun checkCollisionAtPoint(point: Point) = body[0].location == point.location

    private fun checkCollisionAtObstaclesWhileFalling(): Boolean {
        val startTime = System.nanoTime()

        for (i in 0 until body.size) {
            for (j in 0 until obstacles.size) {
                for (k in 0 until obstacles[j].body.size) {
                    val sx = body[i].x
                    val sy = body[i].y
                    val ox = obstacles[j].body[k].x
                    val oy = obstacles[j].body[k].y
                    if (sx == ox && sy + 1 == oy) {
                        println("time elapsed: ${(System.nanoTime() - startTime)} ns.")
                        return true
                    }
                }
            }
        }
        println("time elapsed: ${(System.nanoTime() - startTime)} ns.")
        return false
    }
}