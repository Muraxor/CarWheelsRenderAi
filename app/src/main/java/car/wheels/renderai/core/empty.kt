package car.wheels.renderai.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.EnumSet
import java.util.LinkedList
import java.util.PriorityQueue
import java.util.SortedSet
import java.util.Stack
import java.util.TreeMap
import kotlin.math.absoluteValue
import kotlin.time.Duration.Companion.seconds


fun main() {

    //println(shuffle(intArrayOf(1,3,5,2,4,6), 3))
    //println(findMaxConsecutiveOnes(intArrayOf(1,0,1,1,0,1)))
    //println(findErrorNums2(intArrayOf(1,1)).toList())
    //println(evalRPN(arrayOf("4","13","5","/","+")))
    //println(exclusiveTime(2, listOf("0:start:0","0:start:2","0:end:5","1:start:6","1:end:6","0:end:7")).toList())
    //println(finalPrices(intArrayOf(10,1,1,6)).toList())
    //println(sumSubarrayMins(intArrayOf(11,81,94,43,3,10,15,10000,2313,312312,312,312,312,312543,534,123,123,534)))
    //println(sumSubarray(intArrayOf(11,81,94,43,3,10,15,10000,2313,312312,312,312,312,312543,534,123,123,534)))
    //println(removeDuplicates(intArrayOf(0,0,1,1,1,2,2,3,3,4)))
    //println(threeSumClosest(intArrayOf(2,3,8,9,10), 16))
//    println(threeSum(intArrayOf(
//        -100,-70,-60,110,120,130,160
//    )))
    //println(letterCombinations("234"))
//    println(
//        merge(
//            arrayOf(
//                intArrayOf(4, 7),
//                intArrayOf(1, 4),
//                intArrayOf(15, 18)
//            )
//        ).map { it.toList() }
//    )
    //println(maxProfit(intArrayOf(7,1,5,3,6,4)))
    //println(test1(intArrayOf(3)).toList())
    //println(test3(intArrayOf(1, 3, 2, 4, 10, 8, 4, 2, 5, 3), 4))
    println(findKthLargest(intArrayOf(2,3,123,11,20,33,45,20), 3))
}

fun isAnagram(s: String, t: String): Boolean {
    if(s == t) return true

    val map = hashMapOf<Char, Int>()

    for(i in s.indices) {
        map[s[i]] = map.getOrDefault(s[i], 0) + 1
    }

    for(i in t.indices) {
        val c = t[i]

        if(map.contains(c)) {
            map[c] = map[c]!! - 1
        } else {
            return false
        }
    }

    return map.values.find  {it != 0 } != null
}

fun nextGreaterElement(nums1: IntArray, nums2: IntArray): IntArray {
    if (nums1.isEmpty()) return nums1

    val stack = ArrayDeque<Int>()
    stack.clear()
    val nextGreater = mutableMapOf<Int, Int>() // значение -> следующее большее
    val result = IntArray(nums2.size) { -1 }

    for (i in nums2.indices) {
        while (stack.isNotEmpty() && nums2[stack.last()] < nums2[i]) {
            val index = stack.removeLast()
            nextGreater[nums2[index]] = nums2[i]
        }

        stack.addLast(i)
    }


    println(nextGreater.toList())

    return result
}

class TreeNode(var `val`: Int) {
         var left: TreeNode? = null
         var right: TreeNode? = null
    }


inline fun test3(crossinline lambda: () -> Unit ) {
    lambda()
}


fun hasPathSum(root: TreeNode?, targetSum: Int): Boolean {
    if(root == null) return false

//    test3 {
//        return false
//    }

    val stack = Stack<TreeNode>()
    var res = false
    var sum = 0

    fun dfs(node: TreeNode) {
        if(res) return
        if(node?.left == null && node?.right == null) {
            if(sum == targetSum) {
                res = true
                return
            }
            sum -= node.`val`
        }

        sum+=node.`val`

        dfs(stack.pop())
    }

    dfs(root)

    return res
}

