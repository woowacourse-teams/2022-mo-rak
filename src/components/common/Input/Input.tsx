import React, { InputHTMLAttributes, ButtonHTMLAttributes } from 'react';

import styled from '@emotion/styled';
import { CSSObject, useTheme } from '@emotion/react';

interface Props extends InputHTMLAttributes<HTMLInputElement> {
  colorScheme?: string | null;
  variant: 'outlined' | 'unstyled';
  icon?: string | null;
  // TODO: FUnction에 대한 타이핑 찾아보기
  onClickIcon?: React.MouseEventHandler<HTMLButtonElement>;
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
    default:
      return '';
  }
};

// TODO: padding: 0 3.2rem 자체를 넘겨주는 게 맞을까? 0 3.2rem만 넘겨줘야할까?, 조금 더 생각해보자!
const getPaddingStyle = (icon: Props['icon']) => {
  if (icon) return 'padding: 0 3.2rem;';

  return 'padding: 0 0.4rem;';
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
  textAlign = 'center',
  icon = null,
  onClickIcon,
  id,
  ...props
}: Props &
  // TODO: 깔끔하게 하고싶다!! 근데, 맞는 걸수도~?
  Pick<
    CSSObject,
    'width' | 'height' | 'borderRadius' | 'color' | 'fontSize' | 'placeholder' | 'textAlign'
  >) {
  const variantStyle = getVariantStyle({ variant, colorScheme });
  const paddingStyle = getPaddingStyle(icon);

  return (
    <Container>
      <StyledInput
        placeholder={placeholder}
        width={width}
        height={height}
        borderRadius={borderRadius}
        color={color}
        fontSize={fontSize}
        textAlign={textAlign}
        variantStyle={variantStyle}
        paddingStyle={paddingStyle}
        {...props}
      />
      {icon && (
        <StyledButton type="button" onClick={onClickIcon}>
          <img src={icon} alt={icon} />
        </StyledButton>
      )}
    </Container>
  );
}

const Container = styled.div`
  display: flex;
  align-items: center;
  position: relative;
`;

const StyledInput = styled.input<
  // TODO: 이거는 반복되고 있어서 재사용해줄 수 있을듯
  Pick<CSSObject, 'width' | 'height' | 'borderRadius' | 'color' | 'fontSize' | 'textAlign'> & {
    variantStyle: string;
    paddingStyle: string;
  }
>(
  ({ width, height, borderRadius, color, fontSize, textAlign, variantStyle, paddingStyle }) => `
    ${variantStyle}
    ${paddingStyle}
    width: ${width};
    height: ${height}; 
    border-radius: ${borderRadius};
    color: ${color};
    font-size: ${fontSize};
    text-align: ${textAlign};
  `
);

const StyledButton = styled.button<ButtonHTMLAttributes<HTMLButtonElement>>`
  position: absolute;
  right: 0;
`;

export default Input;
