import styled from '@emotion/styled';
import { CSSProperties } from 'react';

const StyledContainer = styled.div<CSSProperties>(
  ({ width }) => `
  display: flex;
  flex-direction: column;
  gap: 0.8rem;
  width: ${width || '8rem'};
  cursor: pointer;
`
);

const StyledUserImage = styled.img`
  border-radius: 100%;
  aspect-ratio: 1 / 1;
`;

const StyledUserName = styled.p<CSSProperties>(
  ({ fontSize }) => `
  text-align: center;
  font-size: ${fontSize || '1.6rem'};
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
`
);

export { StyledContainer, StyledUserImage, StyledUserName };
