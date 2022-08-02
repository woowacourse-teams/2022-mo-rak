import React, { InputHTMLAttributes, CSSProperties, PropsWithChildren } from 'react';

import styled from '@emotion/styled';
import { useTheme } from '@emotion/react';

interface Props extends PropsWithChildren<InputHTMLAttributes<HTMLInputElement>> {
  variant: 'outlined' | 'unstyled' | 'filled';
  colorScheme?: string;
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
      `;
    case 'filled':
      return `
        background-color: ${colorScheme};
      `;
    default:
      return '';
  }
};

function TextField({ colorScheme, variant, children, ...props }: Props & CSSProperties) {
  const variantStyle = getVariantStyle({ variant, colorScheme });

  return (
    <StyledContainer variantStyle={variantStyle} {...props}>
      {children}
    </StyledContainer>
  );
}

const StyledContainer = styled.div<
  CSSProperties & {
    variantStyle: string;
  }
>(
  ({ width, borderRadius, variantStyle, padding }) => `
    ${variantStyle};
    position: relative;
    text-align: center;
    ${width && `width: ${width}`};
    ${borderRadius && `border-radius: ${borderRadius}`};
    ${padding && `padding: ${padding}`};
  `
);

export default TextField;
