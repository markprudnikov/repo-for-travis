import markprudnikov.virtualMemory.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertAll

internal class VirtualMemoryKtTest {
    private val emptyList: MutableList<Int> = mutableListOf()

    @Test
    fun `validateInput function check`(){
        assertAll(
                { assertEquals(Pair(5,listOf(1,2,2,3,4,5,6,2,1,5)), validateInput("./data/test1.txt")) },
                { assertEquals(Pair(5,listOf(1,2,3,4,5)), validateInput("./data/test2.txt")) },
                { assertEquals(Pair(10,listOf(1,2,3)), validateInput("./data/test3.txt")) },
                { assertEquals(Pair(5,listOf(1,2,3,4,5,6)), validateInput("./data/test4.txt")) }
        )
    }

    @Test
    fun `loadMemory function check`() {
        /*
            * Test all cases for operational size > 0
            * When Sequence of offers less than size, more or equal
         */
        assertAll(
            { assertEquals(listOf(1, 2, 3, 0, 0), mutableListOf(0,0,0,0,0).loadMemory(listOf(1,2,3)))},
            { assertEquals(listOf(1,2,3,4,5),mutableListOf(0,0,0,0,0).loadMemory(listOf(1,2,3,4,5,6))) },
            { assertEquals(listOf(1,2,3,4,5),mutableListOf(0,0,0,0,0).loadMemory(listOf(1,2,3,4,5))) }
        )
        /*
            * Test for operational size == 0
         */
        assertEquals(emptyList, emptyList.loadMemory(listOf(1,2,3)))
  }

    /*
       * Tests for non-zero size of Memory
       * If size is zero - fifoAlgorithm function never calls
     */
    @Test
    fun `FIFO algorithm check`() {
        assertAll(
            { assertEquals(Pair(emptyList,0), mutableListOf(1,2,3).fifoAlgorithm(listOf(1,2,3,1,2,3))) },
            { assertEquals(Pair(listOf(1,2,3),3), mutableListOf(1,2,3).fifoAlgorithm(listOf(1,2,3,4,5,6))) },
            { assertEquals(Pair(listOf(1,2),2), mutableListOf(1,2,3).fifoAlgorithm(listOf(1,2,3,4,5,3))) },
            { assertEquals(Pair(listOf(1,2),2), mutableListOf(1,2,3).fifoAlgorithm(listOf(1,2,3,4,2,5))) },
            { assertEquals(Pair(emptyList,0), mutableListOf(1,2,3).fifoAlgorithm(listOf(1,2,3))) },
            { assertEquals(Pair(listOf(1,2,3,4),4), mutableListOf(1,2,3,4,5).fifoAlgorithm(listOf(1,2,3,4,5,6,7,8,3))) },
            { assertEquals(Pair(listOf(1,2,3,4,5),5), mutableListOf(1,2,3,4,5).fifoAlgorithm(listOf(1,2,3,4,5,5,6,7,8,9,10))) },
            { assertEquals(Pair(listOf(1,2,3,4,5,6),6), mutableListOf(1,2,3,4,5).fifoAlgorithm(listOf(1,2,3,4,5,6,3,2,1,7,8,1,2,3)))}
        )
    }

    /*
        * Tests for non-zero size of Operational Memory
        * If size is zero - lruAlgorithm function never calls
     */
    @Test
    fun `LRU algorithm check`() {

        assertAll(
            { assertEquals(Pair(emptyList,0), mutableListOf(1,2,3).lruAlgorithm(listOf(1,2,3,1,2,3))) },
            { assertEquals(Pair(listOf(1,2,3),3), mutableListOf(1,2,3).lruAlgorithm(listOf(1,2,3,4,5,6))) },
            { assertEquals(Pair(listOf(1,2),2), mutableListOf(1,2,3).lruAlgorithm(listOf(1,2,3,4,5,3))) },
            { assertEquals(Pair(listOf(1,3),2), mutableListOf(1,2,3).lruAlgorithm(listOf(1,2,3,4,2,5))) },
            { assertEquals(Pair(emptyList,0), mutableListOf(1,2,3).lruAlgorithm(listOf(1,2,3))) },
            { assertEquals(Pair(listOf(1,4,5,6),4), mutableListOf(1,2,3,4,5).lruAlgorithm(listOf(1,2,3,4,5,6,3,2,1,7,8))) },
            { assertEquals(Pair(listOf(1,2,3,4),4), mutableListOf(1,2,3,4,5).lruAlgorithm(listOf(1,2,3,4,5,6,7,8,3))) },
            { assertEquals(Pair(listOf(1,2,5),3), mutableListOf(1,2,3,4,5).lruAlgorithm(listOf(1,2,3,4,5,3,6,4,7,8))) },
            { assertEquals(Pair(listOf(1,2,3,4,5),5), mutableListOf(1,2,3,4,5).lruAlgorithm(listOf(1,2,3,4,5,5,6,7,8,9,10))) },
            { assertEquals(Pair(listOf(1,4,5,6),4), mutableListOf(1,2,3,4,5).lruAlgorithm(listOf(1,2,3,4,5,6,3,2,1,7,8,1,2,3)))}
        )
    }

