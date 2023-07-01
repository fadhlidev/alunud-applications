package com.alunud.application.zakatfitrah.controller.recipient

import com.alunud.application.zakatfitrah.dto.CreateZakatRecipientDto
import com.alunud.application.zakatfitrah.dto.UpdateZakatRecipientDto
import com.alunud.application.zakatfitrah.service.ZakatRecipientService
import com.alunud.web.JsonResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/zakatfitrah/{year}/recipients")
class ZakatRecipientController(@Autowired private val zakatRecipientService: ZakatRecipientService) {

    @PostMapping
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@PathVariable year: Int, @RequestBody dto: CreateZakatRecipientDto): JsonResponse {
        val result = zakatRecipientService.create(year, dto)

        return JsonResponse(
            status = HttpStatus.CREATED.value(),
            message = "SUCCESS_ADD_ZAKAT_FITRAH_EDITION_RECIPIENT",
            data = result
        )
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun findAll(@PathVariable year: Int): JsonResponse {
        val result = zakatRecipientService.findAll(year)

        return JsonResponse(
            status = HttpStatus.OK.value(),
            message = "SUCCESS_GET_ZAKAT_FITRAH_EDITION_RECIPIENTS",
            data = result
        )
    }

    @GetMapping("/{recipientId}")
    @ResponseStatus(HttpStatus.OK)
    fun fineOne(@PathVariable year: Int, @PathVariable recipientId: UUID): JsonResponse {
        val result = zakatRecipientService.findOne(year, recipientId)

        return JsonResponse(
            status = HttpStatus.OK.value(),
            message = "SUCCESS_GET_ZAKAT_FITRAH_EDITION_RECIPIENT",
            data = result
        )
    }

    @PutMapping("/{recipientId}")
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    fun update(
        @PathVariable year: Int,
        @PathVariable recipientId: UUID,
        @RequestBody dto: UpdateZakatRecipientDto
    ): JsonResponse {
        val result = zakatRecipientService.update(year, recipientId, dto)

        return JsonResponse(
            status = HttpStatus.OK.value(),
            message = "SUCCESS_UPDATE_ZAKAT_FITRAH_EDITION_RECIPIENT",
            data = result
        )
    }

    @DeleteMapping("/{recipientId}")
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable year: Int, @PathVariable recipientId: UUID) {
        zakatRecipientService.delete(year, recipientId)
    }

}