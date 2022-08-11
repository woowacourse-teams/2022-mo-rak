import React, { ButtonHTMLAttributes, PropsWithChildren, CSSProperties } from 'react';

import styled from '@emotion/styled';
import { useTheme } from '@emotion/react';

const getVariantStyle = ({ variant, colorScheme }: VariantStyleProps) => {
  const theme = useTheme();

  switch (variant) {
    case 'filled':
      return `
        background-color: ${colorScheme};
        color: ${theme.colors.WHITE_100};
      `;
    case 'outlined':
      return `
        border: 0.1rem solid ${colorScheme};
        color: ${colorScheme};
        background-color: ${theme.colors.WHITE_100};
      `;
    default:
      return '';
  }
};

interface Props extends PropsWithChildren<ButtonHTMLAttributes<HTMLButtonElement>> {
  colorScheme: string;
  variant: 'filled' | 'outlined';
}

type VariantStyleProps = Pick<Props, 'colorScheme' | 'variant'>;

function Button({
  children,
  colorScheme,
  type = 'button',
  variant,
  ...props
}: Props & CSSProperties) {
  const variantStyle = getVariantStyle({ variant, colorScheme });

  return (
    <StyledButton type={type} variantStyle={variantStyle} {...props}>
      {children}
    </StyledButton>
  );
}

const StyledButton = styled.button<
  CSSProperties & {
    variantStyle: string;
  }
>(
  // TODO: 긴급! padding 기본값 없애기
  // TODO: button의 width는 언제나 100% 아닐까? 찾아보자
  ({ width, padding, borderRadius, fontSize, variantStyle, disabled }) => `
  position: relative;
  text-align: center;
  border-radius: ${borderRadius || '15px'};
  width: ${width || '100%'};
  padding: ${padding || '1.2rem 0'};
  ${disabled && 'cursor: default'};
  ${fontSize && `font-size: ${fontSize}`};
  ${variantStyle}
  `
);

export default Button;
