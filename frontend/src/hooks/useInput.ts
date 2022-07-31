import { ChangeEvent, useState, ChangeEventHandler } from 'react';

type Elements = HTMLInputElement | HTMLSelectElement;

function useInput(initialValue = ''): [typeof value, ChangeEventHandler<Elements>] {
  const [value, setValue] = useState(initialValue);

  const handleValue = (event: ChangeEvent<Elements>) => {
    setValue(event.target.value);
  };

  return [value, handleValue];
}

export default useInput;
