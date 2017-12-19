import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Book e2e test', () => {

    let navBarPage: NavBarPage;
    let bookDialogPage: BookDialogPage;
    let bookComponentsPage: BookComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Books', () => {
        navBarPage.goToEntity('book');
        bookComponentsPage = new BookComponentsPage();
        expect(bookComponentsPage.getTitle()).toMatch(/jhipsterApp.book.home.title/);

    });

    it('should load create Book dialog', () => {
        bookComponentsPage.clickOnCreateButton();
        bookDialogPage = new BookDialogPage();
        expect(bookDialogPage.getModalTitle()).toMatch(/jhipsterApp.book.home.createOrEditLabel/);
        bookDialogPage.close();
    });

    it('should create and save Books', () => {
        bookComponentsPage.clickOnCreateButton();
        bookDialogPage.setTitleInput('title');
        expect(bookDialogPage.getTitleInput()).toMatch('title');
        bookDialogPage.setDescriptionInput('description');
        expect(bookDialogPage.getDescriptionInput()).toMatch('description');
        bookDialogPage.setPublicationDateInput('2000-12-31');
        expect(bookDialogPage.getPublicationDateInput()).toMatch('2000-12-31');
        bookDialogPage.setPriceInput('5');
        expect(bookDialogPage.getPriceInput()).toMatch('5');
        bookDialogPage.book_authorSelectLastOption();
        bookDialogPage.save();
        expect(bookDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class BookComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-book div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class BookDialogPage {
    modalTitle = element(by.css('h4#myBookLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    titleInput = element(by.css('input#field_title'));
    descriptionInput = element(by.css('input#field_description'));
    publicationDateInput = element(by.css('input#field_publicationDate'));
    priceInput = element(by.css('input#field_price'));
    book_authorSelect = element(by.css('select#field_book_author'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setTitleInput = function(title) {
        this.titleInput.sendKeys(title);
    }

    getTitleInput = function() {
        return this.titleInput.getAttribute('value');
    }

    setDescriptionInput = function(description) {
        this.descriptionInput.sendKeys(description);
    }

    getDescriptionInput = function() {
        return this.descriptionInput.getAttribute('value');
    }

    setPublicationDateInput = function(publicationDate) {
        this.publicationDateInput.sendKeys(publicationDate);
    }

    getPublicationDateInput = function() {
        return this.publicationDateInput.getAttribute('value');
    }

    setPriceInput = function(price) {
        this.priceInput.sendKeys(price);
    }

    getPriceInput = function() {
        return this.priceInput.getAttribute('value');
    }

    book_authorSelectLastOption = function() {
        this.book_authorSelect.all(by.tagName('option')).last().click();
    }

    book_authorSelectOption = function(option) {
        this.book_authorSelect.sendKeys(option);
    }

    getBook_authorSelect = function() {
        return this.book_authorSelect;
    }

    getBook_authorSelectedOption = function() {
        return this.book_authorSelect.element(by.css('option:checked')).getText();
    }

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
