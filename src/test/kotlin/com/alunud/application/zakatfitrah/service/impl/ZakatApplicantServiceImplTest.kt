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
        val request = CreateZakatApplicantDto(
            institutionName = "Pondok Pesantren A",
            institutionAddress = "Tawangsari",
            receivedTime = 1681801200000,
            givenTime = null,
            givenAmount = null
        )

        val response = zakatApplicantService.create(zakat.year, request)

        assertNotNull(response)

        assertEquals(request.institutionName, response.institutionName)
        assertEquals(request.institutionAddress, response.institutionAddress)
        assertEquals(request.receivedTime, response.receivedTime)
        assertEquals(request.givenTime, response.givenTime)
        assertEquals(request.givenAmount, response.givenAmount)
    }

    @Test
    fun `should not create zakat fitrah applicant because receivedTime is earlier that startDate edition`() {
        assertThrows<ConstraintViolationException> {
            val request = CreateZakatApplicantDto(
                institutionName = "Pondok Pesantren A",
                institutionAddress = "Tawangsari",
                receivedTime = 1681542000000,
                givenTime = null,
                givenAmount = null
            )

            zakatApplicantService.create(zakat.year, request)
        }
    }

    @Test
    fun `should not create zakat fitrah applicant because invalid payload`() {
        assertThrows<ConstraintViolationException> {
            val request = CreateZakatApplicantDto(
                institutionName = "",
                institutionAddress = "Tawangsari",
                receivedTime = 1681542000000,
                givenTime = null,
                givenAmount = null
            )

            zakatApplicantService.create(zakat.year, request)
        }

        assertThrows<ConstraintViolationException> {
            val request = CreateZakatApplicantDto(
                institutionName = "Pondok Pesantren A",
                institutionAddress = "",
                receivedTime = 1681542000000,
                givenTime = null,
                givenAmount = null
            )

            zakatApplicantService.create(zakat.year, request)
        }

        assertThrows<ConstraintViolationException> {
            val request = CreateZakatApplicantDto(
                institutionName = "Pondok Pesantren A",
                institutionAddress = "Tawangsari",
                receivedTime = -1,
                givenTime = null,
                givenAmount = null
            )

            zakatApplicantService.create(zakat.year, request)
        }

        assertThrows<ConstraintViolationException> {
            val request = CreateZakatApplicantDto(
                institutionName = "Pondok Pesantren A",
                institutionAddress = "Tawangsari",
                receivedTime = 1681542000000,
                givenTime = -1,
                givenAmount = null
            )

            zakatApplicantService.create(zakat.year, request)
        }

        assertThrows<ConstraintViolationException> {
            val request = CreateZakatApplicantDto(
                institutionName = "Pondok Pesantren A",
                institutionAddress = "Tawangsari",
                receivedTime = 1681542000000,
                givenTime = 1681538400000,
                givenAmount = null
            )

            zakatApplicantService.create(zakat.year, request)
        }

        assertThrows<ConstraintViolationException> {
            val request = CreateZakatApplicantDto(
                institutionName = "Pondok Pesantren A",
                institutionAddress = "Tawangsari",
                receivedTime = 1681542000000,
                givenTime = null,
                givenAmount = 0.0
            )

            zakatApplicantService.create(zakat.year, request)
        }
    }

    @Test
    fun `should not create zakat fitrah applicant because edition doesnt exists`() {
        assertThrows<NotFoundException> {
            val request = CreateZakatApplicantDto(
                institutionName = "Pondok Pesantren A",
                institutionAddress = "Tawangsari",
                receivedTime = 1681801200000,
                givenTime = null,
                givenAmount = null
            )

            zakatApplicantService.create(2022, request)
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

        val request = UpdateZakatApplicantDto(
            institutionName = "Pondok Pesantren B",
            institutionAddress = "Sukoharjo",
            receivedTime = 1681804800000,
            givenTime = 1681808400000,
            givenAmount = 25.0
        )

        val response = zakatApplicantService.update(zakat.year, applicant.id, request)

        assertNotNull(response)

        assertEquals(request.institutionName, response.institutionName)
        assertEquals(request.institutionAddress, response.institutionAddress)
        assertEquals(request.receivedTime, response.receivedTime)
        assertEquals(request.givenTime, response.givenTime)
        assertEquals(request.givenAmount, response.givenAmount)
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
            val request = UpdateZakatApplicantDto(
                institutionName = "Pondok Pesantren B",
                institutionAddress = "Sukoharjo",
                receivedTime = 1681542000000,
                givenTime = null,
                givenAmount = null
            )

            zakatApplicantService.update(zakat.year, applicant.id, request)
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
            val request = UpdateZakatApplicantDto(
                institutionName = "",
                institutionAddress = "Sukoharjo",
                receivedTime = 1681804800000,
                givenTime = 1681808400000,
                givenAmount = 25.0
            )

            zakatApplicantService.update(zakat.year, applicant.id, request)
        }

        assertThrows<ConstraintViolationException> {
            val request = UpdateZakatApplicantDto(
                institutionName = "Pondok Pesantren B",
                institutionAddress = "",
                receivedTime = 1681804800000,
                givenTime = 1681808400000,
                givenAmount = 25.0
            )

            zakatApplicantService.update(zakat.year, applicant.id, request)
        }

        assertThrows<ConstraintViolationException> {
            val request = UpdateZakatApplicantDto(
                institutionName = "Pondok Pesantren B",
                institutionAddress = "Sukoharjo",
                receivedTime = -1,
                givenTime = null,
                givenAmount = null
            )

            zakatApplicantService.update(zakat.year, applicant.id, request)
        }

        assertThrows<ConstraintViolationException> {
            val request = UpdateZakatApplicantDto(
                institutionName = "Pondok Pesantren B",
                institutionAddress = "Sukoharjo",
                receivedTime = 1681808400000,
                givenTime = 1681804800000,
                givenAmount = 25.0
            )

            zakatApplicantService.update(zakat.year, applicant.id, request)
        }

        assertThrows<ConstraintViolationException> {
            val request = UpdateZakatApplicantDto(
                institutionName = "Pondok Pesantren B",
                institutionAddress = "Sukoharjo",
                receivedTime = 1681804800000,
                givenTime = 1681808400000,
                givenAmount = 0.0
            )

            zakatApplicantService.update(zakat.year, applicant.id, request)
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
            val request = UpdateZakatApplicantDto(
            institutionName = "Pondok Pesantren B",
            institutionAddress = "Sukoharjo",
            receivedTime = 1681804800000,
            givenTime = 1681808400000,
            givenAmount = 25.0
        )

            zakatApplicantService.update(2022, applicant.id, request)
        }
    }

    @Test
    fun `should not update zakat fitrah applicant because applicant doesnt exists`() {
        assertThrows<NotFoundException> {
            val request = UpdateZakatApplicantDto(
                institutionName = "Pondok Pesantren B",
                institutionAddress = "Sukoharjo",
                receivedTime = 1681804800000,
                givenTime = 1681808400000,
                givenAmount = 25.0
            )

            zakatApplicantService.update(zakat.year, UUID.randomUUID(), request)
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

}