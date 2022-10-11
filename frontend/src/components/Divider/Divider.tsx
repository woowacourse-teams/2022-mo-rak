import { CSSProperties } from 'react';

import { StyledDivider } from './Divider.styles';

interface Props extends CSSProperties {}

function Divider({ borderColor }: Props) {
  return <StyledDivider borderColor={borderColor} />;
}

export default Divider;
