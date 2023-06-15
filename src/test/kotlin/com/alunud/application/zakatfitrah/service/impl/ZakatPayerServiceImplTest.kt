package com.alunud.application.zakatfitrah.service.impl

import com.alunud.application.AlUnudApplication
import com.alunud.application.zakatfitrah.dto.CreateZakatPayerDto
import com.alunud.application.zakatfitrah.entity.ZakatEdition
import com.alunud.application.zakatfitrah.repository.ZakatEditionRepository
import com.alunud.application.zakatfitrah.repository.ZakatPayerRepository
import com.alunud.application.zakatfitrah.service.ZakatPayerService
import com.alunud.exception.NotFoundException
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest(classes = [AlUnudApplication::class])
@ActiveProfiles("test")
class ZakatPayerServiceImplTest(
    @Autowired val zakatPayerService: ZakatPayerService,
    @Autowired val zakatPayerRepository: ZakatPayerRepository,
    @Autowired val zakatEditionRepository: ZakatEditionRepository
) {

    @AfterEach
    fun cleanZakatPayerRepository() {
        zakatPayerRepository.deleteAll()
    }

    @AfterEach
    fun cleanZakatEditionRepository() {
        zakatEditionRepository.deleteAll()
    }

    @Test
    fun `should create zakat fitrah payer`() {
        val zakat = ZakatEdition(
            id = UUID.randomUUID(),
            year = 2023,
            startDate = 1681578000000,
            amountPerPerson = 2.5,
            endDate = null
        )

        zakatEditionRepository.save(zakat)

        val request = CreateZakatPayerDto(
            name = "Fulan",
            address = "Pojok 2/3",
            totalPeople = 4,
            totalAmount = 11.0,
            excessAmountReturned = true
        )

        val response = zakatPayerService.create(zakat.year, request)

        assertNotNull(response)

        assertEquals(request.name, response.name)
        assertEquals(request.address, response.address)
        assertEquals(request.totalPeople, response.zakat.totalPeople)
        assertEquals(request.totalAmount, response.zakat.totalAmount)
        assertEquals(request.excessAmountReturned, response.zakat.excessAmountReturned)

        val expectedTotalAmount = zakat.amountPerPerson * response.zakat.totalPeople
        assertEquals(request.totalAmount - expectedTotalAmount, response.zakat.excessAmount)
        assertEquals(expectedTotalAmount - request.totalAmount, response.zakat.lessAmount)
    }

    @Test
    fun `should not create zakat fitrah payer because invalid payload`() {
        val zakat = ZakatEdition(
            id = UUID.randomUUID(),
            year = 2023,
            startDate = 1681578000000,
            amountPerPerson = 2.5,
            endDate = null
        )

        zakatEditionRepository.save(zakat)

        assertThrows<ConstraintViolationException> {
            val request = CreateZakatPayerDto(
                name = "",
                address = null,
                totalPeople = 4,
                totalAmount = 11.0,
                excessAmountReturned = true
            )

            zakatPayerService.create(zakat.year, request)
        }

        assertThrows<ConstraintViolationException> {
            val request = CreateZakatPayerDto(
                name = "Fulan",
                address = "",
                totalPeople = 4,
                totalAmount = 11.0,
                excessAmountReturned = true
            )

            zakatPayerService.create(zakat.year, request)
        }

        assertThrows<ConstraintViolationException> {
            val request = CreateZakatPayerDto(
                name = "Fulan",
                address = null,
                totalPeople = 0,
                totalAmount = 11.0,
                excessAmountReturned = true
            )

            zakatPayerService.create(zakat.year, request)
        }

        assertThrows<ConstraintViolationException> {
            val request = CreateZakatPayerDto(
                name = "Fulan",
                address = null,
                totalPeople = 4,
                totalAmount = 0.0,
                excessAmountReturned = true
            )

            zakatPayerService.create(zakat.year, request)
        }
    }

    @Test
    fun `should not create zakat fitrah payer because edition doesnt exists`() {
        assertThrows<NotFoundException> {
            val request = CreateZakatPayerDto(
                name = "Fulan",
                address = "Pojok 2/3",
                totalPeople = 4,
                totalAmount = 11.0,
                excessAmountReturned = true
            )

            zakatPayerService.create(2023, request)
        }
    }

}