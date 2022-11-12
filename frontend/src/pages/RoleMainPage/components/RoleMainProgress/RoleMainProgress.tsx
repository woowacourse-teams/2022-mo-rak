import { AxiosError } from 'axios';
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

import RoleMainButtons from '@/pages/RoleMainPage/components/RoleMainButtons/RoleMainButtons';
import RoleMainRoleEditModal from '@/pages/RoleMainPage/components/RoleMainRoleEditModal/RoleMainRoleEditModal';
import RoleMainRoles from '@/pages/RoleMainPage/components/RoleMainRoles/RoleMainRoles';

import useModal from '@/hooks/useModal';

import { getRoles } from '@/api/role';
import { Group } from '@/types/group';
import { EditRolesRequest } from '@/types/role';

type Props = {
  onClickAllocateRolesButton: () => void;
};

function RoleMainProgress({ onClickAllocateRolesButton }: Props) {
  const { groupCode } = useParams() as { groupCode: Group['code'] };
  const [roles, setRoles] = useState<EditRolesRequest['roles']>([]);
  const [isRoleEditModalVisible, handleOpenRoleEditModal, handleCloseRoleEditModal] = useModal();

  const fetchRoles = async () => {
    try {
      const res = await getRoles(groupCode);
      setRoles(res.data.roles);
    } catch (err) {
      if (err instanceof AxiosError) {
        const errCode = err.response?.data.codeNumber;
        if (errCode === '5300') {
          alert('역할이 존재하지 않습니다');
        }
      }
    }
  };

  useEffect(() => {
    fetchRoles();
  }, []);

  return (
    <>
      <RoleMainRoles roles={roles} />
      <RoleMainButtons
        onClickAllocateRolesButton={onClickAllocateRolesButton}
        onClickEditRolesButton={handleOpenRoleEditModal}
      />
      {isRoleEditModalVisible && (
        <RoleMainRoleEditModal
          close={handleCloseRoleEditModal}
          initialRoles={roles}
          onSubmit={fetchRoles}
        />
      )}
    </>
  );
}

export default RoleMainProgress;
