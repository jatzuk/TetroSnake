package tetrosnake

import util.Util.getRoundX10
import util.Util.getRoundY10
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.util.*
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
    private val timer = Timer(DELAY, this)
    private val food = Point()
    private val obstacles = arrayListOf<Obstacle>()
    private var snake = Snake()
    private var isAlive = true
    private var isFalling = false

    init {
        background = Color.BLACK
        isFocusable = true
        preferredSize = Dimension(WIDTH, HEIGHT)
        initGame()
    }

    private fun initGame() {
        snake.body.add(Point(400 + POINT_SIZE_BLOCK, 200))
        snake.body.add(Point(410 + POINT_SIZE_BLOCK, 200))
        snake.body.add(Point(420 + POINT_SIZE_BLOCK, 200))
        createFood()
        addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent?) {
                when (e?.keyCode) {
                    KeyEvent.VK_UP -> if (snake.direction != Direction.DOWN) snake.direction = Direction.UP
                    KeyEvent.VK_RIGHT -> if (snake.direction != Direction.LEFT) snake.direction = Direction.RIGHT
                    KeyEvent.VK_DOWN -> if (snake.direction != Direction.UP) snake.direction = Direction.DOWN
                    KeyEvent.VK_LEFT -> if (snake.direction != Direction.RIGHT) snake.direction = Direction.LEFT
                    KeyEvent.VK_SPACE -> if (!isFalling) fallDown()
                }
            }
        })
        timer.start()
        ObstacleCreator().start()
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        render(g!! as Graphics2D)
    }

    private fun render(g: Graphics2D) {
        if (isAlive) {
            if (!isFalling && (checkCollisionAtSelf() || checkCollisionAtObstaclesViaHead())) gameEnd()
            if (!isFalling && checkCollisionAtPointViaHead(food)) {
                val point = Point()
                with(snake.body[snake.body.size - 1]) {
                    point.x = this.x
                    point.y = this.y
                }
                createFood()
                snake.body.add(point)
                if (timer.delay > 1) timer.delay -= 1
            }
            g.color = Color.YELLOW
            g.fillRect(snake.body[0].x, snake.body[0].y, POINT_SIZE_SNAKE, POINT_SIZE_SNAKE)
            g.color = Color.WHITE
            for (i in 1 until snake.body.size) g.fillRect(snake.body[i].x, snake.body[i].y, POINT_SIZE_SNAKE, POINT_SIZE_SNAKE)
            g.color = Color.GREEN
            g.fillRect(food.x, food.y, POINT_SIZE_BLOCK, POINT_SIZE_BLOCK)

            if (obstacles.isNotEmpty()) {
                g.color = Color.RED
                for (i in 0 until obstacles.size) {
                    for (j in 0 until obstacles[i].wall.size) g.fillRect(obstacles[i].wall[j]!!.x, obstacles[i].wall[j]!!.y, POINT_SIZE_BLOCK, POINT_SIZE_BLOCK)
                }
            }
        }
        (this.parent.parent.parent as JFrame).title = "Tetro-Snake! | score: ${snake.body.size - 3} food x: ${food.x},y: ${food.y} snake's head - x: ${snake.body[0].x}, y: ${snake.body[0].y}"
    }

    private fun gameEnd() {
        isAlive = false
        timer.stop()
    }

    private fun restart() {
        initGame()
        isAlive = true
    }

    private fun fallDown() {
        isFalling = true
    }

    private fun createObstacleFromSnake() {
        val obstacle = Obstacle(snake.body.size)
        for (i in 0 until snake.body.size) obstacle.wall[i] = snake.body[i]
        obstacles.add(obstacle)

        snake = Snake()
        snake.body.add(Point(400 + POINT_SIZE_BLOCK, 200))
        snake.body.add(Point(410 + POINT_SIZE_BLOCK, 200))
        snake.body.add(Point(420 + POINT_SIZE_BLOCK, 200))
        isFalling = false
    }

    private fun checkCollisionAtPointViaHead(item: Point) =
            snake.body[0].x in item.x - POINT_SIZE_BLOCK + 1 until item.x + POINT_SIZE_BLOCK - 1 &&
                    snake.body[0].y in item.y - POINT_SIZE_BLOCK + 1 until item.y + POINT_SIZE_BLOCK - 1

    private fun checkCollisionAtSelf(): Boolean {
        for (i in 1 until snake.body.size) if (snake.body[0] == snake.body[i]) return true
        return false
    }

    private fun checkCollisionAtObstaclesViaHead(): Boolean {
        for (obstacle in obstacles) {
            for (i in 0 until obstacle.wall.size) if (checkCollisionAtPointViaHead(Point(obstacle.wall[i]!!.x, obstacle.wall[i]!!.y))) return true
        }
        return false
    }

    private fun checkCollisionAtObstacles(): Boolean {
        println("all obstacles size: ${obstacles.size}")
        for (i in 0 until obstacles.size) {
            println("obstacle $i")
            for (j in 0 until snake.body.size) if (snake.body[j] == obstacles[i].wall) return true
        }
        return false
    }

    private fun createFood() {
        food.x = getRoundX10()
        food.y = getRoundY10()
    }

    override fun actionPerformed(e: ActionEvent?) {
        if (!isFalling) {
            for (i in snake.body.size - 1 downTo 1) {
                snake.body[i].x = snake.body[i - 1].x
                snake.body[i].y = snake.body[i - 1].y
            }

            when (snake.direction) {
                Direction.UP -> snake.body[0].y -= POINT_SIZE_BLOCK
                Direction.RIGHT -> snake.body[0].x += POINT_SIZE_BLOCK
                Direction.DOWN -> snake.body[0].y += POINT_SIZE_BLOCK
                Direction.LEFT -> snake.body[0].x -= POINT_SIZE_BLOCK
            }

            when {
                snake.body[0].y > HEIGHT -> snake.body[0].y = 0
                snake.body[0].y < 0 -> snake.body[0].y = HEIGHT
                snake.body[0].x > WIDTH -> snake.body[0].x = 0
                snake.body[0].x < 0 -> snake.body[0].x = WIDTH
            }
        } else {
            for (i in 0 until snake.body.size) snake.body[i].y += POINT_SIZE_BLOCK
            val max = snake.body.maxBy { it.y }!!
            if (checkCollisionAtObstacles() || (max.y > HEIGHT - POINT_SIZE_BLOCK)) createObstacleFromSnake()
        }

        repaint()
    }


    companion object {
        private const val OBSTACLE_CREATOR_DELAY_TIME = 10_000L
        private const val DELAY = 150
        private const val WALL_SIZE = 3
        const val WIDTH = 500
        const val HEIGHT = 300
        const val POINT_SIZE_BLOCK = 10
        const val POINT_SIZE_SNAKE = POINT_SIZE_BLOCK - 1
    }

    private inner class Snake {
        val body = ArrayList<Point>()
        var direction = Direction.LEFT
    }

    private inner class ObstacleCreator : Thread() {
        override fun run() {
            while (isAlive) {
                Thread.sleep(OBSTACLE_CREATOR_DELAY_TIME)
                obstacles.add(Obstacle(WALL_SIZE))
            }
        }
    }

    private inner class Obstacle(size: Int) {
        val wall = arrayOfNulls<Point>(size)

        init {
            val anchor = Point(getRoundX10(), getRoundY10())
            if (Random().nextBoolean()) {
                for (i in 0 until size) {
                    anchor.x += POINT_SIZE_BLOCK + 1
//                    TODO("ugly spaces")
                    wall[i] = Point(anchor.x, anchor.y)
                }
            } else {
                for (i in 0 until size) {
                    anchor.y += POINT_SIZE_BLOCK + 1
                    wall[i] = Point(anchor.x, anchor.y)
                }
            }
        }
    }

    private enum class Direction {
        UP, RIGHT, DOWN, LEFT
    }
}