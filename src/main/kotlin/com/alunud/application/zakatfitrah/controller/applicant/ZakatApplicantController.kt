package com.alunud.application.zakatfitrah.controller.applicant

import com.alunud.application.zakatfitrah.dto.CreateZakatApplicantDto
import com.alunud.application.zakatfitrah.dto.UpdateZakatApplicantDto
import com.alunud.application.zakatfitrah.service.ZakatApplicantService
import com.alunud.web.JsonResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/zakatfitrah/{year}/applicants")
class ZakatApplicantController(@Autowired private val zakatApplicantService: ZakatApplicantService) {

    @PostMapping
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@PathVariable year: Int, @RequestBody dto: CreateZakatApplicantDto): JsonResponse {
        val result = zakatApplicantService.create(year, dto)

        return JsonResponse(
            status = HttpStatus.CREATED.value(),
            message = "SUCCESS_ADD_ZAKAT_FITRAH_EDITION_APPLICANT",
            data = result
        )
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun findAll(@PathVariable year: Int): JsonResponse {
        val result = zakatApplicantService.findAll(year)

        return JsonResponse(
            status = HttpStatus.OK.value(),
            message = "SUCCESS_GET_ZAKAT_FITRAH_EDITION_APPLICANTS",
            data = result
        )
    }

    @GetMapping("/{applicantId}")
    @ResponseStatus(HttpStatus.OK)
    fun fineOne(@PathVariable year: Int, @PathVariable applicantId: UUID): JsonResponse {
        val result = zakatApplicantService.findOne(year, applicantId)

        return JsonResponse(
            status = HttpStatus.OK.value(),
            message = "SUCCESS_GET_ZAKAT_FITRAH_EDITION_APPLICANT",
            data = result
        )
    }

    @PutMapping("/{applicantId}")
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    fun update(
        @PathVariable year: Int,
        @PathVariable applicantId: UUID,
        @RequestBody dto: UpdateZakatApplicantDto
    ): JsonResponse {
        val result = zakatApplicantService.update(year, applicantId, dto)

        return JsonResponse(
            status = HttpStatus.OK.value(),
            message = "SUCCESS_UPDATE_ZAKAT_FITRAH_EDITION_APPLICANT",
            data = result
        )
    }

    @DeleteMapping("/{applicantId}")
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable year: Int, @PathVariable applicantId: UUID) {
        zakatApplicantService.delete(year, applicantId)
    }

}