class Solution {
    fun addTwoNumbers(l1: ListNode?, l2: ListNode?): ListNode? {
        val dummyHead = ListNode(0)
        val head = dummyHead
        var carry = 0

        var curr1 = l1
        var curr2 = l2

        while(curr1 != null && curr2 != null) {
            val first = curr1?.`val` ?: 0
            val second = curr2?.`val` ?: 0

            val (carrySum, value) = sum(first, second, carry)
            carry = carrySum
            val node = ListNode(value)
            head.next = node

            curr1 = curr1?.next
            curr2 = curr2?.next

            StringBuilder().insert(0, '1')
        }

        if(curr1 == null) {
            while(curr2 != null) {
                val second = curr2.`val`
                val (carrySum, value) = sum(0, second, carry)
                carry = carrySum
                val node = ListNode(value)
                head.next = node

                curr2 = curr2?.next
            }
        } else {
            while(curr1 != null) {
                val first = curr1.`val`
                val (carrySum, value) = sum(first, 0, carry)
                carry = carrySum
                val node = ListNode(value)
                head.next = node

                curr1 = curr1?.next
            }
        }

        return head.next
    }

    // carry to value
    fun sum(a: Int, b: Int, carry: Int): Pair<Int,Int> {
        val sum = a + b + carry
        return sum / 10 to sum % 10
    }
}

fun test3(nums: IntArray, count: Int): Int {
    val sum = nums.sum()
    val result1 = IntArray(nums.size) { -1 }
    println(sum)
    val s = (sum % 10)
    val result = (sum / count) + if (s < count) 1 else 0
    return result
}

// 1 3 2 4 10 8 4 2 5 3
// бидон 12
fun test2(nums: IntArray): Int {
    var count = 1
    var current = 12
    for (num in nums) {
        if (current < num) {
            count++
            current = 12 - num
        } else {
            current -= num
        }
    }

    return count
}

//0 1 0 2 0 3
//1 0 0 2
//1
fun test1(nums: IntArray): IntArray {
    if (nums.size < 2) return nums

    var left = 0
    var right = 1

    while (right < nums.size) {
        if (nums[left] == 0) {
            if (nums[right] == 0) {
                right++
                continue
            }

            val temp = nums[right]
            nums[right] = nums[left]
            nums[left] = temp
        } else {

        }

        left++
        right++
    }

    return nums
}

fun lengthOfLastWord(s: String): Int {
    var counter = 0

    for (i in s.length - 1 downTo 0) {
        if (s[i] != ' ') {
            counter++
        } else {
            if (counter != 0) return counter
        }
    }

    //if(counter > max) max = counter

    return counter
}

fun maxProfit(prices: IntArray): Int {
    var maxProfit = 0
    // for(i in prices.indices) {
    //     val buy = prices[i]
    //     for(j in i+1..prices.lastIndex) {
    //         val sell = prices[j]
    //         if(sell > buy) maxProfit = maxOf(maxProfit, sell - buy)
    //     }
    // }

    val sorted = prices
        .asSequence()
        .filter {
            println("filter")
            true
        }
        .mapIndexed { index, i ->
            println("map")
            Wrapper(i, index)
        }
        .sortedWith(
            compareBy<Wrapper> { it.value }
                .thenBy { it.oldPosition }
        )
        .toList()

    println(sorted)

    var left = 0
    var right = sorted.lastIndex

    while (left < right) {
        val buy = sorted[left]
        val sell = sorted[right]
        println("buy $buy")
        println("sell $sell")
        val dif = sell.oldPosition - buy.oldPosition
        when {
            dif > 0 -> {
                println("YES")
                maxProfit = sell.value - buy.value
                break
            }

            sell.oldPosition < buy.oldPosition -> left++
            else -> right--
        }
    }

    return maxProfit
}

data class Wrapper(
    val value: Int,
    val oldPosition: Int
)

