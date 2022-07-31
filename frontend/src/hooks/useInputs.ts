import { useState, ChangeEvent, ChangeEventHandler } from 'react';

type Elements = HTMLInputElement | HTMLSelectElement;

function useInputs<T>(initialValue: T): [typeof values, ChangeEventHandler<Elements>] {
  const [values, setValues] = useState(initialValue);

  const handleValues = (e: ChangeEvent<Elements>) => {
    setValues({ ...values, [e.target.name]: e.target.value });
  };

  return [values, handleValues];
}

export default useInputs;
