import { $ } from '@wdio/globals'
import Page from './page'

/**
 * Page object for the paciente dashboard after login.
 */
class PacienteDashboardPage extends Page {
    public get welcomeTitle () {
        return $('.welcome-title')
    }

    public get navigationShell () {
        return $('app-paciente-inicial')
    }

    public get logoutButton () {
        return $('.logout-btn')
    }

    public get requisicoesLink () {
        return $('a.nav-link[href="/paciente/requisicoes"]')
    }

    public async openRequisicoes () {
        await this.requisicoesLink.scrollIntoView()
        await this.requisicoesLink.waitForClickable({ timeout: 5000 })
        await this.requisicoesLink.click()
    }

    public async logout () {
        await this.logoutButton.waitForClickable({ timeout: 5000 })
        await this.logoutButton.click()
    }
}

export default new PacienteDashboardPage()