fun search2(nums: IntArray, target: Int): Int {
    println("${nums.toList()}, target = $target")
    var left = 0
    var right = nums.size - 1

    while (left <= right) {
        val mid = left + (right - left) / 2
        println("midIndex = $mid number = ${nums[mid]}")
        if (nums[mid] == target) return mid
        println("left = $left right = $right")
        if (nums[mid] >= nums[left]) {
            println("L")
            if (target in nums[left]..nums[mid]) {
                right = mid - 1
            } else {
                left = mid + 1
            }
        } else {
            println("R")
            if (target in nums[mid + 1]..nums[right]) {
                left = mid + 1
            } else {
                right = mid - 1
            }
        }
    }

    return -1
}

fun search(nums: IntArray, target: Int): Int {
    var left = 0
    var right = nums.lastIndex

    while (left <= right) {
        val mid = left + (right - left) / 2

        if (nums[mid] == target) return mid

        // Определяем, какая половина отсортирована
        if (nums[left] <= nums[mid]) {
            // Левая половина отсортирована
            if (target in nums[left]..<nums[mid]) {
                right = mid - 1
            } else {
                left = mid + 1
            }
        } else {
            // Правая половина отсортирована
            if (target in nums[mid] + 1..nums[right]) {
                left = mid + 1
            } else {
                right = mid - 1
            }
        }
    }

    return -1
}

fun insert(intervals: Array<IntArray>, newInterval: IntArray): Array<IntArray> {
    if (intervals.isEmpty()) return emptyArray<IntArray>()

    val sorted = (intervals.plus(arrayOf(newInterval)))
    sorted.sortBy { it.first() }

    val result = mutableListOf<IntArray>()
    var current = sorted[0]

    for (i in 1..<intervals.size) {
        val next = intervals[i]
        if (newInterval[0] <= current[1]) {
            val start = current[0]
            val end = maxOf(newInterval[1], current[1])
            current = intArrayOf(start, end)

            if (current.get(1) >= next.get(0)) {
                current[1] = maxOf(current[1], next[1])
            } else {
                result.add(current)
                current = next
            }
        } else {
            result.add(current)
            current = next
        }
    }

    result.add(current)

    return result.toTypedArray()
}

fun mergeSecondTime(intervals: Array<IntArray>): Array<IntArray> {
    if (intervals.isEmpty()) return emptyArray()

    val sortedIntervals = intervals.sortedBy { it[0] }

    val result = ArrayList<IntArray>()
    var current = sortedIntervals[0]

    for (i in 0..sortedIntervals.size - 2) {
        val next = sortedIntervals[i + 1]

        if (current[1] >= next[0]) {
            val end = maxOf(current[1], next[1])
            val start = minOf(current[0], next[0])
            current = intArrayOf(start, end)
        } else {
            result.add(current)
            current = next
        }
    }

    result.add(current)

    return result.toTypedArray()
}

fun merge(intervals: Array<IntArray>): Array<IntArray> {
    val result = mutableListOf<IntArray>()
    val sortedIntervals = intervals.sortedBy { it.first() }
    result.add(sortedIntervals.first())

    for (i in 1..sortedIntervals.lastIndex) {
        if (result.last()[1] >= sortedIntervals[i][0]) {
            val last = result.removeAt(result.size - 1)
            result.add(intArrayOf(last.first(), maxOf(sortedIntervals[i][1], last[1])))
        } else {
            result.add(sortedIntervals[i])
        }
    }

    return result.toTypedArray()
}

fun kthSmallest(matrix: Array<IntArray>, k: Int): Int {
    val minHeap = PriorityQueue<Int>()
    for (i in matrix.indices) {
        val array = matrix[i]
        for (j in array.indices) {
            minHeap.add(array[j])
            println(minHeap)
            if (minHeap.size > k) {
                minHeap.poll()
            }
        }

    }
    var i = k - 1
    while (i != 1) {
        minHeap.poll()
        i--
    }

    return minHeap.poll() ?: -1
}

