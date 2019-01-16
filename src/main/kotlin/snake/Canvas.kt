package snake

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
 **    for project Snake
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
        snake.body.add(Point(400 + POINT_SIZE, 200))
        snake.body.add(Point(410 + POINT_SIZE, 200))
        snake.body.add(Point(420 + POINT_SIZE, 200))
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
            if (checkCollisionAtSelf() || checkCollisionAtObstaclesViaHead()) gameEnd()
            if (checkCollisionAtPointViaHead(food)) {
                val point = Point()
                with(snake.body[snake.body.size - 1]) {
                    point.x = this.x
                    point.y = this.y
                }
                createFood()
                snake.body.add(point)
                if (timer.delay > 1) timer.delay -= 20
            }
            g.color = Color.YELLOW
            g.fillRect(snake.body[0].x, snake.body[0].y, POINT_SIZE, POINT_SIZE)
            g.color = Color.WHITE
            for (i in 1 until snake.body.size) g.fillRect(snake.body[i].x, snake.body[i].y, POINT_SIZE, POINT_SIZE)
            g.color = Color.GREEN
            g.fillRect(food.x, food.y, POINT_SIZE, POINT_SIZE)

            if (obstacles.isNotEmpty()) {
                println("obstales size: ${obstacles.size}")
                g.color = Color.RED
                for (i in 0 until obstacles.size) {
                    for (j in 0 until WALL_SIZE) g.fillRect(obstacles[i].wall[j]!!.x, obstacles[i].wall[j]!!.y, POINT_SIZE, POINT_SIZE)
                }
            }
        }
        (this.parent.parent.parent as JFrame).title = "Tetro-Snake! | food  collected: ${snake.body.size - 3}"
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

    private fun fixPosition() {
        isFalling = false
        for (i in 0 until snake.body.size step 3) {
            val point = Point(snake.body[i])
            val point2 = Point(snake.body[i + 1])
            val point3 = Point(snake.body[i + 2])
            val obstacle = Obstacle()
            with(obstacle) {
                wall[0] = point
                wall[1] = point2
                wall[2] = point3
            }
            obstacles.add(obstacle)
        }

        snake = Snake()
        snake.body.add(Point(400 + POINT_SIZE, 200))
        snake.body.add(Point(410 + POINT_SIZE, 200))
        snake.body.add(Point(420 + POINT_SIZE, 200))
    }

    private fun checkCollisionAtPointViaHead(item: Point) =
            snake.body[0].x in item.x - POINT_SIZE..item.x + POINT_SIZE &&
                    snake.body[0].y in item.y - POINT_SIZE..item.y + POINT_SIZE

    private fun checkCollisionAtSelf(): Boolean {
        for (i in 1 until snake.body.size) if (snake.body[0] == snake.body[i]) return true
        return false
    }

    private fun checkCollisionAtObstaclesViaHead(): Boolean {
        for (obstacle in obstacles) {
            for (i in 0 until WALL_SIZE) if (checkCollisionAtPointViaHead(Point(obstacle.wall[i]!!.x, obstacle.wall[i]!!.y))) return true
        }
        return false
    }

    private fun checkCollisionAtObstacles(): Boolean {
        for (sp in snake.body) {
            for ((i, obstacle) in obstacles.withIndex()) {
                if (sp.location == obstacle.wall[i]!!.location) return true
            }
        }
        return false
    }

    private fun createFood() {
        food.x = (Math.random() * WIDTH).toInt() + 1 - POINT_SIZE
        food.y = (Math.random() * HEIGHT).toInt() + 1 - POINT_SIZE
    }

    override fun actionPerformed(e: ActionEvent?) {
        if (!isFalling) {
            for (i in snake.body.size - 1 downTo 1) {
                snake.body[i].x = snake.body[i - 1].x
                snake.body[i].y = snake.body[i - 1].y
            }

            when (snake.direction) {
                Direction.UP -> snake.body[0].y -= POINT_SIZE + 1
                Direction.RIGHT -> snake.body[0].x += POINT_SIZE + 1
                Direction.DOWN -> snake.body[0].y += POINT_SIZE + 1
                Direction.LEFT -> snake.body[0].x -= POINT_SIZE + 1
            }

            when {
                snake.body[0].y > HEIGHT -> snake.body[0].y = 0
                snake.body[0].y < 0 -> snake.body[0].y = HEIGHT
                snake.body[0].x > WIDTH -> snake.body[0].x = 0
                snake.body[0].x < 0 -> snake.body[0].x = WIDTH
            }
        } else {
            for (i in 0 until snake.body.size) snake.body[i].y += POINT_SIZE
            val max = snake.body.maxBy { it.y }!!
            if (max.y > HEIGHT - WALL_SIZE || checkCollisionAtObstacles()) fixPosition()
        }

        repaint()
    }

    companion object {
        private const val DELAY = 150
        private const val WIDTH = 500
        private const val HEIGHT = 300
        private const val POINT_SIZE = 10
        private const val WALL_SIZE = 3
    }

    private inner class Snake {
        val body = ArrayList<Point>()
        var direction = Direction.LEFT
    }

    private inner class ObstacleCreator : Thread() {
        override fun run() {
            while (isAlive) {
                Thread.sleep(10_000)
                obstacles.add(Obstacle())
            }
        }
    }

    private inner class Obstacle {
        val wall = arrayOfNulls<Point>(WALL_SIZE)

        init {
            val x = (Math.random() * WIDTH - POINT_SIZE).toInt() + 1
            val y = (Math.random() * HEIGHT - POINT_SIZE).toInt() + 1
            val anchor = Point(x, y)
            if (Random().nextBoolean()) {
                for (i in 0 until WALL_SIZE) {
                    anchor.x += POINT_SIZE
                    wall[i] = Point(anchor.x, anchor.y)
                }
            } else {
                for (i in 0 until WALL_SIZE) {
                    anchor.y += POINT_SIZE
                    wall[i] = Point(anchor.x, anchor.y)
                }
            }
        }
    }

    private enum class Direction {
        UP, RIGHT, DOWN, LEFT
    }
}