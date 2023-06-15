package com.alunud.application.zakatfitrah.service.impl

import com.alunud.application.AlUnudApplication
import com.alunud.application.zakatfitrah.dto.CreateZakatEditionDto
import com.alunud.application.zakatfitrah.service.ZakatEditionService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [AlUnudApplication::class])
@ActiveProfiles("test")
class ZakatEditionServiceImplTest(@Autowired val zakatEditionService: ZakatEditionService) {

    @Test
    fun `should create zakat fitrah edition`() {
        val request = CreateZakatEditionDto(
            year = 2023,
            startDate = 1681578000000,
            amountPerPerson = 2.5
        )

        val response = zakatEditionService.create(request)

        assertNotNull(response)

        assertNotNull(response.id)
        assertEquals(request.year, response.year)
        assertEquals(request.startDate, response.startDate)
        assertEquals(request.amountPerPerson, response.amountPerPerson)
        assertEquals(request.year, response.year)
        assertNull(response.endDate)
    }

}