package com.alunud.application.zakatfitrah.controller.edition

import com.alunud.application.zakatfitrah.dto.CreateZakatEditionDto
import com.alunud.application.zakatfitrah.dto.UpdateZakatEditionDto
import com.alunud.application.zakatfitrah.service.ZakatEditionService
import com.alunud.web.JsonResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/zakatfitrah")
class ZakatEditionController(@Autowired private val zakatEditionService: ZakatEditionService) {

    @PostMapping
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody dto: CreateZakatEditionDto): JsonResponse {
        println(SecurityContextHolder.getContext().authentication.authorities)
        val result = zakatEditionService.create(dto)

        return JsonResponse(
            status = HttpStatus.CREATED.value(),
            message = "SUCCESS_CREATE_ZAKAT_FITRAH_EDITION",
            data = result
        )
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun findAll(): JsonResponse {
        val result = zakatEditionService.findAll()

        return JsonResponse(
            status = HttpStatus.OK.value(),
            message = "SUCCESS_GET_ZAKAT_FITRAH_EDITION_LIST",
            data = result
        )
    }

    @GetMapping("/{year}")
    @ResponseStatus(HttpStatus.OK)
    fun findOne(@PathVariable year: Int): JsonResponse {
        val result = zakatEditionService.findOne(year)

        return JsonResponse(
            status = HttpStatus.OK.value(),
            message = "SUCCESS_GET_ZAKAT_FITRAH_EDITION_DETAIL",
            data = result
        )
    }

    @PutMapping("/{year}")
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    fun update(@PathVariable year: Int, @RequestBody dto: UpdateZakatEditionDto): JsonResponse {
        val result = zakatEditionService.update(year, dto)

        return JsonResponse(
            status = HttpStatus.OK.value(),
            message = "SUCCESS_UPDATE_ZAKAT_FITRAH_EDITION",
            data = result
        )
    }

    @DeleteMapping("/{year}")
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable year: Int) {
        zakatEditionService.delete(year)
    }

}