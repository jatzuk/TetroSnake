package tetrosnake

import tetrosnake.Canvas.Companion.OBSTACLE_TAG
import tetrosnake.Canvas.Companion.WALL_SIZE
import tetrosnake.Canvas.Companion.board
import util.Arrangeable

/*
 * Created with passion and love
 *    for project Snake
 *        by Jatzuk on 21.01.2019
 *                                            *_____*
 *                                           *_*****_*
 *                                          *_(O)_(O)_*
 *                                         **____V____**
 *                                         **_________**
 *                                         **_________**
 *                                          *_________*
 *                                           ***___***
 */

class Obstacle(size: Int = WALL_SIZE, snake: Snake? = null) : Arrangeable {
    init {
        if (snake == null) {
            arrange()?.let {
                repeat(size) { i ->
                    if (it.third) board[it.second][it.first + i] = OBSTACLE_TAG
                    else board[it.second + i][it.first] = OBSTACLE_TAG
                }
            }
        } else for (i in 0 until snake.body.size) board[snake.body[i].y][snake.body[i].x] = OBSTACLE_TAG
    }
}
