package com.example.plaintext.data.repository

import com.example.plaintext.data.dao.PasswordDao
import com.example.plaintext.data.model.Password
import com.example.plaintext.data.model.PasswordInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface PasswordDBStore {
    fun getList(): Flow<List<Password>>
    suspend fun add(password: Password): Long
    suspend fun update(password: Password)
    fun get(id: Int): Password?
    suspend fun save(passwordInfo: PasswordInfo)
    suspend fun isEmpty(): Flow<Boolean>
}

class LocalPasswordDBStore(
    private val passwordDao : PasswordDao
): PasswordDBStore {

    override fun getList(): Flow<List<Password>> {
        return passwordDao.getPasswordList()
    }

    override suspend fun add(password: Password): Long {
        return passwordDao.insert(password)
    }

    override suspend fun update(password: Password) {
        passwordDao.update(password)
    }

    override fun get(id: Int): Password? {
        // Returns null for now if direct single item lookups aren't supported by the baseline DAO
        return null
    }

    override suspend fun save(passwordInfo: PasswordInfo) {
        // Uses the property delegate custom operator written in Password.kt to extract the Entity
        val passwordEntity by passwordInfo
        passwordDao.insert(passwordEntity)
    }

    override suspend fun isEmpty(): Flow<Boolean> {
        return passwordDao.getPasswordList().map { list -> list.isEmpty() }
    }
}