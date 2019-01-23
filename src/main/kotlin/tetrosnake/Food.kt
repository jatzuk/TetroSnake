package tetrosnake

import tetrosnake.Canvas.Companion.FOOD_TAG
import tetrosnake.Canvas.Companion.board
import util.Util
import util.Util.randomX
import util.Util.randomY
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

class Food : Util.GameObject {
    val x = randomX()
    val y = randomY()

    init {
        board[y][x] = FOOD_TAG
    }
}