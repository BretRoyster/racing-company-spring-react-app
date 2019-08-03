import { element, by, ElementFinder } from 'protractor';

export default class RacingCompanyUpdatePage {
  pageTitle: ElementFinder = element(by.id('racingCompanyWebApp.racingCompany.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  nameInput: ElementFinder = element(by.css('input#racing-company-name'));
  gasPriceInput: ElementFinder = element(by.css('input#racing-company-gasPrice'));
  servicePriceInput: ElementFinder = element(by.css('input#racing-company-servicePrice'));
  streetInput: ElementFinder = element(by.css('input#racing-company-street'));
  cityInput: ElementFinder = element(by.css('input#racing-company-city'));
  stateInput: ElementFinder = element(by.css('input#racing-company-state'));
  zipCodeInput: ElementFinder = element(by.css('input#racing-company-zipCode'));
  racingCodeInput: ElementFinder = element(by.css('input#racing-company-racingCode'));
  ownerSelect: ElementFinder = element(by.css('select#racing-company-owner'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setNameInput(name) {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput() {
    return this.nameInput.getAttribute('value');
  }

  async setGasPriceInput(gasPrice) {
    await this.gasPriceInput.sendKeys(gasPrice);
  }

  async getGasPriceInput() {
    return this.gasPriceInput.getAttribute('value');
  }

  async setServicePriceInput(servicePrice) {
    await this.servicePriceInput.sendKeys(servicePrice);
  }

  async getServicePriceInput() {
    return this.servicePriceInput.getAttribute('value');
  }

  async setStreetInput(street) {
    await this.streetInput.sendKeys(street);
  }

  async getStreetInput() {
    return this.streetInput.getAttribute('value');
  }

  async setCityInput(city) {
    await this.cityInput.sendKeys(city);
  }

  async getCityInput() {
    return this.cityInput.getAttribute('value');
  }

  async setStateInput(state) {
    await this.stateInput.sendKeys(state);
  }

  async getStateInput() {
    return this.stateInput.getAttribute('value');
  }

  async setZipCodeInput(zipCode) {
    await this.zipCodeInput.sendKeys(zipCode);
  }

  async getZipCodeInput() {
    return this.zipCodeInput.getAttribute('value');
  }

  async setRacingCodeInput(racingCode) {
    await this.racingCodeInput.sendKeys(racingCode);
  }

  async getRacingCodeInput() {
    return this.racingCodeInput.getAttribute('value');
  }

  async ownerSelectLastOption() {
    await this.ownerSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async ownerSelectOption(option) {
    await this.ownerSelect.sendKeys(option);
  }

  getOwnerSelect() {
    return this.ownerSelect;
  }

  async getOwnerSelectedOption() {
    return this.ownerSelect.element(by.css('option:checked')).getText();
  }

  async save() {
    await this.saveButton.click();
  }

  async cancel() {
    await this.cancelButton.click();
  }

  getSaveButton() {
    return this.saveButton;
  }
}
