import styled from '@emotion/styled';

const StyledContainer = styled.div(
  ({ placementStyle }: { placementStyle: 'center' | 'flex-start' | 'flex-end' }) => `
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: ${placementStyle};
  align-items: center;
`
);

export { StyledContainer };
