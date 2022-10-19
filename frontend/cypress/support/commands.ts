import '@testing-library/cypress/add-commands';

const localStroageMemory = {};

Cypress.Commands.add('saveLocalStorage', () => {
  Object.keys(localStorage).forEach((key) => {
    localStroageMemory[key] = localStorage[key];
  });
});

Cypress.Commands.add('restoreLocalStorage', () => {
  Object.keys(localStroageMemory).forEach((key) => {
    localStorage.setItem(key, localStroageMemory[key]);
  });
});
