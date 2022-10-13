import { PropsWithChildren, useState } from 'react';
import { useTheme } from '@emotion/react';
import Close from '../../assets/close-button.svg';
import {
  StyledContainer,
  StyledContentTrigger,
  StyledContentContainer,
  StyledCloseButton,
  StyledContent
} from './Tooltip.styles';

interface Props extends PropsWithChildren {
  content: string;
  width: string; // TODO: width를 받지 않고 사이즈를 줄 수 있도록 변경
  placement: 'top' | 'bottom' | 'left' | 'right';
  fontSize?: string;
  fontColor?: string;
  backgroundColor?: string;
}

type placementStyleProps = Pick<Props, 'placement' | 'width' | 'backgroundColor'>;

const getPlacementStyle = ({ placement, width, backgroundColor }: placementStyleProps) => {
  const theme = useTheme();

  switch (placement) {
    case 'top':
      return `
        bottom: 100%;
        left: 50%;
        margin: 0 0 0.8rem ${-width / 2}rem;

        &::after {
          top: 100%; 
          left: 50%;
          margin-left: -5px;
          border-color: ${
            backgroundColor || theme.colors.PURPLE_50
          } transparent transparent transparent;
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
          border-color: transparent transparent ${
            backgroundColor || theme.colors.PURPLE_50
          } transparent;
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
          border-color: transparent transparent transparent ${
            backgroundColor || theme.colors.PURPLE_50
          };
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
          border-color: transparent ${
            backgroundColor || theme.colors.PURPLE_50
          } transparent transparent;
        }
      `;
    default:
      return '';
  }
};

function Tooltip({
  children,
  placement,
  width,
  backgroundColor,
  content,
  fontSize,
  fontColor
}: Props) {
  const [isVisible, setIsVisible] = useState(false);

  const placementStyle = getPlacementStyle({ placement, width, backgroundColor });

  const handleToggleContent = () => {
    setIsVisible(!isVisible);
  };

  const handleCloseContent = () => {
    setIsVisible(false);
  };

  return (
    <StyledContainer>
      <StyledContentTrigger onClick={handleToggleContent}>{children}</StyledContentTrigger>
      <StyledContentContainer
        isVisible={isVisible}
        width={width}
        backgroundColor={backgroundColor}
        placementStyle={placementStyle}
      >
        <StyledCloseButton onClick={handleCloseContent} src={Close} alt="close-button" />
        <StyledContent fontSize={fontSize} color={fontColor}>
          {content}
        </StyledContent>
      </StyledContentContainer>
    </StyledContainer>
  );
}

export default Tooltip;
