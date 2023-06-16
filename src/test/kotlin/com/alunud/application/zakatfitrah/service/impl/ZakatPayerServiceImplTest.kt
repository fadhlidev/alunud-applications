package com.alunud.application.zakatfitrah.service.impl

import com.alunud.application.AlUnudApplication
import com.alunud.application.zakatfitrah.dto.CreateZakatPayerDto
import com.alunud.application.zakatfitrah.dto.UpdateZakatPayerDto
import com.alunud.application.zakatfitrah.entity.ZakatEdition
import com.alunud.application.zakatfitrah.entity.ZakatPayer
import com.alunud.application.zakatfitrah.repository.ZakatEditionRepository
import com.alunud.application.zakatfitrah.repository.ZakatPayerRepository
import com.alunud.application.zakatfitrah.service.ZakatPayerService
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
class ZakatPayerServiceImplTest(
    @Autowired val zakatPayerService: ZakatPayerService,
    @Autowired val zakatPayerRepository: ZakatPayerRepository,
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
            endDate = null
        )

        zakatEditionRepository.save(zakat)

        this.zakat = zakat
    }

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

            zakatPayerService.create(2022, request)
        }
    }

    @Test
    fun `should update zakat fitrah payer`() {
        val payer = ZakatPayer(
            id = UUID.randomUUID(),
            name = "Fulan",
            address = "Pojok 2/3",
            totalPeople = 4,
            totalAmount = 11.0,
            excessAmountReturned = true,
            excessAmount = 1.0,
            lessAmount = 0.0,
            submittedTime = System.currentTimeMillis(),
            zakatEdition = zakat
        )

        zakatPayerRepository.save(payer)

        val request = UpdateZakatPayerDto(
            name = "Fulanah",
            address = "Pojok 2/3",
            totalPeople = 5,
            totalAmount = 13.0,
            excessAmountReturned = false
        )

        val response = zakatPayerService.update(zakat.year, payer.id, request)

        assertNotNull(response)

        assertEquals(request.name, response.name)
        assertEquals(request.totalPeople, response.zakat.totalPeople)
        assertEquals(request.totalAmount, response.zakat.totalAmount)
        assertEquals(request.excessAmountReturned, response.zakat.excessAmountReturned)

        val expectedTotalAmount = zakat.amountPerPerson * response.zakat.totalPeople
        assertEquals(request.totalAmount!! - expectedTotalAmount, response.zakat.excessAmount)
        assertEquals(expectedTotalAmount - request.totalAmount!!, response.zakat.lessAmount)
    }

    @Test
    fun `should not update zakat fitrah payer because invalid payload`() {
        val payer = ZakatPayer(
            id = UUID.randomUUID(),
            name = "Fulan",
            address = "Pojok 2/3",
            totalPeople = 4,
            totalAmount = 11.0,
            excessAmountReturned = true,
            excessAmount = 1.0,
            lessAmount = 0.0,
            submittedTime = System.currentTimeMillis(),
            zakatEdition = zakat
        )

        zakatPayerRepository.save(payer)

        assertThrows<ConstraintViolationException> {
            val request = UpdateZakatPayerDto(
                name = "",
                address = "Pojok 2/3",
                totalPeople = 5,
                totalAmount = 13.0,
                excessAmountReturned = false
            )

            zakatPayerService.update(zakat.year, payer.id, request)
        }

        assertThrows<ConstraintViolationException> {
            val request = UpdateZakatPayerDto(
                name = "Fulanah",
                address = "",
                totalPeople = 5,
                totalAmount = 13.0,
                excessAmountReturned = false
            )

            zakatPayerService.update(zakat.year, payer.id, request)
        }

        assertThrows<ConstraintViolationException> {
            val request = UpdateZakatPayerDto(
                name = "Fulanah",
                address = "Pojok 2/3",
                totalPeople = 0,
                totalAmount = 13.0,
                excessAmountReturned = false
            )

            zakatPayerService.update(zakat.year, payer.id, request)
        }

        assertThrows<ConstraintViolationException> {
            val request = UpdateZakatPayerDto(
                name = "Fulanah",
                address = "Pojok 2/3",
                totalPeople = 5,
                totalAmount = 0.0,
                excessAmountReturned = false
            )

            zakatPayerService.update(zakat.year, payer.id, request)
        }
    }

    @Test
    fun `should not update zakat fitrah payer because edition doesnt exists`() {
        assertThrows<NotFoundException> {
            val request = UpdateZakatPayerDto(
                name = "Fulanah",
                address = "Pojok 2/3",
                totalPeople = 5,
                totalAmount = 13.0,
                excessAmountReturned = false
            )

            zakatPayerService.update(2023, UUID.randomUUID(), request)
        }
    }

    @Test
    fun `should not update zakat fitrah payer because payer doesnt exists`() {
        assertThrows<NotFoundException> {
            val request = UpdateZakatPayerDto(
                name = "Fulanah",
                address = "Pojok 2/3",
                totalPeople = 5,
                totalAmount = 13.0,
                excessAmountReturned = false
            )

            zakatPayerService.update(zakat.year, UUID.randomUUID(), request)
        }
    }

}