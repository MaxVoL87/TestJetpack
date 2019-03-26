package com.example.testjetpack.utils

import androidx.collection.ArrayMap

/**
 *  An LRU cache, optimized for cashing recycler or list items, based on `ArrayMap`. (sequential access)
 *
 *  Can be used for keys represented by sequential numbers
 */
class LRUIndexedCache<V>(
    private val cacheSize: Int
) {
    private val map: ArrayMap<Int, V>

    /**
     * Returns a `Collection` that contains a copy of all cache
     * entries.
     *
     * @return a `Collection` with a copy of the cache content.
     */
    val all: Collection<Pair<Int, V>>
        @Synchronized get() = this.map.toList()

    init {
        val capacity = cacheSize + 1
        this.map = ArrayMap(capacity)
    }

    /**
     * Retrieves an entry from the cache.<br></br>
     * The retrieved entry becomes the MRU (most recently used) entry.
     *
     * @param key
     * the key whose associated value is to be returned.
     * @return the value associated to this key, or null if no value with this
     * key exists in the cache.
     */
    @Synchronized
    operator fun get(key: Int): V? {
        return this.map[key]
    }

    /**
     * Adds an entry to this cache.
     * The new entry becomes the MRU (most recently used) entry.
     * If an entry with the specified key already exists in the cache, it is
     * replaced by the new entry.
     * If the cache is full, the LRU (least recently used) entry is removed from
     * the cache.
     *
     * @param key
     * the key with which the specified value is to be associated.
     * @param value
     * a value to be associated with the specified key.
     */
    @Synchronized
    fun put(key: Int, value: V) {
        removeItem(key)
        this.map[key] = value
    }

    /**
     * Clears the cache.
     */
    @Synchronized
    fun clear() {
        this.map.clear()
    }

    /**
     * Returns the number of used entries in the cache.
     *
     * @return the number of entries currently in the cache.
     */
    @Synchronized
    fun usedEntries(): Int {
        return this.map.size
    }

    var curMinKey: Int? = null
    var curMaxKey: Int? = null

    @Synchronized
    private fun removeItem(key: Int) {


        if(curMinKey == null && curMaxKey == null){
            curMinKey = key
            curMaxKey = key
            return
        }

        val isNeedToRemove = map.size == cacheSize
        withNotNull(curMinKey)
        {
            if (key < this && curMaxKey != null) {
                if (isNeedToRemove) map.remove(curMaxKey)

                if (map.size < 0) curMaxKey = null
                else curMaxKey = map.keyAt(map.size - 1)

                curMinKey = key
            }
        }

        withNotNull(curMaxKey)
        {
            if (key > this && curMinKey != null) {
                if (isNeedToRemove) map.remove(curMinKey)

                if (map.size < 0) curMinKey = null
                else curMinKey = map.keyAt(0)

                curMaxKey = key
            }
        }
    }
}
