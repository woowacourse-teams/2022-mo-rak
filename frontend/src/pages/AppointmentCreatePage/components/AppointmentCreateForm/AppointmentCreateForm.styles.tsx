import styled from '@emotion/styled';
import responsive from '../../../../utils/responsive';

const StyledContainer = styled.form`
  width: 64rem;
  display: flex;
  flex-direction: column;
  gap: 3.2rem;

  ${responsive.mobile(`
    width: 52rem;
  `)}
`;

export { StyledContainer };
