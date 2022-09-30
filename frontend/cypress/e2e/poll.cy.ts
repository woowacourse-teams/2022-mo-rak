describe('투표 기능에 대한 e2e 테스트', () => {
  beforeEach(() => {
    localStorage.setItem('token', JSON.stringify(Cypress.env('token')));
    cy.intercept('GET', '**/api/groups/**/polls').as('getPolls');
    cy.intercept('POST', '**/api/groups/**/polls').as('createPoll');
    cy.intercept('GET', '**/api/groups/**/polls/**/items').as('getPollItems');
    cy.intercept('GET', '**/api/groups/**/polls/**/result').as('getPollResult');
    cy.intercept('GET', '**/api/groups/**/members').as('getGroupMembers');
    cy.intercept('GET', '**/api/groups').as('getGroups');
  });

  it('메인페이지에 접속할 수 있다.', () => {
    cy.visit('http://localhost:3000/');
    cy.url().should('contain', '/groups/');
    cy.wait('@getGroupMembers');
    cy.wait('@getGroups');
  });

  it('투표를 생성할 수 있다.', () => {
    const today = new Date();
    today.setDate(today.getDate() + 7);
    const [dateAfter7days, currentTime] = today.toISOString().split('T');
    const currentHourMinute = currentTime.slice(0, 5);

    cy.findByRole('img', { name: 'poll-menu' }).click();
    cy.wait('@getPolls');
    cy.findByRole('button', { name: '투표 생성하기' }).click();
    cy.findByRole('textbox', { name: 'poll-title' }).type('점심 뭐먹지?');
    cy.findByRole('textbox', { name: 'poll-closingDate' }).type(dateAfter7days);
    cy.findByRole('textbox', { name: 'poll-closingTime' }).type(currentHourMinute);
    cy.findByRole('textbox', { name: 'poll-input0' }).type('서브웨이');
    cy.findByRole('textbox', { name: 'poll-input1' }).type('공단');

    cy.findByRole('button', { name: '투표 만들기' }).click();
    cy.wait('@createPoll');
  });

  it('투표를 진행할 수 있다.', () => {
    cy.wait('@getPollItems');
    cy.findByText('서브웨이').click();
    cy.findByRole('textbox', { name: '서브웨이' }).click().type('서브웨이가 세상에서 제일 좋아요!');
    cy.findByText('투표하기').click();
  });

  it('투표 결과를 확인할 수 있다.', () => {
    cy.wait('@getPollResult').then(() => {
      cy.get('[aria-label="서브웨이-result"]').findByText('1').should('exist'); // TODO: 다른 방식으로 get해오는 방식
    });
  });

  it('재투표를 할 수 있다.', () => {
    cy.findByText('재투표하기').click();
    cy.findByText('공단').click();
    cy.findByRole('textbox', { name: '공단' }).click().type('사실 공단이 세상에서 제일 좋아요!');
    cy.findByText('투표하기').click();
    cy.wait('@getPollResult');
  });

  it('재투표 결과를 확인할 수 있다.', () => {
    cy.get('[aria-label="공단-result"]').findByText('1').should('exist'); // TODO: 다른 방식으로 get해오는 방식
  });

  it('투표를 마감할 수 있다.', () => {
    cy.findByText('투표 마감하기').click();
    cy.findAllByText('완료').should('exist');
  });

  it('투표를 삭제할 수 있다.', () => {
    cy.wait('@getGroupMembers');
    cy.findByRole('button', { name: 'CLOSED' }).click();
    cy.findByText('투표 삭제하기').click();
    cy.findByText('점심 뭐먹지?').should('not.exist');
  });
});
