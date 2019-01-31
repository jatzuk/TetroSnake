package util

import kotlinx.coroutines.delay
import tetrosnake.Canvas.Companion.EMPTY_TAG
import tetrosnake.Canvas.Companion.WALL_SIZE
import tetrosnake.Canvas.Companion.board
import tetrosnake.Canvas.Companion.gameFlow
import tetrosnake.Canvas.Companion.isGamePaused
import tetrosnake.Food
import tetrosnake.Obstacle
import java.util.*

/*
 * Created with passion and love 
 *    for project TetroSnake
 *        by Jatzuk on 30-Jan-19
 *                                            *_____*
 *                                           *_*****_*
 *                                          *_(O)_(O)_*
 *                                         **____V____**
 *                                         **_________**
 *                                         **_________**
 *                                          *_________*
 *                                           ***___***
 */

object GameObjectManager : Observer {
    private const val BASE_OBSTACLE_CREATOR_DELAY_TIME = 16_000L
    private const val BASE_FOOD_CREATOR_DELAY_TIME = 8_000L
    private const val BASE_DELAY_TIME_DECREASE = 10L
    private var obstacleDelayTime = BASE_OBSTACLE_CREATOR_DELAY_TIME
    private var foodDelayTime = BASE_FOOD_CREATOR_DELAY_TIME
    lateinit var food: Food

    suspend fun lifeCycle() {
        while (gameFlow.isActive) {
            if (!isGamePaused) {
                createFood()
                delay(foodDelayTime)
                foodDelayTime -= BASE_DELAY_TIME_DECREASE
            }
            if (!isGamePaused) {
                Obstacle(WALL_SIZE)
                delay(obstacleDelayTime)
                obstacleDelayTime -= BASE_DELAY_TIME_DECREASE
            }
        }
    }

    override fun update(o: Observable?, arg: Any?) {
        destroyFood()
    }

    fun foodEaten() {
        destroyFood()
    }

    private fun destroyFood() {
        food.deleteObserver(this)
        board[food.y][food.x] = EMPTY_TAG
        createFood()
    }

    private fun createFood() {
        food = Food().apply { addObserver(this@GameObjectManager) }
    }
}
