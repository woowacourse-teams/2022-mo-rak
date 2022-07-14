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

  // TODO: input 선택자에 color를 InputField에서 정의하는 게 과연 맞을까?
  // TODO: input을 감싸는 div에 background-color를 줘도 input에게 가려져서 style을 적용할 수가 없다.
  switch (variant) {
    case 'outlined':
      return `
        border: 0.1rem solid ${colorScheme};
        background-color: ${theme.colors.WHITE_100};
      `;
    case 'unstyled':
      return `
        input {
          border: none;
          background-color: transparent;
        }
      `;
    case 'filled':
      return `
        input {
          background-color: ${colorScheme};
        }
      `;
    default:
      return '';
  }
};

// TODO: 리팩토링
function TextField({
  colorScheme,
  variant,
  children,
  ...props
}: Props &
  // TODO: 깔끔하게 하고싶다!! 근데, 맞는 걸수도~?
  CSSProperties) {
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
  ({ width, height, borderRadius, variantStyle, position }) => `
    ${variantStyle}
    width: ${width || '100%'};
    height: ${height}; 
    border-radius: ${borderRadius};
    overflow: hidden;
    position: ${position}
  `
);

export default TextField;

/* {icon && (
        <StyledButton type="button" onClick={onClickIcon}>
          <img src={icon} alt={icon} />
        </StyledButton>
      )} */

// icon?: string;
// onClickIcon?: MouseEventHandler<HTMLButtonElement>;
// icon,
// onClickIcon,