fun kClosest(points: Array<IntArray>, k: Int): Array<IntArray> {
    val comparator = Comparator<Pair<Int, Int>> { p0, p1 ->
        p0.first.compareTo(p1.second) * (-1)
    }
    val minHeap = PriorityQueue<Pair<Int, Int>>(comparator)

    for (i in points.indices) {
        minHeap.add(calculate(points[i]) to i)
        println(minHeap.toList())
        if (minHeap.size > k) {
            minHeap.poll()
        }

    }

    return minHeap.map { points[it.second] }.toTypedArray()
}

fun calculate(a: IntArray): Int {
    return (a[0] * a[0]) + (a[1] * a[1])
}

fun <T >ArrayDeque<in T>.pushNullable(value: T?) = if(value != null) this.addLast(value) else Unit

fun largestRectangleArea(heights: IntArray): Int {
    val stack = ArrayDeque<Int>()  // хранит индексы, стек возрастающий
    var maxArea = 0

    val set = hashMapOf<Char, Int>()
    val c = 'c'


    for (i in heights.indices) {
        println("i = $i stack = ${stack.map { heights[it] }}")
        // Пока стек не пуст И текущая высота меньше высоты на вершине стека
        while (stack.isNotEmpty() && heights[i] < heights[stack.last()]) {
            val height = heights[stack.removeLast()]  // высота прямоугольника
            val left = if (stack.isEmpty()) -1 else stack.last()  // левая граница
            val width = i - left - 1
            println("height $height; left ${heights[left]}; width $width")
            maxArea = maxOf(maxArea, height * width)
        }
        stack.addLast(i)
    }

    // Обрабатываем оставшиеся в стеке элементы
    while (stack.isNotEmpty()) {
        val height = heights[stack.removeLast()]
        val left = if (stack.isEmpty()) -1 else stack.last()
        val width = heights.size - left - 1
        maxArea = maxOf(maxArea, height * width)
    }

    return maxArea
}

fun topKFrequent(nums: IntArray, k: Int): IntArray {
    val hashMap = TreeMap<Int, Int>()
    for (num in nums) {
        hashMap.add(num)
    }

    return hashMap
        .entries
        .sortedBy { it.value }
        .map { it.key }
        .takeLast(k)
        .toIntArray()
}

fun TreeMap<Int, Int>.add(key: Int) {
    if (this.contains(key)) {
        this[key] = this[key]!! + 1
    } else {
        this[key] = 1
    }
}

fun findKthLargest(nums: IntArray, k: Int): List<Int> {
    val minHead = PriorityQueue<Int>(k + 1)

    for (num in nums) {
        minHead.add(num)
        if (minHead.size > k) {
            minHead.poll()
        }
    }

    return minHead.toList()
}

fun removeDuplicateLetters(s: String): String {
    val result = StringBuilder()
    val stack = _root_ide_package_.kotlin.collections.LinkedHashSet<Char>()

    for (char in s) {
        if (stack.contains(char) && stack.last() < char) {
            stack.remove(char)
        }
        stack.add(char)
    }

    for (i in stack) {
        result.append(i)
    }

    return result.toString()
}

class ListNode(var `val`: Int) {
    var next: ListNode? = null
}

fun rotateRight(head: ListNode?, k: Int): ListNode? {
    var newHead: ListNode? = head
    repeat(k) {
        newHead = doIt(newHead)
    }
    //newHead?.next = null

    return newHead
}

fun doIt(head: ListNode?): ListNode? {
    val k = 1
    val dummy = ListNode(0)
    var fast = head?.next
    var slow: ListNode? = dummy
    var amount = 0

    while (fast != null) {
        val next = fast.next
        fast = next
        slow = slow?.next
    }

    val newHead = slow?.next
    // var curr = newHead

    // while(curr != null) {
    //     val next = curr.next
    //     curr = next
    // }

    newHead?.next = head

    return newHead
}

fun getDecimalValue(head: ListNode?): Int {
    val queue = LinkedList<Int>()

    var curr = head

    while (curr != null) {
        val next = curr.next
        queue.add(curr.`val`)
        curr = next
    }

    var sum = 0

    for (i in queue.size downTo 0) {
        val value = queue.poll()
        value.shl(i)
        //sum = sum +
    }

    return sum
}

