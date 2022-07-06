import React from 'react';
import styled from '@emotion/styled';
import { CSSObject } from '@emotion/react';

type Props = {
  children: React.ReactNode;
} & Pick<CSSObject, 'flexDirection' | 'alignItems' | 'justifyContent' | 'gap'>;

// TODO: FlexContainer의 존폐에 대해서 생각해보기🙄 width와 height를 넣게 되면 그냥 div에 다 설정해주는 것이랑 다를 게 없기 때문...
function FlexContainer({
  children,
  flexDirection = 'row',
  alignItems,
  justifyContent,
  gap
}: Props) {
  return (
    <Container
      flexDirection={flexDirection}
      alignItems={alignItems}
      justifyContent={justifyContent}
      gap={gap}
    >
      {/* children은 여러개가 들어올 수도 있기 때문에 */}
      {children}
    </Container>
  );
}

const Container = styled.div<
  Pick<CSSObject, 'flexDirection' | 'alignItems' | 'justifyContent' | 'gap'>
>(
  ({ flexDirection, alignItems, justifyContent, gap }) => `
  display: flex;
  flex-direction: ${flexDirection};
  align-items: ${alignItems};
  justify-content: ${justifyContent};
  gap: ${gap}
`
);

export default FlexContainer;
