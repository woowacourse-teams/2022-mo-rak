import Box from '../../../../components/Box/Box';
import Avatar from '../../../../components/Avatar/Avatar';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';

import {
  StyledTitle,
  StyledRole,
  StyledRoleContainer,
  StyledDateContainer,
  StyledRoleResultContainer,
  StyledDate
} from './RoleMainResult.styles';
import Divider from '../../../../components/Divider/Divider';

function RoleMainResult() {
  return (
    <FlexContainer gap="3rem">
      {/* 역할 정하기 결과 */}
      <Box width="70%" height="50rem" padding="2rem">
        <StyledTitle>역할 정하기</StyledTitle>
        <Divider />
        <StyledRoleResultContainer>
          <StyledRoleContainer>
            <StyledRole>데일리마스터</StyledRole>
            <Avatar
              profileUrl="https://pbs.twimg.com/media/EU2rX8dUMAA1sMv.jpg:small"
              name="김위니"
              width="8rem"
            />
          </StyledRoleContainer>
          <StyledRoleContainer>
            <StyledRole>데일리마스터</StyledRole>
            <Avatar
              profileUrl="https://pbs.twimg.com/media/EU2rX8dUMAA1sMv.jpg:small"
              name="김위니"
              width="8rem"
            />
          </StyledRoleContainer>
          <StyledRoleContainer>
            <StyledRole>데일리마스터</StyledRole>
            <Avatar
              profileUrl="https://pbs.twimg.com/media/EU2rX8dUMAA1sMv.jpg:small"
              name="김위니"
              width="8rem"
            />
          </StyledRoleContainer>
          <StyledRoleContainer>
            <StyledRole>데일리마스터</StyledRole>
            <Avatar
              profileUrl="https://pbs.twimg.com/media/EU2rX8dUMAA1sMv.jpg:small"
              name="김위니"
              width="8rem"
            />
          </StyledRoleContainer>
          <StyledRoleContainer>
            <StyledRole>데일리마스터</StyledRole>
            <Avatar
              profileUrl="https://pbs.twimg.com/media/EU2rX8dUMAA1sMv.jpg:small"
              name="김위니"
              width="8rem"
            />
          </StyledRoleContainer>
          <StyledRoleContainer>
            <StyledRole>데일리마스터</StyledRole>
            <Avatar
              profileUrl="https://pbs.twimg.com/media/EU2rX8dUMAA1sMv.jpg:small"
              name="김위니"
              width="8rem"
            />
          </StyledRoleContainer>
          <StyledRoleContainer>
            <StyledRole>데일리마스터</StyledRole>
            <Avatar
              profileUrl="https://pbs.twimg.com/media/EU2rX8dUMAA1sMv.jpg:small"
              name="김위니"
              width="8rem"
            />
          </StyledRoleContainer>
          <StyledRoleContainer>
            <StyledRole>데일리마스터</StyledRole>
            <Avatar
              profileUrl="https://pbs.twimg.com/media/EU2rX8dUMAA1sMv.jpg:small"
              name="김위니"
              width="8rem"
            />
          </StyledRoleContainer>
          <StyledRoleContainer>
            <StyledRole>데일리마스터</StyledRole>
            <Avatar
              profileUrl="https://pbs.twimg.com/media/EU2rX8dUMAA1sMv.jpg:small"
              name="김위니"
              width="8rem"
            />
          </StyledRoleContainer>
          <StyledRoleContainer>
            <StyledRole>데일리마스터</StyledRole>
            <Avatar
              profileUrl="https://pbs.twimg.com/media/EU2rX8dUMAA1sMv.jpg:small"
              name="김위니"
              width="8rem"
            />
          </StyledRoleContainer>
          <StyledRoleContainer>
            <StyledRole>데일리마스터</StyledRole>
            <Avatar
              profileUrl="https://pbs.twimg.com/media/EU2rX8dUMAA1sMv.jpg:small"
              name="김위니"
              width="8rem"
            />
          </StyledRoleContainer>
          <StyledRoleContainer>
            <StyledRole>데일리마스터</StyledRole>
            <Avatar
              profileUrl="https://pbs.twimg.com/media/EU2rX8dUMAA1sMv.jpg:small"
              name="김위니"
              width="8rem"
            />
          </StyledRoleContainer>
        </StyledRoleResultContainer>
      </Box>

      {/* 이전 결과 */}
      <Box width="30%" height="50rem" padding="2rem">
        <StyledTitle>이전 결과</StyledTitle>
        <Divider />
        <StyledDateContainer>
          <StyledDate>2022.10.15(토)</StyledDate>
          <StyledDate>2022.10.15(토)</StyledDate>
          <StyledDate>2022.10.15(토)</StyledDate>
          <StyledDate>2022.10.15(토)</StyledDate>
          <StyledDate>2022.10.15(토)</StyledDate>
          <StyledDate>2022.10.15(토)</StyledDate>
          <StyledDate>2022.10.15(토)</StyledDate>
          <StyledDate>2022.10.15(토)</StyledDate>
          <StyledDate>2022.10.15(토)</StyledDate>
          <StyledDate>2022.10.15(토)</StyledDate>
        </StyledDateContainer>
      </Box>
    </FlexContainer>
  );
}

export default RoleMainResult;
