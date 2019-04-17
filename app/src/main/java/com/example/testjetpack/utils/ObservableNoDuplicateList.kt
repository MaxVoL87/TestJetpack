package com.example.testjetpack.utils

import java.util.function.Predicate

class ObservableNoDuplicateList<E> : ArrayList<E>() {

    var onAdd: (E) -> Unit = {}
    var onRemove: (E) -> Unit = {}

    override fun addAll(elements: Collection<E>): Boolean {
        var allAdded = true
        elements.forEach { el ->
            if (!contains(el)) {
                if (!super.add(el)) allAdded = false
                else {
                    onAdd(el)
                }
            } else {
                allAdded = false
            }
        }
        return allAdded
    }

    override fun addAll(index: Int, elements: Collection<E>): Boolean {
        var i = index
        var allAdded = true
        elements.forEach { el ->
            if (!contains(el)) {
                super.add(i, el)
                onAdd(el)
                i++
            } else {
                allAdded = false
            }
        }
        return allAdded
    }

    override fun add(element: E): Boolean {
        return addAll(listOf(element))
    }

    override fun add(index: Int, element: E) {
        addAll(index, listOf(element))
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        var allRemoved = true
        elements.forEach { el ->
            if (contains(el) && !remove(el)) allRemoved = false
        }
        return allRemoved
    }

    override fun removeRange(fromIndex: Int, toIndex: Int) {
        for (index in fromIndex..toIndex) {
            removeAt(index)
        }
    }

    override fun removeAt(index: Int): E {
        if (index in 0..size) {
            val element = super.removeAt(index)
            onRemove(element)
        }
        throw IndexOutOfBoundsException()
    }

    override fun remove(element: E): Boolean {
        val removed = super.remove(element)
        if (removed) onRemove(element)
        return removed
    }

    override fun removeIf(filter: Predicate<in E>): Boolean {
        throw NotImplementedError()
    }

    override fun clear() {
        removeAll(toList())
    }
}