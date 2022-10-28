import { CSSProperties } from 'react';
import styled from '@emotion/styled';

const StyledContainer = styled.div`
  position: relative;
`;

const StyledContentTrigger = styled.div`
  cursor: pointer;
`;

const StyledContentContainer = styled.div<
  CSSProperties & {
    isVisible: boolean;
    placementStyle: string;
  }
>(
  ({ isVisible, backgroundColor, width, theme, placementStyle }) => `
    ${placementStyle};
    position: absolute;
    display: ${isVisible ? 'block' : 'none'};  
    background: ${backgroundColor ? backgroundColor : theme.colors.PURPLE_50}; 
    width: ${width}rem;
    border-radius: 1.2rem;
    padding: 2rem;
`
);

// TODO: 시맨틱 태그를 위해서, StyledCloseButton 은 button 태그를 생성하고, 내부에 StyledCloseIcon을 넣어줘야할 것 같다. (일관성 위해 일단 이전 방식-모달-과 똑같이 작성)
const StyledCloseButton = styled.img`
  position: absolute;
  right: 0.8rem;
  top: 0.8rem;
  width: 1.2rem;
  cursor: pointer;
`;

const StyledContent = styled.p<CSSProperties>(
  ({ fontSize, color }) => `
    ${fontSize && `font-size: ${fontSize}`};
    ${color && `color: ${color}`};
    line-height: 1.6rem;
  `
);

export {
  StyledContainer,
  StyledContentTrigger,
  StyledContentContainer,
  StyledCloseButton,
  StyledContent
};
