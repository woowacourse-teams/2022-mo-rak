import styled from '@emotion/styled';
import responsive from '../../utils/responsive';

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 6.4rem 20rem;

  ${responsive.mobile(`
    width: 100%;
    padding: 20rem 8rem;
  `)}
`;

const StyledContentContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 4rem;

  ${responsive.mobile(`
    width: 100%;
  `)}
`;

export { StyledContainer, StyledContentContainer };
