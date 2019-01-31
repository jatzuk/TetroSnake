package tetrosnake

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import util.Direction
import util.GameObjectManager
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.IOException
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.Timer

/*
 * Created with passion and love
 *    for project TetroSnake
 *        by Jatzuk on 17-Jan-19
 *                                            *_____*
 *                                           *_*****_*
 *                                          *_(O)_(O)_*
 *                                         **____V____**
 *                                         **_________**
 *                                         **_________**
 *                                          *_________*
 *                                           ***___***
 */

class Canvas : JPanel(), ActionListener {
    private val timer = Timer(BASE_DELAY, this)
    private var gameObjectManager = GameObjectManager

    init {
        background = Color.BLACK
        isFocusable = true
        preferredSize = Dimension(WIDTH - POINT_SIZE_BLOCK, HEIGHT - POINT_SIZE_BLOCK)
        addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent?) {
                with(snake) {
                    when (e?.keyCode) {
                        KeyEvent.VK_UP -> if (direction != Direction.DOWN) direction = Direction.UP
                        KeyEvent.VK_RIGHT -> if (direction != Direction.LEFT) direction = Direction.RIGHT
                        KeyEvent.VK_DOWN -> if (direction != Direction.UP) direction = Direction.DOWN
                        KeyEvent.VK_LEFT -> if (direction != Direction.RIGHT) direction = Direction.LEFT
                        KeyEvent.VK_SPACE -> if (!isFalling) { isFalling = true; direction = Direction.DOWN }
                        KeyEvent.VK_P -> if (!isGamePaused) pause()
                        KeyEvent.VK_H -> if (!isGamePaused) aboutDialog()
                    }
                }
            }
        })
        initGame()
    }

    private fun initGame() {
        for (y in 0 until board.size) for (x in 0 until board[y].size) board[y][x] = EMPTY_TAG
        snake = Snake()
        gameFlow = GlobalScope.launch { gameObjectManager.lifeCycle() }
        timer.start()
    }

    override fun actionPerformed(e: ActionEvent?) {
        checkCollisions()
        if (snake.overfed()) snake.isFalling = true
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
                    SNAKE_OVERFED_TAG -> Color.ORANGE
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
        if (snake.checkCollisionWith(gameObjectManager.food)) {
            val point = Point()
            with(snake.body[snake.body.size - 1]) {
                point.x = this.x
                point.y = this.y
            }
            snake.body.add(point)
            score++
            gameObjectManager.foodEaten()
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

    private fun checkIfHorizontalLinesIsFilled() {
        for (y in 0 until board.size) {
            var fills = 0
            for (x in 0 until board[y].size) if (board[y][x] == OBSTACLE_TAG) fills++ else break
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
            for (n in 0 until board.size) if (board[n][x] == OBSTACLE_TAG) fills++ else break
            if (fills == board.size) clearColumn(x)
        }
    }

    private fun clearColumn(x: Int) {
        for (i in 0 until board.size) board[i][x] = EMPTY_TAG
        score += 10
    }

    private fun gameEnd() {
        snake.isAlive = false
        gameFlow.cancel()
        timer.stop()
        gameEndDialog()
        score = 0
    }

    private fun restart() {
        initGame()
    }

    private fun pause(flag: Boolean = true) {
        timer.stop()
        isGamePaused = true
        if (flag) pauseDialog()
    }

    private fun resume() {
        isGamePaused = false
        timer.start()
    }

    private fun aboutDialog() {
        pause(false)
        val customFont = try {
            BufferedInputStream(FileInputStream("src/main/resources/BalooThambi-Regular.ttf")).use {
                Font.createFont(Font.TRUETYPE_FONT, it).deriveFont(14F)
            }
        } catch (e: IOException) {
            font
        }
        val msg = """Welcome to the TetroSnake game!
                        |Controls:
                        |   direction - arrows
                        |   space - enter tetris mode
                        |   P - pause
                        |   H - about dialog
                    |Snake length more then 10? - falling down. Stop feeding!
                    |When snake length is 9 it will turn orange showing it's
                    |time to dump a load
                    |
                    |Created with passion and love by Jatzuk on 31-Jan-19
                    |                        *_____*
                    |                       *_*****_*
                    |                    *_(O)_(O)_*
                    |                 **____V____**
                    |                 **_________**
                    |                 **_________**
                    |                  *_________*
                    |                       ***___***
                    |""".trimMargin()
        val ta = JTextArea(msg).apply {
            isEditable = false
            font = customFont
            background = Color(238, 238, 238)
        }
        JOptionPane.showMessageDialog(this, ta, "About", JOptionPane.INFORMATION_MESSAGE)
        resume()
    }

    private fun pauseDialog() {
        val choice = JOptionPane.showConfirmDialog(this, "Score: $score\nReady to go?", "Game Paused", JOptionPane.YES_NO_OPTION)
        if (choice == JOptionPane.YES_OPTION) resume()
        else gameEndDialog()
    }

    private fun gameEndDialog() {
        val choice = JOptionPane.showConfirmDialog(this, "Score: $score\nWould you like to retry?\nPress H while in game to show Info dialog", "Game over", JOptionPane.YES_NO_OPTION)
        if (choice == JOptionPane.YES_OPTION) restart()
        else System.exit(0)
    }

    companion object {
        private const val BASE_DELAY = 140
        const val WALL_SIZE = 3
        const val WIDTH = 200
        const val HEIGHT = 200
        const val POINT_SIZE_BLOCK = 10
        const val POINT_SIZE_SNAKE = POINT_SIZE_BLOCK - 1
        const val FOOD_TAG = 'F'
        const val SNAKE_HEAD_TAG = 'H'
        const val SNAKE_BODY_TAG = 'S'
        const val SNAKE_OVERFED_TAG = 'D'
        const val OBSTACLE_TAG = 'O'
        const val EMPTY_TAG = 'E'
        private var score = 0
        val board = Array(HEIGHT / POINT_SIZE_BLOCK) { CharArray(WIDTH / POINT_SIZE_BLOCK) }
        var isGamePaused = false
        lateinit var snake: Snake
        lateinit var gameFlow: Job
    }
}
