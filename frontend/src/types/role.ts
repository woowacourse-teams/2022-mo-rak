import { Member } from '@/types/group';

type Role = {
  date: `${string}-${string}-${string}`;
  role: [
    {
      memberId: Member['id'];
      name: string;
    }
  ];
};

// TODO: Request, Response 고민해보자, Request와 Response가 바뀔 수도 있으니, 변경에 유연하도록
// Request와 Response라는 타입을 만들어서 속성들을 유연하게 추가/수정/삭제를 해줄 수 있도록 해줘도 좋겠다.
type EditRolesRequest = {
  roles: Array<string>;
};

type GetRolesHistoriesResponse = {
  roles: Array<Role>;
};

export { EditRolesRequest, GetRolesHistoriesResponse, Role };
