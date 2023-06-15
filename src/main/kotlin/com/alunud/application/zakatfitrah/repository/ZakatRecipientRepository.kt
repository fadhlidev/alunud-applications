package com.alunud.application.zakatfitrah.repository

import com.alunud.application.zakatfitrah.entity.ZakatRecipient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ZakatRecipientRepository : JpaRepository<ZakatRecipient, UUID>