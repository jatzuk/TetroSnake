package snake

import java.awt.EventQueue
import javax.swing.JFrame
import javax.swing.WindowConstants

/**
 ** Created with passion and love
 **    for project Snake
 **        by Jatzuk on 16-Jan-19
 **                                            *_____*
 **                                           *_*****_*
 **                                          *_(O)_(O)_*
 **                                         **____V____**
 **                                         **_________**
 **                                         **_________**
 **                                          *_________*
 **                                           ***___***
 */

object Frame : JFrame() {
    init {
        contentPane = Canvas()
        setLocationRelativeTo(null)
        title = "Snake"
        isResizable = false
        setLocationRelativeTo(null)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        pack()
    }
}

fun main(args: Array<String>) {
    EventQueue.invokeLater { Frame.isVisible = true }
}