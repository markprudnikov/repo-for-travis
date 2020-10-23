import markprudnikov.virtualMemory.executeAllProgram
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class IntegTest {

    @Test
    fun `first file from data check`(){
        val stringsFromFile = mutableListOf<String>()
        executeAllProgram("./data/test1.txt")
        for (line in File("./data/output.txt").readLines()) stringsFromFile.add(line)
        val fifo = stringsFromFile[2].filter { it.isDigit() }
        val lru = stringsFromFile[6].filter{ it.isDigit() }
        val opt = stringsFromFile[10].filter { it.isDigit() }
        assertEquals("1",opt)
        assertEquals("2",fifo)
        assertEquals("2",lru)
    }
    @Test
    fun `second file from data check`(){
        val stringsFromFile = mutableListOf<String>()
        executeAllProgram("./data/test2.txt")
        for (line in File("./data/output.txt").readLines()) stringsFromFile.add(line)
        val fifo = stringsFromFile[2].filter { it.isDigit() }
        val lru = stringsFromFile[6].filter{ it.isDigit() }
        val opt = stringsFromFile[10].filter { it.isDigit() }
        assertEquals("0",opt)
        assertEquals("0",fifo)
        assertEquals("0",lru)
    }
    @Test
    fun `third file from data check`(){
        val stringsFromFile = mutableListOf<String>()
        executeAllProgram("./data/test3.txt")
        for (line in File("./data/output.txt").readLines()) stringsFromFile.add(line)
        val fifo = stringsFromFile[1].filter { !it.isDigit() }
        val lru = stringsFromFile[4].filter{ !it.isDigit() }
        val opt = stringsFromFile[7].filter { !it.isDigit() }
        assertEquals("Nothing to apply or Error in file.",opt)
        assertEquals("Nothing to apply or Error in file.",fifo)
        assertEquals("Nothing to apply or Error in file.",lru)
    }
    @Test
    fun `forth file from data check`(){
        val stringsFromFile = mutableListOf<String>()
        executeAllProgram("./data/test4.txt")
        for (line in File("./data/output.txt").readLines()) stringsFromFile.add(line)
        val fifo = stringsFromFile[2].filter { it.isDigit() }
        val lru = stringsFromFile[6].filter{ it.isDigit() }
        val opt = stringsFromFile[10].filter { it.isDigit() }
        assertEquals("1",opt)
        assertEquals("1",fifo)
        assertEquals("1",lru)
    }
    @Test
    fun `fifth file from data check`(){
        val stringsFromFile = mutableListOf<String>()
        executeAllProgram("./data/test5.txt")
        for (line in File("./data/output.txt").readLines()) stringsFromFile.add(line)
        val fifo = stringsFromFile[1].filter { !it.isDigit() }
        val lru = stringsFromFile[4].filter{ !it.isDigit() }
        val opt = stringsFromFile[7].filter { !it.isDigit() }
        assertEquals("Nothing to apply or Error in file.",opt)
        assertEquals("Nothing to apply or Error in file.",fifo)
        assertEquals("Nothing to apply or Error in file.",lru)
    }

}