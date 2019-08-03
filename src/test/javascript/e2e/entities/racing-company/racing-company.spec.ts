/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import RacingCompanyComponentsPage from './racing-company.page-object';
import { RacingCompanyDeleteDialog } from './racing-company.page-object';
import RacingCompanyUpdatePage from './racing-company-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('RacingCompany e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let racingCompanyUpdatePage: RacingCompanyUpdatePage;
  let racingCompanyComponentsPage: RacingCompanyComponentsPage;
  /*let racingCompanyDeleteDialog: RacingCompanyDeleteDialog;*/

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.waitUntilDisplayed();

    await signInPage.username.sendKeys('admin');
    await signInPage.password.sendKeys('admin');
    await signInPage.loginButton.click();
    await signInPage.waitUntilHidden();
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load RacingCompanies', async () => {
    await navBarPage.getEntityPage('racing-company');
    racingCompanyComponentsPage = new RacingCompanyComponentsPage();
    expect(await racingCompanyComponentsPage.getTitle().getText()).to.match(/Racing Companies/);
  });

  it('should load create RacingCompany page', async () => {
    await racingCompanyComponentsPage.clickOnCreateButton();
    racingCompanyUpdatePage = new RacingCompanyUpdatePage();
    expect(await racingCompanyUpdatePage.getPageTitle().getText()).to.match(/Create or edit a RacingCompany/);
    await racingCompanyUpdatePage.cancel();
  });

  /* it('should create and save RacingCompanies', async () => {
        async function createRacingCompany() {
            await racingCompanyComponentsPage.clickOnCreateButton();
            await racingCompanyUpdatePage.setNameInput('name');
            expect(await racingCompanyUpdatePage.getNameInput()).to.match(/name/);
            await racingCompanyUpdatePage.setGasPriceInput('5');
            expect(await racingCompanyUpdatePage.getGasPriceInput()).to.eq('5');
            await racingCompanyUpdatePage.setServicePriceInput('5');
            expect(await racingCompanyUpdatePage.getServicePriceInput()).to.eq('5');
            await racingCompanyUpdatePage.setStreetInput('street');
            expect(await racingCompanyUpdatePage.getStreetInput()).to.match(/street/);
            await racingCompanyUpdatePage.setCityInput('city');
            expect(await racingCompanyUpdatePage.getCityInput()).to.match(/city/);
            await racingCompanyUpdatePage.setStateInput('state');
            expect(await racingCompanyUpdatePage.getStateInput()).to.match(/state/);
            await racingCompanyUpdatePage.setZipCodeInput('zipCode');
            expect(await racingCompanyUpdatePage.getZipCodeInput()).to.match(/zipCode/);
            await racingCompanyUpdatePage.setRacingCodeInput('racingCode');
            expect(await racingCompanyUpdatePage.getRacingCodeInput()).to.match(/racingCode/);
            await racingCompanyUpdatePage.ownerSelectLastOption();
            await waitUntilDisplayed(racingCompanyUpdatePage.getSaveButton());
            await racingCompanyUpdatePage.save();
            await waitUntilHidden(racingCompanyUpdatePage.getSaveButton());
            expect(await racingCompanyUpdatePage.getSaveButton().isPresent()).to.be.false;
        }

        await createRacingCompany();
        await racingCompanyComponentsPage.waitUntilLoaded();
        const nbButtonsBeforeCreate = await racingCompanyComponentsPage.countDeleteButtons();
        await createRacingCompany();

        await racingCompanyComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
        expect(await racingCompanyComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });*/

  /* it('should delete last RacingCompany', async () => {
        await racingCompanyComponentsPage.waitUntilLoaded();
        const nbButtonsBeforeDelete = await racingCompanyComponentsPage.countDeleteButtons();
        await racingCompanyComponentsPage.clickOnLastDeleteButton();

        const deleteModal = element(by.className('modal'));
        await waitUntilDisplayed(deleteModal);

        racingCompanyDeleteDialog = new RacingCompanyDeleteDialog();
        expect(await racingCompanyDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/racingCompanyWebApp.racingCompany.delete.question/);
        await racingCompanyDeleteDialog.clickOnConfirmButton();

        await racingCompanyComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
        expect(await racingCompanyComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });*/

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
