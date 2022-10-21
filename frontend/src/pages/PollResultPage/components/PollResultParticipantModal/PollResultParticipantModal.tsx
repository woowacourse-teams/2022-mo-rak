// type Props = {
//   pollItem
//   participants
//   description
// };

import Modal from '../../../../components/Modal/Modal';
import {
  StyledForm,
  StyledIcon,
  StyledTitle,
  StyledTop,
  StyledCloseButton,
  StyledBottom,
  StyledParticipantContainer,
  StyledParticipantDescription
} from './PollResultParticipantModal.styles';
import Close from '../../../../assets/close-button.svg';
import Poll from '../../../../assets/poll.svg';
import Avatar from '../../../../components/Avatar/Avatar';

function PollResultParticipantModal() {
  return (
    <Modal isVisible={true} close={() => {}}>
      <StyledForm onSubmit={() => {}}>
        <StyledTop>
          <StyledIcon src={Poll} alt="poll-logo" />
          <StyledTitle>서브웨이(4명)</StyledTitle>
          <StyledCloseButton onClick={close} src={Close} alt="close-button" />
          {/* TODO: 화면 깨져서 주석으로 처리 */}
          {/* <StyledTriangle /> */}
        </StyledTop>
        <StyledBottom>
          <StyledParticipantContainer>
            <Avatar
              width="20%"
              name="배달이"
              profileUrl="https://mblogthumb-phinf.pstatic.net/MjAxOTA1MTdfMjg5/MDAxNTU4MDU5MjY3NzI0.La9iCTKSS9Cue6MbMeNSJADSkjSr0VMPlAsIdQYGjoYg.q_VK0tw6okzVQOBJbXGKFFGJkLJUqLVT26CZ9qe29Xcg.PNG.smartbaedal/%ED%97%A4%ED%97%A4%EB%B0%B0%EB%8B%AC%EC%9D%B4_%EC%9E%90%EB%A5%B8%EA%B2%83.png?type=w800"
            />
            <StyledParticipantDescription>
              lorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem
              ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem
              ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsum
            </StyledParticipantDescription>
          </StyledParticipantContainer>
          <StyledParticipantContainer>
            <Avatar
              width="20%"
              name="배달이"
              profileUrl="https://mblogthumb-phinf.pstatic.net/MjAxOTA1MTdfMjg5/MDAxNTU4MDU5MjY3NzI0.La9iCTKSS9Cue6MbMeNSJADSkjSr0VMPlAsIdQYGjoYg.q_VK0tw6okzVQOBJbXGKFFGJkLJUqLVT26CZ9qe29Xcg.PNG.smartbaedal/%ED%97%A4%ED%97%A4%EB%B0%B0%EB%8B%AC%EC%9D%B4_%EC%9E%90%EB%A5%B8%EA%B2%83.png?type=w800"
            />
            <StyledParticipantDescription>efef</StyledParticipantDescription>
          </StyledParticipantContainer>
          <StyledParticipantContainer>
            <Avatar
              width="20%"
              name="배달이"
              profileUrl="https://mblogthumb-phinf.pstatic.net/MjAxOTA1MTdfMjg5/MDAxNTU4MDU5MjY3NzI0.La9iCTKSS9Cue6MbMeNSJADSkjSr0VMPlAsIdQYGjoYg.q_VK0tw6okzVQOBJbXGKFFGJkLJUqLVT26CZ9qe29Xcg.PNG.smartbaedal/%ED%97%A4%ED%97%A4%EB%B0%B0%EB%8B%AC%EC%9D%B4_%EC%9E%90%EB%A5%B8%EA%B2%83.png?type=w800"
            />
            <StyledParticipantDescription>efef</StyledParticipantDescription>
          </StyledParticipantContainer>
          <StyledParticipantContainer>
            <Avatar
              width="20%"
              name="배달이"
              profileUrl="https://mblogthumb-phinf.pstatic.net/MjAxOTA1MTdfMjg5/MDAxNTU4MDU5MjY3NzI0.La9iCTKSS9Cue6MbMeNSJADSkjSr0VMPlAsIdQYGjoYg.q_VK0tw6okzVQOBJbXGKFFGJkLJUqLVT26CZ9qe29Xcg.PNG.smartbaedal/%ED%97%A4%ED%97%A4%EB%B0%B0%EB%8B%AC%EC%9D%B4_%EC%9E%90%EB%A5%B8%EA%B2%83.png?type=w800"
            />
            <StyledParticipantDescription>efef</StyledParticipantDescription>
          </StyledParticipantContainer>
          <StyledParticipantContainer>
            <Avatar
              width="20%"
              name="배달이"
              profileUrl="https://mblogthumb-phinf.pstatic.net/MjAxOTA1MTdfMjg5/MDAxNTU4MDU5MjY3NzI0.La9iCTKSS9Cue6MbMeNSJADSkjSr0VMPlAsIdQYGjoYg.q_VK0tw6okzVQOBJbXGKFFGJkLJUqLVT26CZ9qe29Xcg.PNG.smartbaedal/%ED%97%A4%ED%97%A4%EB%B0%B0%EB%8B%AC%EC%9D%B4_%EC%9E%90%EB%A5%B8%EA%B2%83.png?type=w800"
            />
            <StyledParticipantDescription>efef</StyledParticipantDescription>
          </StyledParticipantContainer>
        </StyledBottom>
      </StyledForm>
    </Modal>
  );
}

export default PollResultParticipantModal;
