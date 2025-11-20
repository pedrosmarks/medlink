import { $ } from '@wdio/globals';
import Page from './page';

/**
 * Página com ações disponíveis após o login (ex.: logout)
 */
class UserPage extends Page {
    public get btnLogout () {
        return $('a.button');
    }

    public async logout () {
        await this.btnLogout.waitForClickable({ timeout: 5000 });
        await this.btnLogout.click();
    }
}
 
export default new UserPage();
