type Group = {
  code: string;
  name: string;
  members: Array<Member>;
};

type Member = {
  id: number;
  name: string;
  profileUrl: string;
};

export { Group, Member };