fun sum(node: ListNode?): BigDecimal {
    val firstNumber = BigDecimal.ZERO
    var curr = node
    var counter = BigDecimal.ONE

    while (curr != null) {
        firstNumber.add(BigDecimal(curr.`val`).multiply(counter))
        println(firstNumber)
        curr = curr.next
        counter = counter.multiply(BigDecimal.TEN)
    }
    return firstNumber
}

fun mergeKLists(lists: Array<ListNode?>): ListNode? {
    if (lists.isEmpty()) return null

    if (lists.size == 1) return lists.first()

    val comparator = Comparator<ListNode> { p0, p1 ->
        p0.`val`.compareTo(p1.`val`)
    }
    val q = PriorityQueue(comparator)
    lists.forEach {
        q.add(it)
    }
    q.last()

    return q.first()
}

fun letterCombinations(digits: String): List<String> {
    val phoneMap = mapOf(
        '2' to "abc",
        '3' to "def",
        '4' to "ghi",
        '5' to "jkl",
        '6' to "mno",
        '7' to "pqrs",
        '8' to "tuv",
        '9' to "wxyz"
    )

    if (digits.isEmpty()) return emptyList()

    var result = listOf("")

    for (digit in digits) {
        val letters = phoneMap[digit] ?: continue
        val newResult = mutableListOf<String>()

        for (prefix in result) {
            println("prefix $prefix")
            for (letter in letters) {
                println("letter $letter")
                newResult.add(prefix + letter)
            }
        }
        result = newResult
    }

    return result
}

typealias IndexToValue = Pair<Int, Int>
typealias IndexPair = LinkedHashMap<IndexToValue, IndexToValue>

fun threeSumClosest(nums: IntArray, target: Int): Int {
    val sorted = nums.sorted()
    var result = 10_000

    for (i in sorted.indices) {

        if (i > 0 && sorted[i] == sorted[i - 1]) continue

        var left = i + 1
        var right = sorted.lastIndex

        while (left < right) {
            val sum = sorted[i] + sorted[left] + sorted[right]
            println("sum $sum")

            when {
                sum < target -> left++
                sum > target -> right--
                else -> {
                    return sum
                }
            }

            result = if (result == 10_000) sum else {
                closest(result, sum, target)
            }

            println("result $result")
        }
    }

    return result
}

fun closest(current: Int, new: Int, target: Int): Int {
    print("current $current new $new ")
    val currentDif = (target - current).absoluteValue
    val newDif = (target - new).absoluteValue
    println("currentDif $currentDif")
    println("newDif $newDif")
    return if (newDif < currentDif) new else current
}

fun threeSum(nums: IntArray): List<List<Int>> {
    val result = mutableListOf<List<Int>>()
    val sorted = nums.sorted()

    for (i in sorted.indices) {
        // Пропускаем дубликаты для первого элемента
        if (i > 0 && sorted[i] == sorted[i - 1]) continue
        var left = i + 1
        var right = sorted.lastIndex

        while (left < right) {
            val sum = sorted[i] + sorted[left] + sorted[right]

            when {
                sum < 0 -> left++
                sum > 0 -> right--
                else -> {
                    result.add(listOf(sorted[i], sorted[left], sorted[right]))

                    // Пропускаем дубликаты для left и right
                    while (left < right && sorted[left] == sorted[left + 1]) left++
                    while (left < right && sorted[right] == sorted[right - 1]) right--

                    left++
                    right--
                }
            }
        }
    }

    return result
}

fun removeDuplicates(nums: IntArray): Int {
    if (nums.isEmpty()) return 0

    var firstIndex = 1
    var previous = nums[0]
    for (i in 1..<nums.size) {
        if (previous == nums[i]) {
            continue
        } else {
            nums[firstIndex++] = nums[i]
            previous = nums[i]
        }
    }

    println(nums.toList())

    val expectedNums = nums.take(firstIndex)

    println(expectedNums.toList())

    return expectedNums.size
}

