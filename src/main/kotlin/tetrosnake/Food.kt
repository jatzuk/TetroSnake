package tetrosnake

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tetrosnake.Canvas.Companion.EMPTY_TAG
import tetrosnake.Canvas.Companion.FOOD_TAG
import tetrosnake.Canvas.Companion.board
import util.GameObject
import util.randomX
import util.randomY
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

class Food : Observable(), GameObject {
    private val foodLiveTime = 10_000L
    val x = randomX()
    val y = randomY()
    var interruptFlag = false

    init {
        if (board[y][x] == EMPTY_TAG) board[y][x] = FOOD_TAG
        else placeFoodOnEmptySpace()
    }

    suspend fun placeFood() {
        delay(foodLiveTime)
        if (!interruptFlag) {
            setChanged()
            notifyObservers()
        }
    }

    private fun placeFoodOnEmptySpace() {
        val emptyPoints = ArrayList<Point>()
        for (y in 0 until board.size) {
            for (x in 0 until board[y].size) if (board[y][x] == EMPTY_TAG) emptyPoints.add(Point(x, y))
        }

        val point = emptyPoints[(Math.random() * emptyPoints.size).toInt()]
        board[point.y][point.x] = FOOD_TAG
    }
}
