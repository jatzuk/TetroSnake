package util

import kotlinx.coroutines.delay
import tetrosnake.Canvas
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
    private const val BASE_OBSTACLE_CREATOR_DELAY_TIME = 3_000L
    private const val BASE_FOOD_CREATOR_DELAY_TIME = 2_000L
    private const val BASE_DELAY_TIME_DECREASE = 10L
    private var obstacleDelayTime = BASE_OBSTACLE_CREATOR_DELAY_TIME
    private var foodDelayTime = BASE_FOOD_CREATOR_DELAY_TIME
    lateinit var food: Food

    suspend fun lifeCycle() {
        while (Canvas.gameFlow.isActive) {
            if (!Canvas.isGamePaused) {
                createFood()
                delay(foodDelayTime)
                foodDelayTime -= BASE_DELAY_TIME_DECREASE
            }
            if (!Canvas.isGamePaused) {
                Obstacle(Canvas.WALL_SIZE)
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
        Canvas.board[food.y][food.x] = Canvas.EMPTY_TAG
    }

    private fun createFood() {
        food = Food()
        food.addObserver(this)
    }
}