fun longestCommonPrefix(strs: Array<String>): String {
    if (strs.isEmpty()) return ""

    val first = strs[0]
    var best = first

    for (i in 1..<strs.size) {
        val word = strs[i]
        var j = 0
        val str = StringBuffer("")
        val size = minOf(word.length, first.length)
        while (j < size && word[j] == first[j]) {
            str.append(word[j])
            j++
        }
        best = minOf(best, str.toString())
    }

    return best
}


fun Char.toInteger() = when (this) {
    'I' -> 1
    'V' -> 5
    'X' -> 10
    'L' -> 50
    'C' -> 100
    'D' -> 500
    'M' -> 1000
    else -> 0
}

fun doIt() {

}

fun test(arr: IntArray): Int {
    val MOD = 1_000_000_007

    var resultSum = 0
    val stack = ArrayDeque<Int>()
    val left = IntArray(arr.size)
    val right = IntArray(arr.size)

    for (i in 0..<arr.size) {
        var counter = 1
        while (stack.isNotEmpty() && arr[stack.last()] > arr[i]) {
            counter += left[stack.removeLast()]
        }
        left[i] = counter
        stack.add(i)
    }

    stack.clear()

    for (i in arr.size - 1 downTo 0) {
        var counter = 1
        while (stack.isNotEmpty() && arr[stack.last()] > arr[i]) {
            counter += right[stack.removeLast()]
        }
        right[i] = counter
        stack.add(i)
    }

    for (i in 0..<arr.size) {
        resultSum = (resultSum + arr[i] * left[i] * right[i]) % MOD
    }

    return resultSum
}

/**
 * Мое решеине По времени не прошло
 */

fun sumSubarrayMins(arr: IntArray): Int {
    val MOD = 1_000_000_007
    var sum = 0

    for (i in 0..<arr.size) {
        println("i $i")
        var min = 3_0000
        var j = 0
        var counter = 1
        println("sum $sum")
        if (i == 0) {
            sum = arr.sum() % MOD
        } else {
            while (j < arr.size) {
                //println("minOf($min, ${arr[j]}) = ${minOf(min, arr[j])}")
                min = minOf(min, arr[j])
                if ((i + 1) == counter) {
                    println("+ $min")
                    sum = (sum + min) % MOD
                    j -= i
                    min = arr[j + 1]
                    counter = 1
                } else {
                    counter++
                }
                j++
            }
        }
    }
    return sum
}

fun sumSubarray(a: IntArray): Int {
    val MOD = 1_000_000_007
    val n = a.size
    var result = 0L

    val stack = ArrayDeque<Int>()
    val left = IntArray(n)  // количество элементов слева, которые больше текущего
    val right = IntArray(n) // количество элементов справа, которые больше текущего

    // Находим left
    for (i in 0 until n) {
        var count = 1
        while (stack.isNotEmpty() && a[stack.last()] > a[i]) {
            count += left[stack.removeLast()]
        }
        left[i] = count
        stack.addLast(i)
    }

    stack.clear()

    // Находим right
    for (i in n - 1 downTo 0) {
        var count = 1
        while (stack.isNotEmpty() && a[stack.last()] >= a[i]) {
            count += right[stack.removeLast()]
        }
        right[i] = count
        stack.addLast(i)
    }

    // Вычисляем результат
    for (i in 0 until n) {
        //println(a[i].toLong() * left[i] * right[i])
        result = (result + a[i].toLong() * left[i] * right[i]) % MOD
    }

    return result.toInt()
}

fun finalPrices(prices: IntArray): IntArray {
    val result = IntArray(prices.size)

    for (i in 0..<prices.size - 1) {
        var j = i + 1

        while (j < prices.size) {
            if (prices[i] >= prices[j]) {
                println("prices[j] ${prices[j]}")
                println("diff = ${prices[i] - prices[j]}")
                result[i] = prices[i] - prices[j]
            } else {
                result[i] = prices[i]
            }
            j++
        }
    }

    val n = result.size - 1
    result[n] = prices[n]

    return result
}

