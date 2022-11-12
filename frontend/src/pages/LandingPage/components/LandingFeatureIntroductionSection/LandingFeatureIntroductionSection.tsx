import { HTMLAttributes } from 'react';

import {
  StyledAppointmentImage,
  StyledContainer,
  StyledFeatureCircle,
  StyledFeatureName,
  StyledFeaturesContainer,
  StyledGlitterImage,
  StyledHighlightImage,
  StyledIntroductionTitleContainer,
  StyledPollImage,
  StyledRoleImage,
  StyledSubTitle,
  StyledTitle
} from '@/pages/LandingPage/components/LandingFeatureIntroductionSection/LandingFeatureIntroductionSection.styles';
import LandingNavbar from '@/pages/LandingPage/components/LandingNavbar/LandingNavbar';

import glitterImg from '@/assets/glitter.svg';
import highlightImg from '@/assets/highlight.svg';
import pollSmallImg from '@/assets/poll-small.svg';
import roleImg from '@/assets/role.svg';
import timeImg from '@/assets/time.svg';

type Props = HTMLAttributes<HTMLDivElement>;

function LandingFeatureIntroductionSection({ id }: Props) {
  return (
    <StyledContainer id={id}>
      <LandingNavbar />

      <StyledIntroductionTitleContainer>
        <StyledTitle>모락을 통해서 할 수 있어요!</StyledTitle>
        <StyledSubTitle>모락이 모임을 더 편하고, 즐겁게 할 수 있도록 도와줄게요</StyledSubTitle>
      </StyledIntroductionTitleContainer>

      <StyledFeaturesContainer>
        <div>
          <StyledFeatureCircle>
            <StyledPollImage src={pollSmallImg} alt="poll" />
            <StyledHighlightImage src={highlightImg} alt="highlight" />
          </StyledFeatureCircle>
          <StyledFeatureName>투표하기</StyledFeatureName>
        </div>

        <div>
          <StyledFeatureCircle>
            <StyledAppointmentImage src={timeImg} alt="appointment" />
          </StyledFeatureCircle>
          <StyledFeatureName>약속잡기</StyledFeatureName>
        </div>

        <div>
          <StyledFeatureCircle>
            <StyledRoleImage src={roleImg} alt="role" />
            <StyledGlitterImage src={glitterImg} alt="glitter" />
          </StyledFeatureCircle>
          <StyledFeatureName>역할 정하기</StyledFeatureName>
        </div>
      </StyledFeaturesContainer>
    </StyledContainer>
  );
}

export default LandingFeatureIntroductionSection;
