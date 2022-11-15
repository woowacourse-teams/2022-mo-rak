import styled from '@emotion/styled';
import responsive from '../../utils/responsive';

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6rem;

  ${responsive.mobile(`
    align-items: flex-start;
    width: 100%;
    padding: 20rem 8rem;
  `)}
`;

const StyledContentContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2rem;

  ${responsive.mobile(`
    width: 100%;
  `)}
`;

export { StyledContainer, StyledContentContainer };
