package tetrosnake

import util.Util.Direction
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JFrame
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
    private var isPause = false
    private lateinit var food: Food

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
                        KeyEvent.VK_UP -> if (!isFalling && direction != Direction.DOWN) direction = Direction.UP
                        KeyEvent.VK_RIGHT -> if (!isFalling && direction != Direction.LEFT) direction = Direction.RIGHT
                        KeyEvent.VK_DOWN -> if (!isFalling && direction != Direction.UP) direction = Direction.DOWN
                        KeyEvent.VK_LEFT -> if (!isFalling && direction != Direction.RIGHT) direction = Direction.LEFT
                        KeyEvent.VK_SPACE -> if (!isFalling) isFalling = true
                        KeyEvent.VK_R -> if (!isAlive) restart()
                        KeyEvent.VK_P -> if (!isPause) pause() else unPause()
//                    TODO("debug only")
                        KeyEvent.VK_S -> printBoard()
                    }
                }
            }
        })
        timer.start()
        obstacleCreator.start()
    }

    private fun initGameObjects() {
        for (y in 0 until board.size) {
            for (x in 0 until board[y].size) board[y][x] = EMPTY_TAG
        }

        snake = Snake()
        food = Food()
    }

    override fun actionPerformed(e: ActionEvent?) {
        checkCollisions()
        snake.move()
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
        (this.parent.parent.parent as JFrame).title = "Tetro-Snake! | " +
                "score: ${snake.body.size - 3} $FOOD_TAG: x: ${food.body[0].x},y: ${food.body[0].y} " +
                "$SNAKE_HEAD_TAG: - x: ${snake.body[0].x}, y: ${snake.body[0].y}, d = ${timer.delay}"
    }

    private fun checkCollisions() {
        if (snake.checkCollisionWith(snake)) gameEnd()
        if (snake.checkCollisionWith(food)) {
            val point = Point()
            with(snake.body[snake.body.size - 1]) {
                point.x = this.x
                point.y = this.y
            }
            snake.body.add(point)
            food = Food()
            if (timer.delay > 0) timer.delay--
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

    private fun gameEnd() {
        obstacleCreator.interrupt()
        snake.isAlive = false
        timer.stop()
    }

    private fun restart() {
        obstacleCreator = ObstacleCreator()
        snake.isAlive = true
        snake.isFalling = false
        timer.delay = BASE_DELAY
        initGame()
    }

    private fun pause() {
        isPause = true
        timer.stop()
        obstacleCreator.interrupt()
    }

    private fun unPause() {
        isPause = false
        timer.restart()
        obstacleCreator = ObstacleCreator()
        obstacleCreator.start()
    }

    companion object {
        private const val BASE_DELAY = 140
        private const val OBSTACLE_CREATOR_DELAY_TIME = 1_000L
        const val WALL_SIZE = 3
        const val FOOD_TAG = 'F'
        const val SNAKE_HEAD_TAG = 'H'
        const val SNAKE_BODY_TAG = 'S'
        const val OBSTACLE_TAG = 'O'
        const val EMPTY_TAG = 'E'
        const val WIDTH = 500
        const val HEIGHT = 300
        const val POINT_SIZE_BLOCK = 10
        const val POINT_SIZE_SNAKE = POINT_SIZE_BLOCK - 1
        val board = Array(Canvas.HEIGHT / Canvas.POINT_SIZE_BLOCK) { CharArray(Canvas.WIDTH / Canvas.POINT_SIZE_BLOCK) }
        lateinit var snake: Snake

        fun arrangeNewGameObject(startX: Int, startY: Int, direction: Boolean = true, requestedSize: Int = 1, recursionDepth: Int = 0): Pair<Int, Int> {
//            horizontal

            if (recursionDepth == 10) {
                throw IllegalArgumentException("recursion level reached 10, can not find appropriate coordinates")
            }

            println("trying to place new game object with x = $startX y = $startY requested size: $requestedSize, recursion level: $recursionDepth")
            val endX = startX + requestedSize
            val endY = startY + 1
            var matchedBlocks = 0
            val pair = Pair(startX, startY)

            for (x in startX until endX) {
                if (board[startY][x] == EMPTY_TAG) matchedBlocks++
                if (matchedBlocks == requestedSize) {
                    println("\trequested coordinates for figure with size: $requestedSize, matched successfully with x: $startX, y: $startY, recursion level: $recursionDepth")
                    break
                }
            }

            if (matchedBlocks < requestedSize) {
                when {
//                        is enough space to the right?
                    WIDTH / POINT_SIZE_BLOCK - 1 - endX > 2 -> arrangeNewGameObject(endX + 1, startY, true, requestedSize, recursionDepth + 1)
//                        is enough space to the left?
                    startX > 3 -> arrangeNewGameObject(0, startY, true, requestedSize, recursionDepth + 1)
//                        check the top
                    startY > 3 -> arrangeNewGameObject(startX, 0, true, requestedSize, recursionDepth + 1)
//                        check the bottom
                    HEIGHT / POINT_SIZE_BLOCK - 1 - endY > 2 -> arrangeNewGameObject(startX, endY + 1, true, requestedSize, recursionDepth + 1)
                }
            }

            return pair
        }
    }

    private inner class ObstacleCreator : Thread() {
        override fun run() {
            while (snake.isAlive && !isInterrupted) {
                sleep(OBSTACLE_CREATOR_DELAY_TIME)
                Obstacle(WALL_SIZE)
            }
        }
    }

    fun printBoard() {
        for (y in 23 until board.size) {
            for (x in 25 until board[y].size) {
                print(board[y][x])
            }
            println()
        }
        println("---------------------------------------\n")
    }
}