import { ButtonHTMLAttributes, PropsWithChildren, CSSProperties } from 'react';

import { useTheme } from '@emotion/react';
import { StyledContainer } from '@/components/Button/Button.styles';

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

type Props = {
  colorScheme: string;
  variant: 'filled' | 'outlined';
} & PropsWithChildren<ButtonHTMLAttributes<HTMLButtonElement>> &
  CSSProperties;

type VariantStyleProps = Pick<Props, 'colorScheme' | 'variant'>;

function Button({ children, colorScheme, type = 'button', variant, ...props }: Props) {
  const variantStyle = getVariantStyle({ variant, colorScheme });

  return (
    <StyledContainer type={type} variantStyle={variantStyle} {...props}>
      {children}
    </StyledContainer>
  );
}

export default Button;
