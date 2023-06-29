package com.alunud.application.zakatfitrah.service.impl

import com.alunud.annotation.aspect.Validate
import com.alunud.application.zakatfitrah.dto.CreateZakatApplicantDto
import com.alunud.application.zakatfitrah.dto.UpdateZakatApplicantDto
import com.alunud.application.zakatfitrah.entity.ZakatApplicant
import com.alunud.application.zakatfitrah.repository.ZakatApplicantRepository
import com.alunud.application.zakatfitrah.repository.ZakatEditionRepository
import com.alunud.application.zakatfitrah.response.ZakatApplicantResponse
import com.alunud.application.zakatfitrah.response.response
import com.alunud.application.zakatfitrah.service.ZakatApplicantService
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
class ZakatApplicantServiceImpl(
    @Autowired private val zakatApplicantRepository: ZakatApplicantRepository,
    @Autowired private val zakatEditionRepository: ZakatEditionRepository,
    @Autowired private val validators: Validators
) :
    ZakatApplicantService {

    @Validate
    override fun create(year: Int, dto: CreateZakatApplicantDto): ZakatApplicantResponse {
        val zakat = zakatEditionRepository.findByYear(year)
            ?: throw NotFoundException("Zakat fitrah $year edition not found")

        validators.invalid("Received time cannot be earlier than the start date edition") {
            dto.receivedTime < zakat.startDate
        }

        val applicant = ZakatApplicant(
            id = UUID.randomUUID(),
            institutionName = dto.institutionName,
            institutionAddress = dto.institutionAddress,
            receivedTime = dto.receivedTime,
            givenTime = dto.givenTime,
            givenAmount = dto.givenAmount,
            zakatEdition = zakat
        )

        zakatApplicantRepository.save(applicant)
        return applicant.response()
    }

    override fun create(year: Int, block: CreateZakatApplicantDto.() -> Unit): ZakatApplicantResponse {
        val dto = CreateZakatApplicantDto().apply(block)
        validators.validate(dto)
        return create(year, dto)
    }

    @Validate
    override fun update(year: Int, id: UUID, dto: UpdateZakatApplicantDto): ZakatApplicantResponse {
        val zakat = zakatEditionRepository.findByYear(year)
            ?: throw NotFoundException("Zakat fitrah $year edition not found")

        val applicant = zakatApplicantRepository.findByZakatEditionAndId(zakat, id)
            ?: throw NotFoundException("Zakat fitrah applicant ($id) not found")

        dto.receivedTime?.let {
            validators.invalid("Received time cannot be earlier than the start date edition") {
                it < zakat.startDate
            }
        }

        applicant.apply {
            this.institutionName = dto.institutionName ?: this.institutionName
            this.institutionAddress = dto.institutionAddress ?: this.institutionAddress
            this.receivedTime = dto.receivedTime ?: this.receivedTime
            this.givenTime = dto.givenTime ?: this.givenTime
            this.givenAmount = dto.givenAmount ?: this.givenAmount
        }

        zakatApplicantRepository.save(applicant)
        return applicant.response()
    }

    override fun update(year: Int, id: UUID, block: UpdateZakatApplicantDto.() -> Unit): ZakatApplicantResponse {
        val dto = UpdateZakatApplicantDto().apply(block)
        validators.validate(dto)
        return update(year, id, dto)
    }

    override fun delete(year: Int, id: UUID) {
        val zakat = zakatEditionRepository.findByYear(year)
            ?: throw NotFoundException("Zakat fitrah $year edition not found")

        val applicant = zakatApplicantRepository.findByZakatEditionAndId(zakat, id)
            ?: throw NotFoundException("Zakat fitrah applicant ($id) not found")

        zakatApplicantRepository.delete(applicant)
    }

    override fun findAll(year: Int): List<ZakatApplicantResponse> {
        val zakat = zakatEditionRepository.findByYear(year)
            ?: throw NotFoundException("Zakat fitrah $year edition not found")

        return zakatApplicantRepository.findAllByZakatEdition(zakat, Sort.by("receivedTime")).map { it.response() }
    }

    override fun findOne(year: Int, id: UUID): ZakatApplicantResponse {
        val zakat = zakatEditionRepository.findByYear(year)
            ?: throw NotFoundException("Zakat fitrah $year edition not found")

        val applicant = zakatApplicantRepository.findByZakatEditionAndId(zakat, id)
            ?: throw NotFoundException("Zakat fitrah applicant ($id) not found")

        return applicant.response()
    }

}