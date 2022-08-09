import styled from '@emotion/styled';
import React, { PropsWithChildren, CSSProperties, SelectHTMLAttributes } from 'react';

interface Props extends PropsWithChildren<SelectHTMLAttributes<HTMLSelectElement>> {}

function Select({ children, ...props }: Props & CSSProperties) {
  return <StyledSelect {...props}>{children}</StyledSelect>;
}

const StyledSelect = styled.select<CSSProperties>`
  appearance: none;
  width: 100%;
  font-size: 2.8rem;
  text-align: center;
  border: none;
  cursor: pointer;

  :focus {
    outline: none;
  }
`;

export default Select;
