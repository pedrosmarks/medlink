import { browser, expect } from '@wdio/globals'
import LoginPage from '../pageobjects/login.page'
import PacienteDashboardPage from '../pageobjects/paciente-dashboard.page'
import RequisicoesPage from '../pageobjects/requisicoes.page'

const PACIENTE_EMAIL = process.env.E2E_PACIENTE_EMAIL || 'joao@exemplo.com.br'
const PACIENTE_SENHA = process.env.E2E_PACIENTE_SENHA || '123'

const loginAsPaciente = async () => {
    await LoginPage.open()
    await LoginPage.login(PACIENTE_EMAIL, PACIENTE_SENHA)

    await browser.waitUntil(async () => {
        const url = await browser.getUrl()
        return url.includes('/paciente/dashboard')
    }, {
        timeout: 15000,
        timeoutMsg: 'Esperava redirecionamento para /paciente/dashboard'
    })
}

describe('Login - MedLink Paciente', () => {
    beforeEach(async () => {
        await browser.url('/login')
        await browser.execute(() => {
            window.localStorage?.clear()
            window.sessionStorage?.clear()
        })
        await browser.url('/login')
    })

    it('should authenticate um paciente válido e redirecionar para o dashboard', async () => {
        await loginAsPaciente()

        await expect(PacienteDashboardPage.navigationShell).toBeExisting()
        const welcomeTextRaw = await PacienteDashboardPage.welcomeTitle.getText()
        const normalizedWelcome = welcomeTextRaw
            .normalize('NFD')
            .replace(/[\u0300-\u036f]/g, '')
            .replace(/[^a-z0-9 ,!]/gi, '')
            .toLowerCase()
        expect(normalizedWelcome).toContain('ola')
        expect(normalizedWelcome).toContain('joao')
    })

    it('should permitir que o paciente aprove uma requisição pendente', async () => {
        await loginAsPaciente()
        await PacienteDashboardPage.openRequisicoes()

        await browser.waitUntil(async () => {
            const url = await browser.getUrl()
            return url.includes('/paciente/requisicoes')
        }, {
            timeout: 10000,
            timeoutMsg: 'Esperava navegação para /paciente/requisicoes'
        })

        await expect(RequisicoesPage.titulo).toBeDisplayed()

        await browser.waitUntil(async () => {
            const count = await RequisicoesPage.countPendingCards()
            return count > 0
        }, {
            timeout: 10000,
            timeoutMsg: 'Nenhuma requisição pendente encontrada. Garanta que existe uma solicitação para o paciente de teste.'
        })

        const initialPendentes = await RequisicoesPage.countPendingCards()
        const cardTitle = await RequisicoesPage.aprovarPrimeiraRequisicao()

        let alertText = ''
        try {
            await browser.waitUntil(async () => await browser.isAlertOpen(), {
                timeout: 10000
            })
            alertText = await browser.getAlertText()
            await browser.acceptAlert()
        } catch (error) {
            // Se nenhum alerta for exibido, não falhar: algumas execuções podem bloquear pop-ups.
            if (await browser.isAlertOpen()) {
                alertText = await browser.getAlertText()
                await browser.acceptAlert()
            }
        }

        await browser.waitUntil(async () => {
            const currentPendentes = await RequisicoesPage.countPendingCards()
            return currentPendentes < initialPendentes
        }, {
            timeout: 10000,
            timeoutMsg: 'Lista de requisições pendentes não diminuiu após aprovação'
        })

        await browser.waitUntil(async () => !(await RequisicoesPage.hasPendingCardWithTitle(cardTitle)), {
            timeout: 10000,
            timeoutMsg: 'O cartão aprovado ainda aparece como pendente'
        })

        if (alertText) {
            expect(alertText.toLowerCase()).toContain('aprovad')
        }
    })

    it('should permitir que o paciente realize logout limpo', async () => {
        await loginAsPaciente()
        await expect(PacienteDashboardPage.navigationShell).toBeExisting()

        await PacienteDashboardPage.logout()

        await browser.waitUntil(async () => {
            const url = await browser.getUrl()
            return url.includes('/login')
        }, {
            timeout: 10000,
            timeoutMsg: 'Esperava redirecionamento para /login após logout'
        })

        const storedToken = await browser.execute(() => window.localStorage.getItem('authToken'))
        const storedUserType = await browser.execute(() => window.localStorage.getItem('userType'))
        expect(storedToken).toBe(null)
        expect(storedUserType).toBe(null)
        await expect(LoginPage.inputUsername).toBeExisting()
    })

    it('should surface backend validation for invalid credentials', async () => {
        await LoginPage.open()
        await LoginPage.login(PACIENTE_EMAIL, 'senha-errada')

        await browser.waitUntil(async () => (await LoginPage.errorMessage.isExisting()), {
            timeout: 10000,
            timeoutMsg: 'Esperava mensagem de erro após tentativa inválida'
        })
        const errorText = await LoginPage.errorMessage.getText()
        const normalizedError = errorText
            .normalize('NFD')
            .replace(/[\u0300-\u036f]/g, '')
            .toLowerCase()
        const acceptedMessages = [
            'usuario ou senha invalidos',
            'erro no servidor. tente novamente'
        ]
        const matchedMessage = acceptedMessages.some(message => normalizedError.includes(message))
        expect(matchedMessage).toBe(true)
    })

    it('should keep the submit button disabled until both fields are filled', async () => {
        await LoginPage.open()

        await expect(LoginPage.btnSubmit).toBeDisabled()

        await LoginPage.inputUsername.setValue('paciente@example.com')
        await expect(LoginPage.btnSubmit).toBeDisabled()

        await LoginPage.inputPassword.setValue('SenhaSegura123')
        await expect(LoginPage.btnSubmit).toBeEnabled()
    })
})