fun exclusiveTime(n: Int, logs: List<String>): IntArray {
    val startStack = Stack<Function>()
    val result = IntArray(n)
    var last = 0

    for (i in 0..<logs.size) {
        val splited = logs[i].split(":")
        val id = splited[0].toInt()
        val command = splited[1]
        val timeStamp = splited[2].toInt()
        val function = Function(id, timeStamp)

        if (command == "start") {
            startStack.push(function)
            continue
        }

        if (command == "end") {
            val startFunction = startStack.pop()
            println("startFunction $startFunction")
            println("end function $function")

            val diff = function.timeStamp - startFunction.timeStamp + 1
            println("diff $diff")
//            if(result[id] != 0) {
//
//                println("${result[id]}")
//                if(result[id] > diff)  result[id] += diff else result[id] = diff - last
//            } else {
//                result[id] = diff - last
//                last += diff
//            }
        }
    }

    return result
}

data class Function(
    val id: Int,
    val timeStamp: Int
)

fun evalRPN(tokens: Array<String>): Int {
    val stack = ArrayDeque<Int>()
    var i = 0
    while (i < tokens.size) {
        println("stack $stack")
        try {
            val number = tokens[i].toInt()
            stack.addLast(number)
        } catch (_: NumberFormatException) {
            val value = handle(stack.removeLast(), stack.removeLast(), tokens[i])
            println("value $value")
            stack.addLast(value)
        }
        i++
    }
    println("test ${13 / 5}")

    return stack.last()
}

fun handle(b: Int, a: Int, char: String): Int {
    println("handle $char")
    println("handle $a")
    println("handle $b")
    return when (char) {
        "+" -> {
            a + b
        }

        "-" -> {
            a - b
        }

        "*" -> {
            a * b
        }

        "/" -> {
            a / b
        }

        else -> 0
    }
}

fun findErrorNums2(nums: IntArray): IntArray {
    if (nums.size == 0) return intArrayOf()

    val result = IntArray(2)
    ArrayDeque<Int>().last()
    var duplicate = 0

    val unique = IntArray(nums.size)

    for (i in 0..<nums.size) {
        val index = nums[i] - 1
        if (unique[index] != 0) {
            duplicate = nums[i]
        }
        unique[index] = nums[i]
    }

    result[0] = duplicate

    for (i in 0..<unique.size) {
        if (unique[i] == 0) {
            result[1] = if (i == unique.size - 1) unique[i - 1] + 1 else unique[i + 1] - 1
            break
        }
    }
    return result
}

fun findErrorNums(nums: IntArray): IntArray {
    if (nums.size == 0) return intArrayOf()
    val result = IntArray(2)

    var previous = nums[0]
    for (i in 0..<nums.size) {
        val temp = i + 1 - nums[i]
        if (temp <= -1) {
            result[1] = nums[i] - 1
        } else result[1] = nums[i] + 1
        if (nums[i] == previous) {
            result[0] = nums[i]
            break
        }
        previous = nums[i]
    }

    return result
}

fun findMaxConsecutiveOnes(nums: IntArray): Int {
    var max = 0
    var curr = 0
    for (i in 0..<nums.size) {
        if (nums[i] == 1) {
            curr += 1
            max = curr
            println(curr)
        } else {
            curr = 0
        }
    }

    return max
}

fun getConcatenation(nums: IntArray): IntArray {
    if (nums.isEmpty()) return intArrayOf()

    val result = Array(nums.size * 2) {
        0
    }
    val n = nums.size
    for (i in 0..n step 2) {
        result[i] = nums[i]
        result[i + n] = nums[i]
    }
    return result.toIntArray()
}

fun shuffle(nums: IntArray, n: Int): IntArray {
    val result = IntArray(nums.size)
    for (i in 0..n step 2) {
        println(i)
        result[i] = nums[i]
        result[i + 1] = nums[i + n]
    }
    return result
}