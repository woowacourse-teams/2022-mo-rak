import { InputHTMLAttributes, CSSProperties, PropsWithChildren } from 'react';

import { StyledContainer } from '@/components/TextField/TextField.styles';
import { useTheme } from '@emotion/react';

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

type Props = {
  variant: 'outlined' | 'unstyled' | 'filled';
  colorScheme?: string;
} & PropsWithChildren<InputHTMLAttributes<HTMLInputElement>> &
  CSSProperties;

function TextField({ colorScheme, variant, children, ...props }: Props) {
  const variantStyle = getVariantStyle({ variant, colorScheme });

  return (
    <StyledContainer variantStyle={variantStyle} {...props}>
      {children}
    </StyledContainer>
  );
}

export default TextField;
