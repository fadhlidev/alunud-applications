package com.alunud.application.zakatfitrah.service

import com.alunud.application.AlUnudApplication
import com.alunud.application.zakatfitrah.dto.*
import com.alunud.application.zakatfitrah.repository.ZakatApplicantRepository
import com.alunud.application.zakatfitrah.repository.ZakatEditionRepository
import com.alunud.application.zakatfitrah.repository.ZakatPayerRepository
import com.alunud.application.zakatfitrah.repository.ZakatRecipientRepository
import com.alunud.application.zakatfitrah.response.ZakatEditionResponse
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import kotlin.math.floor

@SpringBootTest(classes = [AlUnudApplication::class])
@ActiveProfiles("test")
class ZakatFitrahIntegrationTest(
    @Autowired val zakatEditionService: ZakatEditionService,
    @Autowired val zakatApplicantService: ZakatApplicantService,
    @Autowired val zakatPayerService: ZakatPayerService,
    @Autowired val zakatRecipientService: ZakatRecipientService,
    @Autowired val zakatEditionRepository: ZakatEditionRepository,
    @Autowired val zakatApplicantRepository: ZakatApplicantRepository,
    @Autowired val zakatPayerRepository: ZakatPayerRepository,
    @Autowired val zakatRecipientRepository: ZakatRecipientRepository
) {

    lateinit var zakat: ZakatEditionResponse

    @BeforeEach
    fun `create default zakat edition`() {
        val payload = CreateZakatEditionDto(
            year = 2023,
            startDate = 1681578000000,
            amountPerPerson = 2.5
        )

        zakat = zakatEditionService.create(payload)
    }

    @AfterEach
    fun `clear all entities from repositories`() {
        zakatRecipientRepository.deleteAll()
        zakatPayerRepository.deleteAll()
        zakatApplicantRepository.deleteAll()
        zakatEditionRepository.deleteAll()
    }

    @Test
    fun `should add applicants into zakat edition`() {
        zakatApplicantService.create(
            zakat.year,
            CreateZakatApplicantDto(
                institutionName = "Pondok Pesantren A",
                institutionAddress = "Tawangsari",
                receivedTime = 1681801200000
            )
        )

        zakatEditionService.findOne(zakat.year).run {
            assertEquals(1, this.report.recipient.totalApplicationProposals)
            assertEquals(0.0, this.report.zakat.totalGivenToApplicants)
        }

        zakatApplicantService.create(
            zakat.year,
            CreateZakatApplicantDto(
                institutionName = "Pondok Pesantren B",
                institutionAddress = "Tawangsari",
                receivedTime = 1681804800000,
                givenTime = 1681808400000,
                givenAmount = 25.0
            )
        )

        zakatEditionService.findOne(zakat.year).run {
            assertEquals(2, this.report.recipient.totalApplicationProposals)
            assertEquals(25.0, this.report.zakat.totalGivenToApplicants)
        }

        zakatApplicantService.create(
            zakat.year,
            CreateZakatApplicantDto(
                institutionName = "Pondok Pesantren C",
                institutionAddress = "Tawangsari",
                receivedTime = 1681808400000,
                givenTime = 1681812000000,
                givenAmount = 50.0
            )
        )

        zakatEditionService.findOne(zakat.year).run {
            assertEquals(3, this.report.recipient.totalApplicationProposals)
            assertEquals(75.0, this.report.zakat.totalGivenToApplicants)
        }
    }

    @Test
    fun `should update applicants of zakat edition`() {
        val applicant = zakatApplicantService.create(
            zakat.year,
            CreateZakatApplicantDto(
                institutionName = "Pondok Pesantren A",
                institutionAddress = "Tawangsari",
                receivedTime = 1681801200000
            )
        )

        zakatEditionService.findOne(zakat.year).run {
            assertEquals(1, this.report.recipient.totalApplicationProposals)
            assertEquals(0.0, this.report.zakat.totalGivenToApplicants)
        }

        zakatApplicantService.update(
            zakat.year,
            applicant.id,
            UpdateZakatApplicantDto(
                givenTime = 1681808400000,
                givenAmount = 25.0
            )
        )

        zakatEditionService.findOne(zakat.year).run {
            assertEquals(1, this.report.recipient.totalApplicationProposals)
            assertEquals(25.0, this.report.zakat.totalGivenToApplicants)
        }
    }

    @Test
    fun `should remove applicants from zakat edition`() {
        zakatApplicantService.create(
            zakat.year,
            CreateZakatApplicantDto(
                institutionName = "Pondok Pesantren A",
                institutionAddress = "Tawangsari",
                receivedTime = 1681801200000
            )
        ).run {
            zakatApplicantService.delete(zakat.year, this.id)

            zakatEditionService.findOne(zakat.year).run {
                assertEquals(0, this.report.recipient.totalApplicationProposals)
                assertEquals(0.0, this.report.zakat.totalGivenToApplicants)
            }
        }

        zakatApplicantService.create(
            zakat.year,
            CreateZakatApplicantDto(
                institutionName = "Pondok Pesantren B",
                institutionAddress = "Tawangsari",
                receivedTime = 1681804800000,
                givenTime = 1681808400000,
                givenAmount = 25.0
            )
        )

        zakatApplicantService.create(
            zakat.year,
            CreateZakatApplicantDto(
                institutionName = "Pondok Pesantren C",
                institutionAddress = "Tawangsari",
                receivedTime = 1681808400000,
                givenTime = 1681812000000,
                givenAmount = 50.0
            )
        ).run {
            zakatApplicantService.delete(zakat.year, this.id)

            zakatEditionService.findOne(zakat.year).run {
                assertEquals(1, this.report.recipient.totalApplicationProposals)
                assertEquals(25.0, this.report.zakat.totalGivenToApplicants)
            }
        }
    }

    @Test
    fun `should add payers into zakat edition`() {
        zakatPayerService.create(
            zakat.year,
            CreateZakatPayerDto(
                name = "Wahid",
                address = "Pojok 2/3",
                totalPeople = 1,
                totalAmount = 3.0,
                excessAmountReturned = true
            )
        )

        zakatEditionService.findOne(zakat.year).run {
            assertEquals(1, this.report.payer.totalRepresentation)
            assertEquals(1, this.report.payer.representativeCumulativeTotal)
            assertEquals(2.5, this.report.zakat.totalActualAmount)
            assertEquals(0.0, this.report.zakat.totalExcessAmount)
        }

        zakatPayerService.create(
            zakat.year,
            CreateZakatPayerDto(
                name = "Isnaini",
                address = "Pojok 2/3",
                totalPeople = 2,
                totalAmount = 5.0,
                excessAmountReturned = true
            )
        )

        zakatEditionService.findOne(zakat.year).run {
            assertEquals(2, this.report.payer.totalRepresentation)
            assertEquals(3, this.report.payer.representativeCumulativeTotal)
            assertEquals(7.5, this.report.zakat.totalActualAmount)
            assertEquals(0.0, this.report.zakat.totalExcessAmount)
        }

        zakatPayerService.create(
            zakat.year,
            CreateZakatPayerDto(
                name = "Salasa",
                address = "Pojok 2/3",
                totalPeople = 4,
                totalAmount = 11.0,
                excessAmountReturned = false
            )
        )

        zakatEditionService.findOne(zakat.year).run {
            assertEquals(3, this.report.payer.totalRepresentation)
            assertEquals(7, this.report.payer.representativeCumulativeTotal)
            assertEquals(18.5, this.report.zakat.totalActualAmount)
            assertEquals(1.0, this.report.zakat.totalExcessAmount)
        }
    }

    @Test
    fun `should update payers of zakat edition`() {
        val payer = zakatPayerService.create(
            zakat.year,
            CreateZakatPayerDto(
                name = "Isnaini",
                address = "Pojok 2/3",
                totalPeople = 2,
                totalAmount = 5.0,
                excessAmountReturned = true
            )
        )

        zakatEditionService.findOne(zakat.year).run {
            assertEquals(1, this.report.payer.totalRepresentation)
            assertEquals(2, this.report.payer.representativeCumulativeTotal)
            assertEquals(5.0, this.report.zakat.totalActualAmount)
            assertEquals(0.0, this.report.zakat.totalExcessAmount)
        }

        zakatPayerService.update(
            zakat.year, payer.id, UpdateZakatPayerDto(
                totalPeople = 3,
                totalAmount = 8.5,
                excessAmountReturned = false
            )
        )

        zakatEditionService.findOne(zakat.year).run {
            assertEquals(1, this.report.payer.totalRepresentation)
            assertEquals(3, this.report.payer.representativeCumulativeTotal)
            assertEquals(8.5, this.report.zakat.totalActualAmount)
            assertEquals(1.0, this.report.zakat.totalExcessAmount)
        }
    }

    @Test
    fun `should remove payers into zakat edition`() {
        val currentZakat = zakat
        zakatPayerService.create(
            zakat.year,
            CreateZakatPayerDto(
                name = "Wahid",
                address = "Pojok 2/3",
                totalPeople = 1,
                totalAmount = 3.0,
                excessAmountReturned = true
            )
        ).run {
            zakatPayerService.delete(currentZakat.year, id)

            zakatEditionService.findOne(currentZakat.year).run {
                assertEquals(0, this.report.payer.totalRepresentation)
                assertEquals(0, this.report.payer.representativeCumulativeTotal)
                assertEquals(0.0, this.report.zakat.totalActualAmount)
                assertEquals(0.0, this.report.zakat.totalExcessAmount)
            }
        }

        zakatPayerService.create(
            zakat.year,
            CreateZakatPayerDto(
                name = "Isnaini",
                address = "Pojok 2/3",
                totalPeople = 2,
                totalAmount = 5.0,
                excessAmountReturned = true
            )
        )

        zakatPayerService.create(
            zakat.year,
            CreateZakatPayerDto(
                name = "Salasa",
                address = "Pojok 2/3",
                totalPeople = 4,
                totalAmount = 11.0,
                excessAmountReturned = false
            )
        ).run {
            zakatPayerService.delete(currentZakat.year, id)

            zakatEditionService.findOne(currentZakat.year).run {
                assertEquals(1, this.report.payer.totalRepresentation)
                assertEquals(2, this.report.payer.representativeCumulativeTotal)
                assertEquals(5.0, this.report.zakat.totalActualAmount)
                assertEquals(0.0, this.report.zakat.totalExcessAmount)
            }
        }
    }

    @Test
    fun `should add recipients into zakat edition`() {
        zakatRecipientService.create(
            zakat.year, CreateZakatRecipientDto(
                name = "Fulan bin A",
                address = "Pojok 2/3",
                givenTime = 1681884000000,
                givenAmount = 7.5
            )
        )

        zakatEditionService.findOne(zakat.year).run {
            assertEquals(1, this.report.recipient.totalIndividual)
            assertEquals(7.5, this.report.zakat.totalGivenToRecipients)
        }

        zakatRecipientService.create(
            zakat.year, CreateZakatRecipientDto(
                name = "Fulan bin B",
                address = "Pojok 2/3",
                givenTime = 1681894800000,
                givenAmount = 5.0,
            )
        )

        zakatEditionService.findOne(zakat.year).run {
            assertEquals(2, this.report.recipient.totalIndividual)
            assertEquals(12.5, this.report.zakat.totalGivenToRecipients)
        }

        zakatRecipientService.create(
            zakat.year, CreateZakatRecipientDto(
                name = "Fulan bin C",
                address = "Pojok 2/3",
                givenTime = 1681898400000,
                givenAmount = 7.5,
            )
        )

        zakatEditionService.findOne(zakat.year).run {
            assertEquals(3, this.report.recipient.totalIndividual)
            assertEquals(20.0, this.report.zakat.totalGivenToRecipients)
        }
    }

    @Test
    fun `should update recipients of zakat edition`() {
        val recipient = zakatRecipientService.create(
            zakat.year, CreateZakatRecipientDto(
                name = "Fulan bin A",
                address = "Pojok 2/3",
                givenTime = 1681884000000,
                givenAmount = 7.5
            )
        )

        zakatEditionService.findOne(zakat.year).run {
            assertEquals(1, this.report.recipient.totalIndividual)
            assertEquals(7.5, this.report.zakat.totalGivenToRecipients)
        }

        zakatRecipientService.update(
            zakat.year,
            recipient.id,
            UpdateZakatRecipientDto(
                givenAmount = 10.0
            )
        )

        zakatEditionService.findOne(zakat.year).run {
            assertEquals(1, this.report.recipient.totalIndividual)
            assertEquals(10.0, this.report.zakat.totalGivenToRecipients)
        }
    }

    @Test
    fun `should remove recipients from zakat edition`() {
        zakatRecipientService.create(
            zakat.year, CreateZakatRecipientDto(
                name = "Fulan bin A",
                address = "Pojok 2/3",
                givenTime = 1681884000000,
                givenAmount = 7.5
            )
        ).run {
            zakatRecipientService.delete(zakat.year, this.id)

            zakatEditionService.findOne(zakat.year).run {
                assertEquals(0, this.report.recipient.totalIndividual)
                assertEquals(0.0, this.report.zakat.totalGivenToRecipients)
            }
        }

        zakatRecipientService.create(
            zakat.year, CreateZakatRecipientDto(
                name = "Fulan bin B",
                address = "Pojok 2/3",
                givenTime = 1681894800000,
                givenAmount = 5.0,
            )
        )

        zakatRecipientService.create(
            zakat.year, CreateZakatRecipientDto(
                name = "Fulan bin C",
                address = "Pojok 2/3",
                givenTime = 1681898400000,
                givenAmount = 7.5,
            )
        ).run {
            zakatRecipientService.delete(zakat.year, this.id)

            zakatEditionService.findOne(zakat.year).run {
                assertEquals(1, this.report.recipient.totalIndividual)
                assertEquals(5.0, this.report.zakat.totalGivenToRecipients)
            }
        }
    }

    @Test
    fun `should add and distribute zakat fitrah`() {
        val payerAmounts = mutableListOf(6.0, 9.5, 11.0, 12.5, 16.0)
        for ((index, amount) in payerAmounts.withIndex()) {
            zakatPayerService.create(
                zakat.year, CreateZakatPayerDto(
                    name = "Payer ${index + 1}",
                    totalPeople = floor(amount / zakat.amountPerPerson).toInt(),
                    totalAmount = amount,
                    excessAmountReturned = false
                )
            )
        }

        zakatPayerService.create(
            zakat.year, CreateZakatPayerDto(
                name = "Payer ${payerAmounts.size + 1}",
                totalPeople = 5,
                totalAmount = 12.0,
                excessAmountReturned = false
            )
        )

        zakatPayerService.create(
            zakat.year, CreateZakatPayerDto(
                name = "Payer ${payerAmounts.size + 2}",
                totalPeople = 3,
                totalAmount = 9.0,
                excessAmountReturned = true
            )
        )

        zakatEditionService.findOne(zakat.year).run {
            assertEquals(7, report.payer.totalRepresentation)
            assertEquals(28, report.payer.representativeCumulativeTotal)
            assertEquals(70.0, report.zakat.totalExpectedAmount)
            assertEquals(74.5, report.zakat.totalActualAmount)
            assertEquals(5.0, report.zakat.totalExcessAmount)
            assertEquals(0.5, report.zakat.totalLessAmount)
            assertEquals(0.0, report.zakat.totalGivenToApplicants)
            assertEquals(0.0, report.zakat.totalGivenToRecipients)
            assertEquals(74.5, report.zakat.totalRemaining)
        }

        zakatRecipientService.create(
            zakat.year, CreateZakatRecipientDto(
                name = "Fulan bin A",
                givenAmount = 7.5,
                givenTime = 1681884000000
            )
        )

        zakatRecipientService.create(
            zakat.year, CreateZakatRecipientDto(
                name = "Fulan bin B",
                givenAmount = 5.0,
                givenTime = 1681898400000
            )
        )

        zakatRecipientService.create(
            zakat.year, CreateZakatRecipientDto(
                name = "Fulan bin C",
                givenAmount = 10.0
            )
        )

        zakatEditionService.findOne(zakat.year).run {
            assertEquals(7, report.payer.totalRepresentation)
            assertEquals(28, report.payer.representativeCumulativeTotal)
            assertEquals(70.0, report.zakat.totalExpectedAmount)
            assertEquals(74.5, report.zakat.totalActualAmount)
            assertEquals(5.0, report.zakat.totalExcessAmount)
            assertEquals(0.5, report.zakat.totalLessAmount)
            assertEquals(0.0, report.zakat.totalGivenToApplicants)
            assertEquals(12.5, report.zakat.totalGivenToRecipients)
            assertEquals(62.0, report.zakat.totalRemaining)
        }

        zakatApplicantService.create(
            zakat.year, CreateZakatApplicantDto(
                institutionName = "Pondok Pesantren A",
                institutionAddress = "Tawangsari",
                receivedTime = 1681804800000,
                givenAmount = 25.0,
                givenTime = 1681808400000
            )
        )

        zakatApplicantService.create(
            zakat.year, CreateZakatApplicantDto(
                institutionName = "Pondok Pesantren B",
                institutionAddress = "Tawangsari",
                receivedTime = 1681808400000,
                givenAmount = 25.0
            )
        )

        zakatEditionService.findOne(zakat.year).run {
            assertEquals(7, report.payer.totalRepresentation)
            assertEquals(28, report.payer.representativeCumulativeTotal)
            assertEquals(70.0, report.zakat.totalExpectedAmount)
            assertEquals(74.5, report.zakat.totalActualAmount)
            assertEquals(5.0, report.zakat.totalExcessAmount)
            assertEquals(0.5, report.zakat.totalLessAmount)
            assertEquals(25.0, report.zakat.totalGivenToApplicants)
            assertEquals(12.5, report.zakat.totalGivenToRecipients)
            assertEquals(37.0, report.zakat.totalRemaining)
        }
    }

}