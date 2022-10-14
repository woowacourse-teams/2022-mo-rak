import styled from '@emotion/styled';

const StyledRoleContainer = styled.div`
  text-align: center;
`;

const StyledTitle = styled.h1`
  font-size: 2rem;
  margin: 3.2rem 0;
  text-align: center;
`;

const StyledRole = styled.div`
  font-size: 1.6rem;
  margin-bottom: 0.8rem;
`;

const StyledRoleResultContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: row;
  flex-wrap: wrap;
  gap: 2rem;
  margin-top: 2rem;
  overflow-y: scroll;
  height: 32rem;
`;

const StyledDateContainer = styled.div`
  padding: 0 1.2rem;
  display: flex;
  flex-direction: column;
  gap: 0.8rem;
  margin-top: 2rem;
  overflow-y: scroll;
  height: 32rem;
`;

const StyledDate = styled.button(
  ({ theme }) => `
  border-radius: 10px;
  box-shadow: rgba(0, 0, 0, 0.16) 0px 1px 4px;
  padding: 2rem;
  font-size: 1.6rem;

  &:hover {
    transform: scale(1.03);
    transition: all 0.2s linear;
    background: ${theme.colors.PURPLE_100};
    color: ${theme.colors.WHITE_100};
  }
`
);

export {
  StyledTitle,
  StyledRole,
  StyledRoleContainer,
  StyledDateContainer,
  StyledRoleResultContainer,
  StyledDate
};
