package com.alunud.application.zakatfitrah.service.impl

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
import jakarta.transaction.Transactional
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
@Slf4j
class ZakatApplicantServiceImpl(
    @Autowired private val zakatApplicantRepository: ZakatApplicantRepository,
    @Autowired private val zakatEditionRepository: ZakatEditionRepository,
    @Autowired private val validators: Validators
) :
    ZakatApplicantService {

    @Transactional
    override fun create(year: Int, dto: CreateZakatApplicantDto): ZakatApplicantResponse {
        validators.validate(dto)

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

    @Transactional
    override fun update(year: Int, id: UUID, dto: UpdateZakatApplicantDto): ZakatApplicantResponse {
        validators.validate(dto)

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

}