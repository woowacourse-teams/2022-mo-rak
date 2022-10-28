import styled from '@emotion/styled';
import responsive from '../../utils/responsive';

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  align-items: center;
  justify-content: center;

  ${responsive.mobile(`
    width: 100%;
    height: 100vh;
    padding: 0 8rem;
  `)}
`;

export { StyledContainer };
