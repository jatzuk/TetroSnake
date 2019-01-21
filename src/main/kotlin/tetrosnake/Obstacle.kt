package tetrosnake

import tetrosnake.Canvas.Companion.OBSTACLE_TAG
import tetrosnake.Canvas.Companion.board
import tetrosnake.Canvas.Companion.canAddGameObject
import tetrosnake.Canvas.Companion.obstacles
import util.Util
import util.Util.randomX
import util.Util.randomY
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

class Obstacle(size: Int = Canvas.WALL_SIZE, snake: Snake? = null) : Util.GameObject {
    override val body = ArrayList<Point>(size)

    init {
        if (snake == null) {
            while (true) {
                val x = randomX(3)
                val y = randomY(3)
                if (Random().nextBoolean()) {
                    if (canAddGameObject(x, y, x + 3, y)) {
                        for (i in 0 until size) {
                            body.add(Point(x + i, y))
                            board[y][x + i] = OBSTACLE_TAG
                        }
                        break
                    } else continue
                } else {
                    if (canAddGameObject(x, y, x, y + 3)) {
                        for (i in 0 until size) {
                            body.add(Point(x, y - i))
                            board[y - i][x] = OBSTACLE_TAG
                        }
                        break
                    } else continue
                }
            }
        } else {
            for (i in 0 until snake.body.size) {
                body.add(Point(snake.body[i]))
                board[snake.body[i].y][snake.body[i].x] = OBSTACLE_TAG
            }
        }
    }
}