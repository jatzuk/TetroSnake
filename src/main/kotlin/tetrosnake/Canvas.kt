package tetrosnake

import util.Direction
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.util.*
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.Timer

/**
 ** Created with passion and love
 **    for project TetroSnake
 **        by Jatzuk on 17-Jan-19
 **                                            *_____*
 **                                           *_*****_*
 **                                          *_(O)_(O)_*
 **                                         **____V____**
 **                                         **_________**
 **                                         **_________**
 **                                          *_________*
 **                                           ***___***
 */

class Canvas : JPanel(), ActionListener {
    private val timer = Timer(BASE_DELAY, this)
    private var obstacleCreator = ObstacleCreator()
    private lateinit var foodManager: FoodManager
    private var isGamePaused = false
    private var score = 0

    init {
        background = Color.BLACK
        isFocusable = true
        preferredSize = Dimension(WIDTH - POINT_SIZE_BLOCK, HEIGHT - POINT_SIZE_BLOCK)
        initGame()
    }

    private fun initGame() {
        initGameObjects()
        addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent?) {
                with(snake) {
                    when (e?.keyCode) {
                        KeyEvent.VK_UP -> if (direction != Direction.DOWN) direction = Direction.UP
                        KeyEvent.VK_RIGHT -> if (!isFalling && direction != Direction.LEFT) direction = Direction.RIGHT
                        KeyEvent.VK_DOWN -> if (direction != Direction.UP) direction = Direction.DOWN
                        KeyEvent.VK_LEFT -> if (!isFalling && direction != Direction.RIGHT) direction = Direction.LEFT
                        KeyEvent.VK_SPACE -> if (!isFalling) isFalling = true
                        KeyEvent.VK_P -> if (!isGamePaused) pause()
                    }
                }
            }
        })
        timer.start()
        obstacleCreator.start()
        foodManager.start()
    }

    private fun initGameObjects() {
        for (y in 0 until board.size) {
            for (x in 0 until board[y].size) board[y][x] = EMPTY_TAG
        }

        for (i in 0 until board.size) board[i][0] = OBSTACLE_TAG

        snake = Snake()
        foodManager = FoodManager()
    }

    override fun actionPerformed(e: ActionEvent?) {
        checkCollisions()
        snake.move()
        checkIfHorizontalLinesIsFilled()
        checkIfVerticalLinesIsFilled()
        repaint()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g as Graphics2D)
        for (y in 0 until board.size) {
            for (x in 0 until board[y].size) {
                g.color = when (board[y][x]) {
                    OBSTACLE_TAG -> Color.RED
                    SNAKE_HEAD_TAG -> Color.YELLOW
                    SNAKE_BODY_TAG -> Color.WHITE
                    FOOD_TAG -> Color.GREEN
                    EMPTY_TAG -> Color.BLACK
                    else -> Color.BLACK
                }
                g.fillRect(x * POINT_SIZE_BLOCK, y * POINT_SIZE_BLOCK, POINT_SIZE_SNAKE, POINT_SIZE_SNAKE)
            }
        }
    }

    private fun checkCollisions() {
        if (snake.checkCollisionWith(snake)) gameEnd()
        if (foodManager.food != null) {
            if (snake.checkCollisionWith(foodManager.food!!)) {
                val point = Point()
                with(snake.body[snake.body.size - 1]) {
                    point.x = this.x
                    point.y = this.y
                }
                snake.body.add(point)
                foodManager.foodEaten()
                if (timer.delay > 0) timer.delay--
                score++
            }
        }

        var checkX = snake.body[0].x
        var checkY = snake.body[0].y
        when (snake.direction) {
            Direction.UP -> checkY = if (checkY > 0) --checkY else HEIGHT / POINT_SIZE_BLOCK - 1
            Direction.RIGHT -> checkX = if (checkX < WIDTH / POINT_SIZE_BLOCK - 1) ++checkX else 0
            Direction.DOWN -> checkY = if (checkY < HEIGHT / POINT_SIZE_BLOCK - 1) ++checkY else 0
            Direction.LEFT -> checkX = if (checkX > 0) --checkX else WIDTH / POINT_SIZE_BLOCK - 1
        }
        if (board[checkY][checkX] == OBSTACLE_TAG && !snake.isFalling) gameEnd()
    }

    private fun checkIfHorizontalLinesIsFilled() {
        for (y in 0 until board.size) {
            var fills = 0
            for (x in 0 until board[y].size) if (board[y][x] == OBSTACLE_TAG) fills++
            if (fills == board[y].size) clearRow(y)
        }
    }

    private fun clearRow(row: Int) {
        for (i in 0 until row) board[row][i] = EMPTY_TAG
        moveObstaclesDown()
        score += 10
    }

    private fun moveObstaclesDown() {
        for (y in board.size - 1 downTo 1) {
            for (x in 0 until board[y].size) {
                if (board[y - 1][x] == OBSTACLE_TAG) {
                    board[y][x] = board[y - 1][x]
                    board[y - 1][x] = EMPTY_TAG
                }
            }
        }
    }

    private fun checkIfVerticalLinesIsFilled() {
        for (x in 0 until board.size) {
            var fills = 0
            for (n in 0 until board.size) if (board[n][x] == OBSTACLE_TAG) fills++
            if (fills == board.size) clearColumn(x)
        }
    }

    private fun clearColumn(x: Int) {
        for (i in 0 until board.size) board[i][x] = EMPTY_TAG
        score += 10
    }

    private fun gameEnd() {
        obstacleCreator.interrupt()
        foodManager.interrupt()
        snake.isAlive = false
        timer.stop()
        showEngGameDialog()
        score = 0
    }

    private fun restart() {
        obstacleCreator = ObstacleCreator()
        foodManager = FoodManager()
        snake.isAlive = true
        snake.isFalling = false
        timer.delay = BASE_DELAY
        initGame()
    }

    private fun pause() {
        isGamePaused = true
        obstacleCreator.interrupt()
        foodManager.interrupt()
        timer.stop()
        showPauseDialog()
    }

    private fun resume() {
        obstacleCreator = ObstacleCreator()
        obstacleCreator.start()
        foodManager = FoodManager()
        foodManager.start()
        isGamePaused = false
        timer.start()
    }

    private fun showPauseDialog() {
        val choice = JOptionPane.showConfirmDialog(this, "Score: $score\nReady to go?", "Game Paused", JOptionPane.YES_NO_OPTION)
        if (choice == JOptionPane.YES_OPTION) resume()
        else showEngGameDialog()
    }

    private fun showEngGameDialog() {
        val choice = JOptionPane.showConfirmDialog(this, "Score: $score\nWould you like to retry?", "Game over", JOptionPane.YES_NO_OPTION)
        if (choice == JOptionPane.YES_OPTION) restart()
        else System.exit(0)
    }

    companion object {
        private const val BASE_DELAY = 140
        private const val BASE_OBSTACLE_CREATOR_DELAY_TIME = 100_000L
        private const val BASE_FOOD_CREATOR_DELAY_TIME = 100_000L
        const val WALL_SIZE = 3
        const val FOOD_TAG = 'F'
        const val SNAKE_HEAD_TAG = 'H'
        const val SNAKE_BODY_TAG = 'S'
        const val OBSTACLE_TAG = 'O'
        const val EMPTY_TAG = 'E'
        const val WIDTH = 200
        const val HEIGHT = 200
        const val POINT_SIZE_BLOCK = 10
        const val POINT_SIZE_SNAKE = POINT_SIZE_BLOCK - 1
        val board = Array(HEIGHT / POINT_SIZE_BLOCK) { CharArray(WIDTH / POINT_SIZE_BLOCK) }
        lateinit var snake: Snake
    }

    private inner class ObstacleCreator : Thread() {
        var delayTime = BASE_OBSTACLE_CREATOR_DELAY_TIME
            private set

        override fun run() {
            while (snake.isAlive && !isInterrupted) {
                try {
                    sleep(delayTime)
                    Obstacle(WALL_SIZE)
                    delayTime -= 10
                } catch (e: InterruptedException) {
                }
            }
        }
    }

    private inner class FoodManager : Thread(), Observer {
        var delayTime = BASE_FOOD_CREATOR_DELAY_TIME
            private set
        private lateinit var foodThread: Thread
        var food: Food? = null

        init {
            if (food == null) food = Food()
        }

        override fun run() {
            createFood()
            while (!isInterrupted) {
                try {
                    if (snake.isAlive && food == null) {
                        createFood()
                        sleep(delayTime)
                        delayTime -= 10
                    }
                } catch (e: InterruptedException) {
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
            food!!.interruptFlag = true
            food!!.deleteObserver(this)
            board[food!!.y][food!!.x] = EMPTY_TAG
            food = null
        }

        private fun createFood() {
            if (food == null) food = Food()
            food!!.addObserver(this)
            foodThread = Thread(food).apply { start() }
        }
    }
}
