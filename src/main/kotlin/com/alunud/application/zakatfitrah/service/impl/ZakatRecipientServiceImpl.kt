package com.alunud.application.zakatfitrah.service.impl

import com.alunud.application.zakatfitrah.dto.CreateZakatRecipientDto
import com.alunud.application.zakatfitrah.entity.ZakatRecipient
import com.alunud.application.zakatfitrah.repository.ZakatEditionRepository
import com.alunud.application.zakatfitrah.repository.ZakatRecipientRepository
import com.alunud.application.zakatfitrah.response.ZakatRecipientResponse
import com.alunud.application.zakatfitrah.response.response
import com.alunud.application.zakatfitrah.service.ZakatRecipientService
import com.alunud.exception.NotFoundException
import com.alunud.util.Validators
import jakarta.transaction.Transactional
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
@Slf4j
class ZakatRecipientServiceImpl(
    @Autowired private val zakatRecipientRepository: ZakatRecipientRepository,
    @Autowired private val zakatEditionRepository: ZakatEditionRepository,
    @Autowired private val validators: Validators
) :
    ZakatRecipientService {

    @Transactional
    override fun create(year: Int, dto: CreateZakatRecipientDto): ZakatRecipientResponse {
        validators.validate(dto)

        val zakat = zakatEditionRepository.findByYear(year)
            ?: throw NotFoundException("Zakat fitrah $year edition not found")

        dto.givenTime?.let {
            validators.invalid("Given time cannot be earlier than the start date edition") {
                it < zakat.startDate
            }
        }

        zakat.endDate?.let {  endDate ->
            dto.givenTime?.let { givenTime ->
                validators.invalid("Given time cannot be later than the end date edition") {
                    givenTime > endDate
                }
            }
        }

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

}