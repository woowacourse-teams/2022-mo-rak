import { StyledLink } from '@/pages/PollResultPage/components/PollResultShareLink/PollResultShareLink.styles';
import linkImg from '@/assets/link.svg';
import { Poll } from '@/types/poll';
import { Group } from '@/types/group';
import { SUCCESS_MESSAGE } from '@/constants/message';

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
        alert(SUCCESS_MESSAGE.COPY_PROCESS_SHARE_LINK);
      });

      return;
    }

    navigator.clipboard.writeText(`${baseLink}/result`).then(() => {
      alert(SUCCESS_MESSAGE.COPY_RESULT_SHARE_LINK);
    });
  };

  return <StyledLink src={linkImg} alt="link" onClick={handleCopyShareLink} />;
}

export default PollResultShareLink;
