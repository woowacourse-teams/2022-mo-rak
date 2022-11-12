import { PropsWithChildren, useState } from 'react';

import {
  StyledCloseButton,
  StyledContainer,
  StyledContent,
  StyledContentContainer,
  StyledContentTrigger
} from '@/components/Tooltip/Tooltip.styles';

import closeButtonImg from '@/assets/close-button.svg';

type placementStyleProps = Pick<Props, 'placement'>;

const getPlacementStyle = ({ placement }: placementStyleProps) => {
  // TODO: 중앙정렬 (현재는 위치만 적용)
  switch (placement) {
    case 'top':
      return `
        bottom: 100%;
        margin-bottom: 0.8rem;
      `;
    case 'bottom':
      return `
        top: 100%;
        margin-top: 0.8rem;
      `;
    case 'left':
      return `
        top: 0;
        right: 105%;
        margin-right: 0.8rem;
      `;
    case 'right':
      return `
        top: 0;
        left: 105%;
        margin-left: 0.8rem;
      `;
    default:
      return '';
  }
};

type Props = {
  content: string;
  width: string; // TODO: width를 받지 않고 사이즈를 줄 수 있도록 변경
  placement: 'top' | 'bottom' | 'left' | 'right';
  fontSize?: string;
  fontColor?: string;
  backgroundColor?: string;
} & PropsWithChildren;

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

  const placementStyle = getPlacementStyle({ placement });

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
        <StyledCloseButton onClick={handleCloseContent} src={closeButtonImg} alt="close-button" />
        <StyledContent fontSize={fontSize} color={fontColor}>
          {content}
        </StyledContent>
      </StyledContentContainer>
    </StyledContainer>
  );
}

export default Tooltip;
