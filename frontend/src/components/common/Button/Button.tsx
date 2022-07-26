import React, { ButtonHTMLAttributes, PropsWithChildren, CSSProperties } from 'react';

import styled from '@emotion/styled';
import { useTheme } from '@emotion/react';

const getVariantStyle = ({ variant, colorScheme }: VariantStyleProps) => {
  const theme = useTheme();

  switch (variant) {
    case 'filled':
      return `
        background-color: ${colorScheme};
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
  ({ width, padding, borderRadius, color, fontSize, variantStyle, disabled, theme }) => `
  ${variantStyle}
  position: relative;
  text-align: center;
  border-radius: ${borderRadius || '15px'};
  width: ${width || '100%'};
  padding: ${padding || '1.2rem 0'};
  color: ${color || theme.colors.BLACK_100};
  ${disabled && 'cursor: default'};
  ${fontSize && `font-size: ${fontSize}`};
  `
);

export default Button;
