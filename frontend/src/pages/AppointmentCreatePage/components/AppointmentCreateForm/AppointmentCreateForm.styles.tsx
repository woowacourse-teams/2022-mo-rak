import styled from '@emotion/styled';
import responsive from '../../../../utils/responsive';

const StyledForm = styled.form`
  width: 66rem;
  display: flex;
  flex-direction: column;
  gap: 3.2rem;

  ${responsive.mobile(`
    width: 52rem;
  `)}
`;

export { StyledForm };
