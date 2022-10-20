declare namespace Cypress {
  interface Chainable<Subject = any> {
    saveLocalStorage(): Chainable<null>;
    restoreLocalStorage(): Chainable<null>;
  }
}
