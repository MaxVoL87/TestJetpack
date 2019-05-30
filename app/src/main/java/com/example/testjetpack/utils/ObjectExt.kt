package com.example.testjetpack.utils

import java.lang.reflect.Modifier
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaGetter

fun Any?.toMapOfStrings(): Map<String, String>? {
    if(this == null) return null
    return this::class.declaredMemberProperties
        .filter { isFieldAccessible(it) }
        .map{ Pair(it.name, it.getter.call(this)?.toString() ?: "") }
        .toMap()
}

fun isFieldAccessible(property: KProperty1<*, *>): Boolean {
    return property.javaGetter?.modifiers?.let { !Modifier.isPrivate(it) } ?: false
}