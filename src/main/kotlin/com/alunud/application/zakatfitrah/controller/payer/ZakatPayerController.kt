package com.alunud.application.zakatfitrah.controller.payer

import com.alunud.application.zakatfitrah.dto.CreateZakatPayerDto
import com.alunud.application.zakatfitrah.dto.UpdateZakatPayerDto
import com.alunud.application.zakatfitrah.service.ZakatPayerService
import com.alunud.web.JsonResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/zakatfitrah/{year}/payers")
class ZakatPayerController(@Autowired private val zakatPayerService: ZakatPayerService) {

    @PostMapping
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@PathVariable year: Int, @RequestBody dto: CreateZakatPayerDto): JsonResponse {
        val result = zakatPayerService.create(year, dto)

        return JsonResponse(
            status = HttpStatus.CREATED.value(),
            message = "SUCCESS_ADD_ZAKAT_FITRAH_EDITION_PAYER",
            data = result
        )
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun findAll(@PathVariable year: Int): JsonResponse {
        val result = zakatPayerService.findAll(year)

        return JsonResponse(
            status = HttpStatus.OK.value(),
            message = "SUCCESS_GET_ZAKAT_FITRAH_EDITION_PAYERS",
            data = result
        )
    }

    @GetMapping("/{payerId}")
    @ResponseStatus(HttpStatus.OK)
    fun fineOne(@PathVariable year: Int, @PathVariable payerId: UUID): JsonResponse {
        val result = zakatPayerService.findOne(year, payerId)

        return JsonResponse(
            status = HttpStatus.OK.value(),
            message = "SUCCESS_GET_ZAKAT_FITRAH_EDITION_PAYER",
            data = result
        )
    }

    @PutMapping("/{payerId}")
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    fun update(
        @PathVariable year: Int,
        @PathVariable payerId: UUID,
        @RequestBody dto: UpdateZakatPayerDto
    ): JsonResponse {
        val result = zakatPayerService.update(year, payerId, dto)

        return JsonResponse(
            status = HttpStatus.OK.value(),
            message = "SUCCESS_UPDATE_ZAKAT_FITRAH_EDITION_PAYER",
            data = result
        )
    }

    @DeleteMapping("/{payerId}")
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable year: Int, @PathVariable payerId: UUID) {
        zakatPayerService.delete(year, payerId)
    }

}