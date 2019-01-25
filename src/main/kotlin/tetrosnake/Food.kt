package tetrosnake

import tetrosnake.Canvas.Companion.FOOD_TAG
import tetrosnake.Canvas.Companion.HEIGHT
import tetrosnake.Canvas.Companion.POINT_SIZE_BLOCK
import tetrosnake.Canvas.Companion.WIDTH
import tetrosnake.Canvas.Companion.board
import util.GameObject
import util.randomX
import util.randomY

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

class Food : GameObject {
    val x = randomX(max = WIDTH / POINT_SIZE_BLOCK)
    val y = randomY(max = HEIGHT / POINT_SIZE_BLOCK)

    init {
        board[y][x] = FOOD_TAG
    }
}