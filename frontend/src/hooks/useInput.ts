import { ChangeEvent, useState, ChangeEventHandler } from 'react';

type Elements = HTMLInputElement | HTMLSelectElement;

function useInput(initialValue = ''): [typeof value, ChangeEventHandler<Elements>, () => void] {
  const [value, setValue] = useState(initialValue);

  const handleValue = (event: ChangeEvent<Elements>) => {
    setValue(event.target.value);
  };

  const resetValue = () => {
    setValue('');
  };

  return [value, handleValue, resetValue];
}

export default useInput;
