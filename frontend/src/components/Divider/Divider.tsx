import { CSSProperties } from 'react';

import { StyledDivider } from '@/components/Divider/Divider.styles';

type Props = CSSProperties;

function Divider({ borderColor }: Props) {
  return <StyledDivider borderColor={borderColor} />;
}

export default Divider;
