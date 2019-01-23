package tetrosnake

import tetrosnake.Canvas.Companion.OBSTACLE_TAG
import tetrosnake.Canvas.Companion.board
import tetrosnake.Canvas.Companion.arrangeNewGameObject
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
    init {
        if (snake == null) {
//            while (true) {
//                val x = randomX(3)
//                val y = randomY(3)
//                if (Random().nextBoolean()) {
//                    if (arrangeNewGameObject(x, y, x + 3, y)) {
//                        for (i in 0 until size) {
//                            body.add(Point(x + i, y))
//                            board[y][x + i] = OBSTACLE_TAG
//                        }
//                        break
//                    } else continue
//                } else {
//                    if (arrangeNewGameObject(x, y, x, y + 3)) {
//                        for (i in 0 until size) {
//                            body.add(Point(x, y - i))
//                            board[y - i][x] = OBSTACLE_TAG
//                        }
//                        break
//                    } else continue
//                }
//            }


            val directionFlag = Random().nextBoolean()
            val (x, y) = arrangeNewGameObject(randomX(2), randomY(2), true, size)!!
            if (true) {
                for (i in 0 until size) board[y][x + i] = OBSTACLE_TAG
            } else for (i in 0 until size) board[y - i][x] = OBSTACLE_TAG

        } else {
            for (i in 0 until snake.body.size) board[snake.body[i].y][snake.body[i].x] = OBSTACLE_TAG

//            {
//                body.add(Point(snake.body[i]))

        }
    }
}