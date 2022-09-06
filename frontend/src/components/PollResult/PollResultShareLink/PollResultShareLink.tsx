import styled from '@emotion/styled';
import Link from '../../../assets/link.svg';
import { PollInterface } from '../../../types/poll';
import { GroupInterface } from '../../../types/group';

interface Props {
  pollCode: PollInterface['code'];
  groupCode: GroupInterface['code'];
  status: PollInterface['status'];
}

function PollResultShareLink({ groupCode, pollCode, status }: Props) {
  const handleCopyShareLink = () => {
    const baseLink = `${process.env.CLIENT_URL}/groups/${groupCode}/poll/${pollCode}`;

    if (status === 'OPEN') {
      navigator.clipboard.writeText(`${baseLink}/progress`).then(() => {
        alert('투표를 진행할 수 있는 링크가 복사되었습니다 👋');
      });

      return;
    }

    navigator.clipboard.writeText(`${baseLink}/result`).then(() => {
      alert('투표 결과를 공유할 수 있는 링크가 복사되었습니다 👋');
    });
  };

  return <StyledLink src={Link} alt="link" onClick={handleCopyShareLink} />;
}

const StyledLink = styled.img`
  width: 2rem;
  height: 2rem;
  cursor: pointer;
`;

export default PollResultShareLink;
