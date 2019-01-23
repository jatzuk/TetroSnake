import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import tetrosnake.Canvas.Companion.EMPTY_TAG
import tetrosnake.Canvas.Companion.HEIGHT
import tetrosnake.Canvas.Companion.OBSTACLE_TAG
import tetrosnake.Canvas.Companion.POINT_SIZE_BLOCK
import tetrosnake.Canvas.Companion.SNAKE_BODY_TAG
import tetrosnake.Canvas.Companion.WIDTH
import tetrosnake.Canvas.Companion.arrangeNewGameObject
import tetrosnake.Canvas.Companion.board
import util.Util.randomX
import util.Util.randomY
import kotlin.math.min

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
    init {
//        for (y in 0 until board.size) {
//            for (x in 0 until board[y].size) {
//                if (y % 3 == 0 || y % 7 == 0 || y % 6 == 0 || y % 9 == 0) {
//                    if (x == 0 || x == 1 || x == 2 || x == 5 || x == 6 || x == 7) {
//                        board[y][x] = OBSTACLE_TAG
//                    } else board[y][x] = EMPTY_TAG
//                } else board[y][x] = EMPTY_TAG
//            }
//        }

        for (y in 0 until board.size) {
            for (x in 0 until board[y].size) board[y][x] = EMPTY_TAG
        }

        for (i in 0 until 3) board[4][3 + i] = SNAKE_BODY_TAG
    }

    @Before
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
//        val (x, y) = arrangeNewGameObject(randomX(1, 8), randomY(1, 8), true, 3)!!
        val (x, y) = arrangeNewGameObject(4, 4, true, 3)!!
        for (i in 0 until 3) {
            board[y][x + i] = OBSTACLE_TAG
        }
        printBoard()
    }
}