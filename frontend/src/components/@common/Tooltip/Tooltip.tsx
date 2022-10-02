import { PropsWithChildren, useState, CSSProperties } from 'react';
import styled from '@emotion/styled';
import { useTheme } from '@emotion/react';
import Close from '../../../assets/close-button.svg'

interface Props extends PropsWithChildren {
  text: string;
  width: string;
  variant: 'top' | 'bottom' | 'left' | 'right';
  fontSize?: string;
  fontColor?: string;
  backgroundColor?: string;
}

type VariantStyleProps = Pick<Props, 'variant' | 'width'>;

const getVariantStyle = ({ variant, width }: VariantStyleProps) => {
  const theme = useTheme();
  
  switch (variant) {
    case 'top':
      return `
        bottom: 100%;
        left: 50%;
        margin: 0 0 0.8rem ${-width / 2}rem;

        &::after {
          top: 100%; 
          left: 50%;
          margin-left: -5px;
          border-color: ${theme.colors.PURPLE_50} transparent transparent transparent;
        }
      `;
    case 'bottom':
      return `
        top: 100%;
        left: 50%;
        margin: 0.8rem 0 0 ${-width / 2}rem;

        &::after {
          bottom: 100%; 
          left: 50%;
          margin-left: -5px;
          border-color: transparent transparent ${theme.colors.PURPLE_50} transparent;
        }
      `;
    case 'left':
      return `
        top: -5px;
        right: 105%;
        margin-right: 0.8rem;
        // TODO: 중앙 정렬 (툴팁 -height / 2rem)

        &::after {
          top: 50%;
          left: 100%;
          margin-top: -5px;
          border-color: transparent transparent transparent ${theme.colors.PURPLE_50};
        }
      `;
    case 'right':
      return `
        top: -5px;
        left: 105%;
        margin-left: 0.8rem;
        // TODO: 중앙 정렬 ( -height / 2rem)

        &::after {
          top: 50%;
          right: 100%; 
          margin-top: -5px;
          border-color: transparent ${theme.colors.PURPLE_50} transparent transparent;
        }
      `;
    default:
      return '';
  }
};

function Tooltip({children, variant, width, backgroundColor, text, fontSize, fontColor}: Props) {
  const [isVisible, setIsVisible] = useState(false);

  const variantStyle = getVariantStyle({ variant, width });

  const handleToggleContent = () => {
    setIsVisible(!isVisible);
  };

  const handleCloseContent = () => {
    setIsVisible(false);
  }

  return (
    <StyledContainer>
      <StyledContentTrigger onClick={handleToggleContent}>{children}</StyledContentTrigger>
      <StyledContentContainer isVisible={isVisible} width={width} backgroundColor={backgroundColor} variantStyle={variantStyle}>
        <StyledCloseButton onClick={handleCloseContent} src={Close} alt="close-button" />
        <StyledText fontSize={fontSize} color={fontColor}>{text}</StyledText>
      </StyledContentContainer>
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  position: relative;
  z-index: 1;
`;

const StyledContentTrigger = styled.div`
  cursor: pointer;
`;

const StyledContentContainer = styled.div<
  CSSProperties & {
    isVisible: boolean;
    variantStyle: string;
  }>(
  ({isVisible, backgroundColor, width, theme, variantStyle}) => `
    ${variantStyle};
    position: absolute;
    visibility: ${isVisible? 'visible' : 'hidden'};  
    background: ${backgroundColor? backgroundColor : theme.colors.PURPLE_50}; 
    width: ${width}rem;
    border-radius: 1.2rem;
    padding: 1.6rem;

    &::after {
      content: " ";
      position: absolute;
      border-width: 0.8rem;
      border-style: solid;
    }
`);

// TODO: 시맨틱 태그를 위해서, StyledCloseButton 은 button 태그를 생성하고, 내부에 StyledCloseIcon을 넣어줘야할 것 같다. (일관성 위해 일단 이전 방식-모달-과 똑같이 작성)
const StyledCloseButton = styled.img`
  position: absolute;
  right: 0.8rem;
  top: 0.8rem;
  width: 1.2rem; 
  height: 1.2rem;
  cursor: pointer;
`;

const StyledText = styled.div<CSSProperties>(
  ({fontSize, color}) =>`
    ${fontSize && `font-size: ${fontSize}`};
    ${color && `color: ${color}`};
  `
);

export default Tooltip;