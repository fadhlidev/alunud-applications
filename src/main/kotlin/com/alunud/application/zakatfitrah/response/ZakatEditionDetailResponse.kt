package com.alunud.application.zakatfitrah.response

import com.alunud.application.zakatfitrah.entity.ZakatEdition

data class ZakatEditionDetailResponse(
    val year: Int,
    val startDate: Long,
    val endDate: Long?,
    val amountPerPerson: Double,
    val report: ZakatEditionReport
)

data class ZakatEditionReport(
    val payer: ZakatEditionPayerReport,
    val recipient: ZakatEditionRecipientReport,
    val zakat: ZakatEditionAmountReport
)

data class ZakatEditionPayerReport(
    val totalRepresentation: Int,
    val representativeCumulativeTotal: Int
)

data class ZakatEditionRecipientReport(
    val totalIndividual: Int,
    val totalApplicationProposals: Int
)

data class ZakatEditionAmountReport(
    val totalExpectedAmount: Double,
    val totalActualAmount: Double,
    val totalExcessAmount: Double,
    val totalLessAmount: Double,
    val totalGivenToRecipients: Double,
    val totalGivenToApplicants: Double,
    val totalRemaining: Double
)

fun ZakatEdition.detail(): ZakatEditionDetailResponse {
    val totalRepresentation = payers.size
    val representativeCumulativeTotal = payers.sumOf { it.totalPeople }

    val totalIndividual = recipients.size
    val totalApplicationProposals = applicants.size

    val totalExpectedAmount = representativeCumulativeTotal * amountPerPerson
    val totalActualAmount = payers.sumOf {
        when {
            it.excessAmount > 0.0 && it.excessAmountReturned -> it.totalAmount - it.excessAmount
            it.lessAmount > 0.0 -> it.totalAmount
            else -> it.totalAmount
        }
    }


    val totalGivenToRecipients = recipients.filter { it.givenTime != null }.sumOf { it.givenAmount ?: 0.0 }
    val totalGivenToApplicants = applicants.filter { it.givenTime != null }.sumOf { it.givenAmount ?: 0.0 }

    val totalRemaining = totalActualAmount - totalGivenToRecipients - totalGivenToApplicants

    val payerReport = ZakatEditionPayerReport(totalRepresentation, representativeCumulativeTotal)
    val recipientReport = ZakatEditionRecipientReport(totalIndividual, totalApplicationProposals)
    val amountReport = ZakatEditionAmountReport(
        totalExpectedAmount = totalExpectedAmount,
        totalActualAmount = totalActualAmount,
        totalExcessAmount = payers.filter { !it.excessAmountReturned }.sumOf { it.excessAmount },
        totalLessAmount = payers.sumOf { it.lessAmount },
        totalGivenToRecipients = totalGivenToRecipients,
        totalGivenToApplicants = totalGivenToApplicants,
        totalRemaining = totalRemaining
    )

    return ZakatEditionDetailResponse(
        year = year,
        startDate = startDate,
        endDate = endDate,
        amountPerPerson = amountPerPerson,
        report = ZakatEditionReport(payerReport, recipientReport, amountReport)
    )
}