    /*
        * Tests for non-zero size of Operational Memory
        * If size is zero - optAlgorithm function never calls
     */
    @Test
    fun `OPT algorithm check`() {

        assertAll(
                { assertEquals(Pair(emptyList,0), mutableListOf(1,2,3).optAlgorithm(listOf(1,2,3,1,2,3))) },
                { assertEquals(Pair(listOf(1,2,3),3), mutableListOf(1,2,3).optAlgorithm(listOf(1,2,3,4,5,6))) },
                { assertEquals(Pair(listOf(1,2),2), mutableListOf(1,2,3).optAlgorithm(listOf(1,2,3,4,5,3))) },
                { assertEquals(Pair(listOf(1,2),2), mutableListOf(1,2,3).optAlgorithm(listOf(1,2,3,4,2,5))) },
                { assertEquals(Pair(emptyList,0), mutableListOf(1,2,3).optAlgorithm(listOf(1,2,3))) },
                { assertEquals(Pair(listOf(4,1,2),3), mutableListOf(1,2,3,4,5).optAlgorithm(listOf(1,2,3,4,5,6,3,2,1,7,8))) },
                { assertEquals(Pair(listOf(1,2,4),3), mutableListOf(1,2,3,4,5).optAlgorithm(listOf(1,2,3,4,5,6,7,8,3))) },
                { assertEquals(Pair(listOf(1,2,3),3), mutableListOf(1,2,3,4,5).optAlgorithm(listOf(1,2,3,4,5,3,6,4,7,8))) },
                { assertEquals(Pair(listOf(1,2,3,4,5),5), mutableListOf(1,2,3,4,5).optAlgorithm(listOf(1,2,3,4,5,5,6,7,8,9,10))) },
                { assertEquals(Pair(listOf(4,5,6),3), mutableListOf(1,2,3,4,5).optAlgorithm(listOf(1,2,3,4,5,6,3,2,1,7,8,1,2,3)))}
        )
    }

    @Test
    fun `apply algo function check`(){
        /*
            * We check the return value:
            * "1" means - nothing to apply (algorithms don't execute)
            * "0" means that they were successfully executed
         */
        assertEquals(1, applyAlgo(emptyList,listOf(1,2,3)){lruAlgorithm(listOf(1,2,3))})
        assertEquals(1, applyAlgo(mutableListOf(1,2,3),emptyList){lruAlgorithm(emptyList)})
        assertEquals(1, applyAlgo(emptyList,listOf(1,2,3)){fifoAlgorithm(listOf(1,2,3))})
        assertEquals(1, applyAlgo(mutableListOf(1,2,3),emptyList){fifoAlgorithm(emptyList)})
        assertEquals(1, applyAlgo(emptyList,listOf(1,2,3)){optAlgorithm(listOf(1,2,3))})
        assertEquals(1, applyAlgo(mutableListOf(1,2,3),emptyList){optAlgorithm(emptyList)})
        assertEquals(1, applyAlgo(mutableListOf(1,2,3,0,0),listOf(1,2,3)){optAlgorithm(listOf(1,2,3))})
        assertEquals(1, applyAlgo(mutableListOf(1,2,3,0,0),listOf(1,2,3)){lruAlgorithm(listOf(1,2,3))})
        assertEquals(1, applyAlgo(mutableListOf(1,2,3,0,0),listOf(1,2,3)){fifoAlgorithm(listOf(1,2,3))})
        assertEquals(0, applyAlgo(mutableListOf(1,2,3),listOf(1,2,3,4,5,6)){fifoAlgorithm(listOf(1,2,3,4,5,6))})
        assertEquals(0, applyAlgo(mutableListOf(1,2,3),listOf(1,2,3,4,5,6)){optAlgorithm(listOf(1,2,3,4,5,6))})
        assertEquals(0, applyAlgo(mutableListOf(1,2,3),listOf(1,2,3,4,5,6)){lruAlgorithm(listOf(1,2,3,4,5,6))})
    }
}