package tetrosnake

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

class Food : Util.GameObject {
    override val body = ArrayList<Point>()

    init {
        with(Point()) {
            x = Util.randomX()
            y = Util.randomY()
            body.add(this)
        }
        Canvas.board[body[0].y][body[0].x] = Canvas.FOOD_TAG
    }
}