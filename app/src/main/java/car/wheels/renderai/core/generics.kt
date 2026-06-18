package car.wheels.renderai.core

interface Animal
interface Mammal: Animal
interface Predator: Animal

class Elephant: Mammal
class Cat: Predator
class Wolf: Predator

fun restor(items: MutableList<Predator>) {
    items.add(Wolf())
    items.forEach {
        println("feed $it")
    }
}

fun test() {
    val cats = ArrayList<Cat>()
    val s = { Unit }
    //restor(cats)
}

class Node(var `val`: Int) {
    var neighbors: ArrayList<Node?> = ArrayList<Node?>()
}

fun Node.copy(visited: MutableMap<Node, Node> = mutableMapOf()): Node {
    visited[this]?.let { return it }
    val copy = Node(this.`val`)
    visited[this] = copy
    copy.neighbors = this.neighbors
        .filterNotNull()
        .map { it.copy(visited) }.toCollection(ArrayList())
    return copy
}