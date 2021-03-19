package com.github.brunomartinscorrea.adapter.database

interface Repository<T, R> : ReadRepository<T, R>, WriteRepository<T, R>
