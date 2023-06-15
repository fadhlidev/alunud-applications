package com.alunud.application.zakatfitrah.repository

import com.alunud.application.zakatfitrah.entity.ZakatPayer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ZakatPayerRepository : JpaRepository<ZakatPayer, UUID>