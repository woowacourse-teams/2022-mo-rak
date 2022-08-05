import React from 'react';
import styled from '@emotion/styled';
import Link from '../../../assets/link.svg';
import { PollInterface } from '../../../types/poll';

interface Props {
  pollCode: PollInterface['code'];
}

function PollResultShareLink({ pollCode }: Props) {
  const handleCopyShareLink = async () => {
    await navigator.clipboard.writeText(`/poll/${pollCode}/progress (temp)`);
    alert('투표를 공유할 수 있는 링크가 복사되었습니다 👋');
  };

  return <StyledLink src={Link} alt="link" onClick={handleCopyShareLink} />;
}

const StyledLink = styled.img`
  width: 2rem;
  height: 2rem;
  cursor: pointer;
`;

export default PollResultShareLink;
