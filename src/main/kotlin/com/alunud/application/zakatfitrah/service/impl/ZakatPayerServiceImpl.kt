package com.alunud.application.zakatfitrah.service.impl

import com.alunud.application.zakatfitrah.dto.CreateZakatPayerDto
import com.alunud.application.zakatfitrah.entity.ZakatPayer
import com.alunud.application.zakatfitrah.repository.ZakatEditionRepository
import com.alunud.application.zakatfitrah.repository.ZakatPayerRepository
import com.alunud.application.zakatfitrah.response.ZakatPayerResponse
import com.alunud.application.zakatfitrah.response.response
import com.alunud.application.zakatfitrah.service.ZakatPayerService
import com.alunud.exception.NotFoundException
import com.alunud.util.Validators
import jakarta.transaction.Transactional
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
@Slf4j
class ZakatPayerServiceImpl(
    @Autowired private val zakatEditionRepository: ZakatEditionRepository,
    @Autowired private val zakatPayerRepository: ZakatPayerRepository,
    @Autowired private val validators: Validators
) :
    ZakatPayerService {

    @Transactional
    override fun create(year: Int, dto: CreateZakatPayerDto): ZakatPayerResponse {
        validators.validate(dto)

        val zakat = zakatEditionRepository.findByYear(year)
            ?: throw NotFoundException("Zakat fitrah $year edition not found")

        val expectedTotalAmount = zakat.amountPerPerson * dto.totalPeople

        val payer = ZakatPayer(
            id = UUID.randomUUID(),
            submittedTime = System.currentTimeMillis(),
            name = dto.name,
            address = dto.address,
            totalPeople = dto.totalPeople,
            totalAmount = dto.totalAmount,
            excessAmountReturned = dto.excessAmountReturned,
            excessAmount = dto.totalAmount - expectedTotalAmount,
            lessAmount = expectedTotalAmount - dto.totalAmount,
            zakatEdition = zakat
        )

        zakatPayerRepository.save(payer)
        return payer.response()
    }

}