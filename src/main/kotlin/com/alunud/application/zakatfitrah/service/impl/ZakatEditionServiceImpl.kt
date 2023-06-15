package com.alunud.application.zakatfitrah.service.impl

import com.alunud.application.zakatfitrah.dto.CreateZakatEditionDto
import com.alunud.application.zakatfitrah.entity.ZakatEdition
import com.alunud.application.zakatfitrah.repository.ZakatEditionRepository
import com.alunud.application.zakatfitrah.response.ZakatEditionResponse
import com.alunud.application.zakatfitrah.response.response
import com.alunud.application.zakatfitrah.service.ZakatEditionService
import jakarta.transaction.Transactional
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
@Slf4j
class ZakatEditionServiceImpl(@Autowired private val zakatEditionRepository: ZakatEditionRepository) :
    ZakatEditionService {

    @Transactional
    override fun create(dto: CreateZakatEditionDto): ZakatEditionResponse {
        val zakat = ZakatEdition(
            id = UUID.randomUUID(),
            year = dto.year,
            startDate = dto.startDate,
            endDate = null,
            amountPerPerson = dto.amountPerPerson
        )

        zakatEditionRepository.save(zakat)
        return zakat.response()
    }

}