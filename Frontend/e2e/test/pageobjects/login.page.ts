import { $ } from '@wdio/globals'
import Page from './page';


/**
 * sub page containing specific selectors and methods for a specific page
 */
class LoginPage extends Page {
    /**
     * define selectors using getter methods
     */
    public get pacienteTab () {
        return $('button.tab-button:nth-child(1)');
    }

    public get medicoTab () {
        return $('button.tab-button:nth-child(2)');
    }

    public get inputUsername () {
        return $('input[name="usuario"]');
    }

    public get inputPassword () {
        return $('input[name="senha"]');
    }

    public get btnSubmit () {
        return $('.login-button');
    }

    public get errorMessage () {
        return $('.error-message');
    }

    // ...apenas métodos essenciais...
    /**
     * a method to encapsule automation code to interact with the page
     * e.g. to login using username and password
     */
    public async login (username: string, password: string) {
        await this.inputUsername.setValue(username);
        await this.inputPassword.setValue(password);
        await this.btnSubmit.waitForEnabled({ timeout: 5000 });
        await this.btnSubmit.waitForClickable({ timeout: 5000 });
        await this.btnSubmit.click();
    }

    /**
     * overwrite specific options to adapt it to page object
     */
    public open () {
        return super.open('/login');
    }
}

export default new LoginPage();
