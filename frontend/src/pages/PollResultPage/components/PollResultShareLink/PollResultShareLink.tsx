import { StyledLink } from './PollResultShareLink.styles';
import Link from '../../../../assets/link.svg';
import { Poll } from '../../../../types/poll';
import { Group } from '../../../../types/group';

type Props = {
  pollCode: Poll['code'];
  groupCode: Group['code'];
  status: Poll['status'];
};

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

export default PollResultShareLink;
