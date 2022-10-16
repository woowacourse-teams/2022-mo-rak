import {
  StyledContainer,
  StyledUserImage,
  StyledUserProfile,
  StyledUserName
} from './PollParticipantsModal.styles';
import { Member } from '../../../../types/group';
import { SelectedPollItem } from '../../../../types/poll';

interface Props {
  participants: Array<Member & Pick<SelectedPollItem, 'description'>>;
}

function PollParticipantModal({ participants }: Props) {
  return (
    <>
      {participants.length > 0 && (
        <StyledContainer>
          {participants.map(({ profileUrl, name, description }) => (
            // TODO: AVATAR로 바꾸기
            <StyledUserProfile key={`${name}-${profileUrl}`}>
              <StyledUserImage src={profileUrl} />
              <StyledUserName>{name === '' ? '익명' : name}</StyledUserName>
              {/* TODO: 컴포넌트로 변경하기 */}
              {description && <div className="description">{description}</div>}
            </StyledUserProfile>
          ))}
        </StyledContainer>
      )}
    </>
  );
}

export default PollParticipantModal;
