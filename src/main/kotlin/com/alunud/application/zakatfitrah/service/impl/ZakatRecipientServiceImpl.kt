package com.alunud.application.zakatfitrah.service.impl

import com.alunud.annotation.aspect.Validate
import com.alunud.application.zakatfitrah.dto.CreateZakatRecipientDto
import com.alunud.application.zakatfitrah.dto.UpdateZakatRecipientDto
import com.alunud.application.zakatfitrah.entity.ZakatEdition
import com.alunud.application.zakatfitrah.entity.ZakatRecipient
import com.alunud.application.zakatfitrah.repository.ZakatEditionRepository
import com.alunud.application.zakatfitrah.repository.ZakatRecipientRepository
import com.alunud.application.zakatfitrah.response.ZakatRecipientResponse
import com.alunud.application.zakatfitrah.response.response
import com.alunud.application.zakatfitrah.service.ZakatRecipientService
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
class ZakatRecipientServiceImpl(
    @Autowired private val zakatRecipientRepository: ZakatRecipientRepository,
    @Autowired private val zakatEditionRepository: ZakatEditionRepository,
    @Autowired private val validators: Validators
) :
    ZakatRecipientService {

    @Validate
    override fun create(year: Int, dto: CreateZakatRecipientDto): ZakatRecipientResponse {
        val zakat = zakatEditionRepository.findByYear(year)
            ?: throw NotFoundException("Zakat fitrah $year edition not found")

        validateGivenTime(dto.givenTime, zakat)

        val recipient = ZakatRecipient(
            id = UUID.randomUUID(),
            name = dto.name,
            address = dto.address,
            givenTime = dto.givenTime,
            givenAmount = dto.givenAmount,
            zakatEdition = zakat
        )

        zakatRecipientRepository.save(recipient)
        return recipient.response()
    }

    override fun create(year: Int, block: CreateZakatRecipientDto.() -> Unit): ZakatRecipientResponse {
        val dto = CreateZakatRecipientDto().apply(block)
        validators.validate(dto)
        return create(year, dto)
    }

    @Validate
    override fun update(year: Int, id: UUID, dto: UpdateZakatRecipientDto): ZakatRecipientResponse {
        val zakat = zakatEditionRepository.findByYear(year)
            ?: throw NotFoundException("Zakat fitrah $year edition not found")

        val recipient = zakatRecipientRepository.findByZakatEditionAndId(zakat, id)
            ?: throw NotFoundException("Zakat fitrah recipient ($id) not found")

        validateGivenTime(dto.givenTime, zakat)

        recipient.apply {
            this.name = dto.name ?: this.name
            this.address = dto.address ?: this.address
            this.givenTime = dto.givenTime ?: this.givenTime
            this.givenAmount = dto.givenAmount ?: this.givenAmount
        }

        zakatRecipientRepository.save(recipient)
        return recipient.response()
    }

    override fun update(year: Int, id: UUID, block: UpdateZakatRecipientDto.() -> Unit): ZakatRecipientResponse {
        val dto = UpdateZakatRecipientDto().apply(block)
        validators.validate(dto)
        return update(year, id, dto)
    }

    override fun delete(year: Int, id: UUID) {
        val zakat = zakatEditionRepository.findByYear(year)
            ?: throw NotFoundException("Zakat fitrah $year edition not found")

        val recipient = zakatRecipientRepository.findByZakatEditionAndId(zakat, id)
            ?: throw NotFoundException("Zakat fitrah recipient ($id) not found")

        zakatRecipientRepository.delete(recipient)
    }

    override fun findAll(year: Int): List<ZakatRecipientResponse> {
        val zakat = zakatEditionRepository.findByYear(year)
            ?: throw NotFoundException("Zakat fitrah $year edition not found")

        return zakatRecipientRepository.findAllByZakatEdition(zakat, Sort.by("givenTime")).map { it.response() }
    }

    override fun findOne(year: Int, id: UUID): ZakatRecipientResponse {
        val zakat = zakatEditionRepository.findByYear(year)
            ?: throw NotFoundException("Zakat fitrah $year edition not found")

        val recipient = zakatRecipientRepository.findByZakatEditionAndId(zakat, id)
            ?: throw NotFoundException("Zakat fitrah recipient ($id) not found")

        return recipient.response()
    }

    private fun validateGivenTime(givenTime: Long?, edition: ZakatEdition) {
        givenTime?.let {
            validators.invalid("Given time cannot be earlier than the start date edition") {
                it < edition.startDate
            }
        }

        edition.endDate?.let { endDate ->
            givenTime?.let { givenTime ->
                validators.invalid("Given time cannot be later than the end date edition") {
                    givenTime > endDate
                }
            }
        }
    }

}