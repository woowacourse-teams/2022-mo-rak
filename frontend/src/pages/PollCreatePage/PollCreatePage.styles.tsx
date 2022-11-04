import styled from '@emotion/styled';
import responsive from '../../utils/responsive';

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  align-items: center;
  justify-content: center;

  ${responsive.mobile(`
    align-items: flex-start;
    width: 100%;
    padding: 20rem 8rem;
  `)}
`;

export { StyledContainer };
