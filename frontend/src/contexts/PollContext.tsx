import React, { PropsWithChildren, createContext, useState, Dispatch, SetStateAction } from 'react';

interface PollContextInterface {
  pollId: number;
  setPollId: Dispatch<SetStateAction<number>>;
}

export const PollContextStore = createContext<PollContextInterface | null>(null);

function PollContext({ children }: PropsWithChildren) {
  // TODO: 0을 주면 안된다...근데 0을 주지 않으면 undefined가 되기 때문에 에러 발생
  const [pollId, setPollId] = useState(0);

  return (
    <PollContextStore.Provider value={{ pollId, setPollId }}>{children}</PollContextStore.Provider>
  );
}

export default PollContext;
