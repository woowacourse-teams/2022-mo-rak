import Divider from '@/components/Divider/Divider';
import { StyledTitle } from '@/pages/MainPage/components/MainFeatureMenuHeader/MainFeatureMenuHeader.styles';

function MainFeatureMenuHeader() {
  return (
    <>
      <StyledTitle>사용할 기능을 선택하세요!</StyledTitle>
      <Divider />
    </>
  );
}

export default MainFeatureMenuHeader;
