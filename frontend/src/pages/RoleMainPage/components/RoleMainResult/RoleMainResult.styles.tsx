import styled from '@emotion/styled';

const StyledRoleContainer = styled.div`
  display: flex;
  align-items: center;
  flex-direction: column;
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

const StyledRoleResultWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: row;
  flex-wrap: wrap;
  gap: 2rem;
  margin-top: 2rem;
  overflow-y: auto;
  height: 32rem;
`;

const StyledDateWrapper = styled.div`
  padding: 0 1.2rem;
  display: flex;
  flex-direction: column;
  gap: 0.8rem;
  margin-top: 2rem;
  overflow-y: auto;
  height: 32rem;
`;

const StyledDate = styled.button<{
  isActive: boolean;
}>(
  ({ theme, isActive }) => `
  border-radius: 10px;
  box-shadow: rgba(0, 0, 0, 0.16) 0px 1px 4px;
  padding: 2rem;
  font-size: 1.6rem;
  color: ${isActive ? theme.colors.WHITE_100 : theme.colors.BLACK_100};
  background: ${isActive ? theme.colors.PURPLE_100 : theme.colors.WHITE_100};


  &:hover {
    transform: scale(1.03);
    transition: all 0.2s linear;
    background: ${theme.colors.PURPLE_100};
    color: ${theme.colors.WHITE_100};
  }
`
);

const StyledEmptyText = styled.div(
  ({ theme }) => `
  font-size: 2rem;
  color: ${theme.colors.GRAY_300};
`
);

export {
  StyledTitle,
  StyledRole,
  StyledRoleContainer,
  StyledDateWrapper,
  StyledRoleResultWrapper,
  StyledDate,
  StyledEmptyText
};
