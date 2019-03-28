package com.example.testjetpack.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Reader


inline fun <reified T> Gson.fromJson(json: String?): T? = this.fromJson<T>(json, object : TypeToken<T>() {}.type)

inline fun <reified T> Gson.fromJson(reader: Reader?): T? = this.fromJson<T>(reader, object : TypeToken<T>() {}.type)