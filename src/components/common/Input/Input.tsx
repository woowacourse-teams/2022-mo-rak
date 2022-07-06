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
        border: 0.1rem solid ${colorScheme};
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
  width = '100%',
  height,
  borderRadius,
  color,
  variant,
  placeholder,
  fontSize,
  textAlign = 'center'
}: Props &
  // TODO: 깔끔하게 하고싶다!! 근데, 맞는 걸수도~?
  Pick<
    CSSObject,
    | 'width'
    | 'height'
    | 'borderRadius'
    | 'color'
    | 'fontSize'
    | 'placeholder'
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
  // TODO: 이거는 반복되고 있어서 재사용해줄 수 있을듯
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
    padding: 0 0.4rem;
    text-align: ${textAlign};
  `
);

export default Input;
