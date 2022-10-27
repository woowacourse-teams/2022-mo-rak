import Modal from '../../../../components/Modal/Modal';
import {
  StyledParticipantsContainer,
  StyledIcon,
  StyledTitle,
  StyledTop,
  StyledCloseIcon,
  StyledBottom,
  StyledParticipantContainer,
  StyledParticipantDescription
} from './PollResultParticipantModal.styles';
import closeButtonImg from '../../../../assets/close-button.svg';
import pollImg from '../../../../assets/poll.svg';
import Avatar from '../../../../components/Avatar/Avatar';
import { Members, PollItem } from '../../../../types/poll';

type Props = {
  isVisible: boolean;
  close: () => void;
  participants: Members;
  subject: PollItem['subject'];
};

function PollResultParticipantModal({ isVisible, close, participants, subject }: Props) {
  const participantsLength = participants.length;

  return (
    <Modal isVisible={isVisible} close={close}>
      <StyledParticipantsContainer>
        <StyledTop>
          <StyledCloseIcon onClick={close} src={closeButtonImg} alt="close-icon" />
          <StyledIcon src={pollImg} alt="poll-icon" />
          <StyledTitle>
            {subject} ({participantsLength}표)
          </StyledTitle>
        </StyledTop>
        <StyledBottom>
          {participants.map(({ name, profileUrl, description }, idx) => {
            return (
              <StyledParticipantContainer key={idx}>
                <Avatar width="15%" name={name} profileUrl={profileUrl} />
                <StyledParticipantDescription>{description}</StyledParticipantDescription>
              </StyledParticipantContainer>
            );
          })}
        </StyledBottom>
      </StyledParticipantsContainer>
    </Modal>
  );
}

export default PollResultParticipantModal;
