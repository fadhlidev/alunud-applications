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
import org.springframework.data.repository.findByIdOrNull
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
    fun `create zakat edition`() {
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
    fun `clean zakat payer repository`() {
        zakatPayerRepository.deleteAll()
    }

    @AfterEach
    fun `clean zakat edition repository`() {
        zakatEditionRepository.deleteAll()
    }

    @Test
    fun `should create zakat fitrah payer`() {
        val payload = CreateZakatPayerDto(
            name = "Fulan",
            address = "Pojok 2/3",
            totalPeople = 4,
            totalAmount = 11.0,
            excessAmountReturned = true
        )

        val result = zakatPayerService.create(zakat.year, payload)

        assertNotNull(result)

        assertEquals(payload.name, result.name)
        assertEquals(payload.address, result.address)
        assertEquals(payload.totalPeople, result.zakat.totalPeople)
        assertEquals(payload.totalAmount, result.zakat.totalAmount)
        assertEquals(payload.excessAmountReturned, result.zakat.excessAmountReturned)
        assertEquals(1.0, result.zakat.excessAmount)
        assertEquals(0.0, result.zakat.lessAmount)
    }

    @Test
    fun `should not create zakat fitrah payer because edition already ended`() {
        val zakat = ZakatEdition(
            id = UUID.randomUUID(),
            year = 2022,
            startDate = 1620234000000,
            amountPerPerson = 2.5,
            endDate = 1618074000000
        )

        zakatEditionRepository.save(zakat)

        assertThrows<ConstraintViolationException> {
            zakatPayerService.create(zakat.year) {
                name = "Fulan"
                address = "Pojok 2/3"
                totalPeople = 4
                totalAmount = 11.0
                excessAmountReturned = true
            }
        }
    }

    @Test
    fun `should not create zakat fitrah payer because invalid payload`() {
        assertThrows<ConstraintViolationException> {
            zakatPayerService.create(zakat.year) {
                name = ""
                address = null
                totalPeople = 4
                totalAmount = 11.0
                excessAmountReturned = true
            }
        }

        assertThrows<ConstraintViolationException> {
            zakatPayerService.create(zakat.year) {
                name = "Fulan"
                address = ""
                totalPeople = 4
                totalAmount = 11.0
                excessAmountReturned = true
            }
        }

        assertThrows<ConstraintViolationException> {
            zakatPayerService.create(zakat.year) {
                name = "Fulan"
                address = null
                totalPeople = 0
                totalAmount = 11.0
                excessAmountReturned = true
            }
        }

        assertThrows<ConstraintViolationException> {
            zakatPayerService.create(zakat.year) {
                name = "Fulan"
                address = null
                totalPeople = 4
                totalAmount = 0.0
                excessAmountReturned = true
            }
        }
    }

    @Test
    fun `should not create zakat fitrah payer because edition doesnt exists`() {
        assertThrows<NotFoundException> {
            zakatPayerService.create(2022) {
                name = "Fulan"
                address = "Pojok 2/3"
                totalPeople = 4
                totalAmount = 11.0
                excessAmountReturned = true
            }
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

        val payload = UpdateZakatPayerDto(
            name = "Fulanah",
            address = "Pojok 2/3",
            totalPeople = 5,
            totalAmount = 13.0,
            excessAmountReturned = false
        )

        val result = zakatPayerService.update(zakat.year, payer.id, payload)

        assertNotNull(result)

        assertEquals(payload.name, result.name)
        assertEquals(payload.totalPeople, result.zakat.totalPeople)
        assertEquals(payload.totalAmount, result.zakat.totalAmount)
        assertEquals(payload.excessAmountReturned, result.zakat.excessAmountReturned)
        assertEquals(0.5, result.zakat.excessAmount)
        assertEquals(0.0, result.zakat.lessAmount)
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
            zakatPayerService.update(zakat.year, payer.id) {
                name = ""
                address = "Pojok 2/3"
                totalPeople = 5
                totalAmount = 13.0
                excessAmountReturned = false
            }
        }

        assertThrows<ConstraintViolationException> {
            zakatPayerService.update(zakat.year, payer.id) {
                name = "Fulanah"
                address = ""
                totalPeople = 5
                totalAmount = 13.0
                excessAmountReturned = false
            }
        }

        assertThrows<ConstraintViolationException> {
            zakatPayerService.update(zakat.year, payer.id) {
                name = "Fulanah"
                address = "Pojok 2/3"
                totalPeople = 0
                totalAmount = 13.0
                excessAmountReturned = false
            }
        }

        assertThrows<ConstraintViolationException> {
            zakatPayerService.update(zakat.year, payer.id) {
                name = "Fulanah"
                address = "Pojok 2/3"
                totalPeople = 5
                totalAmount = 0.0
                excessAmountReturned = false
            }
        }
    }

    @Test
    fun `should not update zakat fitrah payer because edition doesnt exists`() {
        assertThrows<NotFoundException> {
            zakatPayerService.update(2023, UUID.randomUUID()) {
                name = "Fulanah"
                address = "Pojok 2/3"
                totalPeople = 5
                totalAmount = 13.0
                excessAmountReturned = false
            }
        }
    }

    @Test
    fun `should not update zakat fitrah payer because payer doesnt exists`() {
        assertThrows<NotFoundException> {
            zakatPayerService.update(zakat.year, UUID.randomUUID()) {
                name = "Fulanah"
                address = "Pojok 2/3"
                totalPeople = 5
                totalAmount = 13.0
                excessAmountReturned = false
            }
        }
    }

    @Test
    fun `should delete zakat fitrah payer`() {
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

        assertNotNull(zakatPayerRepository.findByIdOrNull(payer.id))

        zakatPayerService.delete(zakat.year, payer.id)

        assertNull(zakatPayerRepository.findByIdOrNull(payer.id))
    }

    @Test
    fun `should throw not found edition when delete zakat fitrah payer`() {
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

        assertNotNull(zakatPayerRepository.findByIdOrNull(payer.id))

        assertThrows<NotFoundException> {
            zakatPayerService.delete(2022, payer.id)
        }
    }

    @Test
    fun `should throw not found payer when delete zakat fitrah payer`() {
        assertThrows<NotFoundException> {
            zakatPayerService.delete(zakat.year, UUID.randomUUID())
        }
    }

    @Test
    fun `should returns list of zakat fitrah payers`() {
        assertEquals(0, zakatPayerService.findAll(zakat.year).size)

        zakatPayerRepository.save(
            ZakatPayer(
                id = UUID.randomUUID(),
                name = "Wahid",
                address = "Pojok 2/3",
                totalPeople = 1,
                totalAmount = 3.0,
                excessAmountReturned = false,
                excessAmount = 0.5,
                lessAmount = 0.0,
                submittedTime = System.currentTimeMillis(),
                zakatEdition = zakat
            )
        )

        assertEquals(1, zakatPayerService.findAll(zakat.year).size)

        zakatPayerRepository.save(
            ZakatPayer(
                id = UUID.randomUUID(),
                name = "Isnaini",
                address = "Pojok 2/3",
                totalPeople = 2,
                totalAmount = 5.0,
                excessAmountReturned = true,
                excessAmount = 0.0,
                lessAmount = 0.0,
                submittedTime = System.currentTimeMillis(),
                zakatEdition = zakat
            )
        )

        zakatPayerRepository.save(
            ZakatPayer(
                id = UUID.randomUUID(),
                name = "Salasa",
                address = "Pojok 2/3",
                totalPeople = 4,
                totalAmount = 11.0,
                excessAmountReturned = true,
                excessAmount = 1.0,
                lessAmount = 0.0,
                submittedTime = System.currentTimeMillis(),
                zakatEdition = zakat
            )
        )

        val result = zakatPayerService.findAll(zakat.year)
        assertEquals(3, result.size)
        assertEquals("Wahid", result[0].name)
        assertEquals("Isnaini", result[1].name)
        assertEquals("Salasa", result[2].name)
    }

    @Test
    fun `should not returns list of zakat fitrah payers because edition doesnt exist`() {
        assertThrows<NotFoundException> {
            zakatPayerService.findAll(2022)
        }
    }

    @Test
    fun `should find zakat fitrah payer`() {
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

        assertNotNull(zakatPayerRepository.findByZakatEditionAndId(zakat, payer.id))

        val response = zakatPayerService.findOne(zakat.year, payer.id)

        assertNotNull(response)

        assertEquals(payer.name, response.name)
        assertEquals(payer.address, response.address)
        assertEquals(payer.totalPeople, response.zakat.totalPeople)
        assertEquals(payer.totalAmount, response.zakat.totalAmount)
        assertEquals(payer.excessAmountReturned, response.zakat.excessAmountReturned)
        assertEquals(1.0, response.zakat.excessAmount)
        assertEquals(0.0, response.zakat.lessAmount)
    }

    @Test
    fun `should throw not found edition when finding zakat fitrah payer`() {
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

        zakatPayerRepository.findByZakatEditionAndId(zakat, payer.id)

        assertThrows<NotFoundException> {
            zakatPayerService.findOne(2022, payer.id)
        }
    }

    @Test
    fun `should throw not found payer when finding zakat fitrah payer`() {
        assertThrows<NotFoundException> {
            zakatPayerService.findOne(zakat.year, UUID.randomUUID())
        }
    }

}