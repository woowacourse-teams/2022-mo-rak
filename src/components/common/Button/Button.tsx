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
        border: 1px solid ${colorScheme};
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
  width,
  height,
  borderRadius = '10px',
  color,
  fontSize,
  variant
}: Props &
  React.PropsWithChildren<
    Pick<CSSObject, 'borderRadius' | 'color' | 'fontSize' | 'width' | 'height'>
  >) {
  const variantStyle = getVariantStyle({ variant, colorScheme });

  return (
    <Wrapper
      width={width}
      height={height}
      borderRadius={borderRadius}
      color={color}
      fontSize={fontSize}
      variantStyle={variantStyle}
    >
      {children}
    </Wrapper>
  );
}

const Wrapper = styled.button<
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
