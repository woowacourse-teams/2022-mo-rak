interface GroupInterface {
  code: string;
  name: string;
  members: Array<MemberInterface>;
}

interface MemberInterface {
  id: number;
  name: string;
  profileUrl: string;
}

export { GroupInterface, MemberInterface };
