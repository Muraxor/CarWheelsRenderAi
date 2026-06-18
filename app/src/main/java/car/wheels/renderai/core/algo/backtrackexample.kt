package car.wheels.renderai.core.algo

import java.util.TreeMap

fun main() {
    println(letterCombinations("23"))
}

fun singleNumber(nums: IntArray): Int {
    if(nums.size in 1..2) return nums[0]

    var result = 0

    for(num in nums) {
        result = (result.xor(num))
    }

    return result
}

fun findDuplicate(nums: IntArray): Int {
    var result = nums[0]

    val test = hashSetOf<Int>()
    //test.add

    for(i in 1..<nums.size) {
        result = result.xor(nums[i])
    }

    return result
}

fun minWindow(s: String, t: String): String {

    val enter = hashMapOf<Char,Int>()
    val subs = ArrayList<String>()
    var start = 0

    for(i in s.indices) {
        val char = s[i]
        val indexOfEnter = enter[char]

        if(indexOfEnter != null) {
            start = maxOf(start, indexOfEnter + 1)
        }

        enter[char] = i


        subs.add(s.substring(start, i + 1))
    }

    val sorted = subs.sortedBy { it.length }
    val sortedT = t.toSortedSet().joinToString()
    return sorted.find { it.toSortedSet().toString().contains(sortedT) } ?: ""
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

    val result = mutableListOf<String>()
    val stringBuilder = StringBuilder()
    for(c in digits) {
        stringBuilder.append(phoneMap[c])
    }
    val str = stringBuilder.toString()
    println(str)
    val used = BooleanArray(str.length)

    fun backtrack(start: Int, path: MutableList<Char>) {
        if (path.size == digits.length) {
            result.add(path.toString())
            return
        }

        for (i in start..str.lastIndex) {
            path.add(str[i])
            backtrack(i + 1, path)
            path.removeAt(path.size - 1)
        }
    }

    backtrack(0, mutableListOf())

    return result
}