import React, { InputHTMLAttributes } from 'react';

import styled from '@emotion/styled';
import { CSSObject, useTheme } from '@emotion/react';

interface Props extends InputHTMLAttributes<HTMLInputElement> {
  colorScheme?: string | null;
  variant: 'outlined' | 'unstyled';
}

type VariantStyleProps = Pick<Props, 'colorScheme' | 'variant'>;

const getVariantStyle = ({ variant, colorScheme }: VariantStyleProps) => {
  const theme = useTheme();

  switch (variant) {
    case 'outlined':
      return `
        border: 1px solid ${colorScheme};
        background-color: ${theme.colors.WHITE_100};
      `;
    case 'unstyled':
      return `
        border: none;
        background-color: transparent;
        
        &:focus {
        outline: none;
        }
      `;
    default:
      return '';
  }
};

// TODO: 리팩토링
function Input({
  colorScheme = null,
  width,
  height,
  borderRadius,
  color,
  variant,
  placeholder,
  fontSize,
  textAlign = 'center'
}: Props &
  Pick<
    CSSObject,
    | 'width'
    | 'height'
    | 'borderRadius'
    | 'color'
    | 'fontSize'
    | 'placeHolder'
    | 'textAlign'
  >) {
  const variantStyle = getVariantStyle({ variant, colorScheme });

  return (
    <StyledInput
      placeholder={placeholder}
      width={width}
      height={height}
      borderRadius={borderRadius}
      color={color}
      fontSize={fontSize}
      textAlign={textAlign}
      variantStyle={variantStyle}
    />
  );
}

const StyledInput = styled.input<
  Pick<
    CSSObject,
    'width' | 'height' | 'borderRadius' | 'color' | 'fontSize' | 'textAlign'
  > & { variantStyle: string }
>(
  ({
    width,
    height,
    borderRadius,
    color,
    fontSize,
    textAlign,
    variantStyle
  }) => `
    ${variantStyle} 
    width: ${width};
    height: ${height}; 
    border-radius: ${borderRadius};
    color: ${color};
    font-size: ${fontSize};
    padding: 0 10px;
    text-align: ${textAlign};
  `
);

export default Input;
