/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import TruckStopComponentsPage from './truck-stop.page-object';
import { TruckStopDeleteDialog } from './truck-stop.page-object';
import TruckStopUpdatePage from './truck-stop-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('TruckStop e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let truckStopUpdatePage: TruckStopUpdatePage;
  let truckStopComponentsPage: TruckStopComponentsPage;
  /*let truckStopDeleteDialog: TruckStopDeleteDialog;*/

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

  it('should load TruckStops', async () => {
    await navBarPage.getEntityPage('truck-stop');
    truckStopComponentsPage = new TruckStopComponentsPage();
    expect(await truckStopComponentsPage.getTitle().getText()).to.match(/Pit Stops/);
  });

  it('should load create TruckStop page', async () => {
    await truckStopComponentsPage.clickOnCreateButton();
    truckStopUpdatePage = new TruckStopUpdatePage();
    expect(await truckStopUpdatePage.getPageTitle().getText()).to.match(/Create or edit a TruckStop/);
    await truckStopUpdatePage.cancel();
  });

  /* it('should create and save TruckStops', async () => {
        async function createTruckStop() {
            await truckStopComponentsPage.clickOnCreateButton();
            await truckStopUpdatePage.setNameInput('name');
            expect(await truckStopUpdatePage.getNameInput()).to.match(/name/);
            await truckStopUpdatePage.setBasePriceInput('5');
            expect(await truckStopUpdatePage.getBasePriceInput()).to.eq('5');
            await truckStopUpdatePage.setOpisPriceInput('5');
            expect(await truckStopUpdatePage.getOpisPriceInput()).to.eq('5');
            await truckStopUpdatePage.setStreetInput('street');
            expect(await truckStopUpdatePage.getStreetInput()).to.match(/street/);
            await truckStopUpdatePage.setCityInput('city');
            expect(await truckStopUpdatePage.getCityInput()).to.match(/city/);
            await truckStopUpdatePage.setStateInput('state');
            expect(await truckStopUpdatePage.getStateInput()).to.match(/state/);
            await truckStopUpdatePage.setZipCodeInput('zipCode');
            expect(await truckStopUpdatePage.getZipCodeInput()).to.match(/zipCode/);
            await truckStopUpdatePage.setMudflapCodeInput('mudflapCode');
            expect(await truckStopUpdatePage.getMudflapCodeInput()).to.match(/mudflapCode/);
            await truckStopUpdatePage.ownerSelectLastOption();
            await waitUntilDisplayed(truckStopUpdatePage.getSaveButton());
            await truckStopUpdatePage.save();
            await waitUntilHidden(truckStopUpdatePage.getSaveButton());
            expect(await truckStopUpdatePage.getSaveButton().isPresent()).to.be.false;
        }

        await createTruckStop();
        await truckStopComponentsPage.waitUntilLoaded();
        const nbButtonsBeforeCreate = await truckStopComponentsPage.countDeleteButtons();
        await createTruckStop();

        await truckStopComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
        expect(await truckStopComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });*/

  /* it('should delete last TruckStop', async () => {
        await truckStopComponentsPage.waitUntilLoaded();
        const nbButtonsBeforeDelete = await truckStopComponentsPage.countDeleteButtons();
        await truckStopComponentsPage.clickOnLastDeleteButton();

        const deleteModal = element(by.className('modal'));
        await waitUntilDisplayed(deleteModal);

        truckStopDeleteDialog = new TruckStopDeleteDialog();
        expect(await truckStopDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/mudflapTruckstopWebApp.truckStop.delete.question/);
        await truckStopDeleteDialog.clickOnConfirmButton();

        await truckStopComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
        expect(await truckStopComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });*/

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
