package tetrosnake

import java.awt.EventQueue
import javax.swing.JFrame
import javax.swing.WindowConstants

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

object Frame : JFrame() {
    init {
        contentPane = Canvas()
        pack()
        isResizable = false
        title = "TS"
        setLocationRelativeTo(null)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    }
}

fun main() {
    EventQueue.invokeLater { Frame.isVisible = true }
}
