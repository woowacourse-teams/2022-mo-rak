describe('약속잡기 기능에 대한 e2e 테스트', () => {
  beforeEach(() => {
    localStorage.setItem('token', JSON.stringify(Cypress.env('token')));
    // cy.intercept('GET', '**/api/groups/**/appointments').as('getAppointments');
    cy.intercept('POST', '**/api/groups/**/appointments').as('createAppointment');
    // cy.intercept('POST', '**/api/groups/**/appointments').as('getAppointmentRecommendation');
    // cy.intercept('POST', '**/api/groups/**/appointments').as('getAppointment');
    // cy.intercept('POST', '**/api/groups/**/appointments').as('progressAppointment');
    // cy.intercept('POST', '**/api/groups/**/appointments').as('closeAppointment');
    // cy.intercept('POST', '**/api/groups/**/appointments').as('deleteAppointment');
    // cy.intercept('POST', '**/api/groups/**/appointments').as('getAppointmentStatus');
    // cy.intercept('GET', '**/api/groups/**/members').as('getGroupMembers');
    // cy.intercept('GET', '**/api/groups').as('getGroups');
  });

  it('메인페이지에 접속할 수 있다.', () => {
    cy.visit('http://localhost:3000/');
  });

  it('약속잡기를 생성할 수 있다.', () => {
    const today = new Date();
    today.setMonth(today.getMonth() + 1);
    const [year, nextMonth] = today.toISOString().split('T')[0].split('-');

    cy.findByRole('img', { name: 'appointment-menu' }).click();
    cy.findByRole('button', { name: '약속 생성하기' }).click();
    cy.findByRole('button', { name: 'next-month' }).click();

    cy.findByRole('generic', { name: `${nextMonth}-15` }).click();
    cy.findByRole('generic', { name: `${nextMonth}-20` }).click();
    cy.findByRole('textbox', { name: 'appointment-title' }).type('회의 언제할까?');
    cy.findByRole('textbox', { name: 'appointment-description' }).type(
      '프로젝트에 관한 회의입니다😀'
    );
    cy.findByRole('combobox', { name: 'appointment-duration-hour' }).select('1');
    cy.findByRole('combobox', { name: 'appointment-duration-minute' }).select('30');
    cy.findByRole('combobox', { name: 'appointment-start-time-limit-period' }).select('오전');
    cy.findByRole('combobox', { name: 'appointment-start-time-limit-hour' }).select('9');
    cy.findByRole('combobox', { name: 'appointment-start-time-limit-minute' }).select('30');
    cy.findByRole('combobox', { name: 'appointment-end-time-limit-period' }).select('오후');
    cy.findByRole('combobox', { name: 'appointment-end-time-limit-hour' }).select('6');
    cy.findByRole('combobox', { name: 'appointment-end-time-limit-minute' }).select('00');

    cy.findByRole('textbox', { name: 'appointment-closeDate' }).type(`${year}-${nextMonth}-19`);
    cy.findByRole('textbox', { name: 'appointment-closeTime' }).type('18:00');
    cy.findByRole('button', { name: '생성' }).click();
    // cy.findByRole('button', { name: '투표 만들기' }).click();
    cy.wait('@createAppointment');
    // cy.wait('@getPollItems');
  });
});
