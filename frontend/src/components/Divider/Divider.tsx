import { CSSProperties } from 'react';

import { StyledDivider } from './Divider.styles';

type Props = CSSProperties;

function Divider({ borderColor }: Props) {
  return <StyledDivider borderColor={borderColor} />;
}

export default Divider;
