import React, { ButtonHTMLAttributes } from 'react';

import styled from '@emotion/styled';
import { CSSObject, useTheme } from '@emotion/react';

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

interface Props extends ButtonHTMLAttributes<HTMLButtonElement> {
  colorScheme: string;
  variant: 'filled' | 'outlined';
}

type VariantStyleProps = Pick<Props, 'colorScheme' | 'variant'>;

// TODO: 리팩토링
function Button({
  children,
  colorScheme,
  width = '100%',
  height,
  type = 'button',
  borderRadius = '10px',
  color,
  fontSize,
  variant,
  ...props
}: Props &
  React.PropsWithChildren<
    Pick<CSSObject, 'borderRadius' | 'color' | 'fontSize' | 'width' | 'height'>
  >) {
  const variantStyle = getVariantStyle({ variant, colorScheme });

  return (
    <StyledButton
      width={width}
      height={height}
      borderRadius={borderRadius}
      type={type}
      color={color}
      fontSize={fontSize}
      variantStyle={variantStyle}
      {...props}
    >
      {children}
    </StyledButton>
  );
}

const StyledButton = styled.button<
  Pick<
    CSSObject,
    'borderRadius' | 'color' | 'fontSize' | 'width' | 'height'
  > & { variantStyle: string }
>(
  ({ width, height, borderRadius, color, fontSize, variantStyle }) => `
    ${variantStyle}
    text-align: center;
    border-radius: ${borderRadius};
    width: ${width};
    height: ${height};
    color: ${color};
    font-size: ${fontSize};
  `
);

export default Button;
