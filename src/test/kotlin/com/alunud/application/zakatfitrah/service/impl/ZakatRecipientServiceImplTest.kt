package com.alunud.application.zakatfitrah.service.impl

import com.alunud.application.AlUnudApplication
import com.alunud.application.zakatfitrah.dto.CreateZakatRecipientDto
import com.alunud.application.zakatfitrah.entity.ZakatEdition
import com.alunud.application.zakatfitrah.repository.ZakatEditionRepository
import com.alunud.application.zakatfitrah.repository.ZakatRecipientRepository
import com.alunud.application.zakatfitrah.service.ZakatRecipientService
import com.alunud.exception.NotFoundException
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest(classes = [AlUnudApplication::class])
@ActiveProfiles("test")
class ZakatRecipientServiceImplTest(
    @Autowired val zakatRecipientService: ZakatRecipientService,
    @Autowired val zakatRecipientRepository: ZakatRecipientRepository,
    @Autowired val zakatEditionRepository: ZakatEditionRepository
) {

    lateinit var zakat: ZakatEdition

    @BeforeEach
    fun createZakatEdition() {
        val zakat = ZakatEdition(
            id = UUID.randomUUID(),
            year = 2023,
            startDate = 1681578000000,
            amountPerPerson = 2.5,
            endDate = 1681923600000
        )

        zakatEditionRepository.save(zakat)

        this.zakat = zakat
    }

    @AfterEach
    fun cleanZakatRecipientRepository() {
        zakatRecipientRepository.deleteAll()
    }

    @AfterEach
    fun cleanZakatEditionRepository() {
        zakatEditionRepository.deleteAll()
    }

    @Test
    fun `should create zakat fitrah recipient`() {
        val payload = CreateZakatRecipientDto(
            name = "Fulan",
            address = "Pojok 2/3",
            givenTime = 1681884000000,
            givenAmount = 5.0
        )

        val result = zakatRecipientService.create(zakat.year, payload)

        assertNotNull(result)

        assertEquals(payload.name, result.name)
        assertEquals(payload.address, result.address)
        assertEquals(payload.givenTime, result.givenTime)
        assertEquals(payload.givenAmount, result.givenAmount)
    }

    @Test
    fun `should not create zakat fitrah recipient because givenTime is earlier than startDate edition`() {
        assertThrows<ConstraintViolationException> {
            val payload = CreateZakatRecipientDto(
                name = "Fulan",
                address = "Pojok 2/3",
                givenTime = 1681520400000,
                givenAmount = 5.0
            )

            zakatRecipientService.create(zakat.year, payload)
        }
    }

    @Test
    fun `should not create zakat fitrah recipient because givenTime is later than endDate edition`() {
        assertThrows<ConstraintViolationException> {
            val payload = CreateZakatRecipientDto(
                name = "Fulan",
                address = "Pojok 2/3",
                givenTime = 1681952400000,
                givenAmount = 5.0
            )

            zakatRecipientService.create(zakat.year, payload)
        }
    }

    @Test
    fun `should not create zakat fitrah recipient because invalid payload`() {
        assertThrows<ConstraintViolationException> {
            val payload = CreateZakatRecipientDto(
                name = "",
                address = "Pojok 2/3",
                givenTime = 1681952400000,
                givenAmount = 5.0
            )

            zakatRecipientService.create(zakat.year, payload)
        }

        assertThrows<ConstraintViolationException> {
            val payload = CreateZakatRecipientDto(
                name = "Fulan",
                address = "",
                givenTime = 1681952400000,
                givenAmount = 5.0
            )

            zakatRecipientService.create(zakat.year, payload)
        }

        assertThrows<ConstraintViolationException> {
            val payload = CreateZakatRecipientDto(
                name = "Fulan",
                address = "Pojok 2/3",
                givenTime = -1,
                givenAmount = 5.0
            )

            zakatRecipientService.create(zakat.year, payload)
        }

        assertThrows<ConstraintViolationException> {
            val payload = CreateZakatRecipientDto(
                name = "Fulan",
                address = "Pojok 2/3",
                givenTime = 1681952400000,
                givenAmount = 0.0
            )

            zakatRecipientService.create(zakat.year, payload)
        }
    }

    @Test
    fun `should not create zakat fitrah recipient because edition doesnt exists`() {
        assertThrows<NotFoundException> {
            val payload = CreateZakatRecipientDto(
                name = "Fulan",
                address = "Pojok 2/3",
                givenTime = 1681952400000,
                givenAmount = 5.0
            )

            zakatRecipientService.create(2022, payload)
        }
    }

}