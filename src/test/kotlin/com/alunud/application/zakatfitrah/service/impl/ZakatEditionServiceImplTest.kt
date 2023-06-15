package com.alunud.application.zakatfitrah.service.impl

import com.alunud.application.AlUnudApplication
import com.alunud.application.zakatfitrah.dto.CreateZakatEditionDto
import com.alunud.application.zakatfitrah.dto.UpdateZakatEditionDto
import com.alunud.application.zakatfitrah.entity.ZakatEdition
import com.alunud.application.zakatfitrah.repository.ZakatEditionRepository
import com.alunud.application.zakatfitrah.service.ZakatEditionService
import com.alunud.exception.EntityExistsException
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
class ZakatEditionServiceImplTest(
    @Autowired val zakatEditionService: ZakatEditionService,
    @Autowired val zakatEditionRepository: ZakatEditionRepository
) {

    @AfterEach
    fun clearZakatEditionRepository() {
        zakatEditionRepository.deleteAll()
    }

    @Test
    fun `should create zakat fitrah edition`() {
        val request = CreateZakatEditionDto(
            year = 2023,
            startDate = 1681578000000,
            amountPerPerson = 2.5
        )

        val response = zakatEditionService.create(request)

        assertNotNull(response)

        assertEquals(request.year, response.year)
        assertEquals(request.startDate, response.startDate)
        assertEquals(request.amountPerPerson, response.amountPerPerson)
        assertEquals(request.year, response.year)
        assertNull(response.endDate)
    }

    @Test
    fun `should not create zakat fitrah edition because invalid payload`() {
        assertThrows<ConstraintViolationException> {
            val request = CreateZakatEditionDto(
                year = 1945,
                startDate = 1681578000000,
                amountPerPerson = 2.5
            )

            zakatEditionService.create(request)
        }

        assertThrows<ConstraintViolationException> {
            val request = CreateZakatEditionDto(
                year = 2023,
                startDate = -1,
                amountPerPerson = 2.5
            )

            zakatEditionService.create(request)
        }

        assertThrows<ConstraintViolationException> {
            val request = CreateZakatEditionDto(
                year = 2023,
                startDate = 1681578000000,
                amountPerPerson = 2.0
            )

            zakatEditionService.create(request)
        }
    }

    @Test
    fun `should not create zakat fitrah edition because its already exists`() {
        val zakat = ZakatEdition(
            id = UUID.randomUUID(),
            year = 2023,
            startDate = 1681578000000,
            amountPerPerson = 2.5,
            endDate = null
        )

        zakatEditionRepository.save(zakat)

        assertThrows<EntityExistsException> {
            val request = CreateZakatEditionDto(
                year = 2023,
                startDate = 1681578000000,
                amountPerPerson = 2.5
            )

            zakatEditionService.create(request)
        }
    }

    @Test
    fun `should update zakat fitrah edition's startDate and endDate`() {
        val zakat = ZakatEdition(
            id = UUID.randomUUID(),
            year = 2023,
            startDate = 1681578000000,
            amountPerPerson = 2.5,
            endDate = null
        )

        zakatEditionRepository.save(zakat)

        val request = UpdateZakatEditionDto(
            startDate = 1681578000000,
            endDate = 1681923600000
        )

        val response = zakatEditionService.update(zakat.year, request)

        assertNotNull(response)

        assertEquals(zakat.year, response.year)
        assertEquals(zakat.amountPerPerson, response.amountPerPerson)
        assertEquals(zakat.startDate, response.startDate)
        assertNotNull(response.endDate)
        assertEquals(response.endDate, request.endDate)
    }

    @Test
    fun `should update zakat fitrah edition's startDate`() {
        val zakat = ZakatEdition(
            id = UUID.randomUUID(),
            year = 2023,
            startDate = 1681578000000,
            amountPerPerson = 2.5,
            endDate = null
        )

        zakatEditionRepository.save(zakat)

        val request = UpdateZakatEditionDto(
            startDate = 1681664400000,
            endDate = null
        )

        val response = zakatEditionService.update(zakat.year, request)

        assertNotNull(response)

        assertEquals(zakat.year, response.year)
        assertEquals(zakat.amountPerPerson, response.amountPerPerson)
        assertNotEquals(zakat.startDate, response.startDate)
        assertNull(response.endDate)
        assertTrue { zakat.startDate < response.startDate }
    }

    @Test
    fun `should update zakat fitrah edition's endDate`() {
        val zakat = ZakatEdition(
            id = UUID.randomUUID(),
            year = 2023,
            startDate = 1681578000000,
            amountPerPerson = 2.5,
            endDate = null
        )

        zakatEditionRepository.save(zakat)

        val request = UpdateZakatEditionDto(
            startDate = null,
            endDate = 1681923600000
        )

        val response = zakatEditionService.update(zakat.year, request)

        assertNotNull(response)

        assertEquals(zakat.year, response.year)
        assertEquals(zakat.amountPerPerson, response.amountPerPerson)
        assertEquals(zakat.startDate, response.startDate)
        assertNotNull(zakat.startDate)
        assertNotNull(response.endDate)
        assertEquals(response.endDate, request.endDate)
        assertTrue { response.startDate < response.endDate!! }
    }

    @Test
    fun `should not update zakat fitrah edition's startDate nor endDate`() {
        val zakat = ZakatEdition(
            id = UUID.randomUUID(),
            year = 2023,
            startDate = 1681578000000,
            amountPerPerson = 2.5,
            endDate = null
        )

        zakatEditionRepository.save(zakat)

        val request = UpdateZakatEditionDto(
            startDate = null,
            endDate = null
        )

        val response = zakatEditionService.update(zakat.year, request)

        assertNotNull(response)

        assertEquals(zakat.year, response.year)
        assertEquals(zakat.amountPerPerson, response.amountPerPerson)
        assertEquals(zakat.startDate, response.startDate)
        assertEquals(zakat.endDate, response.endDate)
    }

    @Test
    fun `should throw error not found update zakat fitrah edition`() {
        assertThrows<NotFoundException> {
            val request = UpdateZakatEditionDto(
                startDate = null,
                endDate = null
            )

            zakatEditionService.update(2023, request)
        }
    }

    @Test
    fun `should delete zakat fitrah edition`() {
        val zakat = ZakatEdition(
            id = UUID.randomUUID(),
            year = 2023,
            startDate = 1681578000000,
            amountPerPerson = 2.5,
            endDate = null
        )

        zakatEditionRepository.save(zakat)

        zakatEditionService.delete(zakat.year)

        assertNull(zakatEditionRepository.findByYear(zakat.year))
    }

    @Test
    fun `should throw error not found delete zakat fitrah edition`() {
        assertThrows<NotFoundException> {
            zakatEditionService.delete(2023)
        }
    }

    @Test
    fun `should returns list of zakat fitrah edition`() {
        zakatEditionRepository.save(ZakatEdition(
            id = UUID.randomUUID(),
            year = 2021,
            startDate = 1620234000000,
            amountPerPerson = 2.5,
            endDate = null
        ))

        assertEquals(1, zakatEditionService.findAll().size)

        zakatEditionRepository.save(ZakatEdition(
            id = UUID.randomUUID(),
            year = 2022,
            startDate = 1650819600000,
            amountPerPerson = 2.5,
            endDate = null
        ))

        zakatEditionRepository.save(ZakatEdition(
            id = UUID.randomUUID(),
            year = 2023,
            startDate = 1681578000000,
            amountPerPerson = 2.5,
            endDate = null
        ))

        val result = zakatEditionService.findAll()
        assertEquals(3, result.size)
        assertEquals(2021, result[0].year)
        assertEquals(2022, result[1].year)
        assertEquals(2023, result[2].year)
    }

}