package com.alunud.application.zakatfitrah.service.impl

import com.alunud.application.AlUnudApplication
import com.alunud.application.zakatfitrah.dto.CreateZakatApplicantDto
import com.alunud.application.zakatfitrah.dto.UpdateZakatApplicantDto
import com.alunud.application.zakatfitrah.entity.ZakatApplicant
import com.alunud.application.zakatfitrah.entity.ZakatEdition
import com.alunud.application.zakatfitrah.repository.ZakatApplicantRepository
import com.alunud.application.zakatfitrah.repository.ZakatEditionRepository
import com.alunud.application.zakatfitrah.service.ZakatApplicantService
import com.alunud.exception.NotFoundException
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest(classes = [AlUnudApplication::class])
@ActiveProfiles("test")
class ZakatApplicantServiceImplTest(
    @Autowired val zakatApplicantService: ZakatApplicantService,
    @Autowired val zakatApplicantRepository: ZakatApplicantRepository,
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
    fun cleanZakatApplicantRepository() {
        zakatApplicantRepository.deleteAll()
    }

    @AfterEach
    fun cleanZakatEditionRepository() {
        zakatEditionRepository.deleteAll()
    }

    @Test
    fun `should create zakat fitrah applicant`() {
        val payload = CreateZakatApplicantDto(
            institutionName = "Pondok Pesantren A",
            institutionAddress = "Tawangsari",
            receivedTime = 1681801200000,
            givenTime = null,
            givenAmount = null
        )

        val result = zakatApplicantService.create(zakat.year, payload)

        assertNotNull(result)

        assertEquals(payload.institutionName, result.institutionName)
        assertEquals(payload.institutionAddress, result.institutionAddress)
        assertEquals(payload.receivedTime, result.receivedTime)
        assertEquals(payload.givenTime, result.givenTime)
        assertEquals(payload.givenAmount, result.givenAmount)
    }

    @Test
    fun `should not create zakat fitrah applicant because receivedTime is earlier that startDate edition`() {
        assertThrows<ConstraintViolationException> {
            val payload = CreateZakatApplicantDto(
                institutionName = "Pondok Pesantren A",
                institutionAddress = "Tawangsari",
                receivedTime = 1681542000000,
                givenTime = null,
                givenAmount = null
            )

            zakatApplicantService.create(zakat.year, payload)
        }
    }

    @Test
    fun `should not create zakat fitrah applicant because invalid payload`() {
        assertThrows<ConstraintViolationException> {
            val payload = CreateZakatApplicantDto(
                institutionName = "",
                institutionAddress = "Tawangsari",
                receivedTime = 1681542000000,
                givenTime = null,
                givenAmount = null
            )

            zakatApplicantService.create(zakat.year, payload)
        }

        assertThrows<ConstraintViolationException> {
            val payload = CreateZakatApplicantDto(
                institutionName = "Pondok Pesantren A",
                institutionAddress = "",
                receivedTime = 1681542000000,
                givenTime = null,
                givenAmount = null
            )

            zakatApplicantService.create(zakat.year, payload)
        }

        assertThrows<ConstraintViolationException> {
            val payload = CreateZakatApplicantDto(
                institutionName = "Pondok Pesantren A",
                institutionAddress = "Tawangsari",
                receivedTime = -1,
                givenTime = null,
                givenAmount = null
            )

            zakatApplicantService.create(zakat.year, payload)
        }

        assertThrows<ConstraintViolationException> {
            val payload = CreateZakatApplicantDto(
                institutionName = "Pondok Pesantren A",
                institutionAddress = "Tawangsari",
                receivedTime = 1681542000000,
                givenTime = -1,
                givenAmount = null
            )

            zakatApplicantService.create(zakat.year, payload)
        }

        assertThrows<ConstraintViolationException> {
            val payload = CreateZakatApplicantDto(
                institutionName = "Pondok Pesantren A",
                institutionAddress = "Tawangsari",
                receivedTime = 1681542000000,
                givenTime = 1681538400000,
                givenAmount = null
            )

            zakatApplicantService.create(zakat.year, payload)
        }

        assertThrows<ConstraintViolationException> {
            val payload = CreateZakatApplicantDto(
                institutionName = "Pondok Pesantren A",
                institutionAddress = "Tawangsari",
                receivedTime = 1681542000000,
                givenTime = null,
                givenAmount = 0.0
            )

            zakatApplicantService.create(zakat.year, payload)
        }
    }

    @Test
    fun `should not create zakat fitrah applicant because edition doesnt exists`() {
        assertThrows<NotFoundException> {
            val payload = CreateZakatApplicantDto(
                institutionName = "Pondok Pesantren A",
                institutionAddress = "Tawangsari",
                receivedTime = 1681801200000,
                givenTime = null,
                givenAmount = null
            )

            zakatApplicantService.create(2022, payload)
        }
    }

    @Test
    fun `should update zakat fitrah applicant`() {
        val applicant = ZakatApplicant(
            id = UUID.randomUUID(),
            institutionName = "Pondok Pesantren A",
            institutionAddress = "Tawangsari",
            receivedTime = 1681801200000,
            givenTime = null,
            givenAmount = null,
            zakatEdition = zakat
        )

        zakatApplicantRepository.save(applicant)

        val payload = UpdateZakatApplicantDto(
            institutionName = "Pondok Pesantren B",
            institutionAddress = "Sukoharjo",
            receivedTime = 1681804800000,
            givenTime = 1681808400000,
            givenAmount = 25.0
        )

        val result = zakatApplicantService.update(zakat.year, applicant.id, payload)

        assertNotNull(result)

        assertEquals(payload.institutionName, result.institutionName)
        assertEquals(payload.institutionAddress, result.institutionAddress)
        assertEquals(payload.receivedTime, result.receivedTime)
        assertEquals(payload.givenTime, result.givenTime)
        assertEquals(payload.givenAmount, result.givenAmount)
    }

    @Test
    fun `should not update zakat fitrah applicant because receivedTime is earlier that startDate edition`() {
        val applicant = ZakatApplicant(
            id = UUID.randomUUID(),
            institutionName = "Pondok Pesantren A",
            institutionAddress = "Tawangsari",
            receivedTime = 1681801200000,
            givenTime = null,
            givenAmount = null,
            zakatEdition = zakat
        )

        zakatApplicantRepository.save(applicant)

        assertThrows<ConstraintViolationException> {
            val payload = UpdateZakatApplicantDto(
                institutionName = "Pondok Pesantren B",
                institutionAddress = "Sukoharjo",
                receivedTime = 1681542000000,
                givenTime = null,
                givenAmount = null
            )

            zakatApplicantService.update(zakat.year, applicant.id, payload)
        }
    }

    @Test
    fun `should not update zakat fitrah applicant because invalid payload`() {
        val applicant = ZakatApplicant(
            id = UUID.randomUUID(),
            institutionName = "Pondok Pesantren A",
            institutionAddress = "Tawangsari",
            receivedTime = 1681801200000,
            givenTime = null,
            givenAmount = null,
            zakatEdition = zakat
        )

        zakatApplicantRepository.save(applicant)

        assertThrows<ConstraintViolationException> {
            val payload = UpdateZakatApplicantDto(
                institutionName = "",
                institutionAddress = "Sukoharjo",
                receivedTime = 1681804800000,
                givenTime = 1681808400000,
                givenAmount = 25.0
            )

            zakatApplicantService.update(zakat.year, applicant.id, payload)
        }

        assertThrows<ConstraintViolationException> {
            val payload = UpdateZakatApplicantDto(
                institutionName = "Pondok Pesantren B",
                institutionAddress = "",
                receivedTime = 1681804800000,
                givenTime = 1681808400000,
                givenAmount = 25.0
            )

            zakatApplicantService.update(zakat.year, applicant.id, payload)
        }

        assertThrows<ConstraintViolationException> {
            val payload = UpdateZakatApplicantDto(
                institutionName = "Pondok Pesantren B",
                institutionAddress = "Sukoharjo",
                receivedTime = -1,
                givenTime = null,
                givenAmount = null
            )

            zakatApplicantService.update(zakat.year, applicant.id, payload)
        }

        assertThrows<ConstraintViolationException> {
            val payload = UpdateZakatApplicantDto(
                institutionName = "Pondok Pesantren B",
                institutionAddress = "Sukoharjo",
                receivedTime = 1681808400000,
                givenTime = 1681804800000,
                givenAmount = 25.0
            )

            zakatApplicantService.update(zakat.year, applicant.id, payload)
        }

        assertThrows<ConstraintViolationException> {
            val payload = UpdateZakatApplicantDto(
                institutionName = "Pondok Pesantren B",
                institutionAddress = "Sukoharjo",
                receivedTime = 1681804800000,
                givenTime = 1681808400000,
                givenAmount = 0.0
            )

            zakatApplicantService.update(zakat.year, applicant.id, payload)
        }
    }

    @Test
    fun `should not update zakat fitrah applicant because edition doesnt exists`() {
        val applicant = ZakatApplicant(
            id = UUID.randomUUID(),
            institutionName = "Pondok Pesantren A",
            institutionAddress = "Tawangsari",
            receivedTime = 1681801200000,
            givenTime = null,
            givenAmount = null,
            zakatEdition = zakat
        )

        zakatApplicantRepository.save(applicant)

        assertThrows<NotFoundException> {
            val payload = UpdateZakatApplicantDto(
            institutionName = "Pondok Pesantren B",
            institutionAddress = "Sukoharjo",
            receivedTime = 1681804800000,
            givenTime = 1681808400000,
            givenAmount = 25.0
        )

            zakatApplicantService.update(2022, applicant.id, payload)
        }
    }

    @Test
    fun `should not update zakat fitrah applicant because applicant doesnt exists`() {
        assertThrows<NotFoundException> {
            val payload = UpdateZakatApplicantDto(
                institutionName = "Pondok Pesantren B",
                institutionAddress = "Sukoharjo",
                receivedTime = 1681804800000,
                givenTime = 1681808400000,
                givenAmount = 25.0
            )

            zakatApplicantService.update(zakat.year, UUID.randomUUID(), payload)
        }
    }

    @Test
    fun `should delete zakat fitrah applicant`() {
        val applicant = ZakatApplicant(
            id = UUID.randomUUID(),
            institutionName = "Pondok Pesantren A",
            institutionAddress = "Tawangsari",
            receivedTime = 1681801200000,
            givenTime = null,
            givenAmount = null,
            zakatEdition = zakat
        )

        zakatApplicantRepository.save(applicant)

        assertNotNull(zakatApplicantRepository.findByIdOrNull(applicant.id))

        zakatApplicantService.delete(zakat.year, applicant.id)

        assertNull(zakatApplicantRepository.findByIdOrNull(applicant.id))
    }

    @Test
    fun `should throw not found edition when delete zakat fitrah applicant`() {
        val applicant = ZakatApplicant(
            id = UUID.randomUUID(),
            institutionName = "Pondok Pesantren A",
            institutionAddress = "Tawangsari",
            receivedTime = 1681801200000,
            givenTime = null,
            givenAmount = null,
            zakatEdition = zakat
        )

        zakatApplicantRepository.save(applicant)

        assertNotNull(zakatApplicantRepository.findByIdOrNull(applicant.id))

        assertThrows<NotFoundException> {
            zakatApplicantService.delete(2022, applicant.id)
        }
    }

    @Test
    fun `should throw not found applicant when delete zakat fitrah applicant`() {
        assertThrows<NotFoundException> {
            zakatApplicantService.delete(zakat.year, UUID.randomUUID())
        }
    }

    @Test
    fun `should returns list of zakat fitrah applicants`() {
        assertEquals(0, zakatApplicantService.findAll(zakat.year).size)

        zakatApplicantRepository.save(ZakatApplicant(
            id = UUID.randomUUID(),
            institutionName = "Pondok Pesantren A",
            institutionAddress = "Tawangsari",
            receivedTime = 1681801200000,
            givenTime = null,
            givenAmount = null,
            zakatEdition = zakat
        ))

        assertEquals(1, zakatApplicantService.findAll(zakat.year).size)

        zakatApplicantRepository.save(ZakatApplicant(
            id = UUID.randomUUID(),
            institutionName = "Pondok Pesantren B",
            institutionAddress = "Tawangsari",
            receivedTime = 1681804800000,
            givenTime = null,
            givenAmount = null,
            zakatEdition = zakat
        ))

        zakatApplicantRepository.save(ZakatApplicant(
            id = UUID.randomUUID(),
            institutionName = "Pondok Pesantren C",
            institutionAddress = "Tawangsari",
            receivedTime = 1681808400000,
            givenTime = null,
            givenAmount = null,
            zakatEdition = zakat
        ))


        val result = zakatApplicantService.findAll(zakat.year)
        assertEquals(3, result.size)
        assertEquals("Pondok Pesantren A", result[0].institutionName)
        assertEquals("Pondok Pesantren B", result[1].institutionName)
        assertEquals("Pondok Pesantren C", result[2].institutionName)
    }

    @Test
    fun `should not returns list of zakat fitrah applicants because edition doesnt exist`() {
        assertThrows<NotFoundException> {
            zakatApplicantService.findAll(2022)
        }
    }

    @Test
    fun `should find zakat fitrah applicant`() {
        val applicant = ZakatApplicant(
            id = UUID.randomUUID(),
            institutionName = "Pondok Pesantren A",
            institutionAddress = "Tawangsari",
            receivedTime = 1681801200000,
            givenTime = null,
            givenAmount = null,
            zakatEdition = zakat
        )

        zakatApplicantRepository.save(applicant)

        assertNotNull(zakatApplicantRepository.findByZakatEditionAndId(zakat, applicant.id))

        val result = zakatApplicantService.findOne(zakat.year, applicant.id)

        assertNotNull(result)

        assertEquals(applicant.institutionName, result.institutionName)
        assertEquals(applicant.institutionAddress, result.institutionAddress)
        assertEquals(applicant.receivedTime, result.receivedTime)
        assertEquals(applicant.givenTime, result.givenTime)
        assertEquals(applicant.givenAmount, result.givenAmount)
    }

    @Test
    fun `should throw not found edition when finding zakat fitrah applicant`() {
        val applicant = ZakatApplicant(
            id = UUID.randomUUID(),
            institutionName = "Pondok Pesantren A",
            institutionAddress = "Tawangsari",
            receivedTime = 1681801200000,
            givenTime = null,
            givenAmount = null,
            zakatEdition = zakat
        )

        zakatApplicantRepository.save(applicant)

        assertNotNull(zakatApplicantRepository.findByZakatEditionAndId(zakat, applicant.id))

        assertThrows<NotFoundException> {
            zakatApplicantService.findOne(2022, applicant.id)
        }
    }

    @Test
    fun `should throw not found applicant when finding zakat fitrah applicant`() {
        assertThrows<NotFoundException> {
            zakatApplicantService.findOne(zakat.year, UUID.randomUUID())
        }
    }

}