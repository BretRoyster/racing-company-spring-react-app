import { element, by, ElementFinder } from 'protractor';

export default class TruckStopUpdatePage {
  pageTitle: ElementFinder = element(by.id('mudflapTruckstopWebApp.truckStop.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  nameInput: ElementFinder = element(by.css('input#truck-stop-name'));
  basePriceInput: ElementFinder = element(by.css('input#truck-stop-basePrice'));
  opisPriceInput: ElementFinder = element(by.css('input#truck-stop-opisPrice'));
  streetInput: ElementFinder = element(by.css('input#truck-stop-street'));
  cityInput: ElementFinder = element(by.css('input#truck-stop-city'));
  stateInput: ElementFinder = element(by.css('input#truck-stop-state'));
  zipCodeInput: ElementFinder = element(by.css('input#truck-stop-zipCode'));
  mudflapCodeInput: ElementFinder = element(by.css('input#truck-stop-mudflapCode'));
  ownerSelect: ElementFinder = element(by.css('select#truck-stop-owner'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setNameInput(name) {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput() {
    return this.nameInput.getAttribute('value');
  }

  async setBasePriceInput(basePrice) {
    await this.basePriceInput.sendKeys(basePrice);
  }

  async getBasePriceInput() {
    return this.basePriceInput.getAttribute('value');
  }

  async setOpisPriceInput(opisPrice) {
    await this.opisPriceInput.sendKeys(opisPrice);
  }

  async getOpisPriceInput() {
    return this.opisPriceInput.getAttribute('value');
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

  async setMudflapCodeInput(mudflapCode) {
    await this.mudflapCodeInput.sendKeys(mudflapCode);
  }

  async getMudflapCodeInput() {
    return this.mudflapCodeInput.getAttribute('value');
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
