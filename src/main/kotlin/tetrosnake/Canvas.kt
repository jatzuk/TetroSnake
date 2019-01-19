package tetrosnake

import util.Util.Direction
import util.Util.GameObject
import util.Util.getRandomX
import util.Util.getRandomY
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
    private val timer = Timer(BASE_DELAY, this)
    private lateinit var snake: Snake
    private lateinit var food: Food
    private var obstacles = ArrayList<Obstacle>()
    private var isAlive = true
    private var isFalling = false
    private var isPause = false
    private val board = Array(HEIGHT / POINT_SIZE_BLOCK) { CharArray(WIDTH / POINT_SIZE_BLOCK) }

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
                when (e?.keyCode) {
                    KeyEvent.VK_UP -> if (snake.direction != Direction.DOWN) snake.direction = Direction.UP
                    KeyEvent.VK_RIGHT -> if (snake.direction != Direction.LEFT) snake.direction = Direction.RIGHT
                    KeyEvent.VK_DOWN -> if (snake.direction != Direction.UP) snake.direction = Direction.DOWN
                    KeyEvent.VK_LEFT -> if (snake.direction != Direction.RIGHT) snake.direction = Direction.LEFT
                    KeyEvent.VK_SPACE -> if (!isFalling) isFalling = true
                    KeyEvent.VK_R -> if (!isAlive) restart()
                    KeyEvent.VK_P -> if (!isPause) pause() else unPause()
//                    TODO("debug only")
                    KeyEvent.VK_S -> printBoard()
                }
            }
        })
        timer.start()
        ObstacleCreator().start()
    }

    private fun initGameObjects() {
        for (y in 0 until board.size) {
            for (x in 0 until board[y].size) board[y][x] = EMPTY_TAG
        }

        snake = Snake()
        with(snake.body) {
            add(Point(40, 20))
            add(Point(41, 20))
            add(Point(42, 20))
        }
        for ((sp, i) in (0 until snake.body.size).withIndex()) {
            val char = if (i == 0) SNAKE_HEAD_TAG else SNAKE_BODY_TAG
            board[snake.body[sp].y][snake.body[sp].x] = char
        }

        food = Food()
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        render(g!! as Graphics2D)
    }

    private fun render(g: Graphics2D) {
        if (isAlive) {
//            if (!isFalling && (checkCollisionAtSelf() || checkCollisionAtObstaclesViaHead())) gameEnd()
//            if (!isFalling && checkCollisionAtPointViaHead(food)) {
//                val point = Point()
//                with(snake.body[snake.body.size - 1]) {
//                    point.x = this.x
//                    point.y = this.y
//                }
//                createFood()
//                snake.body.add(point)
//                if (timer.delay > 1) timer.delay -= 1
//            }
//            g.color = Color.YELLOW
//            g.fillRect(snake.body[0].x, snake.body[0].y, POINT_SIZE_SNAKE, POINT_SIZE_SNAKE)
//            g.color = Color.WHITE
//            for (i in 1 until snake.body.size) g.fillRect(snake.body[i].x, snake.body[i].y, POINT_SIZE_SNAKE, POINT_SIZE_SNAKE)
//            g.color = Color.GREEN
//            g.fillRect(food.x, food.y, POINT_SIZE_BLOCK, POINT_SIZE_BLOCK)
//
//            if (obstacles.isNotEmpty()) {
//                g.color = Color.RED
//                for (i in 0 until obstacles.size) {
//                    for (j in 0 until obstacles[i].wall.size) g.fillRect(obstacles[i].wall[j]!!.x, obstacles[i].wall[j]!!.y, POINT_SIZE_BLOCK, POINT_SIZE_BLOCK)
//                }
//            }

            if (!isFalling && (snake.checkCollisionWith(snake))) gameEnd()
            if (!isFalling && snake.checkCollisionWith(food)) {
                val point = Point()
                with(snake.body[snake.body.size - 1]) {
                    point.x = this.x
                    point.y = this.y
                }
                snake.body.add(point)
                food = Food()
                if (timer.delay > 0) timer.delay--
            }

            if (obstacles.isNotEmpty()) {
                for (i in 0 until obstacles.size) {
                    for (j in 0 until obstacles[i].body.size) if (snake.checkCollisionWith(obstacles[i])) gameEnd()
                }
            }

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
        (this.parent.parent.parent as JFrame).title = "Tetro-Snake! | score: ${snake.body.size - 3} $FOOD_TAG: x: ${food.body[0].x},y: ${food.body[0].y} $SNAKE_HEAD_TAG: - x: ${snake.body[0].x}, y: ${snake.body[0].y}, d = ${timer.delay}"
    }

    private fun gameEnd() {
        isAlive = false
        timer.stop()
    }

    private fun restart() {
        initGame()
        isAlive = true
        isFalling = false
        timer.delay = BASE_DELAY
    }

    private fun pause() {
        isPause = true
        timer.stop()
    }

    private fun unPause() {
        isPause = false
        timer.restart()
    }

//    private fun createObstacleFromSnake() {
//        val obstacle = Obstacle(snake.body.size)
//        for (i in 0 until snake.body.size) obstacle.body[i] = snake.body[i]
//        obstacles.add(obstacle)
//        initGameObjects()
//        isFalling = false
//    }
//
//    private fun checkCollisionAtPointViaHead(item: Point) =
//            snake.body[0].x in item.x - POINT_SIZE_BLOCK + 1 until item.x + POINT_SIZE_BLOCK - 1 &&
//                    snake.body[0].y in item.y - POINT_SIZE_BLOCK + 1 until item.y + POINT_SIZE_BLOCK - 1
//
//    private fun checkCollisionAtSelf(): Boolean {
//        for (i in 1 until snake.body.size) if (snake.body[0] == snake.body[i]) return true
//        return false
//    }
//
//    private fun checkCollisionAtObstaclesViaHead(): Boolean {
//        for (obstacle in obstacles) {
//            for (i in 0 until obstacle.body.size) if (checkCollisionAtPointViaHead(Point(obstacle.body[i]!!.x, obstacle.body[i]!!.y))) return true
//        }
//        return false
//    }
//
//    private fun checkCollisionAtObstacles(): Boolean {
//        println("all obstacles size: ${obstacles.size}")
//
////        TODO("optimization ??")
////        val topBound = snake.body.maxBy { it.y }!!.y
////        val relativeObstacles = mutableListOf<Point>()
////        for (obstacle in obstacles) {
////            for (i in 0 until obstacle.wall.size) {
////                if (obstacle.wall[i]!!.y < topBound) relativeObstacles.add(obstacle.wall[i]!!)
////            }
////        }
//
//        for (i in 0 until obstacles.size) {
//            for (j in 0 until obstacles[i].body.size) {
//                for (k in 0 until snake.body.size) {
//                    val ox = obstacles[i].body[j].x
//                    val oy = obstacles[i].body[j].y
//                    val sx = snake.body[k].x
//                    val sy = snake.body[k].y
//                    println("$sx in ${ox - POINT_SIZE_BLOCK} < ${ox + POINT_SIZE_BLOCK}, $sy in ${oy - POINT_SIZE_BLOCK} < ${oy + POINT_SIZE_BLOCK}")
////                    TODO("without 1 '-1' works perfectly")
//                    if (sx in ox - POINT_SIZE_BLOCK + 1 until ox + POINT_SIZE_BLOCK - 1 && sy in oy - POINT_SIZE_BLOCK until oy + POINT_SIZE_BLOCK - 1) {
//                        println("\tCollision at: $sx in ${ox - POINT_SIZE_BLOCK} < ${ox + POINT_SIZE_BLOCK}, $sy in ${oy - POINT_SIZE_BLOCK} < ${oy + POINT_SIZE_BLOCK}")
//                        return true
//                    }
//                }
//            }
//        }
//
//        return false
//    }

    override fun actionPerformed(e: ActionEvent?) {
        snake.move()
        repaint()
    }

    companion object {
        private const val OBSTACLE_CREATOR_DELAY_TIME = 10_000L
        private const val BASE_DELAY = 140
        private const val WALL_SIZE = 3
        private const val FOOD_TAG = 'F'
        private const val SNAKE_HEAD_TAG = 'H'
        private const val SNAKE_BODY_TAG = 'S'
        private const val OBSTACLE_TAG = 'O'
        private const val EMPTY_TAG = 'E'
        const val WIDTH = 500
        const val HEIGHT = 300
        const val POINT_SIZE_BLOCK = 10
        const val POINT_SIZE_SNAKE = POINT_SIZE_BLOCK - 1
    }

    inner class Snake : GameObject {
        override val body = ArrayList<Point>()

        //        TODO("self killing in right case")
        var direction = /*randomDirection()*/ Direction.LEFT

        fun move() {
            if (!isFalling) {
                board[body[body.size - 1].y][body[body.size - 1].x] = EMPTY_TAG

                for (i in body.size - 1 downTo 1) {
                    body[i].x = body[i - 1].x
                    body[i].y = body[i - 1].y
                    board[body[i].y][body[i].x] = board[body[i - 1].y][body[i - 1].x]
                    board[body[i].y][body[i].x] = SNAKE_BODY_TAG
                }

                when (direction) {
                    Direction.UP -> if (body[0].y > 0) body[0].y-- else body[0].y = HEIGHT / POINT_SIZE_BLOCK - 1
                    Direction.RIGHT -> if (body[0].x < WIDTH / POINT_SIZE_BLOCK - 1) body[0].x++ else body[0].x = 0
                    Direction.DOWN -> if (body[0].y < HEIGHT / POINT_SIZE_BLOCK - 1) body[0].y++ else body[0].y = 0
                    Direction.LEFT -> if (body[0].x > 0) body[0].x-- else body[0].x = WIDTH / POINT_SIZE_BLOCK - 1
                }
                board[body[0].y][body[0].x] = SNAKE_HEAD_TAG
            } else {
                for (i in body.size - 1 downTo 0) {
                    board[body[i].y][body[i].x] = EMPTY_TAG
                    body[i].y++
                    board[body[i].y][body[i].x] = if (i == 0) SNAKE_HEAD_TAG else SNAKE_BODY_TAG

                }
//                val max = body.maxBy { it.y }!!
//                if (checkCollisionAtObstacles() || (max.y > HEIGHT - POINT_SIZE_BLOCK)) createObstacleFromSnake()
            }

        }


    }

    inner class Food : GameObject {
        override val body = ArrayList<Point>()

        init {
            val food = Point()
//            label@ while (true) {
            food.x = getRandomX()
            food.y = getRandomY()
//                println("trying to create new food: ${food.location}")
//                snakeLoop@ for (i in 0 until snake.body.size) {
//                    if (food.location == snake.body[i].location) {
//                        println("retrying recreate food :${food.location} snake: ${snake.body[i].location}")
//                        if (i == snake.body.size - 1) continue@label
//                        else continue
//                    } else break@label
//                }
//
//                obstaclesLoop@ for (i in 0 until obstacles.size) {
//                    for (j in 0 until obstacles[i].body.size) {
//                        if (food.location == obstacles[i].body[j].location) {
//                            println("retrying recreate food: ${food.location} snake: ${obstacles[i].body[j].location}")
//                            continue@label
//                        } else break@label
//                    }
//                }
//            }
            body.add(food)
            board[body[0].y][body[0].x] = 'F'
        }
    }

    private inner class ObstacleCreator : Thread() {
        override fun run() {
            while (isAlive && !isPause) {
                Thread.sleep(OBSTACLE_CREATOR_DELAY_TIME)
                obstacles.add(Obstacle(WALL_SIZE))
            }
        }
    }

    inner class Obstacle(size: Int = WALL_SIZE) : GameObject {
        override val body = ArrayList<Point>(size)

        init {
            val obstacle = Point()
//            label@ while (true) {
            obstacle.x = getRandomX(3)
            obstacle.y = getRandomY(3)
//                println("trying to create new obstacle at :${obstacle.location}")
//                snakeLoop@ for (i in 0 until snake.body.size) {
//                    if (obstacle.location == snake.body[i].location) {
//                        println("retrying recreate obstacle: ${obstacle.location} snake: ${snake.body[i].location}")
//                        continue@label
//                    } else break@snakeLoop
//                }
//                obstaclesLoop@ for (i in 0 until body.size) {
//                    if (obstacle.location == body[i].location) {
//                        println("retrying recreate obstacle: ${obstacle.location} obstacle ${body[i].location}")
//                        continue@label
//                    } else break@label
//                }
//            }

            if (Random().nextBoolean()) for (i in 0 until size) body.add(Point(obstacle.x++, y))
            else for (i in 0 until size) body.add(Point(x, obstacle.y++))
            for (i in 0 until body.size) board[body[i].y][body[i].x] = 'O'
        }
    }

    fun printBoard() {
        for (y in 0 until board.size) {
            for (x in 0 until board[y].size) {
                print(board[y][x])
            }
            println()
        }
        timer.stop()
    }
}