import React, { PropsWithChildren, createContext, useState, Dispatch, SetStateAction } from 'react';

interface PollContextInterface {
  pollId: string;
  setPollId: Dispatch<SetStateAction<string>>;
}

export const PollContextStore = createContext<PollContextInterface | null>(null);

function PollContext({ children }: PropsWithChildren) {
  const [pollId, setPollId] = useState('');

  return (
    <PollContextStore.Provider value={{ pollId, setPollId }}>{children}</PollContextStore.Provider>
  );
}

export default PollContext;
