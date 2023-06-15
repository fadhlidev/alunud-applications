package com.alunud.application.zakatfitrah.service.impl

import com.alunud.application.zakatfitrah.dto.CreateZakatEditionDto
import com.alunud.application.zakatfitrah.dto.UpdateZakatEditionDto
import com.alunud.application.zakatfitrah.entity.ZakatEdition
import com.alunud.application.zakatfitrah.repository.ZakatEditionRepository
import com.alunud.application.zakatfitrah.response.ZakatEditionResponse
import com.alunud.application.zakatfitrah.response.response
import com.alunud.application.zakatfitrah.service.ZakatEditionService
import com.alunud.exception.NotFoundException
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

    @Transactional
    override fun update(year: Int, dto: UpdateZakatEditionDto): ZakatEditionResponse {
        val zakat = zakatEditionRepository.findByYear(year)
            ?: throw NotFoundException("Zakat fitrah $year edition not found")

        zakat.apply {
            zakat.startDate = dto.startDate ?: zakat.startDate
            zakat.endDate = dto.endDate ?: zakat.endDate
        }

        zakatEditionRepository.save(zakat)
        return zakat.response()
    }

    @Transactional
    override fun delete(year: Int) {
        val zakat = zakatEditionRepository.findByYear(year)
            ?: throw NotFoundException("Zakat fitrah $year edition not found")

        zakatEditionRepository.delete(zakat)
    }

}