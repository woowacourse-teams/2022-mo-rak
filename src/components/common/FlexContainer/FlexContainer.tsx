import React from 'react';
import styled from '@emotion/styled';
import { CSSObject } from '@emotion/react';

type Props = {
  children: React.ReactNode;
} & Pick<CSSObject, 'flexDirection' | 'alignItems' | 'justifyContent'>;

function FlexContainer({
  children,
  flexDirection = 'row',
  alignItems,
  justifyContent
}: Props) {
  return (
    <Wrapper
      flexDirection={flexDirection}
      alignItems={alignItems}
      justifyContent={justifyContent}
    >
      {children}
    </Wrapper>
  );
}

const Wrapper = styled.div<
  Pick<CSSObject, 'flexDirection' | 'alignItems' | 'justifyContent'>
>(
  ({ flexDirection, alignItems, justifyContent }) => `
  display: flex;
  flex-direction: ${flexDirection};
  align-items: ${alignItems};
  justify-content: ${justifyContent};
`
);

export default FlexContainer;
