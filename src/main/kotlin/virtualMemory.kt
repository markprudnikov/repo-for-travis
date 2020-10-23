package markprudnikov.virtualMemory

import java.io.File
import java.lang.Exception
val write = File("./data/output.txt").bufferedWriter()

/*
    * typealias to make my code more readable
    * @param [Pages] - pages, that already inside our memory
    * @param [Offers] - pages, that we offer to replace in memory
 */
typealias Pages = Int
typealias Offers = Int

/*
   * typealias to union my algorithms and make code more readable
   * @param [MutableList<Int>] - memory, that we will change after applying the extension function
   * @param [(List<Int>)] - sequence of offers, that we put like an argument to our extension function
   * @returns [Pair<List<Int>,Int>] - pages that we replaced and quantity of replacements
 */
typealias Algo = MutableList<Int>.(List<Int>) -> Pair<List<Int>,Int>

/*
    * Data class to union all data for the particular algorithm
    * @param [mem: MutableList<Pages>] - actual memory
    * @param [seq: List<Offers>] - sequence of offers
 */
data class Algorithm(val mem: MutableList<Pages>, val seq: List<Offers>)

/*
    * Check everything that may be wrong with file and get the data from it
    * @param[path: String] - path to file
    * @returns [Pair<Int,MutableList<Int>>] - size of memory, sequence of offers to pages
 */
fun validateInput(path: String): Pair<Int,MutableList<Offers>>{
    try {
        if (path.isEmpty()) throw Exception("You forgot to write the path to file.")
            val file = File(path)
            val linesFromFile = file.readLines()
            if (linesFromFile.size != 2) throw Exception("Check your file. It seems to be not 2 lines there.")
            val sizeOfMem = linesFromFile[0].toInt()
            if (sizeOfMem < 0) throw Exception("Operational size can't be less than 0.")
            val seqOfOffers = linesFromFile[1].split(" ").mapNotNull{
                if (it.toIntOrNull() == null && it != " ") throw Exception("Be careful! There was a letter \"${it}\" in the sequence! " +
                        "\n(2nd line in your \"${path}\" file).")
                if (it.toInt() < 1) println("Be careful! The number of page can't be \"${it}\" (2nd line in your \"${path}\" file).")
                it.toIntOrNull()
            }.filter {it > 0}.toMutableList()
            return sizeOfMem to seqOfOffers
    } catch (e: Exception) {
        println("Error in input!")
        println(e.message.toString())
        return 0 to mutableListOf()
    }
}

/*
    * Separate function to check if memory is full and sequence of offers isn't empty
    * If not, it applies the algorithm
    * @param [memory: MutableList<Int>] - our memory after loading it for the first time
    * @param [sequence: List<Int>] - our modified sequence after loading for the first time
    * @param [algo: Algo] - algorithm that we'll apply
    * @returns [Int] - 0 means that algorithms were executed,
    * 1 means that there is nothing to apply (we need this for module tests
 */
fun applyAlgo(memory: MutableList<Int>, sequence: List<Int>, algo: Algo): Int{
    return if (!memory.contains(0) && memory.isNotEmpty() && sequence.isNotEmpty()) {
        val result = memory.algo(sequence)
        write.write("Removed el-ts: ${result.first}\nQuantity: ${result.second}\n")
        write.newLine()
        0
    }
    else {
        write.write("Nothing to apply or Error in file.\n")
        write.newLine()
        1
    }
}

/*
    * Extension function for Memory to load first [sizeOfMem] elements in it
    * MutableList<Pages> will be changed after this function
    * @param [seqOfOffers: List<Offers] - offers of pages to load
    * @return [List<Pages>] - we need this return only for unit tests
 */
fun MutableList<Pages>.loadMemory(seqOfOffers: List<Offers>): List<Pages>{
    var takenPages = 0
    while(this.contains(0) && takenPages != seqOfOffers.size){
        if(!this.contains(seqOfOffers[takenPages])) {
            this.removeAt(this.indexOfLast { it != 0 } + 1)
            this.add(this.indexOfLast { it != 0 } + 1, seqOfOffers[takenPages])
        }
        takenPages++
    }
    return this
}

/*
    * Realizing FIFO Algorithm as extension for [MutableList<Pages>] - Memory
    * @param [seqOfOffers: List<Offers>] - list with offers to replace pages
    * @returns [Pair<List<Int>,Int>] - returns list with elements we replaced and the size of it
 */
fun MutableList<Pages>.fifoAlgorithm(seqOfOffers: List<Offers>): Pair<List<Int>,Int>{
    /*
        * List for storing elements, that we removed from Memory
     */
    val fifoLog = mutableListOf<Pages>()
    /*
        * As we do not change anything in @[seqOfOffers: List<Offers>],
        * we need to know from which element we start replacing pages
        * for (x in this.size until seqOfOffers.size)
        * Cycle goes from index of the first element that we didn't process after loading Memory,
        * until the end of the sequence of Offers
     */
    for (x in this.size until seqOfOffers.size) {
        if (!this.contains(seqOfOffers[x])){
            fifoLog.add(this[0])
            this.removeAt(0)
            this.add(seqOfOffers[x])
        }
    }
    return fifoLog to fifoLog.size
}

