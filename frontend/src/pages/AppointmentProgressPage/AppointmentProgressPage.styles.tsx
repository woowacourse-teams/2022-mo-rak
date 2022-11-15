import styled from '@emotion/styled';
import responsive from '../../utils/responsive';

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8rem;

  ${responsive.mobile(`
    width: 100%;
    padding: 20rem 8rem;
  `)}
`;

export { StyledContainer };
