package com.example.simgplechatexample.presentation.test.dimon.wb

class WBWallet(initialBalance: Int, val id: String) {

    var balance: Int = initialBalance
    val history: MutableList<Transaction> = mutableListOf()

    fun deposit(amount: Int) {
        balance += amount
        history += Transaction(amount)
    }

    fun withdraw(amount: Int) {
        balance -= amount
        history += Transaction(-amount)
    }

    fun transfer(otherWallet: WBWallet, amount: Int) {
        synchronized(this) {
            synchronized(otherWallet) {
                balance -= amount
                history += Transaction(-amount)
                otherWallet.balance += amount
                otherWallet.history += Transaction(amount)
            }
        }

    }
}

data class Transaction(
    val amount: Int
)
