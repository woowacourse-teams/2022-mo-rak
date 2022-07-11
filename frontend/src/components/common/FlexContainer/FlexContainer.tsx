import React, { HTMLAttributes, PropsWithChildren, CSSProperties } from 'react';
import styled from '@emotion/styled';

interface Props extends PropsWithChildren<HTMLAttributes<HTMLDivElement>> {}

// TODO: FlexContainer의 존폐에 대해서 생각해보기🙄 width와 height를 넣게 되면 그냥 div에 다 설정해주는 것이랑 다를 게 없기 때문...
function FlexContainer({ children, ...props }: Props & CSSProperties) {
  return <Container {...props}>{children}</Container>;
}

const Container = styled.div<CSSProperties>(
  ({ flexDirection, alignItems, justifyContent, gap }) => `
  display: flex;
  height: 100%;
  flex-direction: ${flexDirection || 'row'};
  align-items: ${alignItems};
  justify-content: ${justifyContent};
  gap: ${gap}
`
);

export default FlexContainer;
