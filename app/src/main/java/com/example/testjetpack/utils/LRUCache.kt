package com.example.testjetpack.utils

import kotlin.collections.Map.Entry


/**
 * An LRU cache, based on `LinkedHashMap`.
 *
 *
 *
 * This cache has a fixed maximum number of elements (`cacheSize`).
 * If the cache is full and another entry is added, the LRU (least recently
 * used) entry is dropped.
 *
 *
 *
 * This class is thread-safe. All methods of this class are synchronized.
 *
 *
 *
 * Author: Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland<br></br>
 * Multi-licensed: EPL / LGPL / GPL / AL / BSD.
 *
 * @param cacheSize
 * the maximum number of entries that will be kept in this cache.
 */
class LRUCache<K, V>(
    private val cacheSize: Int
) {

    private val map: LinkedHashMap<K, V>

    /**
     * Returns a `Collection` that contains a copy of all cache
     * entries.
     *
     * @return a `Collection` with a copy of the cache content.
     */
    val all: Collection<Pair<K, V>>
        @Synchronized get() = this.map.toList()

    init {
        val hashTableCapacity = Math.ceil((cacheSize / LRUCache.hashTableLoadFactor).toDouble()).toInt() + 1
        this.map = object : LinkedHashMap<K, V>(hashTableCapacity, LRUCache.hashTableLoadFactor, true) {
            override fun removeEldestEntry(eldest: Entry<K, V>): Boolean {
                return this.size > this@LRUCache.cacheSize
            }
        }
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
    operator fun get(key: K): V? {
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
    fun put(key: K, value: V) {
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

    companion object {
        private const val hashTableLoadFactor = 0.75f
    }
} // end class LRUCache