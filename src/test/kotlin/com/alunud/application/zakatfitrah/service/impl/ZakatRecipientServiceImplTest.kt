package com.alunud.application.zakatfitrah.service.impl

import com.alunud.application.AlUnudApplication
import com.alunud.application.zakatfitrah.dto.CreateZakatRecipientDto
import com.alunud.application.zakatfitrah.dto.UpdateZakatRecipientDto
import com.alunud.application.zakatfitrah.entity.ZakatEdition
import com.alunud.application.zakatfitrah.entity.ZakatRecipient
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

    @Test
    fun `should update zakat fitrah recipient`() {
        val recipient = ZakatRecipient(
            id = UUID.randomUUID(),
            name = "Fulan",
            address = "Pojok 2/3",
            givenTime = 1681884000000,
            givenAmount = 5.0,
            zakatEdition = zakat
        )

        zakatRecipientRepository.save(recipient)

        val payload = UpdateZakatRecipientDto(
            name = "Fulanah",
            address = "Pojok 3/3",
            givenTime = 1681894800000,
            givenAmount = 7.5
        )

        val result = zakatRecipientService.update(zakat.year, recipient.id, payload)

        assertNotNull(result)

        assertEquals(payload.name, result.name)
        assertEquals(payload.address, result.address)
        assertEquals(payload.givenTime, result.givenTime)
        assertEquals(payload.givenAmount, result.givenAmount)
    }

    @Test
    fun `should not update zakat fitrah recipient because givenTime is earlier than startDate edition`() {
        val recipient = ZakatRecipient(
            id = UUID.randomUUID(),
            name = "Fulan",
            address = "Pojok 2/3",
            givenTime = 1681884000000,
            givenAmount = 5.0,
            zakatEdition = zakat
        )

        zakatRecipientRepository.save(recipient)

        assertThrows<ConstraintViolationException> {
            val payload = UpdateZakatRecipientDto(
                name = "Fulan",
                address = "Pojok 2/3",
                givenTime = 1681520400000,
                givenAmount = 5.0
            )

            zakatRecipientService.update(zakat.year, recipient.id, payload)
        }
    }

    @Test
    fun `should not update zakat fitrah recipient because givenTime is later than endDate edition`() {
        val recipient = ZakatRecipient(
            id = UUID.randomUUID(),
            name = "Fulan",
            address = "Pojok 2/3",
            givenTime = 1681884000000,
            givenAmount = 5.0,
            zakatEdition = zakat
        )

        zakatRecipientRepository.save(recipient)

        assertThrows<ConstraintViolationException> {
            val payload = UpdateZakatRecipientDto(
                name = "Fulan",
                address = "Pojok 2/3",
                givenTime = 1681952400000,
                givenAmount = 5.0
            )

            zakatRecipientService.update(zakat.year, recipient.id, payload)
        }
    }

    @Test
    fun `should not update zakat fitrah recipient because invalid payload`() {
        val recipient = ZakatRecipient(
            id = UUID.randomUUID(),
            name = "Fulan",
            address = "Pojok 2/3",
            givenTime = 1681884000000,
            givenAmount = 5.0,
            zakatEdition = zakat
        )

        zakatRecipientRepository.save(recipient)

        assertThrows<ConstraintViolationException> {
            val payload = UpdateZakatRecipientDto(
                name = "",
                address = "Pojok 2/3",
                givenTime = 1681952400000,
                givenAmount = 5.0
            )

            zakatRecipientService.update(zakat.year, recipient.id, payload)
        }

        assertThrows<ConstraintViolationException> {
            val payload = UpdateZakatRecipientDto(
                name = "Fulan",
                address = "",
                givenTime = 1681952400000,
                givenAmount = 5.0
            )

            zakatRecipientService.update(zakat.year, recipient.id, payload)
        }

        assertThrows<ConstraintViolationException> {
            val payload = UpdateZakatRecipientDto(
                name = "Fulan",
                address = "Pojok 2/3",
                givenTime = -1,
                givenAmount = 5.0
            )

            zakatRecipientService.update(zakat.year, recipient.id, payload)
        }

        assertThrows<ConstraintViolationException> {
            val payload = UpdateZakatRecipientDto(
                name = "Fulan",
                address = "Pojok 2/3",
                givenTime = 1681952400000,
                givenAmount = 0.0
            )

            zakatRecipientService.update(zakat.year, recipient.id, payload)
        }
    }

    @Test
    fun `should not update zakat fitrah recipient because edition doesnt exists`() {
        val recipient = ZakatRecipient(
            id = UUID.randomUUID(),
            name = "Fulan",
            address = "Pojok 2/3",
            givenTime = 1681884000000,
            givenAmount = 5.0,
            zakatEdition = zakat
        )

        zakatRecipientRepository.save(recipient)

        assertThrows<NotFoundException> {
            val payload = UpdateZakatRecipientDto(
                name = "Fulanah",
                address = "Pojok 3/3",
                givenTime = 1681894800000,
                givenAmount = 7.5
            )

            zakatRecipientService.update(2022, recipient.id, payload)
        }
    }

    @Test
    fun `should not update zakat fitrah recipient because recipient doesnt exists`() {
        assertThrows<NotFoundException> {
            val payload = UpdateZakatRecipientDto(
                name = "Fulanah",
                address = "Pojok 3/3",
                givenTime = 1681894800000,
                givenAmount = 7.5
            )

            zakatRecipientService.update(zakat.year, UUID.randomUUID(), payload)
        }
    }

}