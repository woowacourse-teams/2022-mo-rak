import { CSSProperties } from 'react';

import styled from '@emotion/styled';

const StyledContainer = styled.div<CSSProperties>(
  ({ margin }) => `
  margin: ${margin};
`
);

export { StyledContainer };
