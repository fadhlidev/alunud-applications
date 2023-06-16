package com.alunud.application.zakatfitrah.service.impl

import com.alunud.application.AlUnudApplication
import com.alunud.application.zakatfitrah.dto.CreateZakatApplicantDto
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

}