/*
    * Realizing LRU Algorithm as extension for [MutableList<Pages>] - Memory
    * @param [seqOfOffers: List<Offers>] - list with offers to replace pages
    * @returns [Pair<List<Int>,Int>] - returns list with elements we replaced and the size of it
 */
fun MutableList<Pages>.lruAlgorithm(seqOfOffers: List<Offers>): Pair<List<Int>,Int>{
    /*
       * List for storing elements, that we removed from Memory
    */
    val lruLog = mutableListOf<Pages>()
    /*
        * As we do not change anything in @[seqOfOffers: List<Offers>],
        * we need to know from which element we start replacing pages
        * for (x in this.size until seqOfOffers.size)
        * Cycle goes from index of the first element that we didn't process after loading Memory,
        * until the end of the sequence of Offers
    */
    for (x in seqOfOffers.indexOfFirst { it == this.last() } + 1 until seqOfOffers.size) {
        if (!this.contains(seqOfOffers[x])){
            lruLog.add(this[0])
            this.removeAt(0)
            this.add(seqOfOffers[x])
        }
        else {
            this.remove(seqOfOffers[x])
            this.add(seqOfOffers[x])
        }
    }
    return lruLog to lruLog.size
}

/*
    * Realizing OPT Algorithm as extension for [MutableList<Pages>] - Memory
    * Using inside another MutableList<Pages> calls seqForOpt.
    * We use it in order to drop elements that we do not need - elements, that is already inside Memory.
    * Another [indicesOfFirstOffers: MutableList<Int>] we need to store indices of first elements in seqForOpt
    * that already inside our Memory.
    * It gives us an information about the element, that will be offered for in the end.
    * Also there is an If Else construction. If there at least one page that will never offer, in seqForOpt,
    * then we go in another way. Just change exactly this page. If there are 2 or more pages like that,
    * we change first from left.
    * @param [seqOfOffers: List<Offers>] - list with offers to replace pages
    * @returns [Pair<List<Int>,Int>] - returns list with elements we replaced and the size of it
 */
fun MutableList<Pages>.optAlgorithm(seqOfOffers: List<Offers>): Pair<List<Int>,Int>{
    /*
      * List for storing elements, that we removed from Memory
   */
    val optLog = mutableListOf<Pages>()
    val indicesOfFirstOffers = mutableListOf<Int>()
    val seqForOpt =  seqOfOffers.dropWhile { it != this.last() }.toMutableList()
    seqForOpt.removeAt(0)
    if (seqForOpt.containsAll(this)) {
       for (x in  seqForOpt.indices) {
            for (p in this) indicesOfFirstOffers.add(seqForOpt.indexOfFirst { it == p })
            if (!this.contains(seqForOpt[x])) {
                optLog.add(seqForOpt[indicesOfFirstOffers.last()])
                this.remove(seqForOpt[indicesOfFirstOffers.last()])
                this.add(seqForOpt[x])
                indicesOfFirstOffers.removeAll(indicesOfFirstOffers) // Обнуляем лист
            }
        }
    } else{
        for (x in  seqForOpt.indices) {
            val indForChange = this.indexOfFirst {!seqForOpt.contains(it)}
            if (!this.contains(seqForOpt[0])) {
                optLog.add(this[indForChange])
                this.removeAt(indForChange)
                this.add(seqForOpt[0])
                seqForOpt.removeAt(0)
            } else seqForOpt.removeAt(0)
        }
    }
    return optLog to optLog.size
}

fun executeAllProgram(path: String){
    val (sizeOfMem, seqOfOffers) = validateInput(path)

    val fifo = Algorithm(MutableList(sizeOfMem) { 0 },seqOfOffers)
    val lru = Algorithm(MutableList(sizeOfMem) { 0 },seqOfOffers)
    val opt = Algorithm(MutableList(sizeOfMem){ 0 },seqOfOffers)

    fifo.mem.loadMemory(fifo.seq)
    lru.mem.loadMemory(lru.seq)
    opt.mem.loadMemory(opt.seq)

    write.write("FIFO for \"${path}\": ")
    write.newLine()
    applyAlgo(fifo.mem, fifo.seq) { fifoAlgorithm(fifo.seq) }

    write.write("LRU for \"${path}\": ")
    write.newLine()
    applyAlgo(lru.mem, lru.seq) { lruAlgorithm(lru.seq) }

    write.write("OPT for \"${path}\": ")
    write.newLine()
    applyAlgo(opt.mem, opt.seq) {optAlgorithm(opt.seq)}

    write.close()
}
fun main() {
        print("Input the path to file: ")
        for (path in readLine().toString().split(' ')) executeAllProgram(path)

}
