package car.wheels.renderai.core.algo

import car.wheels.renderai.core.algo.Backtrack.permute
import car.wheels.renderai.core.algo.Backtrack.permuteWithLogs


fun main() {
    //println(permuteWithLogs(intArrayOf(1,2,3)))
    println(permute(intArrayOf(1,2,3)))
}

object Backtrack {


    fun permute(nums: IntArray): List<List<Int>> {
        val result = mutableListOf<List<Int>>()
        val used = BooleanArray(nums.size)
        var count = 0

        fun backtrack(path: MutableList<Int>) {
            if (path.size == nums.size) {
                result.add(path.toList())
                return
            }


            for (i in nums.indices) {
                count++
                if (used[i]) continue

                used[i] = true
                path.add(nums[i])
                backtrack(path)

                path.removeAt(path.size - 1)
                used[i] = false
            }
        }

        backtrack(mutableListOf())
        println(count)

        return result
    }

    fun permuteWithLogs(nums: IntArray): List<List<Int>> {
        val result = mutableListOf<List<Int>>()
        val used = BooleanArray(nums.size)

        fun backtrack(path: MutableList<Int>) {
            println("backTrack $path")
            if (path.size == nums.size) {
                println("path.size == nums.size")
                result.add(path.toList())
                return
            }

            for (i in nums.indices) {
                if (used[i]) {
                    println("used[i]==true")
                    continue
                }

                used[i] = true
                path.add(nums[i])
                println("path $path")
                backtrack(path)

                path.removeAt(path.size - 1)
                println("parth remove at ${path.size - 1} and result = path $path")
                used[i] = false
            }
        }

        backtrack(mutableListOf())

        return result
    }
}
