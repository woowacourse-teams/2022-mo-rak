import { ButtonHTMLAttributes, PropsWithChildren, CSSProperties } from 'react';

import { useTheme } from '@emotion/react';
import { StyledButton } from './Button.styles';

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

export default Button;
