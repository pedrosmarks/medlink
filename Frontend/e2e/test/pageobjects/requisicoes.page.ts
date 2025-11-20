import { $, $$ } from '@wdio/globals'
import Page from './page'

/**
 * Page object for a paciente's requisicoes view.
 */
class RequisicoesPage extends Page {
    public get titulo () {
        return $('h5=Requisições de Acesso Médico')
    }

    private async getCardsPendentes () {
        return await $$('div.card.border-warning')
    }

    public async aprovarPrimeiraRequisicao () {
        const cards = await this.getCardsPendentes()
        if (cards.length === 0) {
            throw new Error('Nenhuma requisição pendente para aprovar')
        }
        const primeiroCard = cards[0]
        const titleElement = await primeiroCard.$('h6.card-title')
        const titleText = (await titleElement.getText()).trim()
        const aprovarButton = await primeiroCard.$('button.btn-success')
        await aprovarButton.waitForClickable({ timeout: 5000 })
        await aprovarButton.click()
        return titleText
    }

    public async hasPendingCardWithTitle (title: string) {
        const normalizedTarget = title
            .normalize('NFD')
            .replace(/[\u0300-\u036f]/g, '')
            .toLowerCase()

        const cards = await this.getCardsPendentes()
        for (const card of cards) {
            const cardTitle = (await card.$('h6.card-title').getText()).trim()
            const normalizedCardTitle = cardTitle
                .normalize('NFD')
                .replace(/[\u0300-\u036f]/g, '')
                .toLowerCase()
            if (normalizedCardTitle === normalizedTarget) {
                return true
            }
        }
        return false
    }

    public async countPendingCards () {
        const cards = await this.getCardsPendentes()
        return cards.length
    }
}

export default new RequisicoesPage()
