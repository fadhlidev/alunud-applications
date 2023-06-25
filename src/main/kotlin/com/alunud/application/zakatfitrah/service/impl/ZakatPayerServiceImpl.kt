package com.alunud.application.zakatfitrah.service.impl

import com.alunud.annotation.aspect.Validate
import com.alunud.application.zakatfitrah.dto.CreateZakatPayerDto
import com.alunud.application.zakatfitrah.dto.UpdateZakatPayerDto
import com.alunud.application.zakatfitrah.entity.ZakatPayer
import com.alunud.application.zakatfitrah.repository.ZakatEditionRepository
import com.alunud.application.zakatfitrah.repository.ZakatPayerRepository
import com.alunud.application.zakatfitrah.response.ZakatPayerResponse
import com.alunud.application.zakatfitrah.response.ZakatPayerSimpleResponse
import com.alunud.application.zakatfitrah.response.response
import com.alunud.application.zakatfitrah.response.simpleResponse
import com.alunud.application.zakatfitrah.service.ZakatPayerService
import com.alunud.exception.NotFoundException
import com.alunud.util.Validators
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Slf4j
@Transactional
class ZakatPayerServiceImpl(
    @Autowired private val zakatEditionRepository: ZakatEditionRepository,
    @Autowired private val zakatPayerRepository: ZakatPayerRepository,
    @Autowired private val validators: Validators
) :
    ZakatPayerService {

    @Validate
    override fun create(year: Int, dto: CreateZakatPayerDto): ZakatPayerResponse {
        val zakat = zakatEditionRepository.findByYear(year)
            ?: throw NotFoundException("Zakat fitrah $year edition not found")

        val currentTime = System.currentTimeMillis()
        zakat.endDate?.let {
            validators.invalid("Cannot add data on an expired edition") {
                currentTime > it
            }
        }

        val expectedTotalAmount = zakat.amountPerPerson * dto.totalPeople

        val payer = ZakatPayer(
            id = UUID.randomUUID(),
            submittedTime = currentTime,
            name = dto.name,
            address = dto.address,
            totalPeople = dto.totalPeople,
            totalAmount = dto.totalAmount,
            excessAmountReturned = dto.excessAmountReturned,
            excessAmount = 0.0,
            lessAmount = 0.0,
            zakatEdition = zakat
        )

        payer.apply {
            if (dto.totalAmount > expectedTotalAmount) {
                this.excessAmount = dto.totalAmount - expectedTotalAmount
            }

            if (dto.totalAmount < expectedTotalAmount) {
                this.lessAmount = expectedTotalAmount - dto.totalAmount
            }
        }

        zakatPayerRepository.save(payer)
        return payer.response()
    }

    @Validate
    override fun update(year: Int, id: UUID, dto: UpdateZakatPayerDto): ZakatPayerResponse {
        val zakat = zakatEditionRepository.findByYear(year)
            ?: throw NotFoundException("Zakat fitrah $year edition not found")

        val payer = zakatPayerRepository.findByZakatEditionAndId(zakat, id)
            ?: throw NotFoundException("Zakat fitrah payer ($id) not found")

        payer.apply {
            this.name = dto.name ?: this.name
            this.address = dto.address ?: this.address
            this.excessAmountReturned = dto.excessAmountReturned ?: this.excessAmountReturned

            dto.totalPeople?.let { totalPeople ->
                this.totalPeople = totalPeople
                val expectedTotalAmount = zakat.amountPerPerson * totalPeople

                dto.totalAmount?.let { totalAmount ->
                    this.totalAmount = totalAmount

                    if (totalAmount > expectedTotalAmount) {
                        this.excessAmount = totalAmount - expectedTotalAmount
                    }

                    if (totalAmount < expectedTotalAmount) {
                        this.lessAmount = expectedTotalAmount - totalAmount
                    }
                }
            }
        }

        zakatPayerRepository.save(payer)
        return payer.response()
    }

    override fun delete(year: Int, id: UUID) {
        val zakat = zakatEditionRepository.findByYear(year)
            ?: throw NotFoundException("Zakat fitrah $year edition not found")

        val payer = zakatPayerRepository.findByZakatEditionAndId(zakat, id)
            ?: throw NotFoundException("Zakat fitrah payer ($id) not found")

        zakatPayerRepository.delete(payer)
    }

    override fun findAll(year: Int): List<ZakatPayerSimpleResponse> {
        val zakat = zakatEditionRepository.findByYear(year)
            ?: throw NotFoundException("Zakat fitrah $year edition not found")

        return zakatPayerRepository.findAllByZakatEdition(zakat, Sort.by("submittedTime")).map { it.simpleResponse() }
    }

    override fun findOne(year: Int, id: UUID): ZakatPayerResponse {
        val zakat = zakatEditionRepository.findByYear(year)
            ?: throw NotFoundException("Zakat fitrah $year edition not found")

        val payer = zakatPayerRepository.findByZakatEditionAndId(zakat, id)
            ?: throw NotFoundException("Zakat fitrah payer ($id) not found")

        return payer.response()
    }

}