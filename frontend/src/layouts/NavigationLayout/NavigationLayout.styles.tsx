import styled from '@emotion/styled';
import responsive from '../../utils/responsive';

const StyledContainer = styled.div`
  position: relative;
  display: flex;

  ${responsive.mobile(`
    display: block;
  `)}
`;

export { StyledContainer };
