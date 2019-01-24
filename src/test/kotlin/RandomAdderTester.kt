import org.junit.Before
import org.junit.Test
import tetrosnake.Canvas.Companion.EMPTY_TAG
import tetrosnake.Canvas.Companion.OBSTACLE_TAG
import tetrosnake.Canvas.Companion.SNAKE_BODY_TAG
import tetrosnake.Canvas.Companion.board
import tetrosnake.Obstacle

/**
 ** Created with passion and love
 **    for project TetroSnake
 **        by Jatzuk on 23-Jan-19
 **                                            *_____*
 **                                           *_*****_*
 **                                          *_(O)_(O)_*
 **                                         **____V____**
 **                                         **_________**
 **                                         **_________**
 **                                          *_________*
 **                                           ***___***
 */

class RandomAdderTester {
    @Before
    fun fillBoard() {
        for (y in 0 until board.size) {
            for (x in 0 until board[y].size) {
                board[y][x] = EMPTY_TAG
            }
        }

        for (i in 0 until 3) board[4][3 + i] = SNAKE_BODY_TAG
//    for (i in 0 until WIDTH) board[4][i] = SNAKE_BODY_TAG
    }

    fun printBoard() {
        for (y in 0 until board.size) {
            for (x in 0 until board[y].size) {
                print(board[y][x])
            }
            println()
        }
    }

    @Test
    fun arrangeNewGameObjectTest() {
        fillBoard()
        val (x, y) = Obstacle().arrangeObstacle(4, 4, true) ?: throw IllegalArgumentException("no match")
        for (i in 0 until 3) board[y][x + i] = OBSTACLE_TAG
        printBoard()
    }
}