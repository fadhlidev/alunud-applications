package com.alunud.application.zakatfitrah.service.impl

import com.alunud.application.zakatfitrah.dto.CreateZakatEditionDto
import com.alunud.application.zakatfitrah.dto.UpdateZakatEditionDto
import com.alunud.application.zakatfitrah.entity.ZakatEdition
import com.alunud.application.zakatfitrah.repository.ZakatEditionRepository
import com.alunud.application.zakatfitrah.response.ZakatEditionDetailResponse
import com.alunud.application.zakatfitrah.response.ZakatEditionResponse
import com.alunud.application.zakatfitrah.response.detail
import com.alunud.application.zakatfitrah.response.response
import com.alunud.application.zakatfitrah.service.ZakatEditionService
import com.alunud.exception.EntityExistsException
import com.alunud.exception.NotFoundException
import com.alunud.util.Validators
import jakarta.transaction.Transactional
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*

@Service
@Slf4j
class ZakatEditionServiceImpl(
    @Autowired private val zakatEditionRepository: ZakatEditionRepository,
    @Autowired private val validators: Validators
) :
    ZakatEditionService {

    @Transactional
    override fun create(dto: CreateZakatEditionDto): ZakatEditionResponse {
        validators.validate(dto)

        val zakat = ZakatEdition(
            id = UUID.randomUUID(),
            year = dto.year,
            startDate = dto.startDate,
            endDate = null,
            amountPerPerson = dto.amountPerPerson
        )

        zakatEditionRepository.findByYear(zakat.year)?.let {
            throw EntityExistsException("")
        }

        zakatEditionRepository.save(zakat)
        return zakat.response()
    }

    @Transactional
    override fun update(year: Int, dto: UpdateZakatEditionDto): ZakatEditionResponse {
        validators.validate(dto)

        val zakat = zakatEditionRepository.findByYear(year)
            ?: throw NotFoundException("Zakat fitrah $year edition not found")

        zakat.apply {
            this.startDate = dto.startDate ?: this.startDate

            dto.endDate?.let {
                validators.invalid("End date cannot be less than or equal to start date") {
                    it <= this.startDate
                }

                this.endDate = it
            }
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

    @Transactional
    override fun findAll(): List<ZakatEditionResponse> {
        return zakatEditionRepository.findAll(Sort.by("year")).map { it.response() }
    }

    @Transactional
    override fun findOne(year: Int): ZakatEditionDetailResponse {
        val zakat = zakatEditionRepository.findByYear(year)
            ?: throw NotFoundException("Zakat fitrah $year edition not found")

        return zakat.detail()
    }

}