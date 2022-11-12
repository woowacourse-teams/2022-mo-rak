import { useNavigate } from 'react-router-dom';

import {
  StyledContainer,
  StyledCurrentGroupContainer,
  StyledGroupFirstCharacter,
  StyledLogo,
  StyledName,
  StyledProfileContainer
} from '@/layouts/NavigationLayout/components/GlobalFootbarGlobalbar/GlobalFootbarGlobalbar.styles';

import serviceLogoImg from '@/assets/service-logo.svg';
import { Group } from '@/types/group';

type Props = {
  groupCode: Group['code'];
  groups: Array<Group>;
};

function GlobalFootbarGlobalbar({ groups, groupCode }: Props) {
  const navigate = useNavigate();
  const currentGroup = groups.find((group) => group.code === groupCode);

  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  return (
    <StyledContainer>
      <StyledLogo
        src={serviceLogoImg}
        alt={serviceLogoImg}
        onClick={handleNavigate(`/groups/${groupCode}`)}
      />
      {currentGroup && (
        <StyledCurrentGroupContainer>
          <StyledName>🎉 {currentGroup.name}</StyledName>
          <StyledProfileContainer>
            <StyledGroupFirstCharacter>{currentGroup.name[0]}</StyledGroupFirstCharacter>
          </StyledProfileContainer>
        </StyledCurrentGroupContainer>
      )}
    </StyledContainer>
  );
}

export default GlobalFootbarGlobalbar;
