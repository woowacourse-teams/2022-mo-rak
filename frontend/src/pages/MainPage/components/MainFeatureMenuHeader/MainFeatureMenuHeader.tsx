import Divider from '../../../../components/Divider/Divider';
import { StyledTitle } from './MainFeatureMenuHeader.styles';

function MainFeatureMenuHeader() {
  return (
    <>
      {/* TODO: header 컴포넌트로 따로 분리해야할까? */}
      <StyledTitle>사용할 기능을 선택하세요!</StyledTitle>
      <Divider />
    </>
  );
}

export default MainFeatureMenuHeader;
