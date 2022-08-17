import React from 'react';
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
    const progressLink = `${baseLink}/progress`;
    const progressMessage = '투표를 진행할 수 있는 링크가 복사되었습니다 👋';
    const resultLink = `${baseLink}/result`;
    const resultMessage = '투표 결과를 공유할 수 있는 링크가 복사되었습니다 👋';

    if (status === 'OPEN') {
      navigator.clipboard.writeText(progressLink).then(() => {
        alert(progressMessage);
      });

      return;
    }

    navigator.clipboard.writeText(resultLink).then(() => {
      alert(resultMessage);